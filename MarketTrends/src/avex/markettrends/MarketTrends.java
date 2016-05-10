package avex.markettrends;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;

import avex.models.Market;

public class MarketTrends {

	private AVEXDB avexDB = new AVEXDB();
	
	public void ImportData(){
		
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
		List<Market>marketplace = new ArrayList<>();
		Market value = new Market();
		List<BasicDBObject> athletes = new ArrayList<>();
		athletes = avexDB.GetMarketPlace();
		List<BasicDBObject> market = avexDB.GetMarketTrendsToday();
		
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
		
		for(BasicDBObject m:market)
		{
			Market x = new Market();
			x.setMarketprice((double)(m.get("marketprice")));
			x.setRecordstatusdate(m.getDate("recordstatusdate"));
			x.setRecordstatus((m.getInt("recordstatus")));
			marketplace.add(x);
		}
		
		return marketplace;
	}
	
}
