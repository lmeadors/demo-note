package com.elm.test;

import android.test.ActivityInstrumentationTestCase2;
import com.elm.view.NoteListActivity;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<NoteListActivity> {

    public HelloAndroidActivityTest() {
        super(NoteListActivity.class);
    }

    public void testActivity() {
        NoteListActivity activity = getActivity();
        assertNotNull(activity);
    }
}

