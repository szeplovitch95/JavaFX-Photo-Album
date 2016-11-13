package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PhotoAlbumUsers;
import model.User;
import java.io.IOException;


/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */

public class PhotoAlbum extends Application {
	Stage window;
	
	public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
		window = primaryStage;
		PhotoAlbumUsers uList = new PhotoAlbumUsers();
		
		try {
//		    	User admin = new User("admin", "admin"); 
//		    	uList.addUser(admin);
//			PhotoAlbumUsers.write(uList);
			
			if(PhotoAlbumUsers.read() == null) {
				createAdmin();
			}
			
			PhotoAlbumUsers userList = PhotoAlbumUsers.read();		
			System.out.println(userList.toString());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Photo Album Login Page");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * This method creates the Admin user of the photo album application. 
	 */
	
	private void createAdmin() throws ClassNotFoundException, IOException {
		PhotoAlbumUsers userList = new PhotoAlbumUsers();
		User admin = new User("admin", "admin");
		userList.addUser(admin);
		PhotoAlbumUsers.write(userList);
	}
}