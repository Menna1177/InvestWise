import java.io.*;
import java.util.*;

public class UserManager {
    public static final String filename = "users.ser";



    private List<UltraSimpleUserStorage.User> loadUsers()throws IOException{
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))){
            return (List<UltraSimpleUserStorage.User>) ois.readObject();
        } catch(FileNotFoundException e){
            return new ArrayList<>(); //empty list
        } catch(ClassNotFoundException e){
            throw new IOException("File not found");
        }
    }

    private void saveUsers(List<UltraSimpleUserStorage.User> users) throws IOException{
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(users);
        }
    }

    public void registerUser(String name, String email, String username, String pass) throws IOException{
      List<UltraSimpleUserStorage.User> users = loadUsers();
      UltraSimpleUserStorage.User newUser = new UltraSimpleUserStorage.User();
      newUser.register(name, email, username, pass);
      users.add(newUser);
      saveUsers(users);
    }

    public boolean loginUser(String username, String pass) throws IOException{
        List<UltraSimpleUserStorage.User> users = loadUsers();
        return users.stream().filter(Objects::nonNull).anyMatch(user ->{
            try{
                return user.authenticate(username, pass);
            } catch(Exception e){
                System.err.println("Authentication error for user: " + username);
                return false;
            }
        });
    }
}
