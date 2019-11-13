import java.io.IOException;
import java.util.Scanner;

public class ATM {
    
    private Scanner in;
    private BankAccount activeAccount;
    private Bank bank;
    
    public static final int VIEW = 1;
    public static final int DEPOSIT = 2;
    public static final int WITHDRAW = 3;
    public static final int LOGOUT = 4;
    
    
    public static final int INVALID = 0;
    public static final int INSUFFICIENT = 1;
    public static final int SUCCESS = 2;
    
    
    public void startup() {
    	System.out.println("Welcome to the AIT ATM!");
    	
    	System.out.print("Account No.: ");
    	long accountNo = in.nextLong();
    	
    	System.out.print("PIN        : ");
    	int pin = in.nextInt();
    	
    	if (accountNo == activeAccount.getAccountNo() && pin == activeAccount.getPin()) {
    		System.out.println("[1] View Balance");
    		System.out.println("[2] Deposit Money");
    		System.out.println("[3] Withdraw Money");
    		
    		int selection = in.nextInt();
    		switch (selection) { 
    		case 1:
	    		System.out.println("\nCurrent balance: " + activeAccount.getBalance());
	    		break;
    		case 2:
    			System.out.print("\nEnter amount: ");
    			double depositAmt = in.nextDouble();
    			
    			activeAccount.deposit(depositAmt);
    		case 3:
    			System.out.print("\nEneter amount: ");
    			double withdrawAmt = in.nextDouble();
    			
    			activeAccount.withdraw(withdrawAmt);
    			break;
    		default: 
    			System.out.println("\nInvalid selection.\n");
    			break;
    		}
    		
    	} else {
    		System.out.println("\nInvalid account number and/or PIN.\n");
    		
    	}
    }
    
    /**
     * Constructs a new instance of the ATM class.
     */
    
    public ATM() {
        in = new Scanner(System.in);
        
        activeAccount = new BankAccount(1234, 123456789, 0, new User("Ryan", "Wilson"));
        try {
			this.bank = new Bank();
		} catch (IOException e) {
			// cleanup any resources (i.e., the Scanner) and exit
		}
    }
    
    /*
     * Application execution begins here.
     */
    
    public static void main(String[] args) {
        ATM atm = new ATM();
        
        atm.startup();
    }
}
