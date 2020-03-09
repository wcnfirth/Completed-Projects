package classes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
  private static final long serialVersionUID = -1286432836635041164L;

  private String UserName;

  private float TotalBalance;

  private ArrayList<String> CheckingAccountKeys = new ArrayList<>();

  private ArrayList<String> SavingsAccountKeys = new ArrayList<>();

  private ArrayList<String> BrokerageAccountKeys = new ArrayList<>();

  private Map<String, BankAccount> BankAccounts = new HashMap<>();

  private int NumTotalTransactions;

  private Map<Integer, Transaction> AllTransactions = new HashMap<>();

  private String LogFilePath;

  private File LogFile;

  public User(String UserName, String contextPath) {
    this.UserName = UserName;
    this.TotalBalance = 0.0F;
    this.NumTotalTransactions = 0;
    String tmpPath = contextPath.split(".metadata", 2)[0];
    this.LogFilePath = String.valueOf(tmpPath) + "firth008" + File.separator + "src" + File.separator + "resources" +
      File.separator + "transaction-logs" + File.separator + UserName + ".txt";
    try {
      createLogFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User() {
    this.TotalBalance = 0.0F;
  }

  private void createLogFile() throws IOException {
    this.LogFile = new File(this.LogFilePath);
    this.LogFile.getParentFile().mkdirs();
    this.LogFile.createNewFile();
  }

  public void AddAccount(BankAccountType Type, String AccountName, float Balance) {
    switch (Type) {
      case CHECKING:
        this.CheckingAccountKeys.add(AccountName);
        break;
      case SAVINGS:
        this.SavingsAccountKeys.add(AccountName);
        break;
      case null:
        this.BrokerageAccountKeys.add(AccountName);
        break;
    }
    BankAccount acc = new BankAccount(AccountName, Type, Balance, this);
    this.BankAccounts.put(AccountName, acc);
    this.TotalBalance += Balance;
  }

  public boolean RemoveAccount(String AccountName) {
    BankAccountType Type;
    if (((BankAccount)this.BankAccounts.get(AccountName)).getBalance() > 0.0F)
      return false;
    if (this.CheckingAccountKeys.contains(AccountName)) {
      Type = BankAccountType.CHECKING;
    } else if (this.SavingsAccountKeys.contains(AccountName)) {
      Type = BankAccountType.SAVINGS;
    } else {
      Type = BankAccountType.BROKERAGE;
    }
    switch (Type) {
      case CHECKING:
        this.CheckingAccountKeys.remove(AccountName);
        break;
      case SAVINGS:
        this.SavingsAccountKeys.remove(AccountName);
        break;
      case null:
        this.BrokerageAccountKeys.remove(AccountName);
        break;
    }
    this.BankAccounts.remove(AccountName);
    return true;
  }

  public int MakeTransfer(String TransferFromAcctName, String TransferToAcctName, float TransferAmount) {
    if (TransferFromAcctName.equals(TransferToAcctName))
      return -1;
    BankAccount TransferFrom = getAccount(TransferFromAcctName);
    BankAccount TransferTo = getAccount(TransferToAcctName);
    if (TransferFrom.MakeTransaction(-1.0F * TransferAmount, TransactionType.TRANSFER)) {
      TransferTo.MakeTransaction(TransferAmount, TransactionType.TRANSFER);
      return 0;
    }
    return 1;
  }

  public int StoreTransaction(Transaction transaction) {
    this.AllTransactions.put(Integer.valueOf(this.NumTotalTransactions), transaction);
    incrementNumTotalTransactions();
    return this.NumTotalTransactions - 1;
  }

  public boolean AccountNameExists(String accName) {
    return this.BankAccounts.containsKey(accName);
  }

  public void addToTotalBalance(float BalanceChange) {
    this.TotalBalance += BalanceChange;
  }

  public void incrementNumTotalTransactions() {
    this.NumTotalTransactions++;
  }

  public String toString() {
    return "User [UserName=" + this.UserName + ", TotalBalance=" + this.TotalBalance + ", CheckingAccountKeys=" +
      arrayListToString(this.CheckingAccountKeys) + ", SavingsAccountKeys" + arrayListToString(this.SavingsAccountKeys) +
      ", BrokerageAccountKeys" + arrayListToString(this.BrokerageAccountKeys) + ", BankAccounts=" + bankAccountsToString() +
      "]";
  }

  public String bankAccountsToString() {
    String response = "[";
    for (String s : this.CheckingAccountKeys)
      response = String.valueOf(response) + s + "=" + ((BankAccount)this.BankAccounts.get(s)).toString() + ", ";
    for (String s : this.SavingsAccountKeys)
      response = String.valueOf(response) + s + "=" + ((BankAccount)this.BankAccounts.get(s)).toString() + ", ";
    int idx = 0;
    while (idx < this.BrokerageAccountKeys.size()) {
      String s = this.BrokerageAccountKeys.get(idx);
      response = String.valueOf(response) + s + "=" + ((BankAccount)this.BankAccounts.get(s)).toString();
      if (idx < this.BrokerageAccountKeys.size() - 1)
        response = String.valueOf(response) + ", ";
      idx++;
    }
    response = String.valueOf(response) + "]";
    return response;
  }

  public String arrayListToString(ArrayList<String> list) {
    String response = "[";
    int idx = 0;
    while (idx < list.size()) {
      response = String.valueOf(response) + (String)list.get(idx);
      if (idx < list.size() - 1)
        response = String.valueOf(response) + ", ";
      idx++;
    }
    response = String.valueOf(response) + "]";
    return response;
  }

  public File getLogFile() {
    return this.LogFile;
  }

  public int getNumTotalTransactions() {
    return this.NumTotalTransactions;
  }

  public Transaction getTransactionByIdx(int idx) {
    return this.AllTransactions.get(Integer.valueOf(idx));
  }

  public Collection<BankAccount> getAllAccounts() {
    return this.BankAccounts.values();
  }

  public ArrayList<BankAccount> getCheckingAccounts() {
    ArrayList<BankAccount> CheckingAccounts = new ArrayList<>();
    for (String Key : this.CheckingAccountKeys)
      CheckingAccounts.add(this.BankAccounts.get(Key));
    return CheckingAccounts;
  }

  public ArrayList<BankAccount> getSavingsAccounts() {
    ArrayList<BankAccount> SavingsAccounts = new ArrayList<>();
    for (String Key : this.SavingsAccountKeys)
      SavingsAccounts.add(this.BankAccounts.get(Key));
    return SavingsAccounts;
  }

  public ArrayList<BankAccount> getBrokerageAccounts() {
    ArrayList<BankAccount> BrokerageAccounts = new ArrayList<>();
    for (String Key : this.BrokerageAccountKeys)
      BrokerageAccounts.add(this.BankAccounts.get(Key));
    return BrokerageAccounts;
  }

  public BankAccount getAccount(String AccountName) {
    return this.BankAccounts.get(AccountName);
  }

  public String getUserName() {
    return this.UserName;
  }

  public void setUserName(String UserName) {
    this.UserName = UserName;
  }

  public float getTotalBalance() {
    return this.TotalBalance;
  }

  public void setTotalBalance(float TotalBalance) {
    this.TotalBalance = TotalBalance;
  }
}
