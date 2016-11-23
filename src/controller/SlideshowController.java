package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Main.AlertBox;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class SlideshowController implements Initializable {
    @FXML
    Button homeBtn;
    @FXML
    Button logoutBtn;
    @FXML
    Button previousBtn;
    @FXML
    Button nextBtn;
    @FXML
    Label captionLB;
    @FXML
    Label dateLB;
    @FXML
    Button addTagBtn;
    @FXML
    Button removeTagBtn;
    @FXML
    ListView<Tag> tagsLV;
    @FXML
    ImageView imageView;
    @FXML
    AnchorPane imageParent;

    private PhotoAlbumUsers userList;
    private Stage stage;
    private Album currentAlbumChosen;
    private User currentUser;
    private ObservableList<Tag> obsList;
    private Photo currentPhoto;
    private Tag tagSelected;

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
    }

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     * initalizes the data necessary for the SlideShow Controller to work properly.
     */
    public void initData() throws ClassNotFoundException, IOException {
	captionLB.setText(currentPhoto.getCaption());
	dateLB.setText(currentPhoto.getPhotoDateAndTime().toString());
	obsList = FXCollections.observableArrayList(currentPhoto.getTags());

	tagsLV.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>() {

	    @Override
	    public ListCell<Tag> call(ListView<Tag> p) {
		ListCell<Tag> cell = new ListCell<Tag>() {

		    @Override
		    protected void updateItem(Tag tag, boolean empty) {
			super.updateItem(tag, empty);
			if (tag != null) {
			    setText(tag.getTagType() + " : " + tag.getTagValue());
			} else {
			    setText(null);
			}
		    }
		};

		return cell;
	    }

	});

	refreshImage();
	
	stage.setOnCloseRequest(e -> {
	    try {
		saveData();
		System.out.println("saved");
	    } catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	});
    }

    /**
     * @throws IOException
     * refreshes the image ImageView displayed in the top of the page.
     */
    public void refreshImage() throws IOException {
	imageView.setFitWidth(606);
	imageView.setFitHeight(413);
	imageView.setPreserveRatio(false);
	BufferedImage bufferedImage = null;
	bufferedImage = ImageIO.read(currentPhoto.getImage());
	Image image = SwingFXUtils.toFXImage(bufferedImage, null);
	imageView.setImage(image);
	obsList.setAll(currentPhoto.getTags());
	tagsLV.setItems(obsList);
	dateLB.setText(currentPhoto.getDate());
	captionLB.setText(currentPhoto.getCaption());
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * @throws IOException
     * returns to the home page.
     */
    @FXML
    protected void handleHomeBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumPhotosList.fxml"));
	Parent root = (Parent) loader.load();
	PhotosController controller = loader.<PhotosController>getController();
	Scene homeScene = new Scene(root);
	controller.setListOfUsers(userList);
	controller.setUser(currentUser);
	controller.setAlbum(currentAlbumChosen);
	controller.setStage(stage);
	controller.initData();
	stage.setScene(homeScene);
	stage.show();
    }

    /**
     * @param event
     * @throws ClassNotFoundException
     * @throws IOException
     * this function logs out of the system and returns to the initial page of the application
     */
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

    /**
     * @param event
     * adds a tag to the current photo.
     */
    @FXML
    protected void handleAddTagBtnAction(ActionEvent event) {
	Dialog<Tag> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Add New Tag");
	dialog.setResizable(false);
	dialog.setHeaderText("add a new tag to your photo:");

	Label tagTypeLB = new Label("Tag Type: ");
	TextField tagTypeTF = new TextField();
	Label tagValueLB = new Label("Tag Value:  ");
	TextField tagValueTF = new TextField();

	Platform.runLater(() -> tagTypeTF.requestFocus());

	BooleanBinding bb = new BooleanBinding() {
	    {
		super.bind(tagTypeTF.textProperty(), tagValueTF.textProperty());
	    }

	    @Override
	    protected boolean computeValue() {
		return (tagTypeTF.getText().trim().isEmpty() || tagValueTF.getText().trim().isEmpty());
	    }
	};

	GridPane gridPane = new GridPane();
	gridPane.add(tagTypeLB, 1, 1);
	gridPane.add(tagTypeTF, 2, 1);

	gridPane.add(tagValueLB, 1, 2);
	gridPane.add(tagValueTF, 2, 2);
	dialog.getDialogPane().setContent(gridPane);

	ButtonType addBtn = new ButtonType("Add", ButtonData.OK_DONE);
	ButtonType cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(addBtn, cancelBtn);
	dialog.getDialogPane().lookupButton(addBtn).disableProperty().bind(bb);

	dialog.setResultConverter(new Callback<ButtonType, Tag>() {

	    @Override
	    public Tag call(ButtonType btn) {
		if (btn == addBtn) {
		    if (tagTypeTF.getText().trim().isEmpty() || tagTypeTF.getText().isEmpty()) {
			AlertBox.display("Error: Empty tag values.", "Tag textfields cannot be empty.");
		    } else if (currentPhoto.tagExists(new Tag(tagTypeTF.getText().trim(), tagValueTF.getText().trim()))) {
			AlertBox.display("Duplicate Tags Detected", "Cannot have duplicate tags on the same photo.");
		    }
		    else {
			return new Tag(tagTypeTF.getText(), tagValueTF.getText());
		    }
		}

		return null;
	    }
	});

	Optional<Tag> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {
	    currentPhoto.addTag(dResult.get());
	    obsList.setAll(currentPhoto.getTags());
	    tagsLV.setItems(obsList);
	    try {
		PhotoAlbumUsers.write(userList);
	    } catch (IOException o) {
		o.printStackTrace();
	    }
	}
    }

    /**
     * @param event
     * removes the selected tag for the current photo displayed.
     */
    @FXML
    protected void handleRemoveTagBtnAction(ActionEvent event) {
	tagSelected = tagsLV.getSelectionModel().getSelectedItem();
	currentPhoto.removeTag(tagSelected);
	obsList.remove(tagSelected);
	tagsLV.setItems(obsList);

	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * @param event 
     * switches the image displayed to the previously image in the array list.
     */
    @FXML
    protected void previousBtnAction(ActionEvent event) {
	currentPhoto = currentAlbumChosen.previousPhoto(currentPhoto);
	try {
	    refreshImage();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param event
     * switches the image displayed to the next image in the array list.
     */
    @FXML
    protected void nextBtnAction(ActionEvent event) {
	currentPhoto = currentAlbumChosen.nextPhoto(currentPhoto);
	try {
	    refreshImage();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param p 
     * sets the currentPhoto to the param p.
     */
    public void setCurrentPhoto(Photo p) {
	currentPhoto = p;
    }

    /**
     * @return userList PhotoAlbumUsers
     */
    public PhotoAlbumUsers getUserList() {
	return userList;
    }

    /**
     * @param userList
     * sets the current list of users to the param passed in.
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
     * saves the data into the serialized .dat file.
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
     * sets the current stage to the param.
     */
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    /**
     * @return currentAlbumChosen Album
     */
    public Album getCurrentAlbumChosen() {
	return currentAlbumChosen;
    }

    /**
     * @param currentAlbumChosen 
     * sets the current album chosen to the param passed in
     */
    public void setCurrentAlbumChosen(Album currentAlbumChosen) {
	this.currentAlbumChosen = currentAlbumChosen;
    }

    /**
     * @return currentUser User.
     */
    public User getCurrentUser() {
	return currentUser;
    }

    /**
     * @param currentUser 
     * sets the currentUser of the application to the param passed into this function.
     */
    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }

    /**
     * @return currentPhoto Photo
     */
    public Photo getCurrentPhoto() {
	return currentPhoto;
    }
}
