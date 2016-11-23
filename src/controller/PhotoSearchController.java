package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Album;
import model.PhotoAlbumUsers;
import model.Tag;
import model.User;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class PhotoSearchController implements Initializable {
    @FXML
    Button logoutBtn;
    @FXML
    Button backBtn;
    @FXML
    Button searchBtn;
    @FXML
    Button saveBtn;
    @FXML
    Button addTagBtn;
    @FXML
    Button removeTagBtn;
    @FXML
    TextField tagValueTF;
    @FXML
    TextField tagTypeTF;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    /**
     * 
     */
    @FXML
    ListView<Tag> searchedTagsLV;

    private PhotoAlbumUsers userList;
    private Stage stage;
    private Album currentAlbumChosen;
    private User currentUser;
    private ObservableList<Tag> obsList;
    private List<Tag> tagList = new ArrayList<Tag>();

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     * initializes the necessary data needed for the PhotoSearchController to work properly.
     */
    public void initData() throws ClassNotFoundException, IOException {
	obsList = FXCollections.observableArrayList(tagList);

	searchedTagsLV.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>() {

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

	String pattern = "yyyy-MM-dd";

	startDatePicker.setPromptText(pattern.toLowerCase());

	startDatePicker.setConverter(new StringConverter<LocalDate>() {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	    @Override
	    public String toString(LocalDate date) {
		if (date != null) {
		    return dateFormatter.format(date);
		} else {
		    return "";
		}
	    }

	    @Override
	    public LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
		    return LocalDate.parse(string, dateFormatter);
		} else {
		    return null;
		}
	    }
	});

	endDatePicker.setPromptText(pattern.toLowerCase());

	endDatePicker.setConverter(new StringConverter<LocalDate>() {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	    @Override
	    public String toString(LocalDate date) {
		if (date != null) {
		    return dateFormatter.format(date);
		} else {
		    return "";
		}
	    }

	    @Override
	    public LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
		    return LocalDate.parse(string, dateFormatter);
		} else {
		    return null;
		}
	    }
	});

	BooleanBinding bb = new BooleanBinding() {
	    {
		super.bind(tagValueTF.textProperty(), tagTypeTF.textProperty());
	    }

	    @Override
	    protected boolean computeValue() {
		return (tagValueTF.getText().trim().isEmpty() || tagTypeTF.getText().trim().isEmpty());
	    }
	};

	addTagBtn.disableProperty().bind(bb);

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
     * @throws IOException
     * @throws ClassNotFoundException
     * returns back to the previous window.
     */
    @FXML
    protected void backBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
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

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * logs out the system.
     */
    @FXML
    protected void logoutBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
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
     * @throws IOException
     * @throws ClassNotFoundException
     * switches to the search fxml view file by clicking the search button.
     */
    @FXML
    protected void searchBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearchResult.fxml"));
	Parent root = (Parent) loader.load();
	PhotoSearchResultController controller = loader.<PhotoSearchResultController>getController();
	Scene homeScene = new Scene(root);
	controller.setUserList(userList);
	controller.setCurrentUser(currentUser);
	controller.setStage(stage);
	controller.setSearchedTagsList(tagList);
	controller.setStartDate(startDatePicker.getValue());
	controller.setEndDate(endDatePicker.getValue());
	controller.initData();
	controller.setStage(stage);
	stage.setScene(homeScene);
	stage.show();
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * removes the selected album from the user's current albums.
     */
    @FXML
    protected void removeTagBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
	tagList.remove(searchedTagsLV.getSelectionModel().getSelectedItem());
	obsList.setAll(tagList);
	searchedTagsLV.setItems(obsList);
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * function that adds a new tag to the current photo displayed.
     */
    @FXML
    protected void addTagBtnAction(ActionEvent event) throws IOException, ClassNotFoundException {
	tagList.add(new Tag(tagTypeTF.getText().trim(), tagValueTF.getText().trim()));
	obsList.setAll(tagList);
	searchedTagsLV.setItems(obsList);
    }

    /**
     * @return userList PhotoAlbumUsers
     */
    public PhotoAlbumUsers getUserList() {
	return userList;
    }

    /**
     * @param userList 
     * sets the current userList to the param passed in.
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
     * saves all of the data into the .dat file.
     */
    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(userList);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param stage Stage 
     * sets the current stage to the param 
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
     * sets the currentAlbumChosen to the param passed in.
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
     * sets the currentUser to the param.
     */
    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }
}