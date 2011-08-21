import java.io.IOException;
import java.util.*;
import java.text.*;

public class Trade {
	// practice account
	//enter account and password
	//change real to demon
	private String account="3000018804";
	private String password="Napoleon1";
	public Trade(String instrument, String tradeType, int amount, String setSubscription){
		try {
			tradeType=tradeType.charAt(0)+"";
			instrument=instrument.substring(0, 3)+"/"+instrument.substring(3);
			String command = "fxcmAPI_Real "+account+" "+password+" "+instrument+" "+tradeType+" "+amount+" "+setSubscription;
			Process child = Runtime.getRuntime().exec(command);
			DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date=new Date();

			System.out.println(instrument+" "+tradeType+" "+amount+" "+dateFormat.format(date));
		} catch (IOException e) {
		}
	}

}
