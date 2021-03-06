import control.MainTimeViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main class for the application. Starts the application by loading
 * the main UI and showing it.
 * @author andys
 *
 */
public class Main extends Application {
	
	/**
	 * Essentially just calls launch, which calls start.
	 * @param args unused.
	 */
	public static void main(String[] args) {
		Platform.setImplicitExit(false);
		//starts the program there bud
		launch(args);

	}

	@Override
	public void start(Stage primary) throws Exception {
		
		//load the main panel
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTimeView.fxml"));
        Pane pane = loader.load();

        primary.setScene(new Scene(pane));
        MainTimeViewController controller = loader.getController();
        
        //start that showwww
        controller.initPanel(pane);
        primary.show();
		
	}

}
