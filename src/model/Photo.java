package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
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

    /**
     * @return caption String.
     */
    public String getCaption() {
	return caption;
    }

    /**
     * @param caption String 
     * sets the caption of the Photo to the String param passed in.
     */
    public void setCaption(String caption) {
	this.caption = caption;
    }

    /**
     * @return imageFile File
     */
    public File getImage() {
	return imageFile;
    }

    /**
     * @param imageFile File 
     * sets the imageFile to the param imageFile passed in.
     */
    public void setImageFile(File imageFile) {
	this.imageFile = imageFile;
    }

    /**
     * @return date of the photo which is of type String
     * calculates and seperates the Calendar type into an array of chars to be returned.
     */
    public String getDate() {
	String[] dateArr = photoDateAndTime.getTime().toString().split("\\s+");
	return dateArr[0] + " " + dateArr[1] + " " + dateArr[2] + ", " + dateArr[3];
    }

    /**
     * @return parentAlbums List<Album>
     */
    public List<Album> getParentAlbums() {
	return parentAlbums;
    }

    /**
     * @param pAlbum Album 
     * adds an album to the list of parent albums.
     */
    public void addParentAlbum(Album pAlbum) {
	parentAlbums.add(pAlbum);
    }

    /**
     * @param pAlbum Album 
     * removes an album of the list of all parent albums.
     */
    public void removeParentAlbum(Album pAlbum) {
	parentAlbums.remove(pAlbum);
    }

    /**
     * @param pAlbum
     * @return true if the album exists 
     * @return false if the album does not exists.
     */
    public boolean parentAlbumExists(Album pAlbum) {
	return parentAlbums.contains(pAlbum);
    }

    /**
     * @return tags List<Tag>
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * @param tags List<Tag>
     * sets the tags to the tags parameter.
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * @param t Tag
     * adds a tag to the tags list
     */
    public void addTag(Tag t) {
	tags.add(t);
    }

    /**
     * @param t Tag
     * removes a tag from the tags list.
     */
    public void removeTag(Tag t) {
	tags.remove(t);
    }

    /**
     * @param t Tag
     * @return true if the tag exists in the list of tags
     * @return false if the tag does not exist in the list of tags.
     */
    public boolean tagExists(Tag t) {
	for (Tag t1 : this.tags) {
	    if (t1.equals(t)) {
		return true;
	    }
	}

	return false;
    }

    /**
     * @return photoDateAndTime Calendar
     */
    public Calendar getPhotoDateAndTime() {
	return photoDateAndTime;
    }

    /**
     * @param photoDateAndTime Calendar
     * sets the photoDateAndTime to the param passed into the method.
     */
    public void setPhotoDateAndTime(Calendar photoDateAndTime) {
	this.photoDateAndTime = photoDateAndTime;
    }
}
