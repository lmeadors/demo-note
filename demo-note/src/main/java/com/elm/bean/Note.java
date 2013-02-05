package com.elm.bean;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

	private final Long id;
	private final Date date;
	private final String title;
	private final String text;

	public Note(Long id, Date date, String title, String text) {
		this.title = title;
		this.text = text;
		this.date = date;
		this.id = id;
	}

	public Note(Long newId, Note note) {
		this(newId, note.getDate(), note.getTitle(), note.getText());
	}

	public Note() {
		id = null;
		date = new Date();
		title = null;
		text = null;
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

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "Note{" + id + ":" + title + "}";
	}

}
