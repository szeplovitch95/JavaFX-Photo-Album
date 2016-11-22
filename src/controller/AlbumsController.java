package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Main.AlertBox;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.PhotoAlbumUsers;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class AlbumsController implements Initializable {
    private PhotoAlbumUsers listOfAllUsers;
    private Stage stage;
    private User currentUser;
    private Album currentAlbum;

    @FXML
    Button createBtn;
    @FXML
    Button removeBtn;
    @FXML
    Button renameBtn;
    @FXML
    Button openBtn;
    @FXML
    Button logoutBtn;
    @FXML 
    Button searchBtn;
    @FXML
    Label welcomeLB;
    ObservableList<Album> obsList = null;
    @FXML
    ListView<Album> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     * initialize the required data needed for this scene to be working properly.
     */
    public void initData() throws ClassNotFoundException, IOException {
	// listOfAllUsers = PhotoAlbumUsers.read();
	obsList = FXCollections.observableArrayList(currentUser.getAlbums());
	listView.setItems(obsList);
	welcomeLB.setText("  Welcome " + currentUser.getUsername());

	listView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {

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

	renameBtn.disableProperty().bind(Bindings.size(obsList).isEqualTo(0));
	removeBtn.disableProperty().bind(Bindings.size(obsList).isEqualTo(0));
	openBtn.disableProperty().bind(Bindings.size(obsList).isEqualTo(0));
	
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
     * @param username
     * sets the current user logged in the system to the paramters passed in.
     */
    public void setCurrentUser(String username) {
	currentUser = listOfAllUsers.getUserByUsername(username);
    }
    
    /**
     * @param event
     * @throws ClassNotFoundException
     * @throws IOException
     * changes the scene to the search view when the user clicks the search button.
     */
    @FXML
    protected void handleSearchBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearch.fxml"));
	Parent root = (Parent) loader.load();
	PhotoSearchController controller = loader.<PhotoSearchController>getController();
	Scene homeScene = new Scene(root);
	controller.setUserList(listOfAllUsers);
	controller.setCurrentUser(currentUser);
	controller.setStage(stage);
	controller.initData();
	stage.setScene(homeScene);
	stage.show();
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * opens up a new dialog for creating a new album for the logged in user.
     */
    @FXML
    private void createNewAlbum(ActionEvent event) throws ClassNotFoundException {
	Dialog<Album> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Create a new album");
	dialog.setResizable(false);
	dialog.setHeaderText("Enter Album's infromation below:");

	Label albumName = new Label("Album Name: ");
	TextField albumNameTF = new TextField();
	Platform.runLater(() -> albumNameTF.requestFocus());

	GridPane gridPane = new GridPane();
	gridPane.add(albumName, 1, 1);
	gridPane.add(albumNameTF, 2, 1);
	dialog.getDialogPane().setContent(gridPane);

	ButtonType createBtn = new ButtonType("Create", ButtonData.OK_DONE);
	ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(createBtn, cancelBtn);

	dialog.setResultConverter(new Callback<ButtonType, Album>() {
	    String errorMessage = null;

	    @Override
	    public Album call(ButtonType btn) {
		if (btn == createBtn) {
		    if (albumNameTF.getText().trim().isEmpty()) {
			errorMessage = "Album name field cannot be empty.";
		    }

		    if (currentUser.getAlbumByName(albumNameTF.getText().trim()) != null) {
			errorMessage = "Album name already exists";
		    }

		    if (errorMessage != null) {
			AlertBox.display("Error: unable to create user", errorMessage);
			return null;
		    }

		    return new Album(albumNameTF.getText());
		}

		return null;
	    }
	});

	Optional<Album> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {
	    Album newAlbum = dResult.get();
	    currentUser.addAlbum(newAlbum);
	    obsList.add(newAlbum);
	    listView.getSelectionModel().select(0);

	    try {
		PhotoAlbumUsers.write(listOfAllUsers);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    
    /**
     * @return this.currentUser User
     * returns the current user logged in.
     */
    public User getUser() {
	return this.currentUser;
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * opens a dialog to rename the selected album in the list of albums.
     */
    @FXML
    private void renameAlbum(ActionEvent event) throws ClassNotFoundException {
	Dialog<String> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Rename an Album");
	dialog.setResizable(false);
	dialog.setHeaderText("Enter New Album's Name below:");

	Album currentAlbum = listView.getSelectionModel().getSelectedItem();

	if (currentAlbum == null) {
	    return;
	}

	Label albumName = new Label("Album Name: ");
	TextField albumNameTF = new TextField(currentAlbum.getAlbumName());
	Platform.runLater(() -> albumNameTF.requestFocus());

	GridPane gridPane = new GridPane();
	gridPane.add(albumName, 1, 1);
	gridPane.add(albumNameTF, 2, 1);
	dialog.getDialogPane().setContent(gridPane);

	ButtonType renameBtn = new ButtonType("Rename", ButtonData.OK_DONE);
	ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(renameBtn, cancelBtn);

	dialog.setResultConverter(new Callback<ButtonType, String>() {
	    String errorMessage = null;

	    @Override
	    public String call(ButtonType btn) {
		if (btn == renameBtn) {

		    if (albumNameTF.getText().trim().isEmpty()) {
			errorMessage = "Album's name field cannot be empty.";
		    }

		    if (currentUser.getAlbumByName(albumNameTF.getText().trim()) != null) {
			errorMessage = "Album name already exists";
		    }

		    if (errorMessage != null) {
			AlertBox.display("Error", errorMessage);
			return null;
		    }

		    return albumNameTF.getText();
		}

		return null;
	    }
	});

	Optional<String> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {
	    String newName = dResult.get();
	    currentUser.getAlbumByName(currentAlbum.getAlbumName()).setAlbumName(newName);
	    obsList = FXCollections.observableArrayList(currentUser.getAlbums());
	    listView.setItems(null);
	    listView.setItems(obsList);
	    listView.getSelectionModel().select(0);

	    try {
		PhotoAlbumUsers.write(listOfAllUsers);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * removes an album from the list of albums of the current user.
     */
    @FXML
    private void removeAlbum(ActionEvent event) throws ClassNotFoundException {
	Album a = listView.getSelectionModel().getSelectedItem();
	obsList.remove(a);
	currentUser.removeAlbum(a);
	listView.getSelectionModel().select(0);

	try {
	    PhotoAlbumUsers.write(listOfAllUsers);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    private void openAlbum(ActionEvent event) throws ClassNotFoundException, IOException {
	Parent root;
	Scene photoScene;
	currentAlbum = listView.getSelectionModel().getSelectedItem();
	if (currentAlbum == null) {
	    return;
	}

	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumPhotosList.fxml"));
	root = (Parent) loader.load();
	photoScene = new Scene(root);
	PhotosController controller = loader.<PhotosController>getController();
	controller.setListOfUsers(listOfAllUsers);
	controller.setAlbum(currentAlbum);
	controller.setUser(currentUser);
	controller.setStage(stage);
	controller.initData();
	stage.setScene(photoScene);
	stage.show();
    }

    /**
     * @param stage Stage
     * sets the current stage of the program.
     */
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    /**
     * @param u PhotoAlbumUsers 
     * sets the current list of users of the program.
     */
    public void setListOfUsers(PhotoAlbumUsers u) {
	listOfAllUsers = u;
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * performs the logging out function of the window.
     */
    @FXML
    private void loggingOut(ActionEvent event) throws IOException, ClassNotFoundException {
	Parent root;
	Scene scene;
	Stage logoutStage;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	root = loader.load();

	scene = new Scene(root);
	logoutStage = new Stage();
	logoutStage.setResizable(false);
	logoutStage.setTitle("Photo Album Login Page");
	logoutStage.setScene(scene);
	saveData();
	closeAdminAppWindow();
	stage.close();
	logoutStage.show();
    }

    /**
     *  closes the admin app window.
     */
    private void closeAdminAppWindow() {
	stage.close();
    }

    /**
     * @param u User
     * sets the current user to the paramter.
     */
    public void setUser(User u) {
	this.currentUser = u;
    }

    /**
     * @throws ClassNotFoundException
     * saves the data by serializing it to the dat file of the project.
     */
    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(listOfAllUsers);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}