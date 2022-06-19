package com.project.assignment2.room_schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/html");
		int i=1;
		String s="<form action='/' method='post'>"
				+ "<input type='hidden' name='hidenParam' value='search'>"
				+ "<b>Search by date:  </b><input id='datepicker' autocomplete='off' name='date' />&nbsp;<input type='submit' value='Search'>"
				+ "</form>"
				+ "<br>";
		
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		String signin = us.createLoginURL("/");
		String signout = us.createLogoutURL("/");

		PersistenceManagerFactory per = PMF.get();
		PersistenceManager manager = per.getPersistenceManager();
		
		if(user!=null){
			HttpSession session=req.getSession();
			session.removeAttribute("room");
			resp.getWriter().println("<br>");
			resp.getWriter().println("<h4>User "+user.getEmail()+"</h4>");
			resp.getWriter().println("<br>");
			
		javax.jdo.Query qr = manager.newQuery(RoomInfo.class);
		qr.compile();
		@SuppressWarnings("unchecked")
		List<RoomInfo> l = (List<RoomInfo>) qr.execute();
		resp.getWriter().println("<h4>Available Rooms: </h4>");
		resp.getWriter().println("<br>");
		if (l.size() == 0) {
			resp.getWriter().println("No room infomation available.");
			resp.getWriter().println("<br>");
		} else {
			for (RoomInfo info : l) {
				resp.getWriter().println("<b>"+i+".   "+info.room_name()+"</b>");
				resp.getWriter().println("<br>");
				i++;
			}
			resp.getWriter().println("<br>");
			resp.getWriter().println(s);
			resp.getWriter().println("<br>");
			resp.getWriter().println("<form action='/' method='post'>");
			resp.getWriter().println("<input type='hidden' name='hidenParam' value='show_bookinfo'>");
			resp.getWriter().println("<b>Show Booking Information: </b>");
			resp.getWriter().println("<select autocomplete='off' name='show_booking_info'>");
			resp.getWriter().println("<option value='select'>select</option>");	
			
			for (RoomInfo info1 : l) {	
				resp.getWriter().println("<option value="+info1.room_name()+">"+info1.room_name()+"</option>");	
			}
			resp.getWriter().println("</select>");
			resp.getWriter().println("<input type='submit' value='Show'>");
			resp.getWriter().println("</form>");
			
			resp.getWriter().println("<br>");
			resp.getWriter().println("<br>");
			resp.getWriter().println("<form action='/' method='post'>");
			resp.getWriter().println("<input type='hidden' name='hidenParam' value='add_bookinfo'>");
			resp.getWriter().println("<b>Add Booking Information: </b>");
			resp.getWriter().println("<select autocomplete='off' name='add_booking_info' >");
			resp.getWriter().println("<option value='select'>select</option>");	
			
			for (RoomInfo info2 : l) {	
				resp.getWriter().println("<option value="+info2.room_name()+">"+info2.room_name()+"</option>");	
			}
			resp.getWriter().println("</select>");
			resp.getWriter().println("<input type='submit' value='Add'>");
			resp.getWriter().println("</form>");
			
			
		}	
		}
		manager.close();
		
		req.setAttribute("user", user);
		req.setAttribute("signin", signin);
		req.setAttribute("signout", signout);

		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);	
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/html");
		  
		String hidenParam=null;
		Key k=null;
		RoomInfo info=null;
		
		SimpleDateFormat simple_date_fmt = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		simple_date_fmt.setTimeZone(TimeZone.getDefault());
		
		PersistenceManagerFactory per = PMF.get();
		PersistenceManager manager = per.getPersistenceManager();
		
		 HttpSession session=req.getSession();
		 
		hidenParam=req.getParameter("hidenParam");
		
		if (hidenParam.contentEquals("add_room_name")) {
			String room= req.getParameter("new_room_name").toLowerCase();
			if (room.isEmpty() || room.contentEquals(" ")) {
				resp.getWriter().println("Give room name");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
			} else {
				k= KeyFactory.createKey("RoomInfo", room);
				try {
					info = manager.getObjectById(RoomInfo.class, k);
					resp.getWriter().println("Duplicate name , " + "\"" + room + "\"");
					resp.getWriter().println("<br>");
					resp.getWriter().println("<br>");
					resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
				} catch (javax.jdo.JDOObjectNotFoundException e) {
					info = new RoomInfo();
					info.setID(k);
					info.setRoomName(room);
					manager.makePersistent(info);
					resp.getWriter().println("Room:  "+ room+" added");
					resp.getWriter().println("<br>");
					resp.getWriter().println("<br>");
					resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
				}				
			}
		}
		else if (hidenParam.contentEquals("search")) 
		{
			ArrayList<String> array=new ArrayList<String>();
			String date=req.getParameter("date");
			Date d1 = null, d2 = null;
			int j = 1,c=0;
			if (date.isEmpty()) {
				resp.getWriter().println("Give date");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
			} else {
				try {
					d1 = simple_date_fmt.parse(date + " 00:00 AM");
					d2 = simple_date_fmt.parse(date + " 11:59 PM");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				javax.jdo.Query qr = manager.newQuery(RoomInfo.class);
				qr.compile();
				@SuppressWarnings("unchecked")
				List<RoomInfo> l = (List<RoomInfo>) qr.execute();
				 if (l.size() == 0) {
					 resp.getWriter().println("No room information available ");
					 resp.getWriter().println("<br>");
				 }
				 else{
					 resp.getWriter().println("<b>Room Information: </b>");
					 resp.getWriter().println("<br>");
				for (RoomInfo infoo : l) {
					if (infoo.bookingList().size() == 0) 
					{
						c++;
					}else{
						for (BookingInfo bi : infoo.bookingList()) {
							try {
								String start_time = bi.from();
								String end_time = bi.to();
								if ((simple_date_fmt.parse(start_time).compareTo(d2) <= 0 && simple_date_fmt.parse(end_time).compareTo(d1) >= 0)) {
								array.add("Name: "+infoo.room_name().toString());
								array.add(j + ". Booking Name: " + bi.booking_name()+ " from: " + bi.from() + " to " +bi.to());
								j++;
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							
						}
						
					}
					}
				if(l.size()==c){
					resp.getWriter().println("No booking information available for all rooms. ");
					 resp.getWriter().println("<br>");
					
				}else if(array.size()!=0){
					for(int m=0;m<array.size();m++){
					resp.getWriter().println(array.get(m).toString());
					 resp.getWriter().println("<br>");
					}
					
				}else if(array.size()==0){
					resp.getWriter().println("Booking information not available on the date ");
					 resp.getWriter().println("<br>");
					
				}
				
				 }
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
			}
			
		}
		else if (hidenParam.contentEquals("show_bookinfo")) 
		{
			String room_book_info=req.getParameter("show_booking_info");
			if(room_book_info.isEmpty()||room_book_info.contentEquals("select")){
				resp.getWriter().println("Select room name");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
				
			}else{
				javax.jdo.Query qr = manager.newQuery(RoomInfo.class,"room_name == roomNameParam");
				qr.declareParameters("String roomNameParam");
				qr.compile();
				@SuppressWarnings("unchecked")
				List<RoomInfo> l = (List<RoomInfo>) qr.execute(room_book_info);
				if(l.size()!=0){
				for (RoomInfo infoo : l) {
						resp.getWriter().println("<b> Room Booking Information for : " +infoo.room_name().toString() +"</b>");
						resp.getWriter().println("<br>");

						if (infoo.bookingList().size() == 0) {
							resp.getWriter().println("No booking information available.");
							resp.getWriter().println("<br>");
						} else {
							int i = 1;
							for (BookingInfo bi : infoo.bookingList()) {
								String st[] = bi.id().toString().split("/");
								resp.getWriter().println("<form action='/' method='post'>");
								resp.getWriter().println("<input type='hidden' name='hidenParam' value='remove_bookinfo'>");
								resp.getWriter().println("<input type='hidden' name='booking_id' value=" + st[1]+ ">");
								resp.getWriter().println("<input type='hidden' name='booking_name' value=" +bi.booking_name() + ">");
								resp.getWriter().println("<input type='hidden' name='room' value="+ infoo.room_name() + ">");
								resp.getWriter().println(i + ". Booking Name: " + bi.booking_name()+ " from: " + bi.from() + " to " +bi.to());
								resp.getWriter().println("<input type='submit' value='Remove'>");
								resp.getWriter().println("</form>");
								resp.getWriter().println("<br>");
								i++;
							}
						}
					}
			}
				else{
				resp.getWriter().println("Room Name Not found");
				resp.getWriter().println("<br>");
			}
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");

				
			}
		}
			else if (hidenParam.contentEquals("add_bookinfo")) {
				String add_room_info=req.getParameter("add_booking_info");
				if(add_room_info.isEmpty()||add_room_info.contentEquals("select")||add_room_info.contains(" ")){
					resp.getWriter().println("Select room name");
					resp.getWriter().println("<br>");
					resp.getWriter().println("<br>");
					resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
					
				}else{
						javax.jdo.Query qr = manager.newQuery(RoomInfo.class,"room_name == roomNameParam");
						qr.declareParameters("String roomNameParam");
						qr.compile();
						@SuppressWarnings("unchecked")
						List<RoomInfo> l = (List<RoomInfo>) qr.execute(add_room_info);
						if(l.size()!=0){
							session.setAttribute("room", add_room_info);
							resp.sendRedirect("/addBooking");
					}else{
							resp.getWriter().println("Room Name not found");
							resp.getWriter().println("<br>");
							resp.getWriter().println("<br>");
							resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
							
						}		
				}
			
		}else if(hidenParam.contentEquals("add_book_info"))
		{
			int i=0,j=0;
			String date = simple_date_fmt.format(new Date());
			Date date1=null,date2=null,date3=null;
			String room=req.getParameter("room");
			String newbook=req.getParameter("newbook");
			String input_from=req.getParameter("input_from");
			String t1_hh=req.getParameter("t1_hh");
			String t1_mm=req.getParameter("t1_mm");
			String t1_fmt=req.getParameter("t1_fmt");
			String input_to=req.getParameter("input_to");
			String t2_hh=req.getParameter("t2_hh");
			String t2_mm=req.getParameter("t2_mm");
			String t2_fmt=req.getParameter("t2_fmt");
			
			if(newbook.isEmpty()||newbook.contains(" ")){
				resp.getWriter().println("Give booking name");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
			}else if(input_from.isEmpty()||input_to.isEmpty()){
				resp.getWriter().println("Give booking date");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
				
			}
			else if(t1_hh.contentEquals("select")||t1_mm.contentEquals("select")||t1_fmt.contentEquals("select")||t2_hh.contentEquals("select")||t2_mm.contentEquals("select")||t2_fmt.contentEquals("select")){
				resp.getWriter().println("Give booking time");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("<br>");
				resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
				
			}
			else{
				try {
					date1 = simple_date_fmt.parse(date);
					 date2 = simple_date_fmt.parse(input_from + " " + t1_hh + ":" + t1_mm+" "+t1_fmt);
					 date3 = simple_date_fmt.parse(input_to + " " + t2_hh + ":" + t2_mm+" "+t2_fmt);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (date2.before(date1) || date3.before(date1) || date3.before(date2)) {
					resp.getWriter().println(
							"Booking date cannot be previous date from current date or End date cannot be previous than start date");
					resp.getWriter().println("<br>");
					resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
					resp.getWriter().println("<br>");
					resp.getWriter().println("<br>");
					resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");

				}else{
					k = KeyFactory.createKey("RoomInfo", room);
					info = manager.getObjectById(RoomInfo.class, k);
					
					for (BookingInfo bi : info.bookingList()) {
						try {
							String start_time = bi.from();
							String end_time = bi.to();
							if (simple_date_fmt.parse(start_time).compareTo(date3) <= 0 && simple_date_fmt.parse(end_time).compareTo(date2) >= 0) {
								i++;
							for (BookingInfo bi1 : info.bookingList()) {
									if (bi1.booking_name().contentEquals(newbook)) {
										j++;
									}
								}
							}

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (i == 0 && j == 0) {
						BookingInfo binfo = new BookingInfo();
						binfo.setParent(info);
						binfo.setBookingName(newbook);
						binfo.setFrom(input_from + " " + t1_hh + ":" + t1_mm+" "+t1_fmt);
						binfo.setTo(input_to + " " + t2_hh + ":" + t2_mm+" "+t2_fmt);
						info.addBooking(binfo);
						manager.makePersistent(binfo);
						manager.makePersistent(info);
						resp.getWriter().println(" Booking Name: " + newbook + " from: " + input_from + " " + t1_hh + ":" + t1_mm+" "+t1_fmt  + " to " +input_to + " " + t2_hh + ":" + t2_mm+" "+t2_fmt);
						resp.getWriter().println("<br>");
						resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
						resp.getWriter().println("<br>");
						resp.getWriter().println("<br>");
						resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");

					}else{
						if (j != 0) {
							resp.getWriter().println(
									"Duplicate Booking name , "+"\"" + newbook);
							resp.getWriter().println("<br>");
						}
						if (i != 0) {
							resp.getWriter().println("Booking overlap.");
							resp.getWriter().println("<br>");

						}
						resp.getWriter().println("<br>");
						resp.getWriter().println("<button type='button' name='back' onclick='history.back()'>BACK</button>");
						resp.getWriter().println("<br>");
						resp.getWriter().println("<br>");
						resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
					}
				}
				
			}
			
		}else if(hidenParam.contentEquals("remove_bookinfo"))
		{
			String room = req.getParameter("room");
			String booking_id = req.getParameter("booking_id");
			String booking_name = req.getParameter("booking_name");
			String st = "RoomInfo(" + "\"" + room + "\")" + "/" + booking_id;
			
			k = KeyFactory.createKey("RoomInfo", room);
			RoomInfo infoo = manager.getObjectById(RoomInfo.class, k);
			for (BookingInfo bi : infoo.bookingList()) {
				if (bi.id().toString().contentEquals(st)) {
					infoo.removeBooking(bi);
				}
			}
			manager.makePersistent(infoo);
			resp.getWriter().println("Booking Name "+ booking_name + " removed");
			resp.getWriter().println("<br>");
			resp.getWriter().println("<br>");
			resp.getWriter().println("<form action='/' method='post'>");
			resp.getWriter().println("<input type='hidden' name='hidenParam' value='show_bookinfo'>");
			resp.getWriter().println("<input type='hidden' name='show_booking_info' value="+room+">");
			resp.getWriter().println("<input type='submit' value='BACK'>");
			resp.getWriter().println("</form>");
			resp.getWriter().println("<br>");
			resp.getWriter().println("<br>");
			resp.getWriter().println("&nbsp; &nbsp;<a href='/'>Home Page</a>");
			
		}
		
		
		manager.close();
		
	}
	
}
