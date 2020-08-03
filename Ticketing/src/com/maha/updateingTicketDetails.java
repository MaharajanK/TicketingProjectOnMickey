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



public class updateingTicketDetails extends HttpServlet{
    
    public static String message;
    public static String parameterName;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }

	
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  

      PrintWriter out = response.getWriter();

      String Ticket_Id = FilenameUtils.getName(request.getRequestURI());         //......................print last URI
//      out.println(Ticket_Id);
//      out.println(request.getRequestURI());                                    //.................print URI
//      out.println(request.getQueryString());                                   //.................print parameter
      
      String body = request.getReader().lines()
              .reduce("", (accumulator, actual) -> accumulator + actual);
      JSONObject jObject  = new JSONObject(body);
      
      HashMap<String,String> updatePera = new HashMap<String,String>();
      String[] fields = new String[]{"TASK","OWNER_ID","MAX_DATE","PRIORITY","EMP_ID","STSTUS","STARTING_DATE","FINISHING_DATE"};
   
      
      
    for (String keyStr : jObject.keySet()) {
    	
         String keyvalue = (String) jObject.get(keyStr);
         
         if(!Arrays.asList(fields).contains(keyStr)) {
        	 if(keyStr.equals("TICKET_ID")) {
        		 out.println("Can't update TICKET_Id value");
        	 }
        	 else {
        	 out.println("The field "+"'"+keyStr+"'"+" is not present in DB");
        	 
        	 }
        	 break;
         }
         else if(keyvalue.isEmpty()) {
        	 out.println("The field "+keyStr+"'s value is EMPTY."+"\n"+"Please enter correct value");
         }
         updatePera.put(keyStr, (String) keyvalue);
         
    }
     MickeyDataBaseClass get = new MickeyDataBaseClass();
      
     
     try {
		String result = get.updateTicketDetails(updatePera, Ticket_Id);
		String json = new Gson().toJson(result);
        out.println(json);
	 } 
     catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    

  }

	
}



















