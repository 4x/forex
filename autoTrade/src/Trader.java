import java.util.ArrayList;
import java.io.*;


public class Trader implements Serializable {
	static ArrayList<TradeRecord> openPositions=new ArrayList<TradeRecord>();
	static ArrayList<String> fxList=new ArrayList<String>();
	static ArrayList<String> alerts=new ArrayList<String>();
	static int amount=50;

	public static void main(String [] arg){
		
		load();

		while(true){
			check();

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 

	public static void load(){
		dataFile df=refresh();

		//openPositions load
		//open when closed
		FileInputStream fis;
		try {
			fis = new FileInputStream("traderecords");
			ObjectInputStream ois=new ObjectInputStream(fis);
			openPositions=(ArrayList<TradeRecord>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		//fxList load
		int fxListSize=df.getSize("Signal");
		for(int i=0;i<fxListSize;i++){
			if(df.getValue("Signal", "shortname", i).equals("Breakout2")){
				fxList.add(df.getValue("Signal", "symbol", i));
			}
		}

		//alerts load
		int alertSize=df.getSize("Alert");
		for(int i=0;i<alertSize;i++){
			String message=df.getValue("Alert", "message", i);
			if(message.contains("StopLoss Hit At")||message.contains("Limit Hit At")){
				alerts.add(message);
			}
		}
	}

	public static void check(){
		try{
			dataFile df=refresh();
			
			
			int SS=df.getSize("Signal");
			ArrayList<String[]> TT =new ArrayList<String[]>();
			for(int i=0;i<SS;i++){
				if(df.getValue("Signal", "shortname", i).equals("Breakout2")){
					String[] aa={df.getValue("Signal", "symbol", i),df.getValue("Signal", "estPnl", i)};
					TT.add(aa);
				}
			}
			for(TradeRecord t:openPositions){
				for(int i=0; i<TT.size(); i++){
					String[] aa=TT.get(i);
					if(t.signal.equals(aa[0])){
						t.pl=aa[1];
					}
				}
			}
			
			
			
			checkBuy(df);

			checkSell(df);
			
			int fxListSize=df.getSize("Signal");
			fxList.clear();
			for(int i=0;i<fxListSize;i++){
				if(df.getValue("Signal", "shortname", i).equals("Breakout2")){
					fxList.add(df.getValue("Signal", "symbol", i));
				}
			}
			
			closeDisappearPosition();
			
		}catch (Exception e){
			
		}
	}

	private static void closeDisappearPosition() {
		// TODO Auto-generated method stub
		for(int i=0; i<openPositions.size(); i++){
			if(!fxList.contains(openPositions.get(i).signal)){
				sellAll(openPositions.get(i).signal, openPositions.get(i).direction, openPositions.get(i).amount, i, openPositions.get(i).pl);
				i--;
			}
		}
		
	}

	private static void sellAll(String signal, String direction, int amount, int i, String pl) {
		// TODO Auto-generated method stub
		if(direction.equals("Buy")){
			direction="Sell";
		}else{
			direction="Buy";
		}
		new Trade(signal, direction, amount, "D", pl);
		openPositions.remove(i);
		writeFile();
	}

	public static void checkBuy(dataFile df){
		int size=df.getSize("Signal");
		for(int i=0; i<size; i++){
			if(df.getValue("Signal", "shortname", i).equals("Breakout2")&&!fxList.contains(df.getValue("Signal", "symbol", i))){
				openPosition(df.getValue("Signal", "symbol", i), df.getValue("Signal", "direction", i));
			}
		}
	}

	public static void checkSell(dataFile df){
		int size=df.getSize("Alert");
		for(int i=0; i<size; i++){
			String message=df.getValue("Alert", "message", i);
			if(message.contains("StopLoss Hit At")||message.contains("Limit Hit At")){
				if(!alerts.contains(message)){
					String signal=message.substring(0, 6);
					int s=openPositions.size();
					for(int j=0; j<s; j++){
						if(openPositions.get(j).signal.equals(signal)){
							closePosition(signal, openPositions.get(j).direction, openPositions.get(j).pl);
							alerts.add(message);
							if(alerts.size()>20){
								alerts.remove(alerts.size()-1);
							}
						}
					}
				}
			}
		}
	}

	public static dataFile refresh(){
		return new dataFile(new remoteData().getRemoteDataAsString());
	}

	public static void openPosition(String signal, String direction){
		new Trade(signal, direction, amount, "E", "0.0");
		openPositions.add(new TradeRecord(signal, direction, amount, "0.0"));
//		fxList.add(signal);
		writeFile();
	}

	public static void closePosition(String signal, String direction, String pl){
		if(direction.equals("Buy")){
			direction="Sell";
		}else{
			direction="Buy";
		}
		int size=openPositions.size();
		boolean isClosed=false;
		for(int i=0; i<size; i++){
			if(openPositions.get(i).signal.equals(signal)){
				openPositions.get(i).amount-=amount/5;
				if(openPositions.get(i).amount==0){
					openPositions.remove(i);
					isClosed=true;
					i--;
				}
			}
		}
		if(isClosed){
			new Trade(signal, direction, amount/5, "D", pl);
		}else{
			new Trade(signal, direction, amount/5, "N", pl);
		}
		writeFile();
	}
	public static void writeFile(){
		try {
			FileOutputStream fos=new FileOutputStream("traderecords");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(openPositions);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
