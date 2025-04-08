package business.service.account;

import business.dao.account.AccountDao;
import business.dao.account.AccountDaoImp;
import business.model.Account;

import java.util.List;

public class AccountServiceImp implements AccountService {
    private final AccountDao accountDao;

    public AccountServiceImp() {
        accountDao = new AccountDaoImp();
    }

    @Override
    public int fundsTransfer(int accSenderId, String accSenderName, int accReceiverId, String accReceiverName, double amount) {
        return accountDao.fundsTransfer(accSenderId, accSenderName, accReceiverId, accReceiverName, amount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts(); // Sửa lỗi: gọi DAO thay vì trả về danh sách rỗng
    }

    @Override
    public boolean createAccount(Account account) {
        return accountDao.createAccount(account); // Thêm đầy đủ logic
    }

    @Override
    public boolean updateAccount(Account account) {
        return accountDao.updateAccount(account); // Thêm đầy đủ logic
    }

    @Override
    public boolean deleteAccount(int id) {
        return accountDao.deleteAccount(id); // Thêm đầy đủ logic
    }

    @Override
    public double getBalanceById(int accountId) {
        return accountDao.getBalanceById(accountId); // Sửa lỗi: gọi DAO thay vì để trống
    }

    @Override
    public Account findById(int id) {
        return accountDao.findById(id); // Thêm logic tìm tài khoản theo ID
    }
}
