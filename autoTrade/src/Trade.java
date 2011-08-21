import java.io.IOException;
import java.util.*;
import java.text.*;

public class Trade {

	private String account="3000018804";
	private String password="Napoleon1";
	String[] subscription={"EURUSD", "USDJPY", "GBPUSD", "USDCHF", "EURCHF", "AUDUSD", 
			"USDCAD", "NZDUSD", "EURJPY", "GBPJPY", "CHFJPY", "GBPCHF"};
	boolean buy;
	public Trade(String instrument, String tradeType, int amount){
		buy=false;
		for(String s:subscription){
			if(s.equals(instrument)){
				buy=true;
			}
		}
		if(buy==true){
			try {
				tradeType=tradeType.charAt(0)+"";
				instrument=instrument.substring(0, 3)+"/"+instrument.substring(3);
				String command = "fxcmAPI_Real "+account+" "+password+" "+instrument+" "+tradeType+" "+amount;
				Process child = Runtime.getRuntime().exec(command);
				DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date=new Date();

				System.out.println(instrument+" "+tradeType+" "+amount+" "+dateFormat.format(date));
			} catch (IOException e) {
			}
		}
	}

}
