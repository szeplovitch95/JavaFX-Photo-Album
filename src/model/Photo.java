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
     * @return
     */
    public String getCaption() {
	return caption;
    }

    /**
     * @param caption
     */
    public void setCaption(String caption) {
	this.caption = caption;
    }

    /**
     * @return
     */
    public File getImage() {
	return imageFile;
    }

    /**
     * @param imageFile
     */
    public void setImageFile(File imageFile) {
	this.imageFile = imageFile;
    }

    /**
     * @return
     */
    public String getDate() {
	String[] dateArr = photoDateAndTime.getTime().toString().split("\\s+");
	return dateArr[0] + " " + dateArr[1] + " " + dateArr[2] + ", " + dateArr[3];
    }

    /**
     * @return
     */
    public List<Album> getParentAlbums() {
	return parentAlbums;
    }

    /**
     * @param pAlbum
     */
    public void addParentAlbum(Album pAlbum) {
	parentAlbums.add(pAlbum);
    }

    /**
     * @param pAlbum
     */
    public void removeParentAlbum(Album pAlbum) {
	parentAlbums.remove(pAlbum);
    }

    /**
     * @param pAlbum
     * @return
     */
    public boolean parentAlbumExists(Album pAlbum) {
	return parentAlbums.contains(pAlbum);
    }

    /**
     * @return
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * @param tags
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * @param t
     */
    public void addTag(Tag t) {
	tags.add(t);
    }

    /**
     * @param t
     */
    public void removeTag(Tag t) {
	tags.remove(t);
    }

    /**
     * @param t
     * @return
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
     * @return
     */
    public Calendar getPhotoDateAndTime() {
	return photoDateAndTime;
    }

    /**
     * @param photoDateAndTime
     */
    public void setPhotoDateAndTime(Calendar photoDateAndTime) {
	this.photoDateAndTime = photoDateAndTime;
    }
}
