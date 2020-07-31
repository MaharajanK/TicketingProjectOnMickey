package com.maha;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class deleteTicket extends HttpServlet{
    
    public static String message;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }

	
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
       
	  PrintWriter out = response.getWriter();
	  
	  String Ticket_Id = FilenameUtils.getName(request.getRequestURI());         //......................print last URI
//      out.println(Ticket_Id);
//      out.println(request.getRequestURI());                                    //.................print URI
//      out.println(request.getQueryString());                                   //.................print parameter
	  
	  GetConnection get = new GetConnection();
	  
	  try {
		  out.println();
		  String result = get.deleteTicket(Ticket_Id);
			String json = new Gson().toJson(result);
	        out.println(json);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
       
  }

	
}

