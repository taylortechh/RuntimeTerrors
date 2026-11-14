//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class Main extends Application{
    private String report = "";
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

        TextArea problemInput = new TextArea();
        problemInput.setPrefHeight(80);
        problemInput.setWrapText(true);
        Label problem = new Label("Please briefly describe your problem. ");
        problem.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // buttons

        Button ticketButton = new Button("Create Ticket");
        Button viewButton = new Button("View Report");
        Button sendButton = new Button("Scan and send to IT");

        // Arrange buttons and tile vertically
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));



        ticketButton.setOnAction(e -> {
            VBox ticketLayout = new VBox(20);
            ticketLayout.setAlignment(Pos.TOP_CENTER);
            ticketLayout.setPadding(new Insets(120, 30, 30, 30));

            this.report = problem.getText();

            ticketLayout.getChildren().addAll(
                    problem,
                    problemInput,
                    sendButton
            );



            Scene ticketScene = new Scene(ticketLayout, 600, 450);

            stage.setTitle("TechAssist - IT Support");
            stage.setScene(ticketScene);
            stage.show();

        });


        viewButton.setOnAction(e -> {
            VBox viewLayout = new VBox(20);
            viewLayout.setAlignment(Pos.CENTER);
            viewLayout.setPadding(new Insets(30));

            Scene viewScene = new Scene(viewLayout, 600, 450);

            stage.setTitle("TechAssist - IT Support");
            stage.setScene(viewScene);
            stage.show();

        });

        sendButton.setOnAction(e -> {
            sendButton.setDisable(true);
            this.report = problemInput.getText();
            new Thread (() -> {
                String specs = diagnostics.clientInfo();

                String support = "Customer Issue:\n" + this.report + "\n\nDiagnostics:\n" + specs;
                this.report = support;

                try {
                    HttpClient client = HttpClient.newHttpClient(); {
                        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/ticket")).POST(HttpRequest.BodyPublishers.ofString(support)).build();
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        System.out.print(response.body());

                    };
                }

                catch (IOException | InterruptedException ex) {
                    // in case of connection error
                    ex.printStackTrace();
                    Platform.runLater(() -> sendButton.setDisable(false));
                }

            }).start();
        });


        layout.getChildren().addAll(
                title,
                welcome,
                instructions,
                ticketButton,
                viewButton
        );

        Scene homeScene = new Scene(layout, 600, 450);

        stage.setTitle("TechAssist - IT Support");
        stage.setScene(homeScene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
