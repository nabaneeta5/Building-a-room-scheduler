package com.project.assignment2.room_schedule;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet2 extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/html");
		String room=null;
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		String signin = us.createLoginURL("/");
		String signout = us.createLogoutURL("/");
		 HttpSession session=req.getSession();
		 try{
			room=session.getAttribute("room").toString();
		 }catch(Exception e){
			 resp.sendRedirect("/");
		 }
		req.setAttribute("user", user);
		req.setAttribute("signin", signin);
		req.setAttribute("signout", signout);
		req.setAttribute("room", room);
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root2.jsp");
		rd.forward(req, resp);
	}
	
	
}
