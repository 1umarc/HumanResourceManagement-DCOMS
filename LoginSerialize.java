package application;

import header.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LoginSerialize implements Serializable 
{
    // Recommended to declare explicitly as class would need to identically match
    private static final long serialVersionUID = 1; 

    private String username;
    private String password;

    public LoginSerialize(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Save State through Serialization
    public static void saveBySerialize(String username, String password) {
        try 
        {
            FileOutputStream fileOut = new FileOutputStream(file.Login);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            LoginSerialize loginState = new LoginSerialize(username, password);
            out.writeObject(loginState);

            out.close();
            fileOut.close();

            System.out.println("Login state saved.");
        } 
        catch (IOException e) 
        {
            System.out.println("Error" + e.getMessage());
        }
    }

    // Load State through Deserialization
    public static LoginSerialize loadByDeserialize() 
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(file.Login);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            LoginSerialize loginState = (LoginSerialize) in.readObject(); 
            in.close();
            fileIn.close();
    
            return loginState;
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println("No saved state found.");
            return new LoginSerialize("", ""); // Return empty state if nothing found
        }
    }
}    
