package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

import javafx.application.Platform;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

public class PhotosController implements Initializable {
    @FXML
    Button homeBtn;
    @FXML
    Button searchBtn;
    @FXML
    Button addBtn;
    @FXML
    Button removeBtn;
    @FXML
    Button logoutBtn;
    @FXML
    Button moveBtn;
    @FXML
    Button copyBtn;
    @FXML
    Label nameLB;
    @FXML
    Label sizeLB;
    @FXML
    Label oldestPhotoLB;
    @FXML
    Label dateRangeLB;
    @FXML
    ListView<Photo> listViewPhotos;

    private PhotoAlbumUsers userList;
    private Stage stage;
    private Album currentAlbumChosen;
    private User currentUser;
    private ObservableList<Photo> obsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void refreshData() {
	if (currentAlbumChosen.getSize() > 0) {
	    nameLB.setText(currentAlbumChosen.getAlbumName());
	    sizeLB.setText(currentAlbumChosen.getSize() + "");
	    oldestPhotoLB.setText(currentAlbumChosen.getOldestPhoto().getDate());
	    dateRangeLB.setText(currentAlbumChosen.getEarliestPhoto().getDate() + "-"
		    + currentAlbumChosen.getOldestPhoto().getDate());
	}
    }

    public void initData() throws ClassNotFoundException, IOException {

	obsList = FXCollections.observableArrayList(currentAlbumChosen.getPhotos());

	listViewPhotos.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {

	    @Override
	    public ListCell<Photo> call(ListView<Photo> param) {
		return new CustomListViewCell();
	    }
	});

	listViewPhotos.setItems(obsList);

	refreshData();
    }

    @FXML
    protected void handleHomeBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminMainPage.fxml"));
	Parent root = (Parent) loader.load();
	AlbumsController controller = loader.<AlbumsController>getController();
	Scene homeScene = new Scene(root);
	controller.setListOfUsers(userList);
	controller.setUser(currentUser);
	controller.initData();
	controller.setStage(stage);
	stage.setScene(homeScene);
	stage.show();
    }

    @FXML
    protected void handleAddBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Choose a Photo");
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jpg or png files ONLY (*.jpg, *.png)",
		"*.jpg");
	
	//fileChooser.setInitialDirectory(new File("C:\\Users\\Dror\\Software Meth Workspace\\PhotoAlbum04\\images"));
	
	fileChooser.getExtensionFilters().add(extFilter);
	File file = fileChooser.showOpenDialog(stage);

	if (file == null) {
	    return;
	} else {
	    // check to see if photo exists in the album.
	    for (Photo p : currentAlbumChosen.getPhotos()) {
		if (p.getImage().equals(file)) {
		    System.out.println("cannot add the same image sorry.");
		    return;
		}
	    }
	}

	currentAlbumChosen.addPhoto(new Photo(file));
	obsList.setAll(currentAlbumChosen.getPhotos());
	listViewPhotos.setItems(this.obsList);
	PhotoAlbumUsers.write(userList);
	refreshData();
    }



    @FXML
    protected void handleRemoveBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	Photo p = this.listViewPhotos.getSelectionModel().getSelectedItem();

	if (p == null) {
	    return;
	}

	currentAlbumChosen.removePhoto(p);
	obsList.remove(p);
	listViewPhotos.setItems(obsList);

	refreshData();

	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @FXML
    protected void handleMoveBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	Photo selectedPhoto = listViewPhotos.getSelectionModel().getSelectedItem();

	if (selectedPhoto == null) {
	    return;
	}

	Dialog<Album> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Move Photo ");
	dialog.setResizable(false);
	dialog.setHeaderText("Move photo to another album:");

	Label moveToAlbum = new Label("List of Available Albums: ");

	ListView<Album> availableAlbums = new ListView<Album>();
	ObservableList<Album> albumObsList = FXCollections.observableArrayList(currentUser.getAlbums());

	availableAlbums.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {

	    @Override
	    public ListCell<Album> call(ListView<Album> p) {
		ListCell<Album> cell = new ListCell<Album>() {

		    @Override
		    protected void updateItem(Album album, boolean empty) {
			super.updateItem(album, empty);
			if (album != null) {
			    setText(album.getAlbumName());
			} else {
			    setText(null);
			}
		    }
		};

		return cell;
	    }
	});

	availableAlbums.setItems(albumObsList);

	GridPane gridPane = new GridPane();
	gridPane.add(moveToAlbum, 1, 1);
	gridPane.add(availableAlbums, 1, 2);
	dialog.getDialogPane().setContent(gridPane);
	ButtonType saveBtn = new ButtonType("Save", ButtonData.OK_DONE);
	ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(saveBtn, cancelBtn);

	dialog.setResultConverter(new Callback<ButtonType, Album>() {

	    @Override
	    public Album call(ButtonType btn) {
		if (btn == saveBtn) {
		    return availableAlbums.getSelectionModel().getSelectedItem();
		}

		return null;
	    }
	});

	Optional<Album> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {

	    Album moveToAlb = dResult.get();

	    // checking if the user selected to move the photo to the current
	    // album
	    if (moveToAlb.equals(currentAlbumChosen)) {
		return;
	    }

	    // add photo to moveTo album
	    moveToAlb.addPhoto(selectedPhoto);

	    // remove photo from current album and update the list.
	    currentAlbumChosen.removePhoto(selectedPhoto);
	    obsList.remove(selectedPhoto);
	    listViewPhotos.setItems(obsList);
	    PhotoAlbumUsers.write(userList);
	}
    }

    @FXML
    protected void handleCopyBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	Photo selectedPhotoCpy = listViewPhotos.getSelectionModel().getSelectedItem();

	if (selectedPhotoCpy == null) {
	    return;
	}

	Dialog<Album> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Copy Photo Window");
	dialog.setResizable(false);
	dialog.setHeaderText("Copy Photo to an Album");

	Label cpyToAlbum = new Label("List of Available Albums: ");
	ListView<Album> availableAlbumsCpy = new ListView<Album>();
	ObservableList<Album> albumObsListCpy = FXCollections.observableArrayList(currentUser.getAlbums());
	availableAlbumsCpy.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {

	    @Override
	    public ListCell<Album> call(ListView<Album> p) {
		ListCell<Album> cell = new ListCell<Album>() {

		    @Override
		    protected void updateItem(Album album, boolean empty) {
			super.updateItem(album, empty);
			if (album != null) {
			    setText(album.getAlbumName());
			} else {
			    setText(null);
			}
		    }
		};

		return cell;
	    }
	});

	availableAlbumsCpy.setItems(albumObsListCpy);
	GridPane gridPane = new GridPane();
	gridPane.add(cpyToAlbum, 1, 1);
	gridPane.add(availableAlbumsCpy, 1, 2);
	dialog.getDialogPane().setContent(gridPane);
	ButtonType saveBtn = new ButtonType("Save", ButtonData.OK_DONE);
	ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(saveBtn, cancelBtn);

	dialog.setResultConverter(new Callback<ButtonType, Album>() {

	    @Override
	    public Album call(ButtonType btn) {
		if (btn == saveBtn) {
		    return availableAlbumsCpy.getSelectionModel().getSelectedItem();
		}

		return null;
	    }
	});

	Optional<Album> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {
	    Album cpyToAlb = dResult.get();

	    // checking if the user selected to copy the photo to the current
	    // album
	    if (cpyToAlb.equals(currentAlbumChosen)) {
		return;
	    }

	    // add photo to copyTo album
	    cpyToAlb.addPhoto(selectedPhotoCpy);
	    PhotoAlbumUsers.write(userList);
	}
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

	    Platform.runLater(() -> photoCaptionTF.requestFocus());

	    GridPane gridPane = new GridPane();
	    gridPane.add(photoCaption, 1, 1);
	    gridPane.add(photoCaptionTF, 2, 1);

	    dialog.getDialogPane().setContent(gridPane);

	    ButtonType saveBtn = new ButtonType("Save", ButtonData.OK_DONE);
	    ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, cancelBtn);

	    Optional<Boolean> dResult = dialog.showAndWait();

	    if (dResult.isPresent()) {
		p.setCaption(photoCaptionTF.getText());
		updateItem(p, true);
		obsList.setAll(currentAlbumChosen.getPhotos());
		listViewPhotos.setItems(obsList);
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

    @FXML
    protected void handleLogoutBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	Parent root;
	Scene scene;
	Stage loginStage;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	root = loader.load();
	scene = new Scene(root);
	loginStage = new Stage();
	loginStage.setResizable(false);
	loginStage.setTitle("Photo Album Login Page");
	loginStage.setScene(scene);
	saveData();
	stage.close();
	loginStage.show();
    }

    public PhotoAlbumUsers getUsers() {
	return this.userList;
    }

    public ObservableList<Photo> getObsList() {
	return obsList;
    }

    public void setStage(Stage stage) {
	this.stage = stage;
    }

    public void setListOfUsers(PhotoAlbumUsers u) {
	this.userList = u;
    }

    public Album getAlbum() {
	return this.currentAlbumChosen;
    }

    public void setAlbum(Album a) {
	this.currentAlbumChosen = a;
    }

    public void setUser(User u) {
	this.currentUser = u;
    }

    public User getUser() {
	return this.currentUser;
    }

    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}