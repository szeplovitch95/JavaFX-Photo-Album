package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Photo implements Serializable {
    private String caption;
    private File imageFile;
    private List<Album> parentAlbums;
    private List<Tag> tags;
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

    public String getDate() {
	String[] dateArr = photoDateAndTime.getTime().toString().split("\\s+");
	return dateArr[0] + " " + dateArr[1] + " " + dateArr[2] + ", " + dateArr[3];
    }

    public List<Album> getParentAlbums() {
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

    public List<Tag> getTags() {
	return tags;
    }

    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    public void addTag(Tag t) {
	tags.add(t);
    }

    public void removeTag(Tag t) {
	tags.remove(t);
    }

    public boolean tagExists(Tag t) {
	for (Tag t1 : this.tags) {
	    if (t1.equals(t)) {
		return true;
	    }
	}

	return false;
    }

    public Calendar getPhotoDateAndTime() {
	return photoDateAndTime;
    }

    public void setPhotoDateAndTime(Calendar photoDateAndTime) {
	this.photoDateAndTime = photoDateAndTime;
    }
}
