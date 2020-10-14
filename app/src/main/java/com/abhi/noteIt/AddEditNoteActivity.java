package com.abhi.noteIt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.noteIt.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.achitectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.achitectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.achitectureexample.EXTRA_PRIORITY";
    public static final String EXTRA_PRIORITY_NUMBER = "com.example.achitectureexample.EXTRA_PRIORITY_NUMBER";
    public static final String EXTRA_DATE_TIME = "com.example.achitectureexample.EXTRA_DATE_TIME";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private AutoCompleteTextView priorityMenu;
    private String currentDate;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_Description);
//        spinnerPriority = findViewById(R.id.spinnerPriority);
        priorityMenu = findViewById(R.id.priorityMenu);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.priorityList,
                    R.layout.dropdown_popup_menu_item);
            priorityMenu.setAdapter(adapter);
//            spinnerPriority.setSelection(intent.getIntExtra(EXTRA_PRIORITY_NUMBER,1));
        } else {
            setTitle("Add Note");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.priorityList,
                    R.layout.dropdown_popup_menu_item);
            priorityMenu.setAdapter(adapter);
        }

        priorityMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priority = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void saveNote() {

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_PRIORITY_NUMBER,priority);
        data.putExtra(EXTRA_DATE_TIME,currentDate);

        int  priorityNumber=0;

        switch (priority) {
            case "High":
                priorityNumber = 3;
                break;
            case "Medium":
                priorityNumber = 2;
                break;
            case "Low":
                priorityNumber = 1;
                break;
        }

        data.putExtra(EXTRA_PRIORITY_NUMBER,priorityNumber);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
