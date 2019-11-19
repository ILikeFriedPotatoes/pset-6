import java.io.IOException;
import java.util.Scanner;

public class ATM {
    
    public static final int FIRST_NAME_WIDTH = 20;
	public static final int LAST_NAME_WIDTH = 30;
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
        		System.out.print("\nFirst Name: ");
        		String firstName = in.nextLine();
        		System.out.print("\nLast Name: ");
        		String lastName = in.nextLine();
        		do {
            		System.out.print("\nPIN: ");
        			pin = in.nextInt();
            		in.nextLine();
        		} while(pin < 1000 || pin > 9999);
        		activeAccount = new BankAccount(pin, 123456789, 0, new User(firstName, lastName));
        	} else { 
	        	System.out.print("PIN        : ");
	        	pin = in.nextInt();
	        	in.nextLine();
        	}
        	
        	if(Long.valueOf(accountNo) != activeAccount.getAccountNo() || pin != activeAccount.getPin()) {
        		System.out.println("\nInvalid account number and/or PIN.");
        	}
        	
    	} while(Integer.valueOf(accountNo) != activeAccount.getAccountNo() || pin != activeAccount.getPin());
    	
    	if (isValidLogin(accountNo, pin)) {
    		System.out.println("\nHello, again, " + username.getFirstName() + "!");
	    	
			boolean validLogin = true;
			while (validLogin) {
				switch (getSelection()) { 
					case 1:
			    		showBalance();
			    		break;
					case 2:
						deposit();
						break;
					case 3:
						withdraw();
						break;
					case 4:
						validLogin = false;
						break;
					case 5:
						break;
					default: 
						System.out.println("\nInvalid selection.\n");
						break;
				}
			}
    	} else {
    		System.out.println("\nInvalid account number and/or PIN.\n");
    	}
	}
    
    public boolean isValidLogin(String accountNo, int pin) {
    	return Integer.parseInt(accountNo) == activeAccount.getAccountNo() && pin == activeAccount.getPin();
    }
    
    public int getSelection() {
    	System.out.println("\n[1] View Balance");
		System.out.println("[2] Deposit Money");
		System.out.println("[3] Withdraw Money");
		System.out.println("[4] Transfer Money");
		System.out.println("[5] Logout");
    	
    	return in.nextInt();
    }
    
    public void showBalance() {
    	System.out.println("\nCurrenct balance: " + activeAccount.getBalance());
    }
    
    public void deposit() {
    	System.out.print("\nEnter amount: ");
    	double amount = in.nextDouble();
    	
    	activeAccount.deposit(amount);
    	System.out.println();
    }
    
    public void withdraw() {
    	System.out.print("\nEnter amount: ");
    	double amount = in.nextDouble();
    	
    	activeAccount.withdraw(amount);
    	System.out.println();
    }
    
    /*
     * Application execution begins here.
     */
    
    public static void main(String[] args) {
        ATM atm = new ATM();
        
        atm.startup();
    }
}
