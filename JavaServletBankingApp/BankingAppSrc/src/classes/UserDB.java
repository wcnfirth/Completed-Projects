package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDB implements Serializable {
  private static final long serialVersionUID = 591095757241248967L;
  
  private Map<String, User> Users;
  
  public UserDB(HashMap<String, User> Users) {
    this.Users = Users;
  }
  
  public UserDB() {
    this.Users = new HashMap<>();
  }
  
  public Map<String, User> getUserDB() {
    return this.Users;
  }
  
  public void addNewUser(User newUser) {
    this.Users.put(newUser.getUserName(), newUser);
  }
  
  public User getUser(String userName) {
    return this.Users.get(userName);
  }
  
  public void removeUser(String userName) {
    this.Users.remove(userName);
  }
  
  public List<String> getAllUserNames() {
    List<String> userNames = new ArrayList<>(this.Users.keySet());
    return userNames;
  }
  
  public boolean UserNameExists(String accName) {
    return this.Users.containsKey(accName);
  }
  
  public void BackupUserDatabase(String contextPath) throws IOException {
    String UserDatabasePath = getFullPath(contextPath);
    try {
      FileOutputStream fileOut = new FileOutputStream(UserDatabasePath);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(this.Users);
      out.close();
      fileOut.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static UserDB RestoreUserDatabase(String contextPath) {
    File file = new File(getFullPath(contextPath));
    if (file.length() == 0L)
      return new UserDB(); 
    try {
      FileInputStream fileIn = new FileInputStream(file);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      UserDB db = new UserDB((HashMap<String, User>)in.readObject());
      in.close();
      fileIn.close();
      return db;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static String getFullPath(String contextPath) {
    String tmpPath = contextPath.split(".metadata", 2)[0];
    return String.valueOf(tmpPath) + "firth008" + File.separator + "src" + File.separator + "resources" + 
      File.separator + "UserDatabase.txt";
  }
  
  public String toString() {
    String response = "UserDB [\n";
    Collection<User> UserCollec = this.Users.values();
    List<User> UserList = new ArrayList<>(UserCollec);
    int idx = 0;
    while (idx < UserList.size()) {
      response = String.valueOf(response) + ((User)UserList.get(idx)).toString();
      if (idx < UserList.size() - 1)
        response = String.valueOf(response) + ", \n"; 
      idx++;
    } 
    response = String.valueOf(response) + "\n]";
    return response;
  }
}
