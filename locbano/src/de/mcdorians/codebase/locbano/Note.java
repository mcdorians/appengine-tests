package de.mcdorians.codebase.locbano;

import java.util.Date;

import javax.persistence.Id;

public class Note {
	
	@Id
	private Long id;
	
	private double lat,lng;
	
	private String text;
	
	private Date date;
	
	private double lastDistance =0;
	
	public Note(){
		
	}
	
	public Note(String text,double lat,double lng){
		this.setText(text);
		this.lat=lat;
		this.lng=lng;
		this.date = new Date();
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLng() {
		return lng;
	}


	public long getId() {
		return id;
	}

	public void setLastDistance(double lastDistance) {
		this.lastDistance = lastDistance;
	}

	public double getLastDistance() {
		return lastDistance;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	
	

}
