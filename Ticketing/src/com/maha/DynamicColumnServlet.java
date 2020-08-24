

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



public class DynamicColumnServlet extends HttpServlet{
    
	
    public void init() throws ServletException {
      
    }

    //http://localhost:8080/Ticketing/Column/newColumn/INTEGER                  -------------->   all Employees Details
    
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
           
           
		 PrintWriter out = response.getWriter();

		 CustomColumns C_column = new CustomColumns();
         String fileName = FilenameUtils.getName(request.getRequestURI());
         String URI = request.getRequestURI();
         String[] URIArray = URI.split("/");
         int URILength = URIArray.length;
         
         
         out.println(URILength);
         out.println(URIArray[URILength - 2]);
         out.println(fileName);
         String result;
         
         if(URILength == 5) {
        	 out.println("................hi..................");
        	  try{   
                  result  = C_column.getting_Custom_Column(URIArray[URILength - 2] ,fileName);
                  String json = new Gson().toJson(result);
                  out.println(json);
                   
              }
              catch(Exception e){
                  out.println(e);
              }
        	 
         }
           
    }

}




















