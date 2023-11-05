package objects;

import java.math.BigDecimal;

public class AmountDueResponse {
	
	private BigDecimal amountCharged = BigDecimal.valueOf(0).setScale(2);
	private Result result = Result.CANCELED;

	public enum Result {
		WITHCHANGE,
		NOCHANGE,
		CHARGED,
		CANCELED,
		PAYMENT
	}
	
	public AmountDueResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
