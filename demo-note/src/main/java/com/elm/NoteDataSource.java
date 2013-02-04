package com.elm;

import com.elm.bean.Note;

import java.util.List;

public interface NoteDataSource {

	// these are used by the *Async methods to name the actions
	public static final String LIST_ACTION = NoteDataSource.class.getName() + ".list";
	public static final String FETCH_ACTION = NoteDataSource.class.getName() + ".fetch";
	public static final String INSERT_ACTION = NoteDataSource.class.getName() + ".insert";
	public static final String UPDATE_ACTION = NoteDataSource.class.getName() + ".update";
	public static final String DELETE_ACTION = NoteDataSource.class.getName() + ".delete";

	List<Note> list();

	Note fetch(Integer id);

	Note insert(Note note);

	Note update(Note note);

	Integer delete(Integer id);

	void listAsync();

	void fetchAsync(Integer id);

	void insertAsync(Note note);

	void updateAsync(Note note);

	void deleteAsync(Integer id);

}
