package com.maha;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import com.adventnet.db.api.RelationalAPI;
import com.adventnet.db.persistence.metadata.ColumnDefinition;
import com.adventnet.ds.query.AlterTableQuery;
import com.adventnet.ds.query.AlterTableQueryImpl;
import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.DataSet;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;

public class CustomColumns {

	 public static Scanner Scan = new Scanner(System.in);
	 public static Persistence persobj;
	 public static int count = 0;
	 public static ArrayList<String> allUserDefiedColumnNames = new ArrayList<String>();
	 
	 public static String getting_Custom_Column(String newColumnName, String dataType) throws Exception {
  	      
		 
//  	      String dataType   = Scan.next();
//  	      String newColumnName = Scan.next();
  	   
		 
		 try {
			 
			  persobj = PersistenceClass.getInstance();
		  	 
	  	      SelectQuery sq = new SelectQueryImpl(new Table("UserDefinedColumnsTable_Information"));
	  	      Column c1 = new Column("UserDefinedColumnsTable_Information", "*");
	          sq.addSelectColumn(c1);
	          
	          Criteria selectCt = new Criteria(new Column("UserDefinedColumnsTable_Information", "DATA_TYPE"), dataType, QueryConstants.EQUAL);
	          Criteria selectCt1 = new Criteria(new Column("UserDefinedColumnsTable_Information", "COLUMN_NAME"),"COLUMN*",QueryConstants.LIKE);
	          Criteria c = selectCt.and(selectCt1);
	          sq.setCriteria(c);
		      DataObject dataobj = persobj.get(sq);
		      Row row =dataobj.getFirstRow("UserDefinedColumnsTable_Information");
		      String old_Column_Name = (String) dataobj.getFirstRow("UserDefinedColumnsTable_Information").get("COLUMN_NAME");
		      
		      
		     
		      // UPDATE THE COLUMN NAME AND THE STATUS OF THE COLUMN IN "UserDefinedColumnsTable_Information".
		      
		      row.set("COLUMN_NAME",newColumnName);
		      row.set("STATUS","USED");
		      dataobj.updateRow(row);
	          persobj.update(dataobj);
		      
	          // CHANGEING THE COLUMN NAME OF "User_Defined_Columns"
	          
		      AlterTableQuery aq = new AlterTableQueryImpl("User_Defined_Columns");
		      aq.renameColumn(old_Column_Name, newColumnName);
		      persobj.alterTable(aq);
		      
		     
		      // ADDING THE COLUMN NAME TO THE "fields" ARRAYLIST IN "TicketingTicketServlet" CLASS.
		      
		      TicketingTicketServlet g = new TicketingTicketServlet();

		      g.fields.add(newColumnName);
		      g.fieldsForPut.add(newColumnName);
		      
		      for(int i=0; i<g.fields.size();i++) {
		    	  System.out.println(g.fields.get(i));
		      }
		      
		      
		      
		   // ADDING THE COLUMN NAME TO THE "allUserDefiedColumnNames" ARRAYLIST.
		      
		      allUserDefiedColumnNames.add(newColumnName);
		      
		      
		      
		   // INSERTING THE ALL TICKETS IN "User_Defined_Columns" TABLE.  
		      
		      if(count == 0) {
		      count ++;
		      SelectQuery sq2 = new SelectQueryImpl(new Table("Ticket_Task"));
		      Column c2 = new Column("Ticket_Task", "*");
	          sq2.addSelectColumn(c2);
	          
   		      DataObject dataobj2 = persobj.get(sq2);
   		    
   		      Iterator it = (Iterator)dataobj2.getRows("Ticket_Task");
   		     
   		         while (it.hasNext()) { 
   		             Row row2 = (Row) it.next();
   		           
   		           
   		           Row r = new Row ("User_Defined_Columns");
                   r.set("TICKET_ID", row2.get("TICKET_ID"));
               
                   DataObject d = new WritableDataObject();
                   d.addRow(r);
                  persobj.add(d);
                
   		        }
   		      
		      }
   		      
   		      
		      
		      
		      System.out.println("................FINISH.................");
		      return newColumnName+" : CREATED";
		      
		 }
  	      
		 catch(Exception e) {
			 return e.toString();
		 }
	    
     }
	 
	 
	 
     public static void testing() throws Exception {
//  	   
//  	   persobj = PersistenceClass.getInstance();
//  	   
//  	   
//         
//         
//         System.out.println("..........................");       
//         
//         
//         ColumnDefinition cd = new ColumnDefinition();
//  	   cd.setTableName("Practice1");
//  	   cd.setColumnName("CF2");
//  	   cd.setDataType("CHAR");
//  	   cd.setNullable(true);
//  	   cd.setMaxLength(200);
//  	   cd.setDisplayName("COMMENTS");
//  	   cd.setDefaultValue("None");
//
//  	   AlterTableQuery atq = new AlterTableQueryImpl("Practice1");
//  	   atq.addDynamicColumn(cd);
//
//  	   persobj.alterTable(atq);
//  	   
//  	   
//  	   
//  	   System.out.println("..........................");  
//  	   
//  	   
//         String[] StstusType = new String[]{ "Open","In Progress","To Be Tested","Closed"};
//         
//         for(int i=0; i< StstusType.length; i++) {
//      
//                 Row r = new Row ("Practice1");
//                 r.set("TICKET_ID", StstusType[i]);
//                 r.set("TASK", StstusType[i]);
//                 if(i >= 3) {
//              	 r.set("CF2", "......");
//                 }
//                 
//      
//                  DataObject d = new WritableDataObject();
//                  d.addRow(r);
//                  persobj.add(d);
//         }
//  	   
//  	   
//  	   System.out.println(".........................."); 
  	   
  	   
    	 
    	 
       persobj = PersistenceClass.getInstance();
  	   
  	   RelationalAPI relapi = RelationalAPI.getInstance();
  	   Connection conn = null;
  	   Statement stmt = null;
  	   DataSet ds = null;
         try{   
        	 
             conn = relapi.getConnection();
             SelectQuery sq = new SelectQueryImpl(new Table("User_Defined_Columns"));
             Column c1 = new Column("User_Defined_Columns", "*");
             sq.addSelectColumn(c1);
                 
             ds = relapi.executeQuery(sq, conn);
             ds.getColumnCount();
             
             for(int i=1; i<=ds.getColumnCount(); i++) {
            	 
          	     System.out.println(ds.getColumnName(i)+"--------------"+ds.getColumnDataType(i));
          	     Row r = new Row ("UserDefinedColumnsTable_Information");
                 r.set("COLUMN_NAME", ds.getColumnName(i));
                 r.set("DATA_TYPE", ds.getColumnDataType(i));
                 DataObject d = new WritableDataObject();
                 d.addRow(r);
                 persobj.add(d);
             }
  
             
         }
         
  	   finally
  	   {
  	   if (ds != null)
  	   {
  	   ds.close();
  	   }
  	   if (stmt!= null)
  	   {
  		 ((Connection) stmt).close();
  	   }
  	   //return the connection to the pool
  	   if (conn!=null)
  	   {
  	   conn.close();
  	   }
  	   }
  	   
  	   
  	   
  
   	
     }
     
     
     
     public static String addDataToUserDefiendColumns(String ticket_Id, String columnName, String value) throws Exception {
    	 
    	 try {
    		   persobj = PersistenceClass.getInstance();
    		  boolean present = findingTicketfromUser_Defined_Columns(ticket_Id);
    	if(present) {
    		   UpdateQuery uq = new UpdateQueryImpl("User_Defined_Columns");
    		   
    		   Criteria c = new Criteria(new Column("User_Defined_Columns", "TICKET_ID"),ticket_Id, QueryConstants.EQUAL);
    		   uq.setCriteria(c);
    		     
    		   uq.setUpdateColumn(columnName,value); 
    		  
    		   persobj.update(uq);
    	    	   
 
    	}	 
    	else {
    		SelectQuery sq = new SelectQueryImpl(new Table("UserDefinedColumnsTable_Information"));
     	     Column c1 = new Column("UserDefinedColumnsTable_Information", "*");
            sq.addSelectColumn(c1);
            Criteria c = new Criteria(new Column("UserDefinedColumnsTable_Information", "COLUMN_NAME"),columnName, QueryConstants.EQUAL);
            sq.setCriteria(c);
            DataObject d =persobj.get(sq);
       	 String dataType = (String) d.getFirstRow("UserDefinedColumnsTable_Information").get("DATA_TYPE");
       	 
       	 
       	 
       	 Row Ticket_Task = new Row ("User_Defined_Columns");
            Ticket_Task.set("TICKET_ID", ticket_Id);
            
       	 
            if(dataType.equals("INTEGER")) {
           	 int valueForInt = Integer.parseInt(value);
           	 Ticket_Task.set(columnName, valueForInt);
            }
            else if(dataType.equals("BIGINT")) {
           	 long valueFoeLong = Long.parseLong(value);
           	 Ticket_Task.set(columnName, valueFoeLong);
            }
            else if(dataType.equals("CHAR")) {
           	 Ticket_Task.set(columnName, value);
            }
            
            
            
            DataObject d2 = new WritableDataObject();
           
            d2.addRow(Ticket_Task);
            persobj.add(d2);
            
    	} 
    	 
    	return columnName+" : Added";
    	 }
    	 
    	 catch(Throwable t) {
    		 return t.toString();
    	 }
     }
     
     
     
     public static boolean findingTicketfromUser_Defined_Columns(String Ticket_Id) throws Exception {
  	   
  	   boolean present = false;
  	   
  	     persobj = PersistenceClass.getInstance();
         SelectQuery sq = new SelectQueryImpl(new Table("User_Defined_Columns"));
         Column c1 = new Column("User_Defined_Columns", "*");
         sq.addSelectColumn(c1);
         
         DataObject dataobj = persobj.get(sq);
         
         Iterator it = (Iterator)dataobj.getRows("User_Defined_Columns");
        
         while (it.hasNext()) {
      	   Row row = (Row) it.next();
             if(row.get("TICKET_ID").equals(Ticket_Id)) {
          	   present = true;
             }
         }
  	   return present;
     }
}
