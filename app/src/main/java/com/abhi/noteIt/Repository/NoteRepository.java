package com.abhi.noteIt.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.abhi.noteIt.Database.NoteDatabase;
import com.abhi.noteIt.Database.NoteDoa;
import com.abhi.noteIt.Model.Note;

import java.util.List;

public class NoteRepository  {

    private NoteDoa noteDoa;
    private LiveData<List<Note>> allNotes;
    public NoteRepository(Application application) {

        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDoa = database.noteDoa();
        allNotes = noteDoa.getAllNotes();

    }

    public void insert(Note note) {
        new InsertNodeAsyncTask(noteDoa).execute(note);
    }

    public void update(Note note) {
        new UpdateNodeAsyncTask(noteDoa).execute(note);
    }

    public void delete(Note note) {
        new DeleteNodeAsyncTask(noteDoa).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNodeAsyncTask(noteDoa).execute();
    }

    public LiveData<List<Note>> getSearchedNotes(String NoteText){
        return noteDoa.getSearchedNotes(NoteText);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNodeAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDoa noteDoa;

        private InsertNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDoa.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNodeAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDoa noteDoa;

        private UpdateNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDoa.update(notes[0]);
            return null;
        }
    }


    private static class DeleteNodeAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDoa noteDoa;

        private DeleteNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDoa.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNodeAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDoa noteDoa;

        private DeleteAllNodeAsyncTask(NoteDoa noteDoa) {
            this.noteDoa = noteDoa;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDoa.deleteAllNotes();
            return null;
        }
    }
}
