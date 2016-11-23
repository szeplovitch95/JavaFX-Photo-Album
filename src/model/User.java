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

	public User(String username, String password) {
		albums = new ArrayList<Album>();
		this.username = username;
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	/**
	 * @param albums
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}

	/**
	 * @param album
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}

	/**
	 * @param album
	 */
	public void removeAlbum(Album album) {
		albums.remove(album);
	}

	/**
	 * @param album
	 * @return
	 */
	public boolean albumExists(Album album) {
		return albums.contains(album);
	}

	/**
	 * @param name
	 * @return
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
