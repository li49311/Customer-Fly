package boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/ImportUpdates.fxml"));
		Scene scene = new Scene(root,1000,800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Customer-Fly");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
}