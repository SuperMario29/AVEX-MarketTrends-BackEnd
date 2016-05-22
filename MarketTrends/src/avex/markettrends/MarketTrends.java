package avex.markettrends;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import avex.models.Market;

public class MarketTrends {

	private AVEXDB avexDB = new AVEXDB();
	
	public void ImportData(){
		
		try
		{
		BasicDBObject marketTrend = new BasicDBObject();
		Date date = new Date();
		
		List<BasicDBObject> topLosers = GetBiggestLosers(); 
		List<BasicDBObject> topGainers = GetBiggestGainers();
		List<BasicDBObject> marketplace = GetMarketPlace();
		
		marketTrend.append("topgainerstoday", topGainers);
		marketTrend.append("toploserstoday", topLosers);
		marketTrend.append("totalmarkettoday", marketplace);
		marketTrend.append("recordstatusdate", date);
		
	     avexDB.InsertMarketTrends(marketTrend);
		}
		catch(Exception ex){
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
		}
	}
	
	private List<BasicDBObject> GetBiggestLosers(){
		List<BasicDBObject> value = new ArrayList<>();
		value = avexDB.GetAthletes(false);		
		return value;
	}
	
	private List<BasicDBObject> GetBiggestGainers(){
		List<BasicDBObject> value = new ArrayList<>();
		value = avexDB.GetAthletes(true);		
		return value;
	}
	
	private List<BasicDBObject> GetMarketPlace(){
		try{
		List<BasicDBObject>marketplace = new ArrayList<>();
		BasicDBObject value = new BasicDBObject();
		Market x = new Market();
		List<BasicDBObject> athletes = new ArrayList<>();
		athletes = avexDB.GetMarketPlace();
		BasicDBList market = avexDB.GetMarketTrendsToday();
		
		//Create Current Market Value
		for(BasicDBObject athlete:athletes)
		{
			double price = (double)(athlete.get("currentprice"));
			price += x.getMarketprice();
			x.setMarketprice(price);
		}
		value.append("marketprice", x.getMarketprice() / athletes.size());
		value.append("recordstatusdate", new Date());
		value.append("recordstatus",1);
		marketplace.add(value);
    	System.out.println("Created Current Market Value-" + value.get("marketprice")); 
		
		if (market != null && market.size() > 0){
	    	System.out.println("Market Has Value Previous Market Values"); 
		for(Object m:market)
		{
			BasicDBObject o = (BasicDBObject) m;
			marketplace.add(o);
		}
    	System.out.println("Got Previous Market Values: " + market.size()); 
		}
		else{
		  	System.out.println("No Previous Market Values"); 
		}
		
		return marketplace;
		}
		catch(Exception ex){
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
            return null;
		}
	}
}
