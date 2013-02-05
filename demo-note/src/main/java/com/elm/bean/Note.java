package com.elm.bean;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

	private final Long id;
	private final Date date;
	private final String title;
	private final String note;

	public Note(Long id, Date date, String title, String note) {
		this.title = title;
		this.note = note;
		this.date = date;
		this.id = id;
	}

	public Note(Long newId, Note note) {
		this(newId, note.getDate(), note.getTitle(), note.getNote());
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getNote() {
		return note;
	}

	@Override
	public String toString() {
		return "Note{" + id + ":" + title + "}";
	}

}
