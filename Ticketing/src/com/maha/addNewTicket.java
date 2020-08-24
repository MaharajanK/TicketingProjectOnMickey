package com.maha;

import org.json.JSONObject;

import com.adventnet.persistence.Persistence;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class addNewTicket extends HttpServlet{
    
    public static String message;
    public static Persistence persobj;
    public static String parameterName;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }

	
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
 

      PrintWriter out = response.getWriter();

       String body = request.getReader().lines()
           .reduce("", (accumulator, actual) -> accumulator + actual);
      // out.println(body);

       String Ticket_Id = null;
       String Task = null;
       String Owner_Id = null;
       String Priority = null;
       String Assigned_To = null;
       String Max_Date = null;
       
       
//     LocalDate Max_Date = LocalDate.parse(Date);
//     Date Max_D=Date.valueOf(Max_Date);
     
       JSONObject jObject  = new JSONObject(body);
       
       String[] fields = new String[]{"TASK","OWNER_ID","PRIORITY","EMP_ID","MAX_DATE"};
       boolean Authorization = true;
       
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
    	  String ticket_Id = null;
   	   
   	   out.println("jObject :"+jObject.length());
   	 if(jObject.length() == 5) {
   		 Task=(String) jObject.get("TASK");
	         Owner_Id=(String) jObject.get("OWNER_ID");
	         Priority=(String) jObject.get("PRIORITY");
	         Assigned_To=(String) jObject.get("EMP_ID");
	         Max_Date=(String) jObject.get("MAX_DATE");
	         
	      	 MickeyDataBaseClass get = new MickeyDataBaseClass();
	             
	         try {
	        	 
	        	  persobj = PersistenceClass.getInstance();
	        	  
	        	  
	    	       
	    	       int count = get.findingTotalTicketCounts();
	    	       ticket_Id = "DB-T"+(count);
	    	    	      
	              String result = get.createNewTicket(ticket_Id, Task, Owner_Id, Priority, Assigned_To, Max_Date); 
	              String json = new Gson().toJson(result);
	              out.println(json);
	      	 } 
	         catch (Exception e) {
	      		  e.printStackTrace();
	         }
	   
	         
   	 }  
   	 
   	 else {
   		 CustomColumns c_Columns = new  CustomColumns();
   		 for(int i=0;i<c_Columns.allUserDefiedColumnNames.size();i++) {
   		    	 
   			 try {
					c_Columns.addDataToUserDefiendColumns(ticket_Id, c_Columns.allUserDefiedColumnNames.get(i), (String) jObject.get(c_Columns.allUserDefiedColumnNames.get(i)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   		 
   		 }
   		 
   	 }
     }
       
       
       
       
       
        
        
       
       
       
       
       

       
       

  }

	
}



















