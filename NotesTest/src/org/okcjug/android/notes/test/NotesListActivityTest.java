package org.okcjug.android.notes.test;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.okcjug.android.notes.NotesListActivity;
import org.okcjug.android.notes.R;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

public class NotesListActivityTest extends ActivityInstrumentationTestCase2<NotesListActivity>{
	private Solo solo;

	public NotesListActivityTest() {
		super(NotesListActivity.class);
		setActivityInitialTouchMode(false);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testAddingNotes() {
		solo.sendKey(Solo.MENU);
		solo.clickOnText("Add a Note");
		solo.waitForActivity("Add/Edit a Note", 2);
		final String name = generateRandomString();
		solo.enterText((EditText) solo.getView(R.id.noteName), name);
		solo.enterText((EditText) solo.getView(R.id.noteBody), generateRandomString());
		clickButton("OK");
		
		assertTrue(solo.searchText(name));
	}

	protected void clickButton(String buttonText) {
		for (final Button button : solo.getCurrentButtons()) {
			if (button.getText().equals(buttonText)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						button.performClick();
					}
				});
				getInstrumentation().waitForIdleSync();

				break;
			}
		}
	}
	
    protected String generateRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(16);
    } 
}
