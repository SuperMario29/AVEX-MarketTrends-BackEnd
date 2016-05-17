package avex.markettrends;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class AVEXDB {

	private int limit = 5;
	
	@SuppressWarnings("deprecation")
	public List<BasicDBObject> GetAthletes(Boolean areLosers)
	{
        int i = 1;
		try
		{
		List<BasicDBObject> athleteList = new ArrayList<>();
		
        // To connect to mongodb server
        MongoClient mongoClient = new MongoClient(Program.DATABASE_CONNECTION , Program.DATABASE_PORT );
    	
        // Now connect to your databases
		DB db = mongoClient.getDB(Program.DATABASE_NAME);
        System.out.println("Connect to database successfully");
			
        //boolean auth = db.authenticate(myUserName, myPassword);
        //System.out.println("Authentication: "+auth);         
			
        DBCollection athleteCollection = db.getCollection("athletes");
        System.out.println("Collection athletes selected successfully");
        
        BasicDBObject findValue = new BasicDBObject();
        
        if (areLosers){
        	findValue.append("currentprice", -1);
        }
        else{
        	findValue.append("currentprice", 1);
        }
			
        DBCursor cursor = athleteCollection.find().sort(findValue).limit(limit);
			
        while (cursor.hasNext()) { 
           BasicDBObject athlete = (BasicDBObject) cursor.next();
           athleteList.add(athlete);
           System.out.println("Athlete: " + athlete);
           i++;
        	}
        System.out.println("Received "+ i + " athletes"); 

        mongoClient.close();

		return athleteList;
		}
		catch(Exception ex)
		{
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public List<BasicDBObject> GetMarketPlace()
	{
        int i = 1;
		try
		{
		List<BasicDBObject> athleteList = new ArrayList<>();
		
        // To connect to mongodb server
        MongoClient mongoClient = new MongoClient(Program.DATABASE_CONNECTION , Program.DATABASE_PORT );
    	
        // Now connect to your databases
		DB db = mongoClient.getDB(Program.DATABASE_NAME);
        System.out.println("Connect to database successfully");
			
        //boolean auth = db.authenticate(myUserName, myPassword);
        //System.out.println("Authentication: "+auth);         
			
        DBCollection athleteCollection = db.getCollection("athletes");
        System.out.println("Collection athletes selected successfully");
			
        DBCursor cursor = athleteCollection.find();
			
        while (cursor.hasNext()) { 
           BasicDBObject athlete = (BasicDBObject) cursor.next();
           athleteList.add(athlete); 
           i++;
        	}
        System.out.println("Received "+ i + " athletes"); 

        mongoClient.close();

		return athleteList;
		}
		catch(Exception ex)
		{
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public List<BasicDBObject> GetMarketTrendsToday()
	{
        @SuppressWarnings("unused")
		int i = 1;
		try
		{			
        List<BasicDBObject> market = new ArrayList<>();
        
        MongoClient mongoClient = new MongoClient(Program.DATABASE_CONNECTION , Program.DATABASE_PORT );
    	
        // Now connect to your databases
		DB db = mongoClient.getDB(Program.DATABASE_NAME);
        System.out.println("Connect to database successfully");
			
        //boolean auth = db.authenticate(myUserName, myPassword);
        //System.out.println("Authentication: "+auth);         
			
        DBCollection marketCollection = db.getCollection("markettrends");
        System.out.println("Collection MarketTrends selected successfully");
        
        Date date = new Date();       
        DBCursor cursor = marketCollection.find(new BasicDBObject("recordstatusdate", new BasicDBObject("$gte",date))).sort(new BasicDBObject("recordstatusdate", -1)).limit(1);
	
        if(cursor != null && cursor.size() > 0){
        while (cursor.hasNext()) { 
            BasicDBObject x = (BasicDBObject) cursor.next();
            market.add(x); 
            i++;
         	}
        }
        
        if (market != null && market.size() > 0){
        	System.out.println("Received Market; Market Size-" + market.size()); 
            mongoClient.close();
        	return market;
        }
        else{
            mongoClient.close();
        	System.out.println("Nothing within the Market"); 
        	return null;
        }
		}
		catch(Exception ex)
		{
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void InsertMarketTrends(DBObject marketTrend)
	{
		try
		{
        // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient(Program.DATABASE_CONNECTION , Program.DATABASE_PORT );
	    	
	        // Now connect to your databases
			DB db = mongoClient.getDB(Program.DATABASE_NAME);
        System.out.println("Connect to database successfully");
			
        //boolean auth = db.authenticate(myUserName, myPassword);
        //System.out.println("Authentication: "+auth);         
			
        DBCollection marketCollect = db.getCollection("markettrends");
        System.out.println("Collection MarketTrends selected successfully");
        
        System.out.println("Insert Data into Collection DB: " + marketTrend);
        marketCollect.insert(marketTrend);
					
        mongoClient.close();
		
		}
		catch(Exception ex)
		{
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
		}
	}
}
