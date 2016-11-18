package controller;

import java.awt.image.BufferedImage;
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
	
	BooleanBinding bb = new BooleanBinding() {
	    {
		super.bind(photoResultLV.getProperties(), albumName.textProperty());
	    }

	    @Override
	    protected boolean computeValue() {
		return (photoResultLV.getSelectionModel().isEmpty() || albumName.getText().trim().isEmpty());
	    }
	};

	createNewAlbumBtn.disableProperty().bind(bb);
	
    }

    @FXML
    protected void createNewAlbumBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	Album newAlbum = new Album(albumName.getText().trim());
	if(currentUser.albumExists(newAlbum)) {
	    //create an alert that says that the name is already taken.
	}
	
	newAlbum.setPhotos(subsetPhotos);
	currentUser.addAlbum(newAlbum);
	saveData();
	
	backToAlbumPage(null);
    }

    @FXML
    protected void backToMainBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumPhotosList.fxml"));
	Parent root = (Parent) loader.load();
	PhotosController controller = loader.<PhotosController>getController();
	Scene homeScene = new Scene(root);
	controller.setListOfUsers(userList);
	controller.setUser(currentUser);
	controller.setAlbum(currentAlbumChosen);
	controller.setStage(stage);
	controller.initData();
	controller.setStage(stage);
	stage.setScene(homeScene);
	stage.show();
    }
    
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
	controller.setStage(stage);
	stage.setScene(homeScene);
	stage.show();
    }

    private List<Photo> getSearchedPhotoList() {
	if (searchedTagsList == null || allPhotos == null) {
	    return null;
	}

	List<Photo> searchedPhotos = new ArrayList<Photo>();

	for (Photo p : allPhotos) {
	    if (isContainedInDateRange(startDate, endDate, p)) {
		for (Tag t : p.getTags()) {
		    for(Tag t2 : searchedTagsList) {
			if(t.equals(t2)) {
			    searchedPhotos.add(p);
			}
		    }
		}
	    }
	}

	return searchedPhotos;
    }

    private boolean isContainedInDateRange(LocalDate startDate, LocalDate endDate, Photo p) {
	if(startDate == null && endDate == null) {
	    return true;
	}

	LocalDate date = p.getPhotoDateAndTime().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	if(startDate == null) {
	    if(date.isBefore(endDate) || date.isEqual(endDate)) {
		return true;
	    }
	}
	
	if(endDate == null) {
	    if(date.isAfter(startDate) || date.isEqual(startDate)) {
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
	Button editBtn = new Button("Edit");
	Button viewBtn = new Button("View");

	public CustomListViewCell() {
	    super();
	    editBtn.setMinWidth(100);
	    viewBtn.setMinWidth(100);
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
	    AnchorPane.setRightAnchor(editBtn, 0.0);
	    AnchorPane.setRightAnchor(viewBtn, 0.0);
	    AnchorPane.setBottomAnchor(viewBtn, 0.0);
	    editBtn.setVisible(false);
	    viewBtn.setVisible(false);
	    anchorPane.getChildren().addAll(stackPane, captionL, editBtn, viewBtn);
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
		editBtn.setVisible(false);
		viewBtn.setVisible(false);
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
		viewBtn.setVisible(true);
		editBtn.setVisible(true);
		captionL.setText("Caption: " + photo.getCaption());

		editBtn.setOnAction(e -> {
		    editAction(e, photo);
		});

		viewBtn.setOnAction(e -> {
		    try {
			viewAction(e, photo);
		    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		});
	    }
	}

	@FXML
	public void editAction(ActionEvent e, Photo p) {
	    Dialog<Boolean> dialog = new Dialog<>();
	    dialog.initModality(Modality.APPLICATION_MODAL);
	    dialog.setTitle("Edit Photo");
	    dialog.setResizable(false);
	    dialog.setHeaderText("Edit Photo:");

	    Label photoCaption = new Label("Photo's Caption: ");
	    TextField photoCaptionTF = new TextField(p.getCaption());
	    Label photoTags = new Label("Photo's Tags: ");
	    TextField photoTagsTF = new TextField(p.getTags().toString());

	    Platform.runLater(() -> photoCaptionTF.requestFocus());

	    GridPane gridPane = new GridPane();
	    gridPane.add(photoCaption, 1, 1);
	    gridPane.add(photoCaptionTF, 2, 1);

	    gridPane.add(photoTags, 1, 2);
	    gridPane.add(photoTagsTF, 2, 2);
	    dialog.getDialogPane().setContent(gridPane);

	    ButtonType saveBtn = new ButtonType("Save", ButtonData.OK_DONE);
	    ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, cancelBtn);

	    Optional<Boolean> dResult = dialog.showAndWait();

	    if (dResult.isPresent()) {
		p.setCaption(photoCaptionTF.getText());
		updateItem(p, true);
		obsList.setAll(currentAlbumChosen.getPhotos());
		photoResultLV.setItems(obsList);
		try {
		    PhotoAlbumUsers.write(userList);
		} catch (IOException o) {
		    o.printStackTrace();
		}
	    }
	}

	@FXML
	public void viewAction(ActionEvent e, Photo p) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotosSlideshow.fxml"));
	    Parent root = loader.load();

	    SlideshowController controller = loader.getController();
	    controller.setUserList(userList);
	    controller.setCurrentPhoto(p);
	    controller.setCurrentAlbumChosen(currentAlbumChosen);
	    controller.setCurrentUser(currentUser);
	    controller.setStage(stage);

	    Scene slideshowScene = new Scene(root);
	    try {
		controller.initData();
	    } catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    stage.setScene(slideshowScene);

	    stage.show();

	}
    }

    public PhotoAlbumUsers getUserList() {
	return userList;
    }

    public void setUserList(PhotoAlbumUsers userList) {
	this.userList = userList;
    }

    public Stage getStage() {
	return stage;
    }

    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void setStage(Stage stage) {
	this.stage = stage;
    }

    public Album getCurrentAlbumChosen() {
	return currentAlbumChosen;
    }

    public void setCurrentAlbumChosen(Album currentAlbumChosen) {
	this.currentAlbumChosen = currentAlbumChosen;
    }

    private List<Photo> getAllUserPhotos() {
	List<Photo> userPhotoList = new ArrayList<Photo>();

	for (Album a : currentUser.getAlbums()) {
	    for (Photo p : a.getPhotos()) {
		userPhotoList.add(p);
	    }
	}

	return userPhotoList;
    }

    public User getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }

    public List<Tag> getSearchedTagsList() {
	return searchedTagsList;
    }

    public void setSearchedTagsList(List<Tag> searchedTagsList) {
	this.searchedTagsList = searchedTagsList;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public void setStartDate(LocalDate startDatePicker) {
	this.startDate = startDatePicker;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }
}