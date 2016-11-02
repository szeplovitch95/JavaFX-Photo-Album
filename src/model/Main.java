package model;

import com.sun.java.swing.plaf.windows.resources.windows;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
	
	Stage window;
	Scene scene1, scene2;
//	Button button;
//	Button button2;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		Label label1 = new Label("Welcome to the first scene");
		Button button1 = new Button("Go to Scene 2");
		button1.setOnAction(e -> window.setScene(scene2));
		
		VBox layout1 = new VBox(20);
		layout1.getChildren().addAll(label1, button1);
		
		scene1 = new Scene(layout1, 200, 200);
		
		
		Button button2 = new Button("This scene sucks, go back to scene 1");
		button2.setOnAction(e -> window.setScene(scene1));
		
		//layout2
		StackPane layout2 = new StackPane();
		layout2.getChildren().add(button2);
		scene2 = new Scene(layout2, 600, 300);
		
		window.setScene(scene1);
		window.setTitle("title here");
		
		window.show();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		primaryStage.setTitle("My Application");		
//		button2 = new Button("ASdf");
//		
//		button2.setOnAction(y -> {
//			System.out.println("Asdf");
//		});
//		
//		button = new Button();
//		button.setText("Click");
//		button.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("ASdf");
//			}
//			
//		});
//		
//		StackPane layout = new StackPane();
//		layout.getChildren().addAll(button, button2);
//		
//		Scene scene = new Scene(layout, 300, 250);
//		
//		primaryStage.setScene(scene);
//		primaryStage.show();
	}

	

}
