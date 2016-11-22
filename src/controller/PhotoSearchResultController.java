package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Main.AlertBox;
import controller.PhotosController.CustomListViewCell;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.PhotoAlbumUsers;
import model.Tag;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class PhotoSearchResultController implements Initializable {
    @FXML
    Button backToMainBtn;
    @FXML
    Button createNewAlbumBtn;
    @FXML
    ListView<Photo> photoResultLV;
    @FXML
    TextField albumName;

    private ObservableList<Photo> obsList;
    private PhotoAlbumUsers userList;
    private Stage stage;
    private Album currentAlbumChosen;
    private User currentUser;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Photo> subsetPhotos = new ArrayList<Photo>();
    private List<Photo> allPhotos = new ArrayList<Photo>();
    private List<Tag> searchedTagsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     *             initializes the data necessary for the controller and scene
     *             to work properly.
     */
    public void initData() throws ClassNotFoundException, IOException {
	allPhotos = getAllUserPhotos();
	subsetPhotos = getSearchedPhotoList();

	obsList = FXCollections.observableArrayList(subsetPhotos);

	photoResultLV.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {

	    @Override
	    public ListCell<Photo> call(ListView<Photo> param) {
		return new CustomListViewCell();
	    }
	});

	photoResultLV.setItems(obsList);
	
	stage.setOnCloseRequest(e -> {
	    try {
		saveData();
	    } catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	});
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * @throws IOException
     *             method that creates the new album based on the search results
     *             and checks a few cases in which an album cannot be created.
     */
    @FXML
    protected void createNewAlbumBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	if (albumName.getText().trim().isEmpty()) {
	    AlertBox.display("No album name was provided", "Please provide an album name before creating a new album");
	    return;
	}

	if (obsList.size() == 0) {
	    AlertBox.display("Cannot create an empty album",
		    "Search result is empty so a new album cannot be created at the moment.");
	    return;
	}

	Album newAlbum = new Album(albumName.getText().trim());

	// Checks if the albums name already exists.
	if (currentUser.getAlbumByName(newAlbum.getAlbumName()) != null) {
	    AlertBox.display("Error: Duplicate Album Name", "Please pick a different name for you album.");
	    return;
	}

	newAlbum.setPhotos(createNewPhotoList());
	newAlbum.setSize(subsetPhotos.size());
	currentUser.addAlbum(newAlbum);
	saveData();
	backToAlbumPage(null);
    }

    /**
     * @return newList List<Photo> method to create the new photo list which
     *         will be used for the search results.
     */
    private List<Photo> createNewPhotoList() {
	List<Photo> newList = new ArrayList<Photo>();

	for (Photo p : subsetPhotos) {
	    File newPhotoPath = p.getImage();
	    Photo p1 = new Photo(newPhotoPath);
	    p1.setCaption(p.getCaption());
	    p1.setPhotoDateAndTime(p.getPhotoDateAndTime());
	    p1.setTags(p.getTags());
	    newList.add(p1);
	}

	return newList;
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * method that changes the scene/stage and goes back to the main page of the application.
     */
    @FXML
    protected void backToMainBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminMainPage.fxml"));
	Parent root = (Parent) loader.load();
	AlbumsController controller = loader.<AlbumsController>getController();
	Scene homeScene = new Scene(root);
	controller.setListOfUsers(userList);
	controller.setUser(currentUser);
	controller.setStage(stage);
	controller.initData();
	
	stage.setScene(homeScene);
	stage.show();
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     *	method that changes the scene and goes back to the album page of the current pictures.
     */
    @FXML
    protected void backToAlbumPage(ActionEvent event) throws IOException, ClassNotFoundException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminMainPage.fxml"));
	Parent root = (Parent) loader.load();
	AlbumsController controller = loader.<AlbumsController>getController();
	Scene homeScene = new Scene(root);
	controller.setListOfUsers(userList);
	controller.setUser(currentUser);
	controller.setStage(stage);
	controller.initData();
	
	stage.setScene(homeScene);
	stage.show();
    }

    /**
     * @return searchedPhotos List<Photo>
     * iterates thru the all of the photos in the users albums and gets the searched ones.
     */
    
    private List<Photo> getSearchedPhotoList() {
	if (searchedTagsList.size() == 0 && startDate == null && endDate == null) {
	    return allPhotos;
	}

	List<Photo> searchedPhotos = new ArrayList<Photo>();

	for (Photo p : allPhotos) {
	    if (isContainedInDateRange(startDate, endDate, p)) {
		if (p.getTags().size() == 0) {
		    searchedPhotos.add(p);
		}
		for (Tag t : p.getTags()) {
		    for (Tag t2 : searchedTagsList) {
			if (t.equals(t2)) {
			    searchedPhotos.add(p);
			}
		    }
		}
	    }
	}

	return searchedPhotos;
    }

    /**
     * @param startDate LocalDate
     * @param endDate LocalDate
     * @param p Photo 
     * @return false/true depending if the date is contained within the specified date range.
     */
    private boolean isContainedInDateRange(LocalDate startDate, LocalDate endDate, Photo p) {
	if (startDate == null && endDate == null) {
	    return true;
	}

	LocalDate date = p.getPhotoDateAndTime().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	if (startDate == null) {
	    if (date.isBefore(endDate) || date.isEqual(endDate)) {
		return true;
	    }
	}

	if (endDate == null) {
	    if (date.isAfter(startDate) || date.isEqual(startDate)) {
		return true;
	    }
	}

	if (date.isAfter(startDate) && date.isBefore(endDate)) {
	    return true;
	}

	if (date.isEqual(startDate) || date.isEqual(endDate)) {
	    return true;
	}

	return false;
    }

    public class CustomListViewCell extends ListCell<Photo> {
	AnchorPane anchorPane = new AnchorPane();
	StackPane stackPane = new StackPane();
	ImageView imageView = new ImageView();
	Label captionL = new Label();

	public CustomListViewCell() {
	    super();
	    imageView.setFitWidth(100.0);
	    imageView.setFitHeight(115.0);
	    imageView.setPreserveRatio(true);
	    StackPane.setAlignment(imageView, Pos.CENTER);
	    stackPane.getChildren().add(imageView);
	    stackPane.setPrefHeight(100.0);
	    stackPane.setPrefWidth(45.0);
	    AnchorPane.setLeftAnchor(stackPane, 0.0);
	    AnchorPane.setLeftAnchor(captionL, 130.0);
	    AnchorPane.setTopAnchor(captionL, 0.0);
	    anchorPane.getChildren().addAll(stackPane, captionL);
	    anchorPane.setPrefHeight(115.0);
	    captionL.setMaxWidth(300.0);
	    setGraphic(anchorPane);
	}

	@Override
	public void updateItem(Photo photo, boolean empty) {
	    BufferedImage bufferedImage = null;
	    super.updateItem(photo, empty);
	    setText(null);

	    if (photo == null) {
		imageView.setImage(null);
		captionL.setText("");
	    }

	    if (photo != null) {

		try {
		    bufferedImage = ImageIO.read(photo.getImage());
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

		Image image = SwingFXUtils.toFXImage(bufferedImage, null);
		imageView.setImage(image);
		captionL.setText("Caption: " + photo.getCaption());
	    }
	}
    }

    /**
     * @return userList PhotoAlbumUsers 
     */
    public PhotoAlbumUsers getUserList() {
	return userList;
    }

    /**
     * @param userList PhotoAlbumUsers
     * sets the current userList to the param passed into this function.
     */
    public void setUserList(PhotoAlbumUsers userList) {
	this.userList = userList;
    }

    /**
     * @return stage Stage
     */
    public Stage getStage() {
	return stage;
    }

    /**
     * @throws ClassNotFoundException
     * saves the data by serializing it and writing it to the .dat file of the application.
     */
    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param stage
     * sets the current stage to the param passed into this function.
     */
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    /**
     * @return currentAlbumChosen Album.
     */
    public Album getCurrentAlbumChosen() {
	return currentAlbumChosen;
    }

    /**
     * @param currentAlbumChosen
     * sets the currentAlbumChosen to the param passed into this function.
     */
    public void setCurrentAlbumChosen(Album currentAlbumChosen) {
	this.currentAlbumChosen = currentAlbumChosen;
    }

    /**
     * @return List<photo> userPhotoList 
     * returns all of the photos of the current user.
     */
    private List<Photo> getAllUserPhotos() {
	List<Photo> userPhotoList = new ArrayList<Photo>();

	for (Album a : currentUser.getAlbums()) {
	    for (Photo p : a.getPhotos()) {
		userPhotoList.add(p);
	    }
	}

	return userPhotoList;
    }

    /**
     * @return currentUser User
     */
    public User getCurrentUser() {
	return currentUser;
    }

    /**
     * @param currentUser
     * sets the curent user to the param.
     */
    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }

    /**
     * @return searchedTagsList List<Tag>
     */
    public List<Tag> getSearchedTagsList() {
	return searchedTagsList;
    }

    /**
     * @param searchedTagsList
     * sets the searchedTagsList to the param.
     */
    public void setSearchedTagsList(List<Tag> searchedTagsList) {
	this.searchedTagsList = searchedTagsList;
    }

    /**
     * @return startDate LocalDate
     */
    public LocalDate getStartDate() {
	return startDate;
    }

    /**
     * @param startDatePicker
     * sets the start date of the search result parameter to the param passed into this function.
     */
    public void setStartDate(LocalDate startDatePicker) {
	this.startDate = startDatePicker;
    }

    /**
     * @return endDate LocalDate
     */
    public LocalDate getEndDate() {
	return endDate;
    }

    /**
     * @param endDate LocalDate
     * returns the endDate of the search result parameter to the param passed into this function.
     */
    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }
}