package com.abhi.noteIt.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.noteIt.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.achitectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.achitectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.achitectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.achitectureexample.EXTRA_PRIORITY";
    public static final String EXTRA_PRIORITY_NUMBER = "com.example.achitectureexample.EXTRA_PRIORITY_NUMBER";
    public static final String EXTRA_DATE = "com.example.achitectureexample.EXTRA_DATE";
    public static final String EXTRA_TIME = "com.example.achitectureexample.EXTRA_TIME";

    private TextView tvDate, tvTime;
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private Spinner spinnerPriority;
    //private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Calendar calendar = Calendar.getInstance();
        //currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String date = dateFormat.format(calendar.getTime());
        String ntime = timeFormat.format(calendar.getTime());
        String time = ntime.replace("am", "AM").replace("pm", "PM");

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_Description);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            tvDate.setText(intent.getStringExtra(EXTRA_DATE));
            tvTime.setText(intent.getStringExtra(EXTRA_TIME));
            //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.priorityList,R.layout.style_spinner);
            String[] array = {"High", "Medium", "Low"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner, array);
            spinnerPriority.setAdapter(adapter);
            //spinnerPriority.setSelection(intent.getIntExtra(EXTRA_PRIORITY_NUMBER,1));
        } else {
            setTitle("Add Note");
            tvDate.setText(date);
            tvTime.setText(time);
            //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.priorityList,R.layout.style_spinner);
            String[] array = {"High", "Medium", "Low"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner, array);
            spinnerPriority.setAdapter(adapter);
            //spinnerPriority.setSelection(intent.getIntExtra(EXTRA_PRIORITY_NUMBER,1));
        }
    }

    private void saveNote() {

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String priority = spinnerPriority.getSelectedItem().toString();
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_PRIORITY_NUMBER, spinnerPriority.getSelectedItem().toString());
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_TIME, time);

        int priorityNumber = 0;

        if (priority.equals("High")) {
            priorityNumber = 3;
        } else if (priority.equals("Medium")) {
            priorityNumber = 2;
        } else if (priority.equals("Low")) {
            priorityNumber = 1;
        }

        data.putExtra(EXTRA_PRIORITY_NUMBER, priorityNumber);

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
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
