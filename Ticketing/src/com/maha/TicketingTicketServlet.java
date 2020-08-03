package com.maha;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;

import com.google.gson.Gson;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import com.adventnet.persistence.*;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TicketingTicketServlet extends HttpServlet{
    
    public static String message;
    public static String parameterName;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }
    
    
    
    
    
    //http://localhost:8080/Ticketing/Tickets--------------> all ticket Details
    //and
    //http://localhost:8080/Ticketing/Tickets/(any ticket ID)--------------> all ticket Details

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
           
           
           PrintWriter out = response.getWriter();

           MickeyDataBaseClass get = new MickeyDataBaseClass();
           
           String fileName = FilenameUtils.getName(request.getRequestURI());
           String URI = request.getRequestURI();
           String[] URIArray = URI.split("/");
           int URILength = URIArray.length;
//           
//           out.println("URILength : "+URILength );
//           out.println("URI : "+URI );
//           out.println("fileName : "+fileName );
           
           
           HashMap<String, String> map1 = null;
           ArrayList<HashMap<String, String>> arr1 = null;
    
          
          
          if(URILength == 3) {
        	  try{   
                  arr1  = get.gettingAllTicketsDetails();
                  String json = new Gson().toJson(arr1);
                  out.println(json);
                   
              }
              catch(Exception e){
                  out.println(e);
              }
          }

          else if(URILength == 4) {
        	  
                  
                  try{
                           map1  = get.gettingTicketDetails(fileName);
                           String json = new Gson().toJson(map1);
                           out.println(json);
                            
                  }
                  catch(Exception e){
                           out.println(e);
                  }

          }
	}


	//http://localhost:8080/Ticketing/post  -------------->  Create the new ticket 

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   
		       PrintWriter out = response.getWriter();
		      
		       
		       String fileName = FilenameUtils.getName(request.getRequestURI());
	           String URI = request.getRequestURI();
	           String[] URIArray = URI.split("/");
	           int URILength = URIArray.length;
	           
	           
		       String[] fields = new String[]{"TASK","OWNER_ID","PRIORITY","EMP_ID","MAX_DATE"};
		       String Task = null;
		       String Owner_Id = null;
		       String Priority = null;
		       String Assigned_To = null;
		       String Max_Date = null;
		       boolean Authorization = true;
		       
		       
		       
		   if(URILength == 4 && fileName.equals("post")) {
			   
			       String body = request.getReader().lines()
			             .reduce("", (accumulator, actual) -> accumulator + actual);
			    
			       JSONObject jObject  = new JSONObject(body);
			       
			       
			   
			      for (String keyStr : jObject.keySet()) {
			    	 
			           Object keyvalue = jObject.get(keyStr);

			           if(!Arrays.asList(fields).contains(keyStr)) {
			        	   out.println("Specified field "+keyStr+" not present in DB");
			        	   Authorization = false;
			        	   break;
			           }
			           else if(jObject.get(keyStr) == null || ((String) jObject.get(keyStr)).isEmpty()) {
			        	   out.println("Specified field "+keyStr+"'s value is EMPTY"+"\n"+"Please enter correct value");
			        	   Authorization = false;
			        	   break;
			        	 
			           }
			         
			       }
			   
			       if(Authorization == true) {
			    	 
			         Task=(String) jObject.get("TASK");
			         Owner_Id=(String) jObject.get("OWNER_ID");
			         Priority=(String) jObject.get("PRIORITY");
			         Assigned_To=(String) jObject.get("EMP_ID");
			         Max_Date=(String) jObject.get("MAX_DATE");
			    
			      	 MickeyDataBaseClass get = new MickeyDataBaseClass();
			             
			         try {
			            	   
			              String result = get.createNewTicket(Task, Owner_Id, Priority, Assigned_To, Max_Date); 
			              String json = new Gson().toJson(result);
			              out.println(json);
			      	 } 
			         catch (Exception e) {
			      		  e.printStackTrace();
			         }
			   
			         
			     }
		   }   
		    
	 }
	 
	 
	 //http://localhost:8080/Ticketing/Tickets/put/(any ticket ID)   ---------------->  update the specified Ticket 
	 
	  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   
		      PrintWriter out = response.getWriter();
               
		       String Ticket_Id = FilenameUtils.getName(request.getRequestURI()); 
	           String URI = request.getRequestURI();
	           String[] URIArray = URI.split("/");
	           int URILength = URIArray.length;
	           
	           if(URILength == 5 && URIArray[URILength-2].equals("put")) {
	        	   
	        	   
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
	 				e.printStackTrace();
	 			 }
	 		    
	 		     
	           }
		     

		  }
	  
	  
	  
	  
	  //http://localhost:8080/Ticketing/Tickets/delete/(any ticket ID)   ---------------->  delete the specified Ticket 
	  
	  
	  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    
			   PrintWriter out = response.getWriter();
			  
			   String Ticket_Id = FilenameUtils.getName(request.getRequestURI());       
	           String URI = request.getRequestURI();
	           String[] URIArray = URI.split("/");
	           int URILength = URIArray.length;
	           
	           
			  if(URILength == 5 && URIArray[URILength-2].equals("delete")) {
				  
				  MickeyDataBaseClass get = new MickeyDataBaseClass();
				  try {
					    String result = get.deleteTicket(Ticket_Id);
						String json = new Gson().toJson(result);
				        out.println(json);
				  } 
				  catch (Exception e) {
					    e.printStackTrace();
				  }
			  }
			  
			  
		       
		  }

	
}



















