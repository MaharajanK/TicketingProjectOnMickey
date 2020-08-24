package com.maha;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import com.adventnet.ds.query.AlterTableQuery;
import com.adventnet.ds.query.AlterTableQueryImpl;
import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.DeleteQuery;
import com.adventnet.ds.query.DeleteQueryImpl;
import com.adventnet.ds.query.DerivedColumn;
import com.adventnet.ds.query.GroupByClause;
import com.adventnet.ds.query.GroupByColumn;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.QueryConstructionException;
import com.adventnet.ds.query.Range;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import com.adventnet.swissqlapi.sql.parser.ParseException;
import com.zoho.conf.Configuration;
import com.adventnet.beans.xtable.SortColumn;
import com.adventnet.db.api.RelationalAPI;
import com.adventnet.db.persistence.metadata.ColumnDefinition;
import com.adventnet.db.persistence.metadata.TableDefinition;
import com.adventnet.ds.query.DataSet;
import java.beans.Statement;
import java.sql.Connection;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.Query;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import edu.duke.FileResource;


 

class PersistenceClass{
	
	public static Persistence persobj;
	

	private PersistenceClass() throws Exception {
		
		System.out.println("\n\n"+"********************************  Start server   ********************************"+"\n\n");
	    
	    Configuration.setString("server.home", "E:\\AdventNetMickeyLite\\AdventNet\\MickeyLite");
	    Test gC =new Test();
	    
	    try {
	
	        gC.startServer();
	        System.out.println("\n\n"+"********************************   Starting server   ********************************"+"\n\n");
	        
	        persobj = (Persistence)BeanUtil.lookup("Persistence");
	        
	        
	        System.out.println("START");
	        System.out.println("Got persistence");
	        
	    } 
	    
	    catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		
	}
	
	public static Persistence getInstance() throws Exception {
		
		if(persobj == null) {
			                           
			//persobj = (Persistence) new PersistenceClass();                           // Terminal
			        persobj = (Persistence)BeanUtil.lookup("Persistence");              // API
			System.out.println("////////////////////////////   CREATED   /////////////////////////////////");
		}
		return persobj;
        
	}
	
	
}


public class MickeyDataBaseClass
{       
	
	    public static int TicketsCount;
        public static Scanner Scan = new Scanner(System.in);
        public static Persistence persobj;
        public static MickeyDataBaseClass get = new MickeyDataBaseClass();
        public static TicketingTicketServlet  T_T_Servlet = new TicketingTicketServlet();
        public static CustomColumns c_Column = new CustomColumns();
        
    public static void main(String arg[]) throws Exception {
         
    	System.out.println("............................********************......................................");
    	
    	 //persobj = PersistenceClass.getInstance();
    	 
    	 DataAdding();
    	 CustomColumns.testing();
    	 
    	 //System.out.println(CustomColumns.getting_Custom_Column("maharajan", "INTEGER"));
//       <<<----------------------------------------------------FOR TESTING ONLY TESTING---------------------------------------------------------->>>
    	 
//    	 updateUseingrelationalAPI("ZU-TK-190",3);
    	 // UpdateingTicketStatus("ZU-TK-190", 5);
//    	 MickeyCodeToSqlQuery();
//    	 System.out.println(UpdateingTicketStatus("ZU-TK-190", 2));
    	// System.out.println(get.gettingAllTicketsDetails());
//    	 System.out.println(get.gettingTicketDetails("DB-T2"));
//    	 System.out.println(get.gettingAllEmployeeDetails("ZU-TK-190"));
    	 
    }
    
    
        public HashMap<String, String> gettingTicketDetails(String Ticket_Id) throws DataAccessException{
        	
                try{
                        
                	    persobj = PersistenceClass.getInstance();
                	    if(persobj == null) {
                	    	HashMap<String, String> f = new HashMap<String, String>();
                	    	f.put("null", "null");
                	    	return f;
                	    }
                        return getTicketDetailsUsingTicketId( Ticket_Id);

                }
                catch(Throwable t){
                      return null;
                } 
               
                  
        }


        

        public ArrayList<HashMap<String, String>> gettingEmployeesDetails(String id) throws DataAccessException{
                try{
                	 persobj = PersistenceClass.getInstance();
                     return getEmployeeDetailsUsingEmployeeId( id);
                }
                catch(Throwable t){
                    return null;
                } 
             
        }
        
   
     public static void DataAdding() throws Exception {
    	 
    	 createTicketsForTemUse();
         setPriority();
         setStatus();
         addEmployees();  
         
    }

        
        private static void createTicketsForTemUse() throws Exception {
            
            FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingTasks.csv");
            for(CSVRecord rec:fr.getCSVParser(false)) {
               
            	Random rand = new Random();
                int upperbound = 3;
                //int int_random = rand.nextInt(upperbound);
                LocalDate maxDate = LocalDate.now().minusDays(rand.nextInt(upperbound)-5);
                Date MDate=Date.valueOf(maxDate);
                 
                SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
                String max_Date = sdfr.format(MDate);
                
                
                String ticket_Id = null;
	    	    int count = get.findingTotalTicketCounts();
	    	    ticket_Id = "DB-T"+(count);
	    	    
                get.createNewTicket(ticket_Id, rec.get(1), rec.get(4), rec.get(5), rec.get(6), max_Date);

                
            }
            
            System.out.println("createTicketsForTemUse");

        }
    
         // ......................... ADDIND EMPLOYEE ................................

        private static void addEmployees() throws DataAccessException {
        
        
            FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingEmployeeDetails.csv");
            for(CSVRecord rec:fr.getCSVParser(false)) {
                
                Row r = new Row ("Employee_Details");
                
                r.set("EMP_ID", rec.get(0));
                r.set("EMP_NAME", rec.get(1));
                long mobileNum = Long.parseLong(rec.get(2));
                r.set("MOBILE_NUM", mobileNum);
                Date dob=Date.valueOf(rec.get(3));
                r.set("DOB", dob);
                Date doj=Date.valueOf(rec.get(4));
                r.set("DOJ", doj);

                DataObject d = new WritableDataObject();
                d.addRow(r);
                persobj.add(d);
                
            }
            System.out.println("EMPLOYEE added....");
            
        
        }



               

 // ................................ SET PRIORITY TABLE ........................................
    
    private static void setPriority() throws DataAccessException {
        

        String[] priyorityType = new String[]{ "NONE","MEDIUM","HIGH"};
        
        for(int i=0; i< priyorityType.length; i++) {
            
            Row r = new Row ("Priority_Detail");
            r.set("PRIORITY_ID", i);
            r.set("PRIORITY_NAME", priyorityType[i]);
            
            
            DataObject d = new WritableDataObject();
            d.addRow(r);
            persobj.add(d);
        }
        
         System.out.println("PRIYORITY types added");
    }







          // ................................ SET STATUS ........................................

        private static void setStatus() throws DataAccessException {
                 
               String[] StstusType = new String[]{ "Open","In Progress","To Be Tested","Closed"};
               
               for(int i=0; i< StstusType.length; i++) {
            
                       Row r = new Row ("Status_Types");
                       r.set("STATUS_ID", i);
                       r.set("STATUS_NAME", StstusType[i]);
            
            
                        DataObject d = new WritableDataObject();
                        d.addRow(r);
                        persobj.add(d);
               }
               System.out.println("STATUS types added");        
        }


        

     public ArrayList<HashMap<String, String>> gettingAllTicketsDetails() throws DataAccessException{


                try{
                	
                     ArrayList<String> ids = gettingAllTicketIdsFromDb();
                     
                     ArrayList<HashMap<String, String>> ans = new ArrayList<HashMap<String, String>>();
                     
                     for(int i=0; i<ids.size(); i++){
                        ans.add(getTicketDetailsUsingTicketId(ids.get(i)));
                     }
                     
                     return ans;
                }
                catch(Throwable t){
                	
                    return null;
                } 
             
     }
        


     public ArrayList<HashMap<String, String>> gettingAllEmployeeDetails(String type) {
    	 
    	 try{
    		 
    		persobj = PersistenceClass.getInstance();
    		 //persobj = (Persistence)BeanUtil.lookup("Persistence");
    		 SelectQuery sq = new SelectQueryImpl(new Table("Employee_Details"));
   		     sq.addSelectColumns(Arrays.asList(new Column("Employee_Details","EMP_ID"),new Column("Employee_Details","EMP_NAME"),new Column("Employee_Details","MOBILE_NUM"),new Column("Employee_Details","DOB"),new Column("Employee_Details","DOJ")));
   		   
    		 
    		 if(!type.equals("All")) {
    			    Criteria  SelectCt = new Criteria(new Column("Employee_Details", "EMP_ID"), type , QueryConstants.EQUAL);
    		        sq.setCriteria(SelectCt);
    		 }
    		 
    		 DataObject dataobj = persobj.get(sq);
  		    
  		     SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
  		   
  		    Iterator it = (Iterator)dataobj.getRows("Employee_Details");
  		    
  		    ArrayList<HashMap<String, String>> allEmp_Detail_Array = new ArrayList<HashMap<String, String>>();
  		    HashMap<String, String> empDetail_TempStorage = null;
  		   
  		    while (it.hasNext()) { 
  		       Row row = (Row) it.next();
 		       Long  MOBILE_NUM = (Long) row.get("MOBILE_NUM");
 		     
 		       empDetail_TempStorage  = new HashMap<>();
  		       empDetail_TempStorage.put("EMP_ID", (String) row.get("EMP_ID"));
  		       empDetail_TempStorage.put("EMP_NAME", (String) row.get("EMP_NAME"));
  		       empDetail_TempStorage.put("MOBILE_NUM", Long.toString(MOBILE_NUM));
  		       empDetail_TempStorage.put("DOB", sdfr.format(row.get("DOB")));
  		       empDetail_TempStorage.put("DOJ", sdfr.format(row.get("DOJ")));
  		     
  		       allEmp_Detail_Array.add(empDetail_TempStorage);
  		    }
  		    
  		   return allEmp_Detail_Array;
    		 
        }
        catch(Throwable t){
            return null;
        } 
     }
   

     
     private static ArrayList<String> gettingAllTicketIdsFromDb() throws Exception {
    	 
    	  try {
    		  persobj = PersistenceClass.getInstance();
    		 // persobj = (Persistence)BeanUtil.lookup("Persistence");
              
              SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));
   		      sq.addSelectColumns(Arrays.asList(new Column("Ticket_Task","TICKET_ID"), new Column("Ticket_Task","TASK")));
   		   
   		      DataObject dataobj = persobj.get(sq);
   		    
   		      Iterator it = (Iterator)dataobj.getRows("Ticket_Task");
   		     
   		      ArrayList<String> ids = new ArrayList<String>();
   		   
   		      while (it.hasNext()) { 
   		          Row row = (Row) it.next();
   		          
   		          String ticketId = (String) row.get("TICKET_ID");
   		          ids.add(ticketId);
   		      }
   		      return ids;
    	  }
    	   
 		  catch(Throwable t) {
 			  return null;
 		  }  
 		    
     }
     


   
     public static HashMap<String, String> getTicketDetailsUsingTicketId(String TicketID) throws DataAccessException, SQLException, QueryConstructionException {
    	 System.out.println("getTicketDetailsUsingTicketId");
         RelationalAPI relAPI = RelationalAPI.getInstance();
         Connection conn = null;
         Statement stmt = null;
         DataSet ds = null;
         
         try{   
        	 
                  conn = relAPI.getConnection();
                  SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));

                  Column c1 = new Column("Ticket_Task", "TICKET_ID");
                  Column c2 = new Column("Ticket_Task", "TASK");

                  Column c3 = new Column("Emp_Vs_Ticket", "EMP_ID");
                  Column c4 = new Column("Emp_Vs_Ticket", "TICKET_ID");

                  Column c5 = new Column("Ticket_Vs_Date", "TICKET_ID");
                  Column c6 = new Column("Ticket_Vs_Date", "RELESE_DATE");
                  Column c7 = new Column("Ticket_Vs_Date", "MAX_DATE");

                  Column c8 = new Column("Ticket_Vs_Owner", "TICKET_ID");
                  Column c9 = new Column("Ticket_Vs_Owner", "OWNER_ID");

                  Column c10 = new Column("Ticket_Vs_Priority", "TICKET_ID");
                  Column c11 = new Column("Ticket_Vs_Priority", "PRIORITY_ID");

                  Column c12 = new Column("Priority_Detail", "PRIORITY_ID");
                  Column c13 = new Column("Priority_Detail", "PRIORITY_NAME");

                  Column c14 = new Column("Ticket_Vs_Status", "TICKET_ID");
                  Column c15 = new Column("Ticket_Vs_Status", "STATUS_ID");

                  Column c16 = new Column("Status_Types", "STATUS_ID");
                  Column c17 = new Column("Status_Types", "STATUS_NAME");

                  sq.addSelectColumn(c1);
                  sq.addSelectColumn(c2);

                  sq.addSelectColumn(c3);
                  sq.addSelectColumn(c4);

                  sq.addSelectColumn(c5);
                  sq.addSelectColumn(c6);
                  sq.addSelectColumn(c7);

                  sq.addSelectColumn(c8);
                  sq.addSelectColumn(c9);

                  sq.addSelectColumn(c10);
                  sq.addSelectColumn(c11);

                  sq.addSelectColumn(c12);
                  sq.addSelectColumn(c13);

                  sq.addSelectColumn(c14);
                  sq.addSelectColumn(c15);

                  sq.addSelectColumn(c16);
                  sq.addSelectColumn(c17);


                   
                  Criteria  SelectCt = new Criteria(new Column("Ticket_Task", "TICKET_ID"), TicketID , QueryConstants.EQUAL);
                  sq.setCriteria(SelectCt);
                  


                   Criteria joincriteria1 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Emp_Vs_Ticket","TICKET_ID"), QueryConstants.EQUAL);
                   Join join1 = new Join("Ticket_Task", "Emp_Vs_Ticket", joincriteria1, Join.INNER_JOIN);
                   sq.addJoin(join1);

                   Criteria joincriteria2 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Date","TICKET_ID"), QueryConstants.EQUAL);
                   Join join2 = new Join("Ticket_Task", "Ticket_Vs_Date", joincriteria2, Join.INNER_JOIN);
                   sq.addJoin(join2);

                   Criteria joincriteria3 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Owner","TICKET_ID"), QueryConstants.EQUAL);
                   Join join3 = new Join("Ticket_Task", "Ticket_Vs_Owner", joincriteria3, Join.INNER_JOIN);
                   sq.addJoin(join3);

                   Criteria joincriteria4 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Priority","TICKET_ID"), QueryConstants.EQUAL);
                   Join join4 = new Join("Ticket_Task", "Ticket_Vs_Priority", joincriteria4, Join.INNER_JOIN);
                   sq.addJoin(join4);

                   Criteria joincriteria5 = new Criteria(Column.getColumn("Ticket_Vs_Priority", "PRIORITY_ID"), Column.getColumn("Priority_Detail","PRIORITY_ID"), QueryConstants.EQUAL);
                   Join join5 = new Join("Ticket_Vs_Priority", "Priority_Detail", joincriteria5, Join.INNER_JOIN);
                   sq.addJoin(join5);

                   Criteria joincriteria6 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Status","TICKET_ID"), QueryConstants.EQUAL);
                   Join join6 = new Join("Ticket_Task", "Ticket_Vs_Status", joincriteria6, Join.INNER_JOIN);
                   sq.addJoin(join6);

                   Criteria joincriteria7 = new Criteria(Column.getColumn("Ticket_Vs_Status", "STATUS_ID"), Column.getColumn("Status_Types","STATUS_ID"), QueryConstants.EQUAL);
                   Join join7 = new Join("Ticket_Vs_Status", "Status_Types", joincriteria7, Join.INNER_JOIN);
                   sq.addJoin(join7);


            

                      


           ds = relAPI.executeQuery(sq, conn);
           System.out.println("");
           
           HashMap<String, String> map = null;  
           
       while (ds.next()) {
    	   
                 map  = new HashMap<>();
                 map.put("TICKET_ID", ds.getString(1));
                 map.put("OWNER_ID", ds.getString(9));
                 map.put("RELESE_DATE", ds.getString(6));
                 map.put("MAX_DATE", ds.getString(7));
                 map.put("EMP_ID", ds.getString(3));
                 map.put("PRIORITY", ds.getString(13));
                 map.put("STSTUS", ds.getString(17));
                 map.put("TASK", ds.getString(2));
                  
             
       }
       
        return map;
}


       finally
       {
         if (ds != null){
         ds.close();
       }
       if (stmt!= null){
          ((Connection) stmt).close();
       }
          //return the connection to the pool
       if (conn!=null){
           conn.close();
       }
}
		

     }     
   

                            //...................................................EMP_ID TO TICKET_ID........................................................

       
                     public static ArrayList<HashMap<String, String>> getEmployeeDetailsUsingEmployeeId(String EmpId) throws Exception {
                              System.out.println("getEmployeeDetailsUsingEmployeeId");
                    	      persobj = PersistenceClass.getInstance();
                    	 
                              //HashMap<Integer, HashMap<String,String>> map2 = new HashMap<Integer,HashMap<String,String>>();
                              SelectQuery sq = new SelectQueryImpl(new Table("Emp_Vs_Ticket"));
                              sq.addSelectColumns(Arrays.asList(new Column("Emp_Vs_Ticket","EMP_ID"), new Column("Emp_Vs_Ticket","TICKET_ID")));
                              
                              Criteria  SelectCt = new Criteria(new Column("Emp_Vs_Ticket", "EMP_ID"), EmpId , QueryConstants.EQUAL);
                              sq.setCriteria(SelectCt);
        
           
                              DataObject dataobj = persobj.get(sq);
                              
                              
                              Iterator it = (Iterator)dataobj.getRows("Emp_Vs_Ticket");
                              ArrayList<HashMap<String, String>> empDetailStoringArray = new ArrayList<HashMap<String, String>>();
                              while (it.hasNext()) {
                                  
                                   Row row = (Row) it.next();
                                   String TicketId = (String) row.get("TICKET_ID");
                                   HashMap<String, String> map1 = getTicketDetailsUsingTicketId(TicketId);
                                   empDetailStoringArray.add(map1);
                              }
            
                             return empDetailStoringArray;
                  } 
                     
                     
                     
                  

     
    

       public String createNewTicket(String ticket_Id,String task, String owner_Id, String priority, String assigned_To, String max_Date) throws Exception {
    	   
    	  try {
    		   
    		  persobj = PersistenceClass.getInstance();
//    	       String ticket_Id = null;
//    	       
//    	       int count = findingTotalTicketCounts();
//    	       
//    	    	   ticket_Id = "DB-T"+(count);
    	       
    	    	   
               Row Ticket_Task = new Row ("Ticket_Task");
               Ticket_Task.set("TICKET_ID", ticket_Id);
               Ticket_Task.set("TASK", task);
               
               
               Row Emp_Vs_Ticket = new Row ("Emp_Vs_Ticket");
               Emp_Vs_Ticket.set("EMP_ID", assigned_To);
               Emp_Vs_Ticket.set("TICKET_ID", ticket_Id);
               
               Row Ticket_Vs_Date = new Row ("Ticket_Vs_Date");
		       Date date1=Date.valueOf(LocalDate.now());
		       Date date2=Date.valueOf(max_Date);
               Ticket_Vs_Date.set("RELESE_DATE", date1);
               Ticket_Vs_Date.set("MAX_DATE", date2);
               Ticket_Vs_Date.set("TICKET_ID", ticket_Id);
               
               Row Ticket_Vs_Owner = new Row ("Ticket_Vs_Owner");
               Ticket_Vs_Owner.set("TICKET_ID", ticket_Id);
               Ticket_Vs_Owner.set("OWNER_ID", owner_Id);
               
               Row Ticket_Vs_Priority = new Row ("Ticket_Vs_Priority");
               Ticket_Vs_Priority.set("TICKET_ID", ticket_Id);
//               Ticket_Vs_Priority.set("PRIORITY_ID", Priotityname_To_Priority(persobj, priority));
               Ticket_Vs_Priority.set("PRIORITY_ID", Integer.parseInt(priority));
               
               Row Ticket_Vs_Status = new Row ("Ticket_Vs_Status");
               Ticket_Vs_Status.set("TICKET_ID", ticket_Id);
               Ticket_Vs_Status.set("STATUS_ID", 0);
               
               DataObject d = new WritableDataObject();
              
               d.addRow(Emp_Vs_Ticket);
               d.addRow(Ticket_Vs_Date);
               d.addRow(Ticket_Vs_Owner);
               d.addRow(Ticket_Vs_Priority);
               d.addRow(Ticket_Task);
               d.addRow(Ticket_Vs_Status);
               persobj.add(d);
               
               
               System.out.println(".....................");
               return ticket_Id+" : "+"created";
               
    	      } 
    	      
    	      catch(Throwable t) {
    	    	  return t.toString();
    	      }
       }

       
       public static int findingTotalTicketCounts() throws Exception {
    	   
    	   try {
    		   
    		   persobj = PersistenceClass.getInstance();
        	   SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));
               sq.addSelectColumns(Arrays.asList(new Column("Ticket_Task","TICKET_ID"), new Column("Ticket_Task","TASK")));


               DataObject dataobj = persobj.get(sq);

               Iterator it = (Iterator)dataobj.getRows("Ticket_Task");
               int count = 0;
               while (it.hasNext()) {
            	   Row row = (Row) it.next();
                   count+=1;
               }
        	   
               return count;
    	   }
    	   catch(Throwable t) {
    		   System.out.println(t.toString());
    		   return 1000;
    	   }
    	   
       }

       public String updateTicketDetails(HashMap<String, String> updatePera, String Ticket_Id) throws Exception {
    	       
    	   
    	   
    	   try {
    		   boolean Present = findingTicketfromTable(Ticket_Id);
    		   
    		   if(Present) {
    			   
    			   persobj = PersistenceClass.getInstance();
            	   
            	   
            	   for (String field : updatePera.keySet()) {
            	          
            		   String value = updatePera.get(field);
            		   Date date = null; 
            		   int priorityNum = 0;
            		   int statusNum = 0;
            		   
            		   String tableName = null;
            		   
            		   if(field.equals("TASK")) {
            			    tableName = "Ticket_Task";
            		   }
            		   else if(field.equals("OWNER_ID")) {
            			    tableName = "Ticket_Vs_Owner";
            		   }
            		   else if(field.equals("MAX_DATE") || field.equals("STARTING_DATE") || field.equals("FINISHING_DATE")) {
            			    tableName = "Ticket_Vs_Date";
            			    date=Date.valueOf(value);   			    
            		   }
            		   else if(field.equals("PRIORITY")) {
            			    field = "PRIORITY_ID";
            			    tableName = "Ticket_Vs_Priority";
//            			    priorityNum = Priotityname_To_Priority(persobj, value);
            			    priorityNum = Integer.parseInt(value);
            		   } 
            		   else if(field.equals("EMP_ID")) {
            			    tableName = "Emp_Vs_Ticket";
            		   } 
            		   else if(field.equals("STSTUS")) {
            			    field = "STATUS_ID";
            			    tableName = "Ticket_Vs_Status";
//            			    statusNum = StatusName_To_Status(persobj, value);
            			    statusNum = Integer.parseInt(value);
            			    
            		   } 
            		   else if(c_Column.allUserDefiedColumnNames.contains(field)) {
            			   tableName = "User_Defined_Columns";
            			   
            		   }
            		   
            		   
            		   
            		    UpdateQuery updquery = new UpdateQueryImpl(tableName);
            		    
            		    Criteria UpdateCt = new Criteria(new Column(tableName, "TICKET_ID"), Ticket_Id, QueryConstants.EQUAL);
        			    updquery.setCriteria(UpdateCt);
        			    
        			    
            		    if(field.equals("MAX_DATE") || field.equals("STARTING_DATE") || field.equals("FINISHING_DATE")) {
            		    	updquery.setUpdateColumn(field, date);
            		    }
            		    else if(field.equals("PRIORITY")) {
            		    	updquery.setUpdateColumn(field, priorityNum);
            		    }
                        else if(field.equals("STSTUS")) {
                        	updquery.setUpdateColumn(field, statusNum);
            		    }
            		    
                        else{
                        	updquery.setUpdateColumn(field, value);
                        }
        			    
            		    
            		    
        			    persobj.update(updquery);
        			    
            	     }
            	   
            	     return Ticket_Id+" : "+"UPDATED";
    		   }
    		   
    		   else {
    			   return Ticket_Id+" : The given TicketId not present in DB";
    		   }
    		   
    		  
    	   }
    	   
    	   catch(Throwable t){
               System.out.println(t);
               return t.toString();
           }
       }


       public String deleteTicket(String Ticket_Id) throws Exception {
    	   
    	  try {
    		boolean Present = findingTicketfromTable(Ticket_Id);
    		
    		if(Present) {
    			
    			 persobj = PersistenceClass.getInstance();
      		  
    			DeleteQuery d = new DeleteQueryImpl("Ticket_Task");
    		    Criteria cr = new Criteria(new Column("Ticket_Task", "TICKET_ID"), Ticket_Id, QueryConstants.EQUAL);
    	        d.setCriteria(cr);
    	        persobj.delete(d);
    	        
    	        
    	        DeleteQuery d1 = new DeleteQueryImpl("Emp_Vs_Ticket");
    		    Criteria cr1 = new Criteria(new Column("Emp_Vs_Ticket", "TICKET_ID"), Ticket_Id, QueryConstants.EQUAL);
    	        d1.setCriteria(cr1);
    	        persobj.delete(d1);
    	        
    	        
    	        return Ticket_Id+" : DELETED";
    		}
    		else {
    			return Ticket_Id+" : The given TicketId not present in DB";
    		}
    		
	        		
    	  }
    	  catch(Throwable t){
              System.out.println(t);
              return t.toString();
          }
       }
    

       public boolean findingTicketfromTable(String Ticket_Id) throws Exception {
    	   
    	   boolean present = false;
    	   persobj = PersistenceClass.getInstance();
           SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));
           sq.addSelectColumns(Arrays.asList(new Column("Ticket_Task","TICKET_ID"), new Column("Ticket_Task","TASK")));
           DataObject dataobj = persobj.get(sq);
           
           Iterator it = (Iterator)dataobj.getRows("Ticket_Task");
          
           while (it.hasNext()) {
        	   Row row = (Row) it.next();
               if(row.get("TICKET_ID").equals(Ticket_Id)) {
            	   present = true;
               }
           }
    	   return present;
       }
    
       
     
       public static String updateingTicketStatus(String emp_id, int status_id) throws Exception {
    	   

//         <-------------------------------- Use selectQuery get DataObject and update the row------------------------------------------>    	   
    	   
    	   persobj = PersistenceClass.getInstance();
    	   
    	   SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Vs_Status"));
    	   Column c1 = new Column("Ticket_Vs_Status", "*");
    	   Column c2 = new Column("Emp_Vs_Ticket", "*");
           sq.addSelectColumn(c1);
           sq.addSelectColumn(c2);
           
           Criteria joincriteria1 = new Criteria(Column.getColumn("Ticket_Vs_Status", "TICKET_ID"), Column.getColumn("Emp_Vs_Ticket","TICKET_ID"), QueryConstants.EQUAL);
           Join join1 = new Join("Ticket_Vs_Status", "Emp_Vs_Ticket", joincriteria1, Join.INNER_JOIN);
           sq.addJoin(join1);
           
           Criteria c = new Criteria(new Column("Emp_Vs_Ticket", "EMP_ID"),emp_id, QueryConstants.EQUAL);
           sq.setCriteria(c);
           
           DataObject d =persobj.get(sq);
           
           
           Iterator it = (Iterator) d.getRows("Ticket_Vs_Status");
           
                  while (it.hasNext()) {
                        Row row = (Row) it.next();
                        System.out.println(row);
                        
                        row.set("STATUS_ID",status_id);
                        d.updateRow(row);
                        persobj.update(d);
    
                  }
    	   return emp_id+" : UPDATED";
    	   
    	   
    	   
//         <--------------------------------------------Use updateQuery directly------------------------------------------------------>    	   
    	   
    	   
//	   persobj = PersistenceClass.getInstance();
//	   UpdateQuery uq = new UpdateQueryImpl("Ticket_Vs_Status");
//	   
//	   Join  join1 = new Join("Ticket_Vs_Status", "Emp_Vs_Ticket", new  String[]{"TICKET_ID"}, new String[]{"TICKET_ID"}, Join.INNER_JOIN);
//	   uq.addJoin(join1);
//	   
//	   Criteria c = new Criteria(new Column("Emp_Vs_Ticket", "EMP_ID"),emp_id, QueryConstants.EQUAL);
//	   uq.setCriteria(c);
//	     
//	   uq.setUpdateColumn("STATUS_ID",2); 
//	  
//	   persobj.update(uq);
    	   
//     return emp_id+" : UPDATED";    	   
	   
	   
	   
    	   
       }
        
       
      
       public static void updateingTicketStatusUsingRelationalAPI(String emp_id,int status_id) throws Exception {
		
    	   
//    	   persobj = PersistenceClass.getInstance();
//    	   UpdateQuery uq = new UpdateQueryImpl("Ticket_Vs_Status");
//    	   Join  join1 = new Join("Ticket_Vs_Status", "Ticket_Vs_Priority", new  String[]{"TICKET_ID"}, new String[]{"TICKET_ID"}, Join.INNER_JOIN);
//    	   uq.addJoin(join1);
//    	   Criteria c = new Criteria(new Column("Ticket_Vs_Status", "TICKET_ID"),emp_id, QueryConstants.EQUAL);
//    	   uq.setCriteria(c);
//    	   uq.setUpdateColumn("STATUS_ID",new Column("Ticket_Vs_Priority", "PRIORITY_ID"));
//    	   persobj.update(uq);
//    	   System.out.println("....................");
    	   
    	   
    	   
  	   
    	 RelationalAPI relAPI = RelationalAPI.getInstance();
         Connection conn = null;
         Statement stmt = null;
         DataSet ds = null;
         
         
         try(Connection con = relAPI.getConnection())
 		{
 			UpdateQuery uq = new UpdateQueryImpl("Ticket_Vs_Status");
 			uq.setUpdateColumn("STATUS_ID", status_id);
 			
 			Join  join1 = new Join("Ticket_Vs_Status", "Emp_Vs_Ticket", new  String[]{"TICKET_ID"}, new String[]{"TICKET_ID"}, Join.INNER_JOIN);
 	        uq.addJoin(join1);
 	   
 	        Criteria c = new Criteria(new Column("Emp_Vs_Ticket", "EMP_ID"),emp_id, QueryConstants.EQUAL);
 	        uq.setCriteria(c);
 	        
 			String sql = relAPI.getUpdateSQL(uq);
 			
 			try
 			{   	//<------------API1(String , OBJECT)------------>
 				 
 				relAPI.executeUpdate(relAPI.getUpdateSQL(uq), new Object[0]);
 			
 				   //<------------API2(Connection, String , OBJECT)------------>
 			
//				    relAPI.executeUpdate(conn, relAPI.getUpdateSQL(uq), new Object[0]);
				                     
				   //<-----------API3(PreparedStatement)------------->
				    
// 				try(PreparedStatement ps = con.prepareStatement(sql))
// 				{   
// 					relAPI.executeUpdate(ps);
// 					System.out.println("...............UPDATED2..............");
// 				}
 				
 			}
 			catch(SQLException e)
 			{
 				e.printStackTrace();
 				//assertTrue("TestSQLExceptionHandlerImpl class is not invoked for the SQLException", e.getNextException().getMessage().contains("Exception is handled"));
 			}
 		}

   finally
   {
     if (ds != null){
     ds.close();
   }
   if (stmt!= null){
      ((Connection) stmt).close();
   }
      //return the connection to the pool
   if (conn!=null){
       conn.close();
   }
}
        
        
    	   
       }   
       
       

       
       
       
    
      
       
       
       
       
       
       
       
//   <----------------------------------------------MICKEY CODE TO SQL QUERY---------------------------------------------->       
       
       
//       public static void MickeyCodeToSqlQuery() throws DataAccessException, SQLException, QueryConstructionException {
//
//        
//
//    	   
//    	   System.out.println("getTicketDetailsUsingTicketId");
//           RelationalAPI relAPI = RelationalAPI.getInstance();
//           Connection conn = null;
//           Statement stmt = null;
//           DataSet ds = null;
//           
//           try{   
//          	 
//
//        	   
//        	   conn = relAPI.getConnection();
//               SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));
//
//               Column c1 = new Column("Ticket_Task", "TICKET_ID");
//               Column c2 = new Column("Ticket_Task", "TASK");
//
//               Column c3 = new Column("Emp_Vs_Ticket", "EMP_ID");
//               Column c4 = new Column("Emp_Vs_Ticket", "TICKET_ID");
//
//               Column c5 = new Column("Ticket_Vs_Date", "TICKET_ID");
//               Column c6 = new Column("Ticket_Vs_Date", "RELESE_DATE");
//               Column c7 = new Column("Ticket_Vs_Date", "MAX_DATE");
//
//               Column c8 = new Column("Ticket_Vs_Owner", "TICKET_ID");
//               Column c9 = new Column("Ticket_Vs_Owner", "OWNER_ID");
//
//               Column c10 = new Column("Ticket_Vs_Priority", "TICKET_ID");
//               Column c11 = new Column("Ticket_Vs_Priority", "PRIORITY_ID");
//
//               Column c12 = new Column("Priority_Detail", "PRIORITY_ID");
//               Column c13 = new Column("Priority_Detail", "PRIORITY_NAME");
//
//               Column c14 = new Column("Ticket_Vs_Status", "TICKET_ID");
//               Column c15 = new Column("Ticket_Vs_Status", "STATUS_ID");
//
//               Column c16 = new Column("Status_Types", "STATUS_ID");
//               Column c17 = new Column("Status_Types", "STATUS_NAME");
//
//               sq.addSelectColumn(c1);
//               sq.addSelectColumn(c2);
//
//               sq.addSelectColumn(c3);
//               sq.addSelectColumn(c4);
//
//               sq.addSelectColumn(c5);
//               sq.addSelectColumn(c6);
//               sq.addSelectColumn(c7);
//
//               sq.addSelectColumn(c8);
//               sq.addSelectColumn(c9);
//
//               sq.addSelectColumn(c10);
//               sq.addSelectColumn(c11);
//
//               sq.addSelectColumn(c12);
//               sq.addSelectColumn(c13);
//
//               sq.addSelectColumn(c14);
//               sq.addSelectColumn(c15);
//
//               sq.addSelectColumn(c16);
//               sq.addSelectColumn(c17);
//
//
//                
//               Criteria  SelectCt = new Criteria(new Column("Ticket_Task", "TICKET_ID"), "DB-T2" , QueryConstants.EQUAL);
//               sq.setCriteria(SelectCt);
//               
//
//
//                Criteria joincriteria1 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Emp_Vs_Ticket","TICKET_ID"), QueryConstants.EQUAL);
//                Join join1 = new Join("Ticket_Task", "Emp_Vs_Ticket", joincriteria1, Join.INNER_JOIN);
//                sq.addJoin(join1);
//
//                Criteria joincriteria2 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Date","TICKET_ID"), QueryConstants.EQUAL);
//                Join join2 = new Join("Ticket_Task", "Ticket_Vs_Date", joincriteria2, Join.INNER_JOIN);
//                sq.addJoin(join2);
//
//                Criteria joincriteria3 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Owner","TICKET_ID"), QueryConstants.EQUAL);
//                Join join3 = new Join("Ticket_Task", "Ticket_Vs_Owner", joincriteria3, Join.INNER_JOIN);
//                sq.addJoin(join3);
//
//                Criteria joincriteria4 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Priority","TICKET_ID"), QueryConstants.EQUAL);
//                Join join4 = new Join("Ticket_Task", "Ticket_Vs_Priority", joincriteria4, Join.INNER_JOIN);
//                sq.addJoin(join4);
//
//                Criteria joincriteria5 = new Criteria(Column.getColumn("Ticket_Vs_Priority", "PRIORITY_ID"), Column.getColumn("Priority_Detail","PRIORITY_ID"), QueryConstants.EQUAL);
//                Join join5 = new Join("Ticket_Vs_Priority", "Priority_Detail", joincriteria5, Join.INNER_JOIN);
//                sq.addJoin(join5);
//
//                Criteria joincriteria6 = new Criteria(Column.getColumn("Ticket_Task", "TICKET_ID"), Column.getColumn("Ticket_Vs_Status","TICKET_ID"), QueryConstants.EQUAL);
//                Join join6 = new Join("Ticket_Task", "Ticket_Vs_Status", joincriteria6, Join.INNER_JOIN);
//                sq.addJoin(join6);
//
//                Criteria joincriteria7 = new Criteria(Column.getColumn("Ticket_Vs_Status", "STATUS_ID"), Column.getColumn("Status_Types","STATUS_ID"), QueryConstants.EQUAL);
//                Join join7 = new Join("Ticket_Vs_Status", "Status_Types", joincriteria7, Join.INNER_JOIN);
//                sq.addJoin(join7);
//        		      
//
//			    System.out.println(RelationalAPI.getInstance().getSelectSQL(sq));
//               
//}
//
//
//     finally
//     {
//       if (ds != null){
//       ds.close();
//     }
//     if (stmt!= null){
//        ((Connection) stmt).close();
//     }
//        //return the connection to the pool
//     if (conn!=null){
//         conn.close();
//     }
//}
//		
//
//   }   
       
       
       
//     //........................WHEN CLICK THE SPECIFIED TICKET IT FETCH THE ALL THE INFORMATION FROM THIS TICKET...............................
//
//
// public static HashMap<String, String> getTicketDetailsUsingTicketId(Persistence persobj, String TicketID) throws DataAccessException {
//      
//    try {  
//  	   
//         HashMap<String, String> map = null;  
//         String TASK = (String) gettingTaskFromTicketId(persobj,TicketID);                                          //  finding TASK
//  
//         Date[] Dates = gettingDateFromTicketId(persobj,TicketID);                                                 //  finding R_DATE and m_DATE
//         Date releseDate = Dates[0];
//         Date maxDate = Dates[1];
//   
//         String owner = (String) gettingOwnerIdFromTicketId(persobj,TicketID);                                       //   finding OWNER
//   
//         String currentWorkerId = gettingEmployeeIdFromTicketId(persobj,TicketID);                                    //   finding CURRENT_WORKER
//   
//         Row currentWorkerDetails = gettingEmployeeRowFromTicketId(currentWorkerId);                                   //   finding CURRENT_WORKER_DETAILS
//    
//         int PriorityId = gettingPriorityIdFromTicketId(persobj,TicketID);                                        //   finding PRIORITY_ID
//  
//         String priority = gettingPriorityNameFromPriorityId(persobj,PriorityId);                                     //   finding PRIORITY
//   
//         int statusId = gettingStatusIdFromTicketId(persobj, TicketID);
//   
//         String status = gettingStatusNameFromStstusId(persobj,statusId);
//
//         SimpleDateFormat sDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
//    
//         map  = new HashMap<>();
//         map.put("TICKET_ID", TicketID);
//         map.put("OWNER_ID", owner);
//         map.put("RELESE_DATE", sDateFormat.format(releseDate));
//         map.put("MAX_DATE", sDateFormat.format(maxDate));
//         map.put("EMP_ID", currentWorkerDetails.get("EMP_ID").toString());
//         map.put("PRIORITY", priority);
//         map.put("STSTUS", status);
//         map.put("TASK", TASK);
//         
//         return map;
//   }
//   catch(Throwable t){
//          System.out.println(t.toString());
//          return null;
//   }
//  }
 
 
       
       
//       //................................................TICKET_ID TO TASK...........................................................
//       
//       private static Object gettingTaskFromTicketId(Persistence persobj, String TicketID) throws DataAccessException {     
//
//           SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Task"));
//           sq.addSelectColumns(Arrays.asList(new Column("Ticket_Task","TICKET_ID"), new Column("Ticket_Task","TASK")));
//           Criteria  SelectCt = new Criteria(new Column("Ticket_Task", "TICKET_ID"), TicketID , QueryConstants.EQUAL);
//           sq.setCriteria(SelectCt);
//       
//       
//           DataObject dataobj = persobj.get(sq);
//           Row r = dataobj.getRow("Ticket_Task");
//           return r.get("TASK");
//       
//       }
//       
//       
//       //...................................................TICKET_ID TO DATE........................................................
//       
//       private static Date[] gettingDateFromTicketId(Persistence persobj, String TicketID) throws DataAccessException {          
//
//           SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Vs_Date"));
//           sq.addSelectColumns(Arrays.asList(new Column("Ticket_Vs_Date","TICKET_ID"), new Column("Ticket_Vs_Date","RELESE_DATE"), new Column("Ticket_Vs_Date","MAX_DATE")));
//           Criteria  SelectCt = new Criteria(new Column("Ticket_Vs_Date", "TICKET_ID"), TicketID , QueryConstants.EQUAL);
//           sq.setCriteria(SelectCt);
//       
//       
//           DataObject dataobj = persobj.get(sq);
//           Iterator it = (Iterator)dataobj.getRows("Ticket_Vs_Date");
//           Date [] dates = new Date[2];
//           
//              while (it.hasNext()) { 
//                  Row row = (Row) it.next();
//                  dates[0] = (Date) row.get("RELESE_DATE");
//                  dates[1] = (Date) row.get("MAX_DATE");
//              }
//           return dates;
//       
//       }
//       
//       
//       
//       //...................................................TICKET_ID TO OWNER........................................................
//       
//               private static Object gettingOwnerIdFromTicketId(Persistence persobj, String TicketID) throws DataAccessException {
//
//                   SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Vs_Owner"));
//                   sq.addSelectColumns(Arrays.asList(new Column("Ticket_Vs_Owner","TICKET_ID"), new Column("Ticket_Vs_Owner","OWNER_ID")));
//                   Criteria  SelectCt = new Criteria(new Column("Ticket_Vs_Owner", "TICKET_ID"), TicketID , QueryConstants.EQUAL);
//                   sq.setCriteria(SelectCt);
//               
//               
//                   DataObject dataobj = persobj.get(sq);
//                   Row r = dataobj.getRow("Ticket_Vs_Owner");
//                   return r.get("OWNER_ID");
//               
//               }
//               
//               
//               
//               //...................................................TICKET_ID TO EMP_ID........................................................
//               
//               private static String gettingEmployeeIdFromTicketId(Persistence persobj, String TicketId) throws DataAccessException{
//                   
//                   SelectQuery sq = new SelectQueryImpl(new Table("Emp_Vs_Ticket"));
//                   sq.addSelectColumns(Arrays.asList(new Column("Emp_Vs_Ticket","EMP_ID"), new Column("Emp_Vs_Ticket","TICKET_ID")));
//                   Criteria  SelectCt = new Criteria(new Column("Emp_Vs_Ticket", "TICKET_ID"), TicketId , QueryConstants.EQUAL);
//                   sq.setCriteria(SelectCt);
//                   
//               
//                   DataObject dataobj = persobj.get(sq);
//                   Row r = dataobj.getRow("Emp_Vs_Ticket");
//                   return  (String) r.get("EMP_ID");
//                   
//               }
//               
//               
//               
//               
//               //...................................................EMP_ID TO EMP_ALL_DETAILS........................................................
//               
//                  private static Row gettingEmployeeRowFromTicketId(String EmpId) throws Exception{
//                       
//               	    Persistence persobj = (Persistence)BeanUtil.lookup("Persistence");
//                       SelectQuery sq = new SelectQueryImpl(new Table("Employee_Details"));
//                       sq.addSelectColumns(Arrays.asList(new Column("Employee_Details","EMP_ID"), new Column("Employee_Details","EMP_NAME"), new Column("Employee_Details","MOBILE_NUM"), new Column("Employee_Details","DOB"), new Column("Employee_Details","DOJ")));
//                       Criteria  SelectCt = new Criteria(new Column("Employee_Details", "EMP_ID"), EmpId , QueryConstants.EQUAL);
//                       sq.setCriteria(SelectCt);
//                   
//                       
//                       DataObject dataobj = persobj.get(sq);
//                       Row row = dataobj.getRow("Employee_Details");
//                       
//                       return row;
//                       
//                   }
//                  
//                  
//                  
//                   //...................................................TICKET_ID TO PRIORITY_ID........................................................
//                   
//                   private static int gettingPriorityIdFromTicketId(Persistence persobj, String TicketID) throws DataAccessException {
//
//                       SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Vs_Priority"));
//                       sq.addSelectColumns(Arrays.asList(new Column("Ticket_Vs_Priority","TICKET_ID"), new Column("Ticket_Vs_Priority","PRIORITY_ID")));
//                       Criteria  SelectCt = new Criteria(new Column("Ticket_Vs_Priority", "TICKET_ID"), TicketID , QueryConstants.EQUAL);
//                       sq.setCriteria(SelectCt);
//                   
//                   
//                       DataObject dataobj = persobj.get(sq);
//                       Row r = dataobj.getRow("Ticket_Vs_Priority");
//                       return (int) r.get("PRIORITY_ID");
//                   
//                   }
//                   
//                   //...................................................PRIORITY_ID TO PRIORITY_NAME........................................................
//                   
//                   private static String gettingPriorityNameFromPriorityId(Persistence persobj, int PriorityId) throws DataAccessException {
//
//                       SelectQuery sq = new SelectQueryImpl(new Table("Priority_Detail"));
//                       sq.addSelectColumns(Arrays.asList(new Column("Priority_Detail","PRIORITY_ID"), new Column("Priority_Detail","PRIORITY_NAME")));
//                       Criteria  SelectCt = new Criteria(new Column("Priority_Detail", "PRIORITY_ID"), PriorityId , QueryConstants.EQUAL);
//                       sq.setCriteria(SelectCt);
//                   
//                   
//                       DataObject dataobj = persobj.get(sq);
//                       Row r = dataobj.getRow("Priority_Detail");
//                       return (String) r.get("PRIORITY_NAME");
//                   
//                   }
//                   
//                   
//                   
//                   //...................................................PRIORITY_ID TO PRIORITY_NAME........................................................
//                   
//                   private static int gettingPriorityIdFromPriorityName(Persistence persobj, String PriorityName) throws DataAccessException {
//
//                       SelectQuery sq = new SelectQueryImpl(new Table("Priority_Detail"));
//                       sq.addSelectColumns(Arrays.asList(new Column("Priority_Detail","PRIORITY_ID"), new Column("Priority_Detail","PRIORITY_NAME")));
//                       Criteria  SelectCt = new Criteria(new Column("Priority_Detail", "PRIORITY_NAME"), PriorityName , QueryConstants.EQUAL);
//                       sq.setCriteria(SelectCt);
//                   
//                   
//                       DataObject dataobj = persobj.get(sq);
//                       Row r = dataobj.getRow("Priority_Detail");
//                       return (int) r.get("PRIORITY_ID");
//                   
//                   }
//                   
//                   
//                   
//                    //...................................................STATUS_ID TO STATUS........................................................
//                     
//                     
//                      private static String gettingStatusNameFromStstusId(Persistence persobj,int StstusId) throws DataAccessException{
//                          
//
//                           try{
//                               SelectQuery sq = new SelectQueryImpl(new Table("Status_Types"));
//                               sq.addSelectColumns(Arrays.asList(new Column("Status_Types","STATUS_ID"), new Column("Status_Types","STATUS_NAME")));
//                               Criteria  SelectCt = new Criteria(new Column("Status_Types", "STATUS_ID"), StstusId , QueryConstants.EQUAL);
//                               sq.setCriteria(SelectCt);
//                               //Persistence p = (Persistence)BeanUtil.lookup("Persistence");
//                               DataObject dataobj = persobj.get(sq);
//                               Row r = dataobj.getRow("Status_Types");
//                               System.out.println(".............................................................");
//                               return  (String) r.get("STATUS_NAME");
//                           }
//                           catch(Throwable t){
//                               System.out.println(t);
//                               return t.toString();
//                           }
//                           
//                           
//                       }
//                      
//                    //...................................................STATUS_NAME TO STATUS_ID........................................................
//                      
//                      
//                      private static int gettingStatusIdFromStstusName(Persistence persobj,String StstusName) throws DataAccessException{
//                          
//
//                                 SelectQuery sq = new SelectQueryImpl(new Table("Status_Types"));
//                                 sq.addSelectColumns(Arrays.asList(new Column("Status_Types","STATUS_ID"), new Column("Status_Types","STATUS_NAME")));
//                                 Criteria  SelectCt = new Criteria(new Column("Status_Types", "STATUS_NAME"), StstusName , QueryConstants.EQUAL);
//                                 sq.setCriteria(SelectCt);
//                                 
//                                 DataObject dataobj = persobj.get(sq);
//                                 Row r = dataobj.getRow("Status_Types");
//                                 System.out.println(".............................................................");
//                                 return  (int) r.get("STATUS_ID");
//                                
//                           
//                       }
//                      
//
//                    //...................................................TICKET_ID TO STATUS_ID........................................................
//                      
//                      private static int gettingStatusIdFromTicketId(Persistence persobj, String TicketId) throws DataAccessException{
//                           
//                           SelectQuery sq = new SelectQueryImpl(new Table("Ticket_Vs_Status"));
//                           sq.addSelectColumns(Arrays.asList(new Column("Ticket_Vs_Status","TICKET_ID"), new Column("Ticket_Vs_Status","STATUS_ID")));
//                           Criteria  SelectCt = new Criteria(new Column("Ticket_Vs_Status", "TICKET_ID"), TicketId , QueryConstants.EQUAL);
//                           sq.setCriteria(SelectCt);
//                       
//                       
//                           DataObject dataobj = persobj.get(sq);
//                           Row r = dataobj.getRow("Ticket_Vs_Status");
//                           return  (int) r.get("STATUS_ID");
//                           
//                       }
//                        

       
       
       
       
       
       
       
       
       // ................................ ADDING TASK TO TICKETS ........................................
       
//     public static void AddTask() throws DataAccessException {
//         
//         
//             FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingTasks.csv");
//             for(CSVRecord rec:fr.getCSVParser(false)) {
//                
//                 Row task = new Row ("Ticket_Task");
//                 task.set("TICKET_ID", rec.get(0));
//                 task.set("TASK", rec.get(1));
//                 
//                 DataObject d = new WritableDataObject();
//                 d.addRow(task);
//                 persobj.add(d);
//                 TicketsCount++;
//             }
//             
//             System.out.println("TASK added");
//     }
     
     
     // ................................ ADDING DATE TO TICKETS ........................................
     
//     public static void AddDateForTicket() throws DataAccessException {
//         
//         FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingTasks.csv");
//         for(CSVRecord rec:fr.getCSVParser(false)) {
//                
//                Random rand = new Random();
//                int upperbound = 3;
//                int int_random = rand.nextInt(upperbound);
//                
//                LocalDate releseDate = LocalDate.now().minusDays(rand.nextInt(upperbound));
//                LocalDate maxDate = LocalDate.now().minusDays(rand.nextInt(upperbound)-5);
//               
//            
//            
//                 Row date = new Row ("Ticket_Vs_Date");
//                 
//                 date.set("TICKET_ID", rec.get(0));
//                 Date RDate=Date.valueOf(releseDate);
//                 date.set("RELESE_DATE", RDate);
//                 Date MDate=Date.valueOf(maxDate);
//                 date.set("MAX_DATE", MDate);
//            
//            
//                 DataObject d = new WritableDataObject();
//                 d.addRow(date);
//                 persobj.add(d);
//            
//        }
//        System.out.println("DATE added");
 //    
//     }


     // ................................ ADDING OWNER TO TICKETS ........................................
     
//     public static void AddOwnerForTicket() throws DataAccessException {
//         
//         FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingTasks.csv");
//         for(CSVRecord rec:fr.getCSVParser(false)) {
//           
//             
//             Row r = new Row ("Ticket_Vs_Owner");
//             r.set("TICKET_ID", rec.get(0));
//             r.set("OWNER_ID", rec.get(4));
//             
//             
//             DataObject d = new WritableDataObject();
//             d.addRow(r);
//             persobj.add(d);
//             
//         }
//         System.out.println("OWNER added");
//         
//     }

       
       
       // ................................ ADDING PRIORITY TO TICKETS ........................................
       
//   public static void AddPriorityTOTickets() throws DataAccessException {
//       
//       FileResource fr=new FileResource("C:\\Users\\maha\\MickeyPractice\\Sourcefiles\\TicketingTasks.csv");
//       for(CSVRecord rec:fr.getCSVParser(false)) {
//         
//           Row r = new Row ("Ticket_Vs_Priority");
//           r.set("TICKET_ID", rec.get(0));
//           r.set("PRIORITY_ID", Integer.parseInt(rec.get(5)));
//           
//           
//           DataObject d = new WritableDataObject();
//           d.addRow(r);
//           persobj.add(d);
//           
//       }
//       System.out.println("PRIORITY added");
//       
//   }

     
       // ................................. FOR TEMP USE ONLY ............................................

//public static void setTicketToEmp(String TicketId, String EmpId) throws DataAccessException {        
//   
//   Row r = new Row ("Emp_Vs_Ticket");                                                               
//   r.set("EMP_ID", EmpId);
//   r.set("TICKET_ID", TicketId);
//   DataObject d=new WritableDataObject();
//   d.addRow(r);
//   persobj.add(d);
//   
//}


// ................................. FOR TEMP USE ONLY ............................................


//public static void setStatusToTheTicket(String TicketId, int StstusId) throws DataAccessException {          
//   
//   Row r = new Row ("Ticket_Vs_Status");
//   r.set("TICKET_ID", TicketId);
//   r.set("STATUS_ID", StstusId);
//   DataObject d=new WritableDataObject();
//   d.addRow(r);
//   persobj.add(d);
//   
//}

       
       // public static String allTicketsDetails() throws DataAccessException{


       //             try{
       //                  Persistence persobj = (Persistence)BeanUtil.lookup("Persistence");
       //                  String[] tickets = new String[] {"UX-T300","UX-T301","DB-T302","CL-T303","DB-T304","UX-T305","DB-T306","DB-T307"};
       //                  ArrayList<HashMap<String, String>> ans = new ArrayList<HashMap<String, String>>();
       //                  for(int i=0; i<tickets.length; i++){
       //                     ans.add(getTicketDetailsUsingTicketId(persobj, tickets[i]));
       //                  }
       //                  //return ans;
       //                  return"maha";
       //             }
       //             catch(Throwable t){
       //                 return t.toString();
       //             } 
                
                
       // }



}



class Test extends StandAlonePersistence{
    public void postPopulation() throws Exception
    {
        System.out.println("\n\n"+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   postPopulation Started   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+"\n\n");
      
    }
    public void prePopulation() throws Exception
    {
        System.out.println("\n\n"+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   prePopulation Started   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+"\n\n");
    }
}











