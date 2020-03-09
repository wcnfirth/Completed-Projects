package classes;

public enum TransactionType {
  DEPOSIT {
    public String toString() {
      return "deposit";
    }
  },
  WITHDRAWAL {
    public String toString() {
      return "withdrawal";
    }
  },
  TRANSFER {
    public String toString() {
      return "transfer";
    }
  };

  public static TransactionType StringToTransactionType(String type) {
    if (type.equals("deposit") || type.equals("Deposit"))
      return DEPOSIT;
    if (type.equals("withdrawal") || type.equals("Withdrawal"))
      return WITHDRAWAL;
    if (type.equals("transfer") || type.equals("Transfer"))
      return TRANSFER;
    return null;
  }
}
