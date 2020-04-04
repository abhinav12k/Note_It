package com.example.achitectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Database(entities = {Note.class},version = 3)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;
    public abstract NoteDoa noteDoa();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,NoteDatabase.class,"notes_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDoa noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDoa();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Calendar calendar = Calendar.getInstance();
            String currentDate  = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//            String result = currentDate+", "+sdf.format(Calendar.getInstance().getTime());
//            System.out.println(result);

            noteDao.insert(new Note("Title 1", "Description 1", "High",3,currentDate));
            noteDao.insert(new Note("Title 2", "Description 2", "Medium",2,currentDate));
            noteDao.insert(new Note("Title 3", "Description 3", "Low",1,currentDate));
            return null;
        }
    }

}
