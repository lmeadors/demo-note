package com.elm.utility;

public class NullUtility {

	public boolean nullOrZero(Number noteId) {
		return noteId == null || noteId.equals(0);
	}

}
