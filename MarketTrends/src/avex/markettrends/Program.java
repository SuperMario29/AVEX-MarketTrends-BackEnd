package avex.markettrends;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


public class Program {

	public static String DATABASE_NAME = "";
	public static String DATABASE_CONNECTION = "";
	public static int DATABASE_PORT = 0;
	
	public static void main(String[] args) {
		
		try{
			InputStream inputStream;
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream =  Program.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		
            //get the property value and print it out
            DATABASE_NAME = prop.getProperty("databasename");
	        DATABASE_CONNECTION = prop.getProperty("databaseconnection");
	        DATABASE_PORT = Integer.valueOf(prop.getProperty("databaseport"));
	        
			MarketTrends marketTrends = new MarketTrends();
			marketTrends.ImportData();
		}
		catch(Exception ex){
            System.out.println("Exception: " + ex.getLocalizedMessage() + "; Stack Trace: " + ex.getStackTrace()); 
		}
	}
}
