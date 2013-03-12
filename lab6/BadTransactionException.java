

public class BadTransactionException extends Exception {

	public int transactionAmount;  // The invalid account number.

	  /**
	   *  Creates an exception object for nonexistent account "badAcctNumber".
	   **/
	  public BadTransactionException(int badTransactionAmount) {
	    System.out.println("Invalid Transaction Amount: " + badTransactionAmount);

	    transactionAmount = badTransactionAmount;
	  }

}
