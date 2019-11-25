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
    	long accountNo = 0;
    	
    	while (true) {
    		System.out.print("\nAccount No.  : ");
        	String accountNumber = in.nextLine();
        	int pin;
        	
        	//Creates a new account
        	if(accountNumber.equals("+")) {
        		createAccount();
        	} else if(isNumber(accountNumber)) {
        		Long.parseLong(accountNumber);
        	}
        	
        	System.out.print("PIN          : ");
        	pin = in.nextInt();
        	in.nextLine();
        	
        	if (isValidLogin(accountNo, pin)) {
        		System.out.println("\nHello, again, " + getFirstName() + "!");
        		
    			boolean validLogin = true;
    			while (validLogin) {
    				switch (getSelection()) { 
    					case 1:
    						in.nextLine();
    			    		showBalance();
    			    		break;
    					case 2:
    						in.nextLine();
    						deposit();
    						break;
    					case 3:
    						in.nextLine();
    						withdraw();
    						break;
    					case 4:
    						in.nextLine();
    						validLogin = false;
    						break;
    					case 5:
    						in.nextLine();
    						break;
    					default: 
    						in.nextLine();
    						System.out.println("\nInvalid selection.\n");
    						break;
    				}
    			}
        	} else { 
	        	if(accountNo == -1 && pin == -1) {
	        		shutdown();
	        	} else {
	        		System.out.println("\nInvalid account number and/or PIN.\n");
	        	}
        	}
        	
    	}
    	
    }
   
    public boolean isValidLogin(long accountNo, int pin) {
        return accountNo == activeAccount.getAccountNo() && pin == activeAccount.getPin();
    }
    
    public String getFirstName() {
    	return username.getFirstName();
    }
    
    public String getLastName() {
    	return username.getLastName();
    }
    
    public BankAccount createAccount() {
    	System.out.print("\nFirst Name: ");
		String firstName = in.nextLine();
		
		System.out.print("\nLast Name: ");
		String lastName = in.nextLine();
		
		int pin;
		do {
    		System.out.print("\nPIN: ");
			pin = in.nextInt();
    		in.nextLine();
		} while(pin < 1000 || pin > 9999);
		
		BankAccount newAccount = bank.createAccount(pin, new User(firstName, lastName));
		bank.update(newAccount);
		bank.save();
		
		System.out.println("\nThank you! Your account number is: " + newAccount.getAccountNo());
		System.out.println("You may now login to your new account.\n");
		
		return newAccount;
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
    
    private boolean isNumber(String str) {
    	try {
    		Long.parseLong(str);
    		return true;
    	} catch(Exception E) {
    		return false;
    	}
    }
    
    public void deposit() {
    	System.out.print("\nEnter amount: ");
    	double amount = in.nextDouble();
    	
    	int status = activeAccount.deposit(amount);
    	if (status == ATM.INVALID) {
    		System.out.println("\nDeposit rejected. Amount must be greater than $0.00.\n");
    	} else if (status == ATM.SUCCESS) {
    		System.out.println("\nDeposit accepted.\n");
    	}
    }
    
    public void withdraw() {
    	System.out.print("\nEnter amount: ");
    	double amount = in.nextDouble();
    	
    	int status = activeAccount.withdraw(amount);
        if (status == ATM.INVALID) {
            System.out.println("\nWithdrawal rejected. Amount must be greater than $0.00.\n");
        } else if (status == ATM.INSUFFICIENT) {
            System.out.println("\nWithdrawal rejected. Insufficient funds.\n");
        } else if (status == ATM.SUCCESS) {
            System.out.println("\nWithdrawal accepted.\n");
        }
    }
    
    public void shutdown() {
    	if (in != null) {
    		in.close();
    	}
    	
    	System.out.println("\nGoodbye!");
    	System.exit(0);
    }
    
    /*
     * Application execution begins here.
     */
    
    public static void main(String[] args) {
        ATM atm = new ATM();
        
        atm.startup();
    }
}
