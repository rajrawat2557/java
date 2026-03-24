import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=====================================");
        System.out.println("         WELCOME TO EASYPAY          ");
        System.out.println("=====================================\n");

        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.print("Enter choice: ");

        int ch = Integer.parseInt(sc.nextLine());
        int userId = -1;

        if (ch == 1) {
            System.out.print("Enter Username: ");
            String u = sc.nextLine();

            System.out.print("Enter Password: ");
            String p = sc.nextLine();

            userId = UserService.signup(u, p);
            if (userId == -1) return;
        }

        System.out.print("\nLogin Username: ");
        String u = sc.nextLine();
        System.out.print("Login Password: ");
        String p = sc.nextLine();

        userId = UserService.login(u, p);
        if (userId == -1) return;

        int walletId = WalletService.getWalletId(userId);
        System.out.println("\nYour Account Number: " + walletId);

        while (true) {
            System.out.println("\n1 Deposit\n2 Withdraw\n3 Transfer\n4 Balance\n5 History\n6 Exit");
            System.out.print("Enter your choice: ");
            int c = Integer.parseInt(sc.nextLine());

            if (c == 1) {
                System.out.print("Enter deposit amount: ");
                double a = Double.parseDouble(sc.nextLine());
                WalletService.deposit(walletId, a);

            } else if (c == 2) {
                System.out.print("Enter withdraw amount: ");
                double a = Double.parseDouble(sc.nextLine());
                WalletService.withdraw(walletId, a);

            } else if (c == 3) {
                System.out.print("Enter receiver wallet id: ");
                int to = Integer.parseInt(sc.nextLine());
                System.out.print("Enter transfer amount: ");
                double a = Double.parseDouble(sc.nextLine());
                TransactionService.transfer(walletId, to, a);

            } else if (c == 4) {
                System.out.println("Fetching balance...");
                System.out.println("Current Balance: " + WalletService.getBalance(walletId));

            } else if (c == 5) {
                System.out.println("Fetching your transaction history...");
                TransactionService.showHistory(walletId);

            } else break;
        }
    }
}