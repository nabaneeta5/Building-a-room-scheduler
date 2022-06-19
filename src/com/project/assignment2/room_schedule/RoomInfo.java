package com.project.assignment2.room_schedule;


import java.util.ArrayList;
import java.util.List;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;




@PersistenceCapable
public class RoomInfo {

	
	@PrimaryKey
	@Persistent
	private Key id;
	
	@Persistent
	private String room_name;
	
	
	
	@Persistent(mappedBy="parent")
	private List<BookingInfo> bookingList;

	public Key id() { return id; }
	public void setID(final Key id) { this.id = id; }
	

	public String room_name() { return room_name; }
	public void setRoomName(final String room_name) { this.room_name = room_name; }
	
		
	
	public void addBooking(BookingInfo c) {
	if(bookingList == null)
	bookingList = new ArrayList<BookingInfo>();
	bookingList.add(c);
	}
	
	public void removeBooking(BookingInfo c) {
		if(bookingList == null)
		bookingList = new ArrayList<BookingInfo>();
		bookingList.remove(c);
		}
	
	public List<BookingInfo> bookingList() { return bookingList; }
	

}
