package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class Album implements Serializable {
    private String albumName;
    private List<Photo> photos;
    private int size = 0;
    private Photo earliestPhoto;
    private Photo oldestPhoto;
    private double dateRange;

    public Album() {
	photos = new ArrayList<Photo>();
    }

    /**
     * @param albumName String 
     * creates a new album with the specified name passed into the constructor.
     */
    public Album(String albumName) {
	photos = new ArrayList<Photo>();
	this.albumName = albumName;
    }

    /**
     * @return albumName String 
     */
    public String getAlbumName() {
	return albumName;
    }

    /**
     * @param albumName STRING 
     * sets the albumName to the param
     */
    public void setAlbumName(String albumName) {
	this.albumName = albumName;
    }

    /**
     * @return photos List<Photo>
     */
    public List<Photo> getPhotos() {
	return photos;
    }

    /**
     * @param photos List<Photo> 
     * sets the photos of the current albums.
     */
    public void setPhotos(List<Photo> photos) {
	this.photos = photos;
	this.size = photos.size();
	sortByDate();
	this.setEarliestPhoto(this.photos.get(0));
	this.setOldestPhoto(this.photos.get(this.size - 1));
    }

    /**
     * @param photo Photo 
     * adds a photo to the list of photos of the album
     */
    public void addPhoto(Photo photo) {
	this.photos.add(photo);
	size++;
	sortByDate();
	this.setEarliestPhoto(this.photos.get(0));
	this.setOldestPhoto(this.photos.get(this.size - 1));
    }

    /**
     * sorts the the list of photos by date.
     */
    public void sortByDate() {
	Collections.sort(this.photos, new Comparator<Photo>() {
	    public int compare(Photo p1, Photo p2) {
		if (p1.getPhotoDateAndTime() == p2.getPhotoDateAndTime()) {
		    return 0;
		}

		return p1.getPhotoDateAndTime().compareTo(p2.getPhotoDateAndTime());
	    }
	});
    }

    /**
     * @param photo Photo 
     * removes the passed in Photo argument from the list of photos. 
     */
    public void removePhoto(Photo photo) {
	photos.remove(photo);
	size--;
    }

    /**
     * @param photo Photo
     * @return boolean 
     * returns true if the photo argument exists in the list of photos 
     * returns false if the photo argument does NOT exist in the list of photos.
     */
    public boolean photoExists(Photo photo) {
	return photos.contains(photo);
    }

    /**
     * @return size Int 
     * size = size of the list of all photos.
     */
    public int getSize() {
	return size;
    }

    /**
     * @param size int 
     * sets the size of the album to the param passed in.
     */
    public void setSize(int size) {
	this.size = size;
    }

    /**
     * @return Photo 
     * returns the earliest Photo in the album. 
     */
    public Photo getEarliestPhoto() {
	return earliestPhoto;
    }

    /**
     * @param earliestPhoto Photo 
     * sets the earliestPhoto of the album to the param passed in.
     */
    public void setEarliestPhoto(Photo earliestPhoto) {
	if (earliestPhoto != null) {
	    this.earliestPhoto = earliestPhoto;
	}
    }

    /**
     * @param p Photo
     * @return Photo 
     * returns the previous photo in the list of photos. 
     * returns null if the size of the list is 1 
     * returns null if the photo argument passed in is the first photo in the list.
     */
    public Photo previousPhoto(Photo p) {
	if (p == null) {
	    return null;
	}

	if (size < 2) {
	    return p;
	}

	for (Photo photo : photos) {
	    if (p.getImage().equals(photo.getImage())) {
		if (photos.indexOf(photo) != 0) {
		    return photos.get(photos.indexOf(photo) - 1);
		}
	    }
	}

	return p;
    }

    /**
     * @param p Photo
     * @return Photo
     * returns the next photo in the list of photos. 
     * returns the photo passed in if the size of the list 1.
     * returns null if the photo argument passed in is equal to null.
     */
    public Photo nextPhoto(Photo p) {
	if (p == null) {
	    return null;
	}

	if (size < 2) {
	    return p;
	}

	for (Photo photo : photos) {
	    if (p.getImage().equals(photo.getImage())) {
		if (photos.indexOf(photo) != photos.size() - 1) {
		    return photos.get(photos.indexOf(photo) + 1);
		}
	    }
	}

	return p;
    }

    /**
     * @return oldestPhoto Photo
     */
    public Photo getOldestPhoto() {
	return oldestPhoto;
    }

    /**
     * @param oldestPhoto Photo 
     * sets the oldestPhoto in the album to the param passed in.
     */
    public void setOldestPhoto(Photo oldestPhoto) {
	if (oldestPhoto != null) {
	    this.oldestPhoto = oldestPhoto;
	}
    }

    /**
     * @return the range of the earliest and oldest dates in the album. String type is returned
     */
    public String getDateRange() {
	if (this.size > 0) {
	    return this.getOldestPhoto() + " - " + this.getEarliestPhoto();
	}

	return "Empty Album.";
    }

    /**
     * @param dateRange double 
     * sets the data range to the param passed in.
     */
    public void setDateRange(double dateRange) {
	this.dateRange = dateRange;
    }

}
