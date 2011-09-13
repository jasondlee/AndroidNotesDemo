package org.okcjug.android.notes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditNoteActivity extends Activity {
	private DatabaseAdapter dbAdapter;
	private String noteId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_note);
            dbAdapter = DatabaseAdapter.instance(this);
            noteId = this.getIntent().getStringExtra("noteId");

            findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", noteId);
                    resultIntent.putExtra("name", getTextValue(R.id.noteName));
                    resultIntent.putExtra("body", getTextValue(R.id.noteBody));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
            findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                };
            });

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

    protected String getTextValue(int id) {
    	TextView tv = (TextView)findViewById(id);
    	return tv.getText().toString();
    }

    protected void setTextValue(int id, String value) {
    	TextView tv = (TextView)findViewById(id);
    	tv.setText(value);
    }
}