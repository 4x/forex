import java.io.IOException;
import java.util.*;
import java.text.*;
import java.util.ArrayList;
import java.sql.*;
public class Trade {
	// practice account
	//enter account and password
	//change real to demon
	public Trade(String instrument, String tradeType, int amount, String setSubscription, String pl){
		connectDatabase();
		try{
            Statement statement = connection.createStatement();
			
            String sql="insert into record (instrument,type,amount,pl) values('"+instrument+"','"+tradeType+"',"+amount+","+pl+")";
			
            statement.execute(sql);
            statement.close();
            
        }catch(Exception e){
            System.out.print(e);
        }
	}
	Connection connection=null;

	public void connectDatabase(){
        String userName="zjin";
        String userPasswd="spq0bmioa";
        String dbName="forex";
        String url="jdbc:mysql://cloud.cutediy.com/"+dbName+"?user="+userName+"&password="+userPasswd;
		
        try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection=DriverManager.getConnection(url);
			
        }catch(Exception e){
            System.out.print(e);
        }

    }

}
