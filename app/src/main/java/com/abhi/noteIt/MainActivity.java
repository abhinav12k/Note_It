package com.abhi.noteIt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.noteIt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int Add_Note_Request = 1;
    public static final int Edit_Note_Request = 2;
    private NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
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

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(), "Note Deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                String title = note.getTitle();
                String description = note.getDescription();
                String priority = note.getPriority();

                int  priorityNumber=0;

                if(priority.equals("High")){
                    priorityNumber=3;
                }else if(priority.equals("Medium")){
                    priorityNumber=2;
                }else if(priority.equals("Low")){
                    priorityNumber=1;
                }

                int id=note.getId();
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,title);
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,description);
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,priority);
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY_NUMBER,priorityNumber);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,id);
                startActivityForResult(intent,Edit_Note_Request);
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
            String dateTime = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE_TIME);
            int  priorityNumber=0;

            if(priority.equals("High")){
                priorityNumber=3;
            }else if(priority.equals("Medium")){
                priorityNumber=2;
            }else if(priority.equals("Low")){
                priorityNumber=1;
            }

            Note note = new Note(title, description, priority,priorityNumber,dateTime);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
        }else if(requestCode == Edit_Note_Request && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String dateTime = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE_TIME);
            int  priorityNumber=0;
            if(priority.equals("High")){
                priorityNumber=3;
            }else if(priority.equals("Medium")){
                priorityNumber=2;
            }else if(priority.equals("Low")){
                priorityNumber=1;
            }
            Note note = new Note(title, description, priority,priorityNumber,dateTime);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note updated Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNotesFromDb(query);
                return false;
            }

            private void getNotesFromDb(String searchText) {
                searchText = "%"+searchText+"%";

                noteViewModel.getSearchedNotes(searchText).observe(MainActivity.this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        if(notes==null){
                            return;
                        }
//                        NoteAdapter adapter = new NoteAdapter();
                        adapter.submitList(notes);
                        recyclerView.setAdapter(adapter);

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
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
