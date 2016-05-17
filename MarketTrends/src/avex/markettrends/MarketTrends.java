package avex.markettrends;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<Market> marketplace = GetMarketPlace();
		
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
	
	private List<Market> GetMarketPlace(){
		try{
		List<Market>marketplace = new ArrayList<>();
		Market value = new Market();
		List<BasicDBObject> athletes = new ArrayList<>();
		athletes = avexDB.GetMarketPlace();
		List<BasicDBObject> market = avexDB.GetMarketTrendsToday();
		
		//Create Current Market Value
		for(BasicDBObject athlete:athletes)
		{
			double price = (double)(athlete.get("currentprice"));
			price += value.getMarketprice();
			value.setMarketprice(price);
		}
		value.setMarketprice(value.getMarketprice() / athletes.size());
		value.setRecordstatusdate(new Date());
		value.setRecordstatus(1);
		marketplace.add(value);
    	System.out.println("Created Current Market Value-" + value.getMarketprice()); 
		
		if (market != null && market.size() > 0){
	    	System.out.println("Market Has Value Previous Market Values"); 
		for(BasicDBObject m:market)
		{
			Market x = new Market();
			x.setMarketprice((double)(m.get("marketprice")));
			x.setRecordstatusdate(m.getDate("recordstatusdate"));
			x.setRecordstatus((m.getInt("recordstatus")));
			marketplace.add(x);
		}
    	System.out.println("Got Previous Market Values" + market.size()); 
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
