package com.maha;

import java.util.HashMap;
import java.util.ArrayList;

import com.google.gson.Gson;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import java.util.Enumeration;

import com.adventnet.persistence.*;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class getTicketUsingEmployeeId extends HttpServlet{
    
    public static String message;
    public static String parameterName;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
           
           
           PrintWriter out = response.getWriter();

           MickeyDataBaseClass get = new MickeyDataBaseClass();
           
           
           String fileName = FilenameUtils.getName(request.getRequestURI());
           ArrayList<HashMap<String, String>> arr1 = null;
           
           HashMap<String, String> empDetailMAP = null;
           
           String URI = request.getRequestURI();
           
           String[] URIArray = URI.split("/");
           int URILength = URIArray.length;
           
           if(URILength == 3) {
            	   arr1  = get.gettingAllEmployeeDetails("All");
                   String json = new Gson().toJson(arr1);
                   out.println(json);
               
           }
           
           else if(URILength == 4) {
        	   empDetailMAP = new HashMap<String, String>();
        	try {
        		arr1  = get.gettingAllEmployeeDetails(fileName);
                String json = new Gson().toJson(arr1);
                out.println(json);
			} 
        	
        	
        	catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
           
           else if(URILength == 5) {
        	   if(fileName.equals("Tickets")) {
        		   try {
        			   
        			   String Emp_id = URIArray[URIArray.length - 2];
        			   
					   arr1  = get.gettingEmployeesDetails(Emp_id);
	                   String json = new Gson().toJson(arr1);
	                   out.println(json);
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		   
        	   }
           }
           
           else {
        	   System.out.println("Enter into 6");
           }
         
          
	}


  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
    
  }
}






















