package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.*;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */

public class LoginController implements Initializable {
    @FXML
    TextField usernameTF;
    @FXML
    PasswordField passwordTF;
    @FXML
    Label invalidLogin;
    @FXML
    FlowPane flowPane;
    @FXML
    Button loginBtn;

    private Stage stage;
    private String username;
    private PhotoAlbumUsers userList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	try {
	    initData();
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     * initializes data necessary for the LoginController to work properly.
     */
    public void initData() throws ClassNotFoundException, IOException {
	userList = PhotoAlbumUsers.read();
	BooleanBinding bb = new BooleanBinding() {
	    {
		super.bind(usernameTF.textProperty(), passwordTF.textProperty());
	    }

	    @Override
	    protected boolean computeValue() {
		return (usernameTF.getText().trim().isEmpty() || passwordTF.getText().trim().isEmpty());
	    }
	};

	loginBtn.disableProperty().bind(bb);

	flowPane.setOnKeyReleased(e -> {
	    try {
		if (e.getCode() == KeyCode.ENTER) {
		    login(null);
		}
	    } catch (ClassNotFoundException | IOException o) {
		o.printStackTrace();
	    }
	});
    }

    /**
     * @param event
     *            ActionEvent
     * @throws ClassNotFoundException,
     *             IOException This method catches the user's click event to
     *             login into the system.
     */

    @FXML
    private void login(ActionEvent event) throws ClassNotFoundException, IOException {
	Parent root;
	Stage adminStage, userStage;
	Scene adminScene, userScene;
	AdminController adminCtrl;

	try {
	    if (userList.userExists(usernameTF.getText().trim(), passwordTF.getText().trim())) {
		if (usernameTF.getText().trim().equals("admin") && passwordTF.getText().trim().equals("admin")) {
		    username = "admin";
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminMainPage.fxml"));
		    root = loader.load();
		    adminScene = new Scene(root);
		    adminStage = new Stage();
		    adminStage.setResizable(false);
		    adminStage.setTitle("Admin Subsystem");
		    adminStage.setScene(adminScene);
		    adminCtrl = loader.getController();
		    adminCtrl.setStage(adminStage);
		    adminCtrl.initData();
		    closeLoginWindow();
		    adminStage.show();
		} else {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminMainPage.fxml"));
		    root = loader.load();
		    userScene = new Scene(root);
		    userStage = new Stage();
		    userStage.setResizable(false);
		    userStage.setTitle("Welcome to Photo Album App");
		    userStage.setScene(userScene);
		    AlbumsController controller = loader.getController();
		    controller.setListOfUsers(userList);
		    controller.setCurrentUser(usernameTF.getText().trim());
		    controller.setStage(userStage);
		    controller.initData();
		    closeLoginWindow();
		    userStage.show();
		}
	    }

	    invalidLogin.setText("Username and password combination does not exist.");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * closed the the current nonadmin user system window.
     */

    public void closeLoginWindow() {
	stage = (Stage) usernameTF.getScene().getWindow();
	stage.close();
    }

    /**
     * @param stage
     *            Stage sets the Admin's controller stage to the stage param.
     */

    public void setStage(Stage stage) {
	this.stage = stage;
    }
}