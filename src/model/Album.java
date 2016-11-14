package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Album implements Serializable {
    private String albumName;
    private ArrayList<Photo> photos;
    private int size = 0; 
    private Photo earliestPhoto;
    private Photo oldestPhoto;
    private double dateRange;

    public Album() {
	photos = new ArrayList<Photo>();
    }

    public Album(String albumName) {
	photos = new ArrayList<Photo>();
	this.albumName = albumName;
    }

    public String getAlbumName() {
	return albumName;
    }

    public void setAlbumName(String albumName) {
	this.albumName = albumName;
    }

    public ArrayList<Photo> getPhotos() {
	return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
	this.photos = photos;
    }

    public void addPhoto(Photo photo) {
	this.photos.add(photo);
	size++;
	sortByDate();
	this.setEarliestPhoto(this.photos.get(0));
	this.setOldestPhoto(this.photos.get(this.size - 1));
    }

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

    public void removePhoto(Photo photo) {
	photos.remove(photo);
	size--;
    }

    public boolean photoExists(Photo photo) {
	return photos.contains(photo);
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public Photo getEarliestPhoto() {
	return earliestPhoto;
    }

    public void setEarliestPhoto(Photo earliestPhoto) {
	if(earliestPhoto != null) {
	    this.earliestPhoto = earliestPhoto;	    
	}
    }

    public Photo getOldestPhoto() {
	return oldestPhoto;
    }

    public void setOldestPhoto(Photo oldestPhoto) {
	if(oldestPhoto != null) {
	    this.oldestPhoto = oldestPhoto;	    
	}
    }

    public String getDateRange() {
        if(this.size > 0) {
            return this.getOldestPhoto() + " - " + this.getEarliestPhoto(); 
        }
        
        return "Empty Album.";
    }

    public void setDateRange(double dateRange) {
        this.dateRange = dateRange;
    }

}
