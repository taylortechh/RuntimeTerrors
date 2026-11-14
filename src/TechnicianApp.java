import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class TechnicianApp extends Application{
    @Override
    public void start(Stage stage) {

        // Application title
        Label title = new Label("Technician Dashboard");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );
        // Number of new tickets (sample data)
        int newTickets = 0;

        // Notification label
        Label notifications = new Label(
                "Notifications: " + newTickets
        );
        notifications.setStyle(
                "-fx-background-color: #D94A4A;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 8;"
        );

        // Header layout
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(title, notifications);

        // Welcome message
        Label welcome = new Label(
                "Welcome! You have " + newTickets +
                        " new support tickets."
        );

        Button loadButton = new Button("Load Tickets");
        VBox ticketList = new VBox(10);
        ticketList.setAlignment(Pos.CENTER);



        // for when the tech pushes the button it loads the information
        loadButton.setOnAction(event -> {

            ticketList.getChildren().setAll(
                    //getting ticket from backend
                    //display ticket here
            );

        });

        // Main layout
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
                header,
                welcome,
                loadButton,
                ticketList
        );

        // Window
        Scene scene = new Scene(layout, 750, 500);

        stage.setTitle("Technician Portal");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
