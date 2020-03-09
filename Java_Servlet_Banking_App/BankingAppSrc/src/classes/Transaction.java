package classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class Transaction implements Serializable {
  private static final long serialVersionUID = -4897361289054875203L;

  TransactionType Type;

  float Amount;

  float ResultingBalance;

  BankAccount Parent;

  public Transaction(TransactionType Type, float Amount, BankAccount Parent) {
    this.Type = Type;
    this.Amount = Amount;
    this.ResultingBalance = Parent.getBalance();
    this.Parent = Parent;
    try {
      log();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void log() throws IOException {
    File LogFile = this.Parent.getOwner().getLogFile();
    Writer out = new BufferedWriter(new FileWriter(LogFile, true));
    out.append(toString());
    out.append(System.lineSeparator());
    out.close();
  }

  public TransactionType getType() {
    return this.Type;
  }

  public void setType(TransactionType type) {
    this.Type = type;
  }

  public float getAmount() {
    return this.Amount;
  }

  public void setAmount(float amount) {
    this.Amount = amount;
  }

  public float getResultingBalance() {
    return this.ResultingBalance;
  }

  public void setResultingBalance(float resultingBalance) {
    this.ResultingBalance = resultingBalance;
  }

  public String toString() {
    return String.valueOf(this.Type.toString()) + "," + Float.toString(this.Amount) + "," + Float.toString(this.ResultingBalance);
  }
}
