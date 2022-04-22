import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class BankApp{

    public static final String NAME_USER = "root";
    public static final String PASSWORD = "iridos09";
    public static final String URL = "jdbc:mysql://localhost:3306/mysql";
    public static Statement statement;
    public static Connection connection;
    public static ResultSet resultSet;

    static{
        try{
            connection = DriverManager.getConnection(URL, NAME_USER, PASSWORD);
        } catch (SQLException throwables){
            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    static{
        try{
            statement = connection.createStatement();
        } catch (SQLException throwables){
            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException,SQLException{

        Class.forName("com.mysql.cj.jdbc.Driver");

        int choose = 0;
        String bank_account = null, status;
        BankApp account = new BankApp();
        boolean exit = true;

        Scanner in = new Scanner(System.in);

        while(exit) {

            System.out.println("Hello! What do you to do with bank account?");
            System.out.println("     Input one of the next number:");
            System.out.println("        1. Add new bank account.");
            System.out.println("        2. Top up balance/Remove from balance.");
            System.out.println("        3. Print all bank accounts.");
            System.out.println("        4. Activate/deactivate bank account.");
            System.out.println("        5. Check status.");
            System.out.println("        6. Print all transactions.");
            System.out.println("        0. Exit.");

            choose = in.nextInt();

            if(choose>=0 && choose<=6) {
                switch(choose) {

                    case 0: exit = false; break;

                    case 1: account.insert_values(); break;

                    case 2:
                        bank_account = account.check_bank_account();
                        if (bank_account != "1") {
                            status = account.check_status(bank_account);
                            if (status.equals("ON")) {
                                account.change_balance(bank_account);
                            }else {
                                System.out.println("Sorry, but bank account deactive.");
                                System.out.println("Active it, if you want to contune.");
                                System.out.println();
                            }
                        }

                        break;

                    case 3: account.print_bank_accounts(); break;

                    case 4: bank_account = account.check_bank_account();
                        if (bank_account != "1") {
                            account.change_status(bank_account);}
                        break;

                    case 5: bank_account = account.check_bank_account();
                        if (bank_account != "1") {
                            status = account.check_status(bank_account);
                            System.out.println();
                            System.out.println("Bank account: "+ bank_account);
                            System.out.println("Status: "+ status);
                            System.out.println();}
                        break;

                    case 6: account.print_transactions(); break;

                }
            } else {
                System.out.println();
                System.out.println("Input correct number.");
                System.out.println();
            }


        }
    }



    public int add_bank_account(String new_bank_acc_number, double money, String new_status, String new_fname, String new_lname,
                                String new_pass_id, String new_password) {

        try {

            String query = "INSERT INTO BANK_ACCOUNTS (BANK_ACCNO, BANK_ACC, STATUS, "
                    + "FNAME, LNAME, PASS_ID, PASSWORD) VALUES (?,?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1,new_bank_acc_number);
            ps.setDouble(2, 0);
            ps.setString(3,new_status);
            ps.setString(4,new_fname);
            ps.setString(5,new_lname);
            ps.setString(6,new_pass_id);
            ps.setString(7,new_password);
            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
            return -1;

        }

        return 1;
    }

    public void insert_values() {

        int status_choose;
        String new_status = null, new_bank_acc_number, new_pass_id, new_lname, new_fname;

        System.out.println();

        Scanner in = new Scanner(System.in);
        // input bnk_accno


        while(true) {

            System.out.println("Input Bank account number. "
                    + "Attention! Bank account number must contains seven characters: ");
            new_bank_acc_number = in.next();

            if (new_bank_acc_number.length() != 7) {
                System.out.println();
                System.out.println("Bank account number doesn't contrain seven characters. "
                        + "Input correct data.");
                System.out.println();

            } else {
                break;
            }
        }

        System.out.println();

        // input status

        System.out.println("Do you want active our bank account?");
        System.out.println("        1. Yes       2. No");


        while(true) {

            status_choose = in.nextInt();

            if (status_choose == 1 || status_choose == 2) {
                if(status_choose == 1) {
                    new_status = "ON";
                } else {
                    new_status = "OFF";
                }
                break;

            } else {
                System.out.println();
                System.out.println("Input correct number.");
            }
        }

        System.out.println();

        // input fname

        while(true) {

            System.out.println("Input first name: ");
            new_fname = in.next();

            if (new_fname.length() > 8) {
                System.out.println();
                System.out.println("First name is too long.");
                System.out.println();

            } else {
                break;
            }
        }

        System.out.println();

        // input lname

        while(true) {

            System.out.println("Input last name: ");
            new_lname = in.next();

            if (new_lname.length() > 10) {
                System.out.println();
                System.out.println("Last name is too long.");
                System.out.println();

            } else {
                break;
            }
        }

        System.out.println();

        // input pass_id

        while(true) {

            System.out.println("Input passport idenficator.");
            System.out.println("Format: BMxxxxxx. Input numbers xxxxxx");

            new_pass_id = in.next();

            if (new_pass_id.length() != 6) {
                System.out.println();
                System.out.println("Passport identificator doesn't contrain six numbers. "
                        + "Input correct data.");
                System.out.println();

            } else {
                break;
            }
        }

        new_pass_id = "BM" + new_pass_id;

        System.out.println();


        // input password

        System.out.println("Input password: ");
        String new_password = in.next();
        System.out.println();

        if (new BankApp().add_bank_account(new_bank_acc_number, 0, new_status,
                new_fname.toUpperCase(), new_lname.toUpperCase(), new_pass_id, new_password)  == 1) {
            System.out.println("Account created!");
            System.out.println();
        }
        else {
            System.out.println();
            System.out.println("Cant create account!");
            System.out.println();
        }
    }

    public void change_balance(String bank_account) throws SQLException {

        int balance_choose = 0;
        double money;
        /*String bank_account;*/
        String bank_account_temp;
        double money_test = 0;

        Scanner in = new Scanner(System.in);

		/*System.out.println();

		System.out.println("  Input your bank account number:");
		bank_account = in.next();*/

        System.out.println();

        System.out.println("  Input one of the next number:");
        System.out.println("     1. Top up balance.");
        System.out.println("     2. Remove from balance.");

        while(true) {

            balance_choose = in.nextInt();
            System.out.println();

            if (balance_choose == 1 || balance_choose == 2) {
                break;
            }else {
                System.out.println("Input correct number.");
            }
        }

        System.out.println();

        if (balance_choose == 1) {
            try {
                System.out.println("  Input the amount you want to top up your balance?");
                money = in.nextDouble();

                String query = "UPDATE BANK_ACCOUNTS SET BANK_ACC = BANK_ACC + ?"
                        + "WHERE BANK_ACCNO = ?";

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setDouble(1, money);
                ps.setString(2, bank_account);
                ps.executeUpdate();

                System.out.println();
                System.out.println("Balance topped up.");
                BankApp account = new BankApp();
                String message = "BANK ACCOUNT REPLENISHMENT";
                account.tranc_add(bank_account, message);
                System.out.println();

            } catch (SQLException e) {

                e.printStackTrace();

            }

        } else {

            try {

                System.out.println("  Input the amount you want to withdraw from your balance?");
                money = in.nextDouble();


                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery("SELECT BANK_ACCNO, BANK_ACC FROM BANK_ACCOUNTS");

                while (resultSet1.next()) {

                    bank_account_temp = resultSet1.getString(1).replaceAll(" ","");

                    if(bank_account_temp.equals(bank_account)) {
                        money_test = resultSet1.getDouble(2);
                        break;
                    }
                }

                if (money_test - money >= 0) {

                    try {

                        String query2 = "UPDATE BANK_ACCOUNTS SET BANK_ACC = BANK_ACC - ?"
                                + "WHERE BANK_ACCNO = ?";

                        PreparedStatement ps2 = connection.prepareStatement(query2);

                        ps2.setDouble(1, money);
                        ps2.setString(2, bank_account);
                        ps2.executeUpdate();

                        System.out.println();
                        System.out.println("Money withdraw successfully.");
                        BankApp account = new BankApp();
                        String message = "WITHDRAWAL FROM A BANK ACCOUNT";
                        account.tranc_add(bank_account, message);
                        System.out.println();

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }

                } else {
                    System.out.println();
                    System.out.println("Insufficient funds.");
                    System.out.println();
                }


            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

    }



    public String print_bank_accounts() {
        String bank_acc = null;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT BANK_ACCNO, BANK_ACC, STATUS, CLINO, "
                    + "SUBSTR(FNAME,1,8), SUBSTR(LNAME,1,10), PASS_ID, PASSWORD FROM BANK_ACCOUNTS");
            System.out.println();
            System.out.println("BANK_ACCNO BANK_ACC STATUS CLINO FNAME   LNAME      PASS_ID   PASSWORD");
            System.out.println("----------------------------------------------------------------------");
            while(resultSet.next()) {
                System.out.printf("%s %7.2f  %s   %3d    %s %s %s %s%n",resultSet.getString(1),resultSet.getDouble(2),resultSet.getString(3),
                        resultSet.getInt(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8));

                bank_acc = resultSet.getString(1)+ " " + Double.toString(resultSet.getInt(2)) + " " + resultSet.getString(3)+ "\n";
            }
            System.out.println();

            System.out.println(bank_acc);
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bank_acc;
    }


    public  String check_bank_account(){

        String bank_account = null, bank_account_temp = null;
        Scanner in = new Scanner (System.in);
        boolean exit = true;
        int choose = 0;

        while(exit){
            try {
                System.out.println();
                System.out.println("Input bank account number");
                bank_account = in.next();

                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery("SELECT BANK_ACCNO, BANK_ACC FROM BANK_ACCOUNTS");

                while (resultSet1.next()) {

                    bank_account_temp = resultSet1.getString(1).replaceAll(" ","");

                    if(bank_account_temp.equals(bank_account)) {
                        break;
                    }
                }

                if (bank_account_temp.equals(bank_account)){
                    exit = false;
                } else {
                    System.out.println();
                    System.out.println("Inputed bank account number doesn't exists.");
                    System.out.println("Do you want to input bank accout again?");
                    System.out.println("     1.  Yes              2. No ");

                    while(true) {

                        choose = in.nextInt();
                        System.out.println();

                        if (choose == 1 || choose == 2) {
                            if(choose == 1) {
                            } else {
                                bank_account = "1";
                                exit = false;
                            }
                            break;

                        } else {
                            System.out.println("Input correct number.");
                        }
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bank_account;
    }


    public void change_status(String bank_account) {

        Scanner in = new Scanner(System.in);
        int status_choose = 0;

        System.out.println();

        System.out.println("  Input one of the next number:");
        System.out.println("     1. Active bank account.");
        System.out.println("     2. Deactive bank account.");


        while(true) {

            status_choose = in.nextInt();
            System.out.println();

            if (status_choose == 1 || status_choose == 2) {
                break;
            }else {
                System.out.println("Input correct number.");
            }
        }



        if (status_choose == 1) {
            try {
                String query = "UPDATE BANK_ACCOUNTS SET STATUS = 'ON' "
                        + "WHERE BANK_ACCNO = ?";

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, bank_account);
                ps.executeUpdate();

                System.out.println("Bank account activated.");
                System.out.println();

            } catch (SQLException e) {

                e.printStackTrace();

            }

        } else {

            try {
                String query = "UPDATE BANK_ACCOUNTS SET STATUS = 'OFF' "
                        + "WHERE BANK_ACCNO = ?";

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, bank_account);
                ps.executeUpdate();

                System.out.println("Bank account deactivated.");
                System.out.println();

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }
    }


    public  String check_status(String bank_account){

        String status = null, bank_account_temp = null;
        Scanner in = new Scanner (System.in);
        int choose = 0;

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery("SELECT BANK_ACCNO, STATUS FROM BANK_ACCOUNTS");

            while (resultSet1.next()) {

                bank_account_temp = resultSet1.getString(1).replaceAll(" ","");

                if(bank_account_temp.equals(bank_account)) {
                    status = resultSet1.getString(2).replaceAll(" ","");
                    break;
                }
            }

            if (status.equals("OFF")) {
                System.out.println();
                System.out.println("Bank account deactive.");
                System.out.println("   Do you want it active?.");
                System.out.println("     1. Yes       2. No");

                while(true) {

                    choose = in.nextInt();
                    System.out.println();

                    if (choose == 1 || choose == 2) {

                        if (choose == 1) {
                            BankApp account_temp = new BankApp();
                            account_temp.change_status(bank_account);
                            status = "ON";
                        }
                        break;
                    }else {
                        System.out.println("Input correct number.");
                    }
                }

            }


        } catch (SQLException e) {

            e.printStackTrace();

        }

        return status;
    }


    public void tranc_add(String bank_account, String message) {

        try {

            String query = "INSERT INTO TRANSACTION (BANK_ACCNO, TRANC_NAME, TRANC_DATE, "
                    + "TRANC_TIME) VALUES (?,?, CURRENT_DATE(), CURRENT_TIME())";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1,bank_account);
            ps.setString(2,message);
            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public void print_transactions() {
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM  TRANSACTION");
            System.out.println();
            System.out.println("TRANC_NO  BANK_ACCNO TRANSACTION                     DATE       TIME");
            System.out.println("---------------------------------------------------------------------");
            while(resultSet.next()) {
                System.out.printf("%5d",resultSet.getInt(1));
                System.out.println("     "+resultSet.getString(2) + " " + resultSet.getString(3) + "  " +
                        resultSet.getDate(4) + " " + resultSet.getTime(5));
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

