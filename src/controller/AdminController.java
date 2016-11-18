package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */

public class AdminController implements Initializable {
    @FXML
    private Button createBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private ListView<User> listView;
    PhotoAlbumUsers listOfAllUsers = null;
    Stage stage;
    private ObservableList<User> obsList = null;

    /*
     * Called to initialize a controller after its root element has been
     * completely processed. (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	try {
	    initData();
	} catch (ClassNotFoundException | IOException o) {
	    o.printStackTrace();
	}
    }

    /**
     * This method initializes the controllers data variables after its root
     * element is complete.
     */

    public void initData() throws ClassNotFoundException, IOException {
	listOfAllUsers = PhotoAlbumUsers.read();
	obsList = FXCollections.observableArrayList(listOfAllUsers.getUsers());
	listView.setItems(obsList);

	listView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {

	    @Override
	    public ListCell<User> call(ListView<User> p) {
		ListCell<User> cell = new ListCell<User>() {

		    @Override
		    protected void updateItem(User user, boolean empty) {
			super.updateItem(user, empty);
			if (user != null) {
			    setText(user.getUsername());
			} else {
			    setText(null);
			}
		    }
		};

		return cell;
	    }
	});

	listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
	    if (obs != null) {
		if (obs.getValue().getUsername().equals("admin")) {
		    removeBtn.setDisable(true);
		} else {
		    removeBtn.setDisable(false);
		}
	    }
	});
    }

    /**
     * @param event
     *            ActionEvent.
     * @throws ClassNotFoundException
     *             This method catches the user's click event to create a new
     *             user and creates a new dialog for the user.
     */

    @FXML
    private void createNewUserAction(ActionEvent event) throws ClassNotFoundException {
	GridPane gridPane;
	ButtonType createBtn, cancelBtn;

	Dialog<User> dialog = new Dialog<>();
	dialog.initModality(Modality.APPLICATION_MODAL);
	dialog.setTitle("Create a new user");
	dialog.setResizable(false);
	dialog.setHeaderText("Enter user's infromation below:");

	Label usernameLB = new Label("Username: ");
	Label passwordLB = new Label("Password: ");
	TextField usernameTF = new TextField();
	PasswordField passwordTF = new PasswordField();
	Platform.runLater(() -> usernameTF.requestFocus());

	gridPane = new GridPane();
	gridPane.add(usernameLB, 1, 1);
	gridPane.add(passwordLB, 1, 2);
	gridPane.add(usernameTF, 2, 1);
	gridPane.add(passwordTF, 2, 2);

	dialog.getDialogPane().setContent(gridPane);
	createBtn = new ButtonType("Create", ButtonData.OK_DONE);
	cancelBtn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(createBtn, cancelBtn);

	dialog.setResultConverter(new Callback<ButtonType, User>() {
	    String errorMessage = null;

	    @Override
	    public User call(ButtonType btn) {
		if (btn == createBtn) {
		    if (usernameTF.getText().trim().isEmpty()) {
			errorMessage = "Username field cannot be empty.";
		    } else if (listOfAllUsers.isUsernameTaken(usernameTF.getText())) {
			errorMessage = "Username already taken.";
		    } else if (passwordTF.getText().trim().isEmpty()) {
			errorMessage = "Password field cannot be empty.";
		    } else {
			errorMessage = null;
		    }

		    if (errorMessage != null) {
			AlertBox.display("Error: unable to create user", errorMessage);
			return null;
		    }

		    return new User(usernameTF.getText(), passwordTF.getText());
		}

		return null;
	    }
	});

	Optional<User> dResult = dialog.showAndWait();

	if (dResult.isPresent()) {
	    User newUser = dResult.get();
	    listOfAllUsers.addUser(newUser);
	    obsList.add(newUser);
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
     *            ActionEvent
     * @throws ClassNotFoundException
     *             This method catches the user's click event to remove a new
     *             user and removes the selected user from the listOfAllUsers.
     */

    @FXML
    private void removeUserAction(ActionEvent event) throws ClassNotFoundException, IOException {
	User userToBeRemoved = listView.getSelectionModel().getSelectedItem();
	obsList.remove(userToBeRemoved);
	listOfAllUsers.removeUser(userToBeRemoved);
	listView.getSelectionModel().select(0);
	PhotoAlbumUsers.write(listOfAllUsers);
    }

    /**
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     */

    @FXML
    private void loggingOut(ActionEvent event) throws IOException, ClassNotFoundException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	Parent root = loader.load();
	Scene scene = new Scene(root);
	Stage stage = new Stage();
	stage.setResizable(false);
	stage.setTitle("Photo Album Login Page");
	stage.setScene(scene);
	saveData();
	closeAdminAppWindow();
	stage.show();
    }

    /**
     * closed the the current Admin system window.
     */

    private void closeAdminAppWindow() {
	this.stage.close();
    }

    /**
     * @throws ClassNotFoundException
     *             This method saves the data of all users.
     */

    private void saveData() throws ClassNotFoundException {
	try {
	    PhotoAlbumUsers.write(listOfAllUsers);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param stage
     *            Stage sets the Admin's controller stage to the stage param.
     */

    public void setStage(Stage stage) {
	this.stage = stage;
    }
}
