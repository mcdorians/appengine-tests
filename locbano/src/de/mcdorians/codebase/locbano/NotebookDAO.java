package de.mcdorians.codebase.locbano;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class NotebookDAO extends DAOBase {

	static {
		ObjectifyService.register(Notebook.class);
		ObjectifyService.register(Note.class);
	}

	public Notebook getNotebook(String token) {
		Notebook found = ofy().find(Notebook.class, token);
		if (found == null) {
			return null;
		} else {
			return found;
		}
	}
	
	public Note getNote(long id) {
		Note found = ofy().find(Note.class, id);
		if (found == null) {
			return null;
		} else {
			return found;
		}
	}
	
	public List<Note> getNotes(List<Long> notes){
		Map<Long, Note> fetched = ofy().get(Note.class,notes);
		return new ArrayList<Note>(fetched.values());
	}

}
