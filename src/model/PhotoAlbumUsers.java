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
	public static final String storeFile = "users1.dat";

	private ArrayList<User> users;

	/**
	 * 
	 */
	public PhotoAlbumUsers() {
		users = new ArrayList<User>();
	}

	/**
	 * @param user
	 */
	public void addUser(User user) {
		users.add(user);
	}

	/**
	 * @return
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * @param user
	 */
	public void removeUser(User user) {
		users.remove(user);
	}

	/**
	 * @param username
	 * @return
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
	 * @param username
	 * @return
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
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean userExists(String username, String password) {
		for (User u : users) {
			if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	public static PhotoAlbumUsers read() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		PhotoAlbumUsers listOfAllUsers = (PhotoAlbumUsers) ois.readObject();
		ois.close();
		return listOfAllUsers;
	}

	public static void write(PhotoAlbumUsers listOfAllUsers) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(listOfAllUsers);
		oos.close();
	}
}
