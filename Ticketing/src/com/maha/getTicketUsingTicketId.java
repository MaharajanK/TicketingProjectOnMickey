package com.maha;

import java.util.HashMap;
import java.util.ArrayList;
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


public class getTicketUsingTicketId extends HttpServlet{
    
    public static String message;
    public static String parameterName;
    public void init() throws ServletException {
      
       message = "HELLO SERVLET";
    }

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
           
           
           PrintWriter out = response.getWriter();

           MickeyDataBaseClass get = new MickeyDataBaseClass();
           
           String fileName = FilenameUtils.getName(request.getRequestURI());
           String URI = request.getRequestURI();
           String[] URIArray = URI.split("/");
           int URILength = URIArray.length;
           
//           
//           Enumeration enumeration = request.getParameterNames();
//           int numberOfArg =0;
//           
//           while (enumeration.hasMoreElements()) {
//               parameterName = (String) enumeration.nextElement();
//               numberOfArg++;
//          }
//
//         
          HashMap<String, String> map1 = null;
          ArrayList<HashMap<String, String>> arr1 = null;
    
          
          if(URILength == 3) {
        	  try{     
//                arr1  = get.gettingAllTicketsDetails();
//                String json = new Gson().toJson(arr1);
//                out.println(json);
                out.println(get.gettingAllTicketsDetails());
                   
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




  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
    doGet(request, response);

  }

	
}



















