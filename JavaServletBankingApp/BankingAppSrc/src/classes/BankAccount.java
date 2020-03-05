package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class BankAccount implements Serializable {
  private static final long serialVersionUID = 7474480861156589307L;
  
  private String Name;
  
  private BankAccountType Type;
  
  private float Balance;
  
  private ArrayList<Integer> TransactionKeys;
  
  private User Owner;
  
  public BankAccount(String Name, BankAccountType Type, float Balance, User Owner) {
    this.Name = Name;
    this.Type = Type;
    this.Balance = Balance;
    this.TransactionKeys = new ArrayList<>();
    this.Owner = Owner;
    if (Balance > 0.0F)
      addNewTransaction(new Transaction(TransactionType.DEPOSIT, Balance, this)); 
  }
  
  public boolean MakeTransaction(float Amount, TransactionType transactionType) {
    boolean SuccessfulTransaction = false;
    switch (transactionType) {
      case null:
        SuccessfulTransaction = Deposit(Amount);
        break;
      case WITHDRAWAL:
        SuccessfulTransaction = Withdraw(Amount);
        break;
      case TRANSFER:
        SuccessfulTransaction = (Amount >= 0.0F) ? Deposit(Amount) : Withdraw(-1.0F * Amount);
        break;
    } 
    if (!SuccessfulTransaction)
      return false; 
    addNewTransaction(new Transaction(transactionType, Amount, this));
    return true;
  }
  
  public boolean Deposit(float DepositAmount) {
    addToBalance(DepositAmount);
    this.Owner.addToTotalBalance(DepositAmount);
    return true;
  }
  
  public boolean Withdraw(float WithdrawalAmount) {
    if (WithdrawalAmount > this.Balance)
      return false; 
    addToBalance(-1.0F * WithdrawalAmount);
    this.Owner.addToTotalBalance(-1.0F * WithdrawalAmount);
    return true;
  }
  
  public boolean isEmpty() {
    return (this.Balance <= 0.0F);
  }
  
  public void addToBalance(float BalanceChange) {
    this.Balance += BalanceChange;
  }
  
  public void addNewTransaction(Transaction newTransaction) {
    int TransactionIdx = this.Owner.StoreTransaction(newTransaction);
    this.TransactionKeys.add(Integer.valueOf(TransactionIdx));
  }
  
  public String toString() {
    return "Account [Name=" + this.Name + ", Type=" + this.Type.toString() + ", CurrentBalance=" + this.Balance + "]";
  }
  
  public ArrayList<Transaction> getAllTransactions() {
    ArrayList<Transaction> transactions = new ArrayList<>();
    for (int i = this.TransactionKeys.size() - 1; i >= 0; i--)
      transactions.add(this.Owner.getTransactionByIdx(((Integer)this.TransactionKeys.get(i)).intValue())); 
    return transactions;
  }
  
  public int getNumTransactions() {
    return this.TransactionKeys.size();
  }
  
  public float getBalance() {
    return this.Balance;
  }
  
  public User getOwner() {
    return this.Owner;
  }
  
  public String getName() {
    return this.Name;
  }
  
  public void setBalance(float balance) {
    this.Balance = balance;
  }
  
  public void setName(String name) {
    this.Name = name;
  }
  
  public void setType(BankAccountType type) {
    this.Type = type;
  }
}
