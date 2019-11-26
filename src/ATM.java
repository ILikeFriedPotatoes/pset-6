import java.io.IOException;
import java.util.Scanner;

public class ATM {

    public static final int FIRST_NAME_WIDTH = 20;
	public static final int LAST_NAME_WIDTH = 30;
	private Scanner in;
    private BankAccount activeAccount;
    //I'll keep this for now, but eclipse says this variable is useless. private User username;
    private Bank bank;
    private long accountNo;
    private int pin;

    //Private variables for selection
    private static final int VIEW = 1;
    private static final int DEPOSIT = 2;
    private static final int WITHDRAW = 3;
    private static final int TRANSFER = 4;
    private static final int LOGOUT = 5;

    //Static variables for withdraw()
    public static final int INVALID = 0;
    public static final int INSUFFICIENT = 1;
    public static final int SUCCESS = 2;

    /**
     * Constructs a new instance of the ATM class.
     */

    public ATM() {
        in = new Scanner(System.in);
        //username = new User("Ryan", "Wilson");
        try {
			this.bank = new Bank();
		} catch (IOException e) {
			// cleanup any resources (i.e., the Scanner) and exit
			in.close();
		}
    }

    public void startup() {
    	System.out.println("Welcome to the AIT ATM!");

    	enterAccountInformation();
        System.out.println("\nHello, again, " + this.activeAccount.getAccountHolder().getFirstName() + "!");
        selectOption();
    }
    
    /*
     *  Method for selecting the show bank account, deposit, withdraw, or logout.
     */
    
    private void selectOption() {
    	 boolean validLogin = true;
         while (validLogin) {
             switch (getSelection()) {
                 case VIEW:
                     showBalance(); break;
                 case DEPOSIT:
                     deposit(); break;
                 case WITHDRAW:
                     withdraw(); break;
                 case TRANSFER:
                     transfer(); break;
                 case LOGOUT:
                     enterAccountInformation(); break;
                 default:
                     System.out.println("\nInvalid selection.\n"); break;
             }
         }
    }

    /*
     * Methods for user entering account and pin information
     */

    private void enterAccountInformation() {
    	try {
    		this.accountNo = enterAccountNumber();
    		System.out.println(accountNo);
    		this.pin = enterPin();
    		if(this.pin == -1 && this.accountNo == -1) {
    			shutdown();
    		}
			this.activeAccount = this.bank.login(this.accountNo, this.pin);
		} catch(Exception e) {
			System.out.println("\nInvalid account number and/or PIN.\n");
			enterAccountInformation();
		}
    }

    private long enterAccountNumber() {
    	long accountNo = 0;
    	System.out.print("\nAccount No.  : ");
    	String accountNumber = in.nextLine();
    	if(accountNumber.equals("+")) {
    		createAccount();
    	} else if (accountNumber.equals(""))  {
    		System.out.println("\nYour input was invalid. Please try again.");
    		enterAccountNumber();
    	} else if(isNumber(accountNumber)) {
    		accountNo = Long.parseLong(accountNumber);
    	} else {
    		System.out.println("\nYour input was invalid. Please try again.");
    		enterAccountNumber();
    	}

    	return accountNo;
    }

    private int enterPin() {
        int pin = 0;
        try {
        	do {
	            System.out.print("PIN          : ");
	            pin = in.nextInt();
	            in.nextLine();
	            if(pin == -1 && this.accountNo == -1) {
	            	shutdown();
	            }
        	} while (pin < 1000 || pin > 9999);
        } catch(Exception e) {
            System.out.println("\nYour input was invalid. Please try again.");
            startup();
        }
        return pin;
    }

    /*
     * The four key functions: show balance, deposit, withdraw, and transfer.
    */

    public void showBalance() {
    	System.out.println("\nCurrenct balance: " + activeAccount.getBalance());
    }

    public void deposit() {
    	System.out.print("\nEnter amount: ");
    	double amount = 0;
    	try {
    		amount = in.nextDouble();
    	} catch (Exception e) {
    		System.out.println("\nYour input was invalid. Please try again");
    		deposit();
    	}
    	int status = activeAccount.deposit(amount);
    	if (status == ATM.INVALID) {
    		System.out.println("\nDeposit rejected. Amount must be greater than $0.00.\n");
    		selectOption();
    	} else if (status == ATM.SUCCESS) {
    		System.out.println("\n" + activeAccount);
    		this.bank.update(activeAccount);
    		this.bank.save();
    		System.out.println("\nDeposit accepted.");
    	} else if (amount > (999999999999.99 - activeAccount.getNumBalance())) {
    		System.out.println("\nDeposit rejected. Amount would cause balance to exceed $999,999,999,999.99.");
    		selectOption();
    	}
    }

    public void withdraw() {
    	System.out.print("\nEnter amount: ");
    	double amount = 0;
    	try {
    		amount = in.nextDouble();
    	} catch (Exception e) {
    		System.out.println("");
    		this.bank.update(activeAccount);
    		this.bank.save();
    		System.out.println("\nWithdrawal accpeted");
    	}

    	int status = activeAccount.withdraw(amount);
        if (status == ATM.INVALID) {
            System.out.println("\nWithdrawal rejected. Amount must be greater than $0.00.\n");
        } else if (status == ATM.INSUFFICIENT) {
            System.out.println("\nWithdrawal rejected. Insufficient funds.\n");
        } else if (status == ATM.SUCCESS) {
        	System.out.println(activeAccount);
        	this.bank.update(activeAccount);
        	this.bank.save();
            System.out.println("\nWithdrawal accepted.\n");
        }
    }

    public void transfer() {
        long accountNo = 0;
        double amount = -1;

        do {
            System.out.println("Enter account: ");
            try {
            	accountNo = in.nextLong();
            } catch(Exception e) {
            	System.out.println("\nTransfer rejected. Destination not found");
        		selectOption();
            }
        } while(accountNo < 100000000 || accountNo > 999999999);

        do {
            System.out.println("Enter an ammount: ");
            amount = in.nextDouble();
        } while(amount < 0 || amount > activeAccount.getNumBalance());
        if (amount < 0) {
        	System.out.println("\nTransfer rejected. Amount must be greater than $0.00.");
    		selectOption();
        } else if (amount > (999999999999.99 - bank.getAccount(accountNo).getNumBalance())) {
        	System.out.println("\nTransfer rejected. Amount would cause destination balance to exceed $999,999,999,999.99.");
        	selectOption();
        } else if (amount > activeAccount.getNumBalance()) {
        	System.out.println("\nTransfer rejected. Insufficient funds.");
        	selectOption();
        }
        transferalDeposit(accountNo, amount);
        transferalWithdrawal(activeAccount.getAccountNo(), amount);
        
    }

    public void transferalDeposit(long accountNo, double amount) {
    	BankAccount accountDestination;
    	try {
    		accountDestination = bank.getAccount(accountNo);
        	accountDestination.deposit(amount);
        	bank.update(accountDestination);
        	bank.save();
    	} catch (Exception e) {
    		System.out.println("\nTransfer rejected. Destination not found");
    		this.accountNo = 0;
    		selectOption();
    	}
    }
    

    public void transferalWithdrawal(long usedAccount, double amount) {
    	activeAccount.withdraw(amount);
    	bank.update(activeAccount);
    	bank.save();
    }

    /*
     * Auxiliary functions
    */

    private BankAccount createAccount() {
    	String firstName = "";
    	do {
    		System.out.print("\nFirst Name: ");
    		firstName = in.nextLine();
    	} while(firstName.length() < 1 || firstName.length() > FIRST_NAME_WIDTH);
		
    	String lastName = "";
    	do {
	    	System.out.print("\nLast Name: ");
			lastName = in.nextLine();
    	} while (lastName.length() < 1 || lastName.length() > LAST_NAME_WIDTH);

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

    private void shutdown() {
    	if (in != null) {
    		in.close();
    	}

    	System.out.println("\nGoodbye!");
    	System.exit(0);
    }

    private boolean isNumber(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(Exception E) {
            return false;
        }
    }

    private int getSelection() {
        int choice = 0;
        while(choice < 1 || choice > 5) {
            System.out.println("\n[1] View Balance");
            System.out.println("[2] Deposit Money");
            System.out.println("[3] Withdraw Money");
            System.out.println("[4] Transfer Money");
            System.out.println("[5] Logout\n");
            choice = in.nextInt();
            in.nextLine();
            if(choice < 1 || choice > 5) {
            	 System.out.println("\nInvalid selection.\n");
            }
        }
        return choice;
    }


    /*
     * Application execution begins here.
     */

    public static void main(String[] args) {
        ATM atm = new ATM();

        atm.startup();
    }
}
