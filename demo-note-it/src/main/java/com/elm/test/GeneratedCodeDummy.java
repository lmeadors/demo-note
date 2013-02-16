package com.elm.test;

import android.test.InstrumentationTestCase;
import android.util.Log;
import com.elm.R;

public class GeneratedCodeDummy extends InstrumentationTestCase {

	private static final String TAG = GeneratedCodeDummy.class.getName();

	public void test_should_cover_generated_code() throws IllegalAccessException, InstantiationException {

		// BuildConfig doesn't seem to be covered by anything I try :-/

		// create an instance of this so emma is aware of it
		assertNotNull(new R());

		// R has inner static classes - we'll create an instance of each of them so emma is aware of them
		final Class<?>[] classes = R.class.getClasses();
		for (Class inner : classes) {
			Log.d(TAG, "class: " + inner);
			inner.newInstance();
		}

	}

}
