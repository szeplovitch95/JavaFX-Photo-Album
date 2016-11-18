package model;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.*;

public class AlertBox {
	
	public static void display(String title, String message) {
		Stage window = new Stage();
		  
		//this blocks any other events from happening until we take 
		//care of the alert box window. 
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(350);
		window.setMinHeight(200);
		
		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button("Try Again");
		
		closeButton.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}