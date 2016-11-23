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

	public Album(String albumName) {
		photos = new ArrayList<Photo>();
		this.albumName = albumName;
	}

	/**
	 * @return
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * @param albumName
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * @return
	 */
	public List<Photo> getPhotos() {
		return photos;
	}

	/**
	 * @param photos
	 */
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
		this.size = photos.size();
		sortByDate();
		this.setEarliestPhoto(this.photos.get(0));
		this.setOldestPhoto(this.photos.get(this.size - 1));
	}

	/**
	 * @param photo
	 */
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
		size++;
		sortByDate();
		this.setEarliestPhoto(this.photos.get(0));
		this.setOldestPhoto(this.photos.get(this.size - 1));
	}

	/**
	 * 
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
	 * @param photo
	 */
	public void removePhoto(Photo photo) {
		photos.remove(photo);
		size--;
	}

	/**
	 * @param photo
	 * @return
	 */
	public boolean photoExists(Photo photo) {
		return photos.contains(photo);
	}

	/**
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return
	 */
	public Photo getEarliestPhoto() {
		return earliestPhoto;
	}

	/**
	 * @param earliestPhoto
	 */
	public void setEarliestPhoto(Photo earliestPhoto) {
		if (earliestPhoto != null) {
			this.earliestPhoto = earliestPhoto;
		}
	}

	/**
	 * @param p
	 * @return
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
	 * @param p
	 * @return
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
	 * @return
	 */
	public Photo getOldestPhoto() {
		return oldestPhoto;
	}

	/**
	 * @param oldestPhoto
	 */
	public void setOldestPhoto(Photo oldestPhoto) {
		if (oldestPhoto != null) {
			this.oldestPhoto = oldestPhoto;
		}
	}

	/**
	 * @return
	 */
	public String getDateRange() {
		if (this.size > 0) {
			return this.getOldestPhoto() + " - " + this.getEarliestPhoto();
		}

		return "Empty Album.";
	}

	/**
	 * @param dateRange
	 */
	public void setDateRange(double dateRange) {
		this.dateRange = dateRange;
	}

}
