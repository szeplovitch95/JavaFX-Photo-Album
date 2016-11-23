package model;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class PhotoAlbumUsers implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6950811887381647695L;
    public static final String storeDir = "Users";
    public static final String storeFile = "allusers.dat";

    private ArrayList<User> users;

    public PhotoAlbumUsers() {
	users = new ArrayList<User>();
    }

    /**
     * @param user User
     * method that adds a new user to the Photo Album List of Users.
     */
    public void addUser(User user) {
	users.add(user);
    }

    /**
     * @return users ArrayList<User>
     * returns the list of users of the application
     */
    public ArrayList<User> getUsers() {
	return users;
    }

    /**
     * @param user User 
     * remove a user from the list of users.
     */
    public void removeUser(User user) {
	users.remove(user);
    }

    /**
     * @param username String 
     * @return User
     *	method that tries to find and return the User object that matches the username String variable 
     *	passed into the method. 
     *	returns null if not found.
     */
    public User getUserByUsername(String username) {
	for (User u : users) {
	    if (username.trim().equals(u.getUsername())) {
		return u;
	    }
	}

	return null;
    }

    /**
     * @param username String
     * @return boolean : true/false 
     * checks to see if the username is taken or not by iterating thru the user list array to see
     * if the username String variable argument matches any of them.
     */
    public boolean isUsernameTaken(String username) {
	for (User u : users) {
	    if (username.equals(u.getUsername())) {
		return true;
	    }
	}

	return false;
    }

    /**
     * @param username String
     * @param password String
     * @return boolean true or false 
     * checks to see if the user exists in the main list of all users.
     */
    public boolean userExists(String username, String password) {
	for (User u : users) {
	    if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
		return true;
	    }
	}

	return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
	String userList = "";

	if (users == null) {
	    return "No users exist.";
	}

	for (User u : users) {
	    userList += u.getUsername() + "\n";
	}
	return userList;
    }

    /**
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * reads the data from the serialized .dat file of the system.
     */
    public static PhotoAlbumUsers read() throws IOException, ClassNotFoundException {
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
	PhotoAlbumUsers listOfAllUsers = (PhotoAlbumUsers) ois.readObject();
	ois.close();
	return listOfAllUsers;
    }

    /**
     * @param listOfAllUsers PhotoAlbumUsers
     * @throws IOException
     * writes data into the .dat serialized file.
     */
    public static void write(PhotoAlbumUsers listOfAllUsers) throws IOException {
	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
	oos.writeObject(listOfAllUsers);
	oos.close();
    }
}
