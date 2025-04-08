package presentation;

import business.model.Account;
import business.service.account.AccountService;
import business.service.account.AccountServiceImp;

import java.util.List;
import java.util.Scanner;

public class AccountUI {

    public static void displayAccountMenu(Scanner scanner) {
        AccountService accountService = new AccountServiceImp();
        do {
            System.out.println("*************** ACCOUNT MENU **************");
            System.out.println("1. Danh sách tài khoản");
            System.out.println("2. Tạo tài khoản");
            System.out.println("3. Cập nhật tài khoản"); // Tên + trạng thái
            System.out.println("4. Xóa tài khoản"); // Cập nhật trạng thái là inactive
            System.out.println("5. Chuyển khoản");
            System.out.println("6. Tra cứu số dư tài khoản");
            System.out.println("7. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    displayAllAccounts(accountService);
                    break;
                case 2:
                    createNewAccount(scanner, accountService);
                    break;
                case 3:
                    updateAccount(scanner, accountService);
                    break;
                case 4:
                    deleteAccount(scanner, accountService);
                    break;
                case 5:
                    fundsTransfer(scanner, accountService);
                    break;
                case 6:
                    checkAccountBalance(scanner, accountService);
                    break;
                case 7:
                    return;
                default:
                    System.err.println("Vui lòng chọn từ 1-7");
            }
        } while (true);
    }

    public static void displayAllAccounts(AccountService accountService) {
        List<Account> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("Không có tài khoản nào.");
        } else {
            for (Account acc : accounts) {
                System.out.println(acc);
            }
        }
    }

    public static void createNewAccount(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập tên tài khoản:");
        String name = scanner.nextLine();
        System.out.println("Nhập số dư ban đầu:");
        double balance = Double.parseDouble(scanner.nextLine());
        Account newAccount = new Account(name, balance, true);
        accountService.createAccount(newAccount);
        System.out.println("Tạo tài khoản thành công.");
    }

    public static void updateAccount(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập ID tài khoản cần cập nhật:");
        int updateId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên mới:");
        String newName = scanner.nextLine();
        System.out.println("Trạng thái (true: active, false: inactive):");
        boolean newStatus = Boolean.parseBoolean(scanner.nextLine());
        Account updateAccount = new Account(newName, updateId, newStatus);
        accountService.updateAccount(updateAccount);
        System.out.println("Cập nhật thành công.");
    }

    public static void deleteAccount(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập ID tài khoản cần xóa:");
        int deleteId = Integer.parseInt(scanner.nextLine());
        accountService.deleteAccount(deleteId);
        System.out.println("Đã chuyển trạng thái tài khoản sang inactive.");
    }

    public static void fundsTransfer(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập số tài khoản người gửi:");
        int accSenderId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên tài khoản người gửi:");
        String accSenderName = scanner.nextLine();
        System.out.println("Nhập số tài khoản người nhận:");
        int accReceiverId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên tài khoản người nhận:");
        String accReceiverName = scanner.nextLine();
        System.out.println("Nhập số tiền chuyển:");
        double amount = Double.parseDouble(scanner.nextLine());
        int result = accountService.fundsTransfer(accSenderId, accSenderName, accReceiverId, accReceiverName, amount);
        switch (result) {
            case 1:
                System.err.println("Thông tin tài khoản người gửi không chính xác");
                break;
            case 2:
                System.err.println("Thông tin tài khoản người nhận không chính xác");
                break;
            case 3:
                System.err.println("Số dư tài khoản không đủ để chuyển khoản");
                break;
            case 4:
                System.out.println("Chuyển khoản thành công!!!");
                break;
            default:
                System.err.println("Chuyển khoản thất bại do lỗi không xác định.");
        }
    }

    public static void checkAccountBalance(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập ID tài khoản cần tra cứu:");
        int accId = Integer.parseInt(scanner.nextLine());
        double accBalance = accountService.getBalanceById(accId);
        System.out.println("Số dư hiện tại: " + accBalance);
    }
}
