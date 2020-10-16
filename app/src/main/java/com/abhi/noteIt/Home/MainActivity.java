package com.abhi.noteIt.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.noteIt.Adapter.NoteAdapter;
import com.abhi.noteIt.ViewModel.NoteViewModel;
import com.abhi.noteIt.Utilities.SwipeToDeleteCallback;
import com.abhi.noteIt.Model.Note;
import com.android.noteIt.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int Add_Note_Request = 1;
    public static final int Edit_Note_Request = 2;
    private NoteViewModel noteViewModel;
    private List<Note> completeList;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isDarkModeOn;
    MaterialAlertDialogBuilder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, Add_Note_Request);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        builder = new MaterialAlertDialogBuilder(
                new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
                completeList = new ArrayList<>(notes);
            }
        });


        new ItemTouchHelper(new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                builder.setBackground(getResources().getDrawable(R.drawable.alert_shape));
                builder.setMessage("Do you want to delete this note ?")
                        .setTitle("Alert")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final int adapterPosition = viewHolder.getAdapterPosition();
                                final Note mNote = adapter.getNoteAt(adapterPosition);
                                final View viewPos = findViewById(R.id.myCoordinatorLayout);

                                Snackbar snackbar = Snackbar
                                        .make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int mAdapterPosition = viewHolder.getAdapterPosition();
                                                noteViewModel.insert(mNote);
                                            }
                                        });

                                snackbar.setActionTextColor(getResources().getColor(R.color.primaryLightColor));
                                View snackBarView = snackbar.getView();
                                int snackbarTextId = com.google.android.material.R.id.snackbar_text;
                                TextView textView = snackBarView.findViewById(snackbarTextId);
                                textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
                                snackBarView.setBackground(getResources().getDrawable(R.drawable.snackbar_shape));
                                snackbar.show();

                                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                String title = note.getTitle();
                String description = note.getDescription();
                String priority = note.getPriority();
                String date = note.getDate();
                String time = note.getTime();

                int priorityNumber = 0;

                if (priority.equals("High")) {
                    priorityNumber = 3;
                } else if (priority.equals("Medium")) {
                    priorityNumber = 2;
                } else if (priority.equals("Low")) {
                    priorityNumber = 1;
                }

                int id = note.getId();
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, title);
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, description);
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, priority);
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY_NUMBER, priorityNumber);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, id);
                intent.putExtra(AddEditNoteActivity.EXTRA_DATE, date);
                intent.putExtra(AddEditNoteActivity.EXTRA_TIME, time);
                startActivityForResult(intent, Edit_Note_Request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Add_Note_Request && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddEditNoteActivity.EXTRA_TIME);
            int priorityNumber = 0;

            if (priority.equals("High")) {
                priorityNumber = 3;
            } else if (priority.equals("Medium")) {
                priorityNumber = 2;
            } else if (priority.equals("Low")) {
                priorityNumber = 1;
            }

            Note note = new Note(title, description, priority, priorityNumber, date, time);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == Edit_Note_Request && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddEditNoteActivity.EXTRA_TIME);
            int priorityNumber = 0;
            if (priority.equals("High")) {
                priorityNumber = 3;
            } else if (priority.equals("Medium")) {
                priorityNumber = 2;
            } else if (priority.equals("Low")) {
                priorityNumber = 1;
            }
            Note note = new Note(title, description, priority, priorityNumber, date, time);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor
                = sharedPreferences.edit();
        isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        final MenuItem nightMode = menu.findItem(R.id.night_mode);
        final MenuItem dayMode = menu.findItem(R.id.day_mode);

        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            dayMode.setVisible(true);
            nightMode.setVisible(false);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            dayMode.setVisible(false);
            nightMode.setVisible(true);

        }

        nightMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                editor.putBoolean(
                        "isDarkModeOn", true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode On ", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(true);
                nightMode.setVisible(false);
                return true;
            }
        });

        dayMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                editor.putBoolean(
                        "isDarkModeOn", false);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode Off", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(false);
                nightMode.setVisible(true);
                return true;

            }
        });

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNotesFromDb(query);
                return false;
            }

            private void getNotesFromDb(String searchText) {
                searchText = "%" + searchText + "%";

                noteViewModel.getSearchedNotes(searchText).observe(MainActivity.this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        if (notes == null) {
                            return;
                        }
//                        NoteAdapter adapter = new NoteAdapter();
                        adapter.submitList(notes);
//                        completeList = notes;
//                        recyclerView.setAdapter(adapter);

                    }
                });

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getNotesFromDb(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
