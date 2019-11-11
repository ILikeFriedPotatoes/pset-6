import java.io.IOException;
import java.util.Scanner;
import java.util.Scanner;

public class ATM {
    
    private Scanner in;
    private BankAccount activeAccount;
    private Bank bank;
    
    public void startup() {
    	System.out.println("Welcome to the AIT ATM!");
    	
    	System.out.print("Account No.: ");
    	long accountNo = in.nextLong();
    	
    	System.out.print("PIN        : ");
    	int pin = in.nextInt();
    	
    	if (accountNo == activeAccount.getAccount() && pin ==activeAccount.getPin()) {
    		System.out.println("[1] View Balance");
    		System.out.println("[2] Deposit Money");
    		System.out.println("[3] Withdraw Money");
    		
    	} else {
    		System.out.println("\nInvalid account number and/or PIN.\n")
    		
    	}
    }
    
    /**
     * Constructs a new instance of the ATM class.
     */
    
    public ATM() {
        this.in = new Scanner(System.in);
        
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
