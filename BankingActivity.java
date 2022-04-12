import java.util.Scanner;

public class BankingActivity {

    public static void main(String[] args) {
        
        Scanner inputScanner = new Scanner(System.in);

        int usersArraySize = 5;
        int availableUsers = 0;
        String[] usersName = new String[usersArraySize];
        double[] usersFund = new double[usersArraySize];
        String[] usersCurrency = new String[usersArraySize];
        String[] availableCurrencies = {"PHP", "USD", "YEN"};

        System.out.println("Welcome to Banking App");
        if(usersName[0] == null){
            System.out.println("No account detected.");
            availableUsers = addUser(availableUsers, usersName, usersFund, usersCurrency, availableCurrencies, inputScanner);
        }
        
        int transactionNumber;
        int foundUserIndex;

        do {
            transactionNumber = getTransactionNumber(inputScanner);

            switch(transactionNumber) {
                case 1: {
                    //working loop but commented for now
                    // int goBack = 1;
                    // do {
                        foundUserIndex = startUserSelection(availableUsers, usersName, usersFund, inputScanner);
                        if(foundUserIndex == -1) {
                            System.out.println("User not found.");
                        }
                        else {
                            displayInformation(usersName[foundUserIndex], usersFund[foundUserIndex], usersCurrency[foundUserIndex], inputScanner);
                        }
                    //     System.out.println("[0] Go Back");
                    //     goBack = inputScanner.nextInt();
                    // } while (goBack != 0);

                    break;
                }
                case 2: {
                    foundUserIndex = startUserSelection(availableUsers, usersName, usersFund, inputScanner);
                    if(foundUserIndex == -1) {
                        System.out.println("User not found.");
                    }
                    else {
                        addFunds(foundUserIndex, usersFund, usersCurrency[foundUserIndex], inputScanner);
                    }
                    break;                    
                }
                case 3: {
                    foundUserIndex = startUserSelection(availableUsers, usersName, usersFund, inputScanner);
                    if(foundUserIndex == -1) {
                        System.out.println("User not found.");
                    }
                    else {
                        withdrawFunds(foundUserIndex, usersFund, usersCurrency[foundUserIndex], inputScanner);
                    }
                    break;                    
                }
                case 4: {
                    foundUserIndex = startUserSelection(availableUsers, usersName, usersFund, inputScanner);
                    if(foundUserIndex == -1) {
                        System.out.println("User not found.");
                    }
                    else {
                        convertFunds(foundUserIndex, usersFund, usersCurrency, availableCurrencies, inputScanner);
                    }
                    break;
                }
                case 5: {
                    availableUsers = addUser(availableUsers, usersName, usersFund, usersCurrency, availableCurrencies, inputScanner);
                    break;
                }        
            }
        } while (transactionNumber != 0);

        inputScanner.close();
    }

    public static int startUserSelection(int availableUsers, String[] usersName, double[] usersFund, Scanner inputScanner){
        System.out.println("<==========================>");
        System.out.println("--Account List--");
        System.out.println("(Display Format: User - Funds)");
        for(int x = 0; x < availableUsers; x++)
        {
            System.out.println("> " + usersName[x] + " - " + usersFund[x]);
        }
        System.out.println("<==========================>");
        System.out.println("Please enter user of account you want to use for this transaction: ");
        
        inputScanner.nextLine();
        String user = inputScanner.nextLine();
        
        int foundUserIndex = -1;

        for(int index = 0; index < availableUsers; index++)
        {   
            if(usersName[index].equals(user)) {
                foundUserIndex = index;
            }
        }

        return foundUserIndex;
    }

    public static double getCurrencyConversionRate(String fromCurrency, String toCurrency) {
        double conversionRate = 1;

        switch(fromCurrency) {
            case "PHP": {
                if(toCurrency == "USD") {
                    conversionRate = 0.019;
                } else if(toCurrency == "YEN") {
                    conversionRate = 2.41;
                }
                break;
            }
            case "USD": {
                if(toCurrency == "PHP") {
                    conversionRate = 52.07;
                } else if(toCurrency == "YEN") {
                    conversionRate = 125.67;
                }
                break;
            }
            case "YEN": {
                if(toCurrency == "PHP") {
                    conversionRate = 0.41;
                } else if(toCurrency == "USD") {
                    conversionRate = 0.008;
                }
                break;
            }
        }

        return conversionRate;
    }

    public static void convertFunds(int currentUserIndex, double[] usersFund, String[] usersCurrency, String[] availableCurrencies, Scanner inputScanner){
        String userCurrentCurrency = usersCurrency[currentUserIndex];
        String[] newCurrencyOptions = new String[availableCurrencies.length - 1];
        
        System.out.println("[Convert Funds]");
        System.out.println("Current Currency is set to " + userCurrentCurrency);
        System.out.println("Available Currencies: ");

        int optionNumber = 1;
        for (int index = 0; index < availableCurrencies.length; index++) {
            if(availableCurrencies[index] != userCurrentCurrency){
                System.out.println("[" + optionNumber + "] " + availableCurrencies[index] +
                " (1 " + userCurrentCurrency + " = " 
                + getCurrencyConversionRate(userCurrentCurrency, availableCurrencies[index]) + " " 
                + availableCurrencies[index] + ")");
                newCurrencyOptions[optionNumber - 1] = availableCurrencies[index];
                optionNumber++;
            }
        }

        System.out.println("Please enter the number of the currency you want to convert to: ");
        
        int newCurrencyIndex = inputScanner.nextInt();
        String newCurrency = newCurrencyOptions[newCurrencyIndex - 1];
        double conversionRate = getCurrencyConversionRate(userCurrentCurrency, newCurrency);

        usersFund[currentUserIndex] *= conversionRate;
        usersCurrency[currentUserIndex] = newCurrency;
    }

    public static void withdrawFunds(int currentUserIndex, double[] usersFund, String userCurrency, Scanner inputScanner) {
        System.out.println("[Withdraw Funds]");
        System.out.println("Withdraw Amount: (" + userCurrency + ") ");
        double withdrawAmount = inputScanner.nextDouble();
        

        if(withdrawAmount > usersFund[currentUserIndex]) {
            System.out.println("Invalid amount. Amount exceeds your current funds.");
        }
        else if(withdrawAmount < 500) {
            System.out.println("Invalid amount. Only amounts worth 500 or more accepted.");
        }
        else {
            usersFund[currentUserIndex] -= withdrawAmount;
        }
    }

    public static void addFunds(int currentUserIndex, double[] usersFund, String userCurrency, Scanner inputScanner) {
        System.out.println("[Add Funds]");
        System.out.println("Deposit Amount: (" + userCurrency + ") ");
        double depositAmount = inputScanner.nextDouble();
        

        if(depositAmount < 500) {
            System.out.println("Invalid amount. Only amounts worth 500 or more accepted.");
        }
        else {
            usersFund[currentUserIndex] += depositAmount;
        }
    }

    public static void displayInformation(String userName, double userFund, String userCurrency, Scanner inputScanner) {
        System.out.println("[Display Information]");
        System.out.println("Name: " + userName);
        System.out.println("Funds: " + userCurrency + " " + userFund);
        System.out.println("Currency: " + userCurrency);
    }

    public static int getTransactionNumber(Scanner inputScanner){
        System.out.println("<==========================>");
        System.out.println("[1] Display Information");
        System.out.println("[2] Add Funds");
        System.out.println("[3] Withdraw Funds");
        System.out.println("[4] Convert Funds");
        System.out.println("[5] Add User");
        System.out.println("[0] Exit");
        System.out.println("<==========================>");
        System.out.println("Please enter the number of the transaction you want to do: ");
        return inputScanner.nextInt();
    }

    public static int addUser(int availableUsers, String[] usersName, double[] usersFund, String[] usersCurrency, String[] availableCurrencies, Scanner inputScanner){
        String name;
        System.out.println("[Add User]");
        System.out.println("Please enter your name: ");
        
        if (availableUsers > 0) {
            inputScanner.nextLine();
        }
        
        name = inputScanner.nextLine();

        usersName[availableUsers] = name;

        // commented for now, used to skip initiliazing when testing
        // usersName[0] = "John Doe";

        usersFund[availableUsers] = 10000.00;
        usersCurrency[availableUsers] = "PHP";
        
        availableUsers++;

        return availableUsers;
    }
}
