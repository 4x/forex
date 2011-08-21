import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class remoteData {
	public String getRemoteDataAsString(){
		String r="";
		try {
			URL google = new URL("https://fxsignals.dailyfx.com/fxsignals-ds/json/all.do");
			URLConnection yc = google.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				r+=inputLine;

			}
			in.close();
		} catch (Exception e) {
			DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        Date date=new Date();
			System.out.println(e+" "+dateFormat.format(date));
		}
		return r;
	}
}
