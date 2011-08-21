import java.io.Serializable;


public class TradeRecord implements Serializable {
	String signal;
	String direction;
	int amount;
	public TradeRecord(String signal, String direction, int amount){
		this.signal=signal;
		this.direction=direction;
		this.amount=amount;
	}
}
