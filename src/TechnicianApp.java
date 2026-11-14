import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


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
                    int count = technician.tickets.size();
                    notifications.setText("Notifications: " + count);
                    welcome.setText("Welcome! you have " + count + " new supports tickets.");
                    ticketList.getChildren().clear();
                    for (String ticket : technician.tickets) {
                        TextArea ticketBox = new TextArea(ticket);
                        ticketBox.setEditable(false);
                        ticketBox.setWrapText(true);

                        ticketList.getChildren().add(ticketBox);
                    }
                });

        Button terminateButton = new Button("Terminate");
        terminateButton.setStyle("-fx-background-color: #D94A4A; -fx-text-fill: white;");
        terminateButton.setOnAction(event -> confirmAndTerminate());

        HBox buttons = new HBox(15, loadButton, terminateButton);
        buttons.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
                header,
                welcome,
                buttons,
                ticketList
        );

        // Window
        Scene scene = new Scene(layout, 750, 500);

        stage.setTitle("Technician Portal");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            event.consume();
            confirmAndTerminate();
        });
        technician.serverSide();
        stage.show();
    }

    private void confirmAndTerminate() {
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Stop the server and close the dashboard?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(answer -> {
            if (answer == ButtonType.YES) {
                terminate();
            }
        });
    }

    private void terminate() {
        technician.stopServer();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
