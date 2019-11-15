import java.io.IOException;
import java.util.Scanner;

public class ATM {
    
    private Scanner in;
    private BankAccount activeAccount;
    private User username;
    private Bank bank;
    
    /**
     * Constructs a new instance of the ATM class.
     */
    
    public ATM() {
        in = new Scanner(System.in);
        
        activeAccount = new BankAccount(1234, 123456789, 0, new User("Ryan", "Wilson"));
        username = new User("Ryan", "Wilson");
        try {
			this.bank = new Bank();
		} catch (IOException e) {
			// cleanup any resources (i.e., the Scanner) and exit
			in.close();
		}
    }
    
    public static final int VIEW = 1;
    public static final int DEPOSIT = 2;
    public static final int WITHDRAW = 3;
    public static final int LOGOUT = 4;
    
    
    public static final int INVALID = 0;
    public static final int INSUFFICIENT = 1;
    public static final int SUCCESS = 2;
    
    
    public void startup() {
    	System.out.println("Welcome to the AIT ATM!");
    	String accountNo;
    	int pin;
    	
    	do {
    		System.out.print("\nAccount No.: ");
        	accountNo = in.nextLine();
        	
        	//Creates a new account
        	if(accountNo.equals("+")) {
        		System.out.println("\naccountNo == +\n");
        		break;
        	}
        	
        	System.out.print("PIN        : ");
        	pin = in.nextInt();
        	in.nextLine();
        	
        	if(Long.valueOf(accountNo) != activeAccount.getAccountNo() || pin != activeAccount.getPin()) {
        		System.out.println("\nInvalid account number and/or PIN.");
        	}
    	} while(Integer.valueOf(accountNo) != activeAccount.getAccountNo() || pin != activeAccount.getPin());
		
    	System.out.println("\nHello, again, " + username.getFirstName() + "!");
    	
    	System.out.println("\n[1] View Balance");
		System.out.println("[2] Deposit Money");
		System.out.println("[3] Withdraw Money");
		System.out.println("[4] Transfer Money");
		System.out.println("[5] Logout");
		
		int selection = in.nextInt();
		switch (selection) { 
		case 1:
    		System.out.println("\nCurrent balance: " + activeAccount.getBalance());
    		break;
		case 2:
			System.out.print("\nEnter amount: ");
			double depositAmt = in.nextDouble();
			
			activeAccount.deposit(depositAmt);
			break;
		case 3:
			System.out.print("\nEnter amount: ");
			double withdrawAmt = in.nextDouble();
			
			activeAccount.withdraw(withdrawAmt);
			break;
		case 4:
			break;
		case 5:
			break;
		default: 
			System.out.println("\nInvalid selection.\n");
			break;
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
