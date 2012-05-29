package de.mcdorians.codebase.locbano;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Id;

public class Notebook {
	
	public final double DISTANCE = 10.0;
	
	@Id
	private String token;
	
	private List<Long> notes;
	
	public Notebook(){
		
	}
	
	public Notebook(String token){
		this.token=token;
	}
	
	public List<Long> getNotesIds(){
		if(notes==null){
			notes= new ArrayList<Long>();
		}
		return notes;
	}
	
	public List<Note> getNotes(){
		NotebookDAO dao = new NotebookDAO();
		return dao.getNotes(getNotesIds());
	}
	
	public List<Note> getNotes(double lat,double lng){
		NotebookDAO dao = new NotebookDAO();
		List<Note> all = getNotes();
		SortedSet<Note> sorted = new TreeSet<Note>(new Comparator<Note>() {

			@Override
			public int compare(Note n1, Note n2) {
				if(n1.getLastDistance()<n2.getLastDistance()){
					return -1;
				}else{
					if(n1.getLastDistance()==n2.getLastDistance()){
						return 1; // actually 0
					}else{
						return 1;
					}
				}
			}
		});
		for(Note n:all){			
			double distance =GeoMath.calcDistance(lat, lng, n.getLat(), n.getLng());
			n.setLastDistance(distance);
			sorted.add(n);
			dao.ofy().put(n);
		}
		return new ArrayList<Note>(sorted);
	}

}
