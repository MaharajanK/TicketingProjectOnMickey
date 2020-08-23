package com.maha;

import java.beans.Statement;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Scanner;

import com.adventnet.db.api.RelationalAPI;
import com.adventnet.db.persistence.metadata.ColumnDefinition;
import com.adventnet.ds.query.AlterTableQuery;
import com.adventnet.ds.query.AlterTableQueryImpl;
import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.DataSet;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;

public class CustomColumns {

	 public static Scanner Scan = new Scanner(System.in);
	 public static Persistence persobj;
	 
	 
	 
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
		      
		      
		      row.set("COLUMN_NAME",newColumnName);
		      row.set("STATUS","USED");
		      dataobj.updateRow(row);
	          persobj.update(dataobj);
		      
		      AlterTableQuery aq = new AlterTableQueryImpl("User_Defined_Columns");
		      aq.renameColumn(old_Column_Name, newColumnName);
		      persobj.alterTable(aq);
		      
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
}
