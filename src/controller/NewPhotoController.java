package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.PhotoAlbumUsers;
import model.User;

public class NewPhotoController implements Initializable {
	@FXML private Button createBtn;
	@FXML private Button browseBtn;
	@FXML private Button cancelBtn;
	@FXML private AnchorPane anchorPane;
	@FXML private TextField photoName;
	@FXML private TextField photoCaption;
	@FXML private TextField fileLabel;
	Stage stage; 
	User currentUser;
	PhotoAlbumUsers allUsers;
	Album currentAlbum;
	File file;
	FileChooser fileChooser;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			allUsers = PhotoAlbumUsers.read();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BooleanBinding bb = fileLabel.textProperty().isEqualTo("");
		createBtn.disableProperty().bind(bb);
	}
	
	public void setUser(User u) {
		currentUser = u;
	}
	
	public void setStage(Stage s) {
		stage = s;
	}
	
	public void setAlbum(Album a) {
		currentAlbum = a;

	}
	
	@FXML protected void handleCreateBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
//		Photo newPhoto = new Photo(photoName.getText(), photoCaption.getText(), file.getAbsoluteFile());
//		currentAlbum.printPhotos();
//		currentAlbum.addPhoto(newPhoto);
//		currentAlbum.printPhotos();
//		
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumPhotosList.fxml"));
//		Parent root = (Parent)loader.load();
//		PhotosController ctrl = loader.getController();
//		PhotoAlbumUsers.write(allUsers);
//		
//		ctrl.setAlbum(currentAlbum);
//		ctrl.setUser(currentUser);
//		ctrl.initData();
//		stage.close();
	}	
	
	@FXML protected void handleBrowseBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
		
	}
	
	@FXML protected void handleCancelBtnAction(ActionEvent event) throws ClassNotFoundException, IOException {
//		stage.close();
	}
}
