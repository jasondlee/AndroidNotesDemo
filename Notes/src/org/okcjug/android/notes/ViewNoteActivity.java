package org.okcjug.android.notes;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewNoteActivity extends Activity {
	private DatabaseAdapter dbAdapter;
	private String noteId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_note);
            dbAdapter = DatabaseAdapter.instance(this);
            noteId = this.getIntent().getStringExtra("noteId");

            fillData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void fillData() {
        try {
        	if (noteId != null) {
	            Cursor cursor = dbAdapter.getNote(noteId);
	            cursor.moveToFirst();
	            setTextValue(R.id.noteName, cursor.getString(cursor.getColumnIndex("name")));
	            setTextValue(R.id.noteBody, cursor.getString(cursor.getColumnIndex("body")));
	            cursor.close();
        	}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void onButtonPressed(View view) {
		finish();
	}

    protected void setTextValue(int id, String value) {
    	TextView tv = (TextView)findViewById(id);
    	tv.setText(value);
    }
}