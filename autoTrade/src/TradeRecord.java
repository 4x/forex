import java.io.Serializable;


public class TradeRecord implements Serializable {
	String signal;
	String direction;
	int amount;
	String pl;
	public TradeRecord(String signal, String direction, int amount, String pl){
		this.signal=signal;
		this.direction=direction;
		this.amount=amount;
		this.pl=pl;
	}
}
