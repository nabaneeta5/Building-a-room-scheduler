package com.project.assignment2.room_schedule;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class BookingInfo {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String booking_name;
	
	@Persistent
	private String from;
	
	@Persistent
	private String to;
	
	@Persistent
	private RoomInfo parent;
	
	public Key id() { return id; }
	
	public String booking_name() { return booking_name; }
	public void setBookingName(final String booking_name) { this.booking_name = booking_name; }
	
	
		public String from() { return from; }
		public void setFrom(final String from) { this.from = from; }
		
		public String to() { return to; }
		public void setTo(final String to) { this.to = to; }
		
	public void setParent(final RoomInfo parent) { this.parent = parent; }
	
}
