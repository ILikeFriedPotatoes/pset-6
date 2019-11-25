import java.io.IOException;
import java.util.Scanner;

public class ATM {

    public static final int FIRST_NAME_WIDTH = 20;
	public static final int LAST_NAME_WIDTH = 30;
	private Scanner in;
    private BankAccount activeAccount;
    //I'll keep this for now, but eclipse says this variable is useless. private User username;
    private Bank bank;
    
    //Private variables for selection
    private static final int VIEW = 1;
    private static final int DEPOSIT = 2;
    private static final int WITHDRAW = 3;
    private static final int TRANSFER = 4;
    private static final int LOGOUT = 5;

    //Static variables for withdraw()
    private static final int INVALID = 0;
    private static final int INSUFFICIENT = 1;
    private static final int SUCCESS = 2;

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

    	long accountNo = enterAccountNumber();
    	int pin = enterPin();
    	enterAccountInformation(accountNo, pin);
		/*
    	try {
			activeAccount = bank.login(accountNo, pin);
			System.out.println("\nHello, again, " + activeAccount.getAccountHolder().getFirstName() + "!");
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
						validLogin = false; break;
					default:
						in.nextLine();
						System.out.println("\nInvalid selection.\n"); break;
				}
			}
		} catch(Exception e) {
			System.out.println("\nInvalid account number and/or PIN.");
			//enterAccountInformation()
		}
		*/
    }

    /*
     * Methods for user entering account and pin information
     */

    private void enterAccountInformation(long accountNo, int pin) {
    	try {
			activeAccount = bank.login(accountNo, pin);
			System.out.println("\nHello, again, " + activeAccount.getAccountHolder().getFirstName() + "!");
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
						validLogin = false; break;
					default:
						in.nextLine();
						System.out.println("\nInvalid selection.\n"); break;
				}
			}
		} catch(Exception e) {
			System.out.println("\nInvalid account number and/or PIN.\n");
			enterAccountInformation(accountNo, pin);
		}
    }
    
    private long enterAccountNumber() {
    	long accountNo = 0;
    	System.out.print("\nAccount No.  : ");
    	String accountNumber = in.nextLine();
    	if(accountNumber.equals("+")) {
    		createAccount();
    		startup();
    	} else if (accountNumber.equals(""))  {
    		System.out.println("\nYour input was invalid. Please try again.");
    		startup();
    	} else if(isNumber(accountNumber)) {
    		accountNo = Long.parseLong(accountNumber);
    	} else {
    		System.out.println("\nYour input was invalid. Please try again.");
    		startup();
    	}

    	return accountNo;
    }

    private int enterPin() {
        int pin = 0;
        try {
            System.out.print("PIN          : ");
            pin = in.nextInt();
            in.nextLine();
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
    	} else if (status == ATM.SUCCESS) {
    		System.out.println("\n" + activeAccount);
    		bank.update(activeAccount);
    		bank.save();
    		System.out.println("\nDeposit accepted.");
    	}
    }

    public void withdraw() {
    	System.out.print("\nEnter amount: ");
    	double amount = 0;
    	try {
    		amount = in.nextDouble();
    	} catch (Exception e) {
    		System.out.println("");
    		bank.update(activeAccount);
    		bank.save();
    		System.out.println("\nWithdrawal accpeted");
    	}

    	int status = activeAccount.withdraw(amount);
        if (status == ATM.INVALID) {
            System.out.println("\nWithdrawal rejected. Amount must be greater than $0.00.\n");
        } else if (status == ATM.INSUFFICIENT) {
            System.out.println("\nWithdrawal rejected. Insufficient funds.\n");
        } else if (status == ATM.SUCCESS) {
            System.out.println("\nWithdrawal accepted.\n");
        }
    }

    public void transfer() {
        long accountNo = 0;
        double amount = -1;

        do {
            System.out.println("Enter account: ");
            accountNo = in.nextLong();
        } while(accountNo < 100000000 || accountNo > 999999999);

        do {
            System.out.println("Enter an amount: ");
            amount = in.nextDouble();
        } while(amount < 0 || amount > activeAccount.getNumBalance());
        
        transferralDeposit(accountNo, amount);
        transferralWithdrawal(activeAccount.getAccountNo(), amount);
        
    }

    private void transferralDeposit(long accountDestination, double amount) {
    	BankAccount depositAccount = bank.getAccount(accountDestination);
    	depositAccount.deposit(amount);
    	bank.update(depositAccount);
    	bank.save();
    }

    private void transferralWithdrawal(long usedAccount, double amount) {
    	activeAccount.withdraw(amount);
    	bank.update(activeAccount);
    	bank.save();
    }

    /*
     * Auxiliary functions
    */

    private BankAccount createAccount() {
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
