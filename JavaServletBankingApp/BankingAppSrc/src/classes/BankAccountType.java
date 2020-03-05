package classes;

public enum BankAccountType {
  CHECKING {
    public String toString() {
      return "checking";
    }
  },
  SAVINGS {
    public String toString() {
      return "savings";
    }
  },
  BROKERAGE {
    public String toString() {
      return "brokerage";
    }
  };
  
  public static BankAccountType StringToAccountType(String type) {
    if (type.equals("checking") || type.equals("Checking"))
      return CHECKING; 
    if (type.equals("savings") || type.equals("Savings"))
      return SAVINGS; 
    if (type.equals("brokerage") || type.equals("Brokerage"))
      return BROKERAGE; 
    return null;
  }
}
