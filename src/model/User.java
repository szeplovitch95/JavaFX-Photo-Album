package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class User implements Serializable {
    private String username;
    private String password;
    private ArrayList<Album> albums;

    public User() {
	albums = new ArrayList<Album>();
    }

    /**
     * @param username
     * @param password
     */
    public User(String username, String password) {
	albums = new ArrayList<Album>();
	this.username = username;
	this.password = password;
    }

    /**
     * @return username String
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username String
     * sets the username variable to the argument passed in.
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * @return password String
     */
    public String getPassword() {
	return password;
    }

    /**
     * @param password String
     * sets the password variable to the argument passed in.
     */
    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * @return albums ArrayList<Album>
     */
    public ArrayList<Album> getAlbums() {
	return albums;
    }

    /**
     * @param albums ArrayList<Album>
     * sets the albums of the users to the album list passed in.
     */
    public void setAlbums(ArrayList<Album> albums) {
	this.albums = albums;
    }

    /**
     * @param album Album
     * adds an album to the list of albums of the user.
     */
    public void addAlbum(Album album) {
	albums.add(album);
    }

    /**
     * @param album Album
     * removes the pass in argument Album from the list of albums
     */
    public void removeAlbum(Album album) {
	albums.remove(album);
    }

    /**
     * @param album Album
     * @return true if album exists 
     * @return false if album does not exist
     */
    public boolean albumExists(Album album) {
	return albums.contains(album);
    }

    /**
     * @param name
     * @return a Album 
     * returns the album by looking for the right name.
     */
    public Album getAlbumByName(String name) {
	for (Album a : albums) {
	    if (name.equals(a.getAlbumName())) {
		return a;
	    }
	}
	return null;
    }

}
