package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable {
	private String caption;
	private File imageFile;
	private ArrayList<Album> parentAlbums;
	private ArrayList<Tag> tags;
	private Calendar photoDateAndTime;
	
	public Photo(File imageFile) {
		this.imageFile = imageFile;
		parentAlbums = new ArrayList<Album>();
		tags = new ArrayList<Tag>();
		photoDateAndTime = Calendar.getInstance();
		photoDateAndTime.set(Calendar.MILLISECOND, 0);
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public File getImage() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public ArrayList<Album> getParentAlbums() {
		return parentAlbums;
	}
	
	public void addParentAlbum(Album pAlbum) {
		parentAlbums.add(pAlbum);
	}
	
	public void removeParentAlbum(Album pAlbum) {
		parentAlbums.remove(pAlbum);
	}
	
	public boolean parentAlbumExists(Album pAlbum) {
		return parentAlbums.contains(pAlbum);
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag t) {
		tags.add(t);
	}
	
	public void removeTag(Tag t) {
		tags.remove(t);
	}
	
	public boolean tagExists(Tag t) {
		return tags.contains(t);
	}
	
	public Calendar getPhotoDateAndTime() {
		return photoDateAndTime;
	}

	public void setPhotoDateAndTime(Calendar photoDateAndTime) {
		this.photoDateAndTime = photoDateAndTime;
	}
}
