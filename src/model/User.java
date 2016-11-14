package model;

import java.io.Serializable;
import java.util.ArrayList;

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

	public ArrayList<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void removeAlbum(Album album) {
		albums.remove(album);
	}
	
	public boolean albumExists(Album album) {
		return albums.contains(album);
	}
	
	public Album getAlbumByName(String name) {
		for(Album a: albums) {
			if(name.equals(a.getAlbumName())) {
				return a;
			}
		}
		return null;
	}
	
	

}
