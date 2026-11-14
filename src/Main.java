//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application{
    @Override
    public void start(Stage stage){
        //title
        Label title = new Label("Tech Support");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // welcome message
        Label welcome = new Label("Welcome to Tech Support!");
        welcome.setStyle("-fx-font-size: 18px;");

        // instructions
        Label instructions = new Label(
                "Scan your computer and send a report to IT Support."
        );
        // buttons
        Button scanButton = new Button("Create Ticket");
        Button reportButton = new Button("View Report");
        Button sendButton = new Button("Send to IT Support");

        // Arrange buttons and tile vertically
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
                title,
                welcome,
                instructions,
                scanButton,
                reportButton,
                sendButton
        );

        Scene scene = new Scene(layout, 600, 450);

        stage.setTitle("TechAssist - IT Support");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
