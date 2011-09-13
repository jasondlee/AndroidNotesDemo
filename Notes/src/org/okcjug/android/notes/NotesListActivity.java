package org.okcjug.android.notes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NotesListActivity extends ListActivity {
    private static final int ACTIVITY_ADD_EDIT_NOTE = 1;
    private static final int ACTIVITY_VIEW_NOTE = 2;
    
    protected static final int CONTEXTMENU_EDIT_NOTE = 0;
    protected static final int CONTEXTMENU_DELETE_NOTE = 1;
    
    private DatabaseAdapter dbAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbAdapter = DatabaseAdapter.instance(this);
        
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int pos, long id) {
                onListItemClick(view,pos,id);
            }
        });
        
		getListView().setOnCreateContextMenuListener(
				new View.OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("Note Menu");
						menu.add(0, CONTEXTMENU_EDIT_NOTE, 1, "Edit Note");
						menu.add(0, CONTEXTMENU_DELETE_NOTE, 1, "Delete Note");
					}
				});
        
        fillData();
    }
    
	public void fillData() {
        try {
            Cursor notesCursor = dbAdapter.fetchAllNotes();
            startManagingCursor(notesCursor);

            SimpleCursorAdapter scouts = new SimpleCursorAdapter(this, 
            		R.layout.note_row, 
            		notesCursor, 
                    new String[] { "_id", "name" }, 
                    new int[] { R.id.noteId, R.id.noteName });
            setListAdapter(scouts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.menuAddNote:
	            Intent intent = new Intent();
	            intent.setClassName("org.okcjug.android.notes", EditNoteActivity.class.getCanonicalName());
	            startActivityForResult(intent, ACTIVITY_ADD_EDIT_NOTE);
	            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
	public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        Cursor c = (Cursor)getListView().getAdapter().getItem(info.position);
		final String noteId = c.getString(c.getColumnIndex("_id"));

		switch (item.getItemId()) {
	        case CONTEXTMENU_EDIT_NOTE:
	            Intent intent = new Intent();
				intent.putExtra("noteId", noteId);
	            intent.setClassName("org.okcjug.android.notes", EditNoteActivity.class.getCanonicalName());
	            startActivityForResult(intent, ACTIVITY_ADD_EDIT_NOTE);
	            fillData();
	            return true;
	        case CONTEXTMENU_DELETE_NOTE:
	        	dbAdapter.deleteNote(noteId);
	        	fillData();
	        	return true;
	    }
		return super.onContextItemSelected(item);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_ADD_EDIT_NOTE: {
                if (resultCode == RESULT_OK) {
                	String id = (String)data.getExtras().get("id");
                	String name = (String)data.getExtras().get("name");
                	String body = (String)data.getExtras().get("body");
                	if (id != null) {
                		dbAdapter.updateNote(id, name, body);
                		toast("Note updated");
                	} else {
                		dbAdapter.addNote(name, body);
                		toast("Note added");
                	}
                }
                break;
            }
        }
        
        fillData();
    }

    protected void onListItemClick(View view, int position, long id) {
    	Cursor c = (Cursor)getListView().getAdapter().getItem(position);

    	Intent intent = new Intent();
        intent.putExtra("noteId", c.getString(c.getColumnIndex("_id")));
        intent.setClassName("org.okcjug.android.notes", ViewNoteActivity.class.getCanonicalName());
        startActivityForResult(intent, ACTIVITY_VIEW_NOTE);
    }
    
    protected void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}