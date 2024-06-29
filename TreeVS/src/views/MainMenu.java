package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("TreeData");

        Label titleLabel = new Label("Tree Visualization");
        titleLabel.setStyle("-fx-font-size:150px; -fx-font-weight: bold;-fx-background-color: #F5F5DC;");

        // Button GridPane
        GridPane panelButtons = new GridPane();
        panelButtons.setPadding(new Insets(10));
        panelButtons.setVgap(10);
        panelButtons.setHgap(10);
        panelButtons.setAlignment(Pos.CENTER);
        panelButtons.setStyle("-fx-background-color: #F5F5DC;");
        addButtons(panelButtons);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleLabel);
        mainLayout.setCenter(panelButtons);
        BorderPane.setAlignment(titleLabel, Pos.TOP_CENTER);

        Color customColor = Color.rgb(245,245,220);

        Scene scene = new Scene(mainLayout); // Increased height to fit all buttons
        mainLayout.setBackground(new Background(new BackgroundFill(customColor, CornerRadii.EMPTY, Insets.EMPTY))); // Set pink background

        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addButtons(GridPane panelButtons) {
        ButtonListener btnListener = new ButtonListener();

        Button binaryTreeButton = new Button("Binary Tree");
        configureButton(binaryTreeButton, btnListener);
        panelButtons.add(binaryTreeButton, 0, 0);

        Button genericTreeButton = new Button("Generic Tree");
        configureButton(genericTreeButton, btnListener);
        panelButtons.add(genericTreeButton, 0, 1);

        Button balancedTreeButton = new Button("Balanced Tree");
        configureButton(balancedTreeButton, btnListener);
        panelButtons.add(balancedTreeButton, 0, 2);

        Button balancedBinaryTreeButton = new Button("Balanced Binary Tree");
        configureButton(balancedBinaryTreeButton, btnListener);
        panelButtons.add(balancedBinaryTreeButton, 0, 3);

        Button quitButton = new Button("Quit");
        configureButton(quitButton, btnListener);
        panelButtons.add(quitButton, 0, 4);

        Button helpButton = new Button("Help");
        configureButton(helpButton, btnListener);
        panelButtons.add(helpButton, 0, 5);
    }

    private void configureButton(Button button, ButtonListener listener) {
    	button.setPrefSize(700, 90);
        button.setStyle("-fx-background-color: #708090; -fx-text-fill: #FAF0E6; -fx-font-size: 50;");
        button.setOnAction(listener);
    }

    private class ButtonListener implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Button source = (Button) event.getSource();
            String buttonText = source.getText();

            switch (buttonText) {
                case "Binary Tree":
                    switchToTreeVisualizer("BinaryTree");
                    break;
                case "Generic Tree":
                    switchToTreeVisualizer("GenericTree");
                    break;
                case "Balanced Tree":
                    switchToTreeVisualizer("BalancedTree");
                    break;
                case "Balanced Binary Tree":
                    switchToTreeVisualizer("BalancedBinaryTree");
                    break;
                case "Quit":
                    showQuitDialog();
                    break;
                case "Help":
                    showHelpDialog();
                    break;
            }
        }
    }

    private void switchToTreeVisualizer(String treeType) {
        TreeVisualizer treeVisualizer = new TreeVisualizer(treeType, primaryStage, primaryStage.getScene());
        Scene scene = new Scene(treeVisualizer.getView(), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
    }

    private void showQuitDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to quit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Quit Confirmation");
        alert.setHeaderText(null);
        alert.initOwner(primaryStage); // Set the owner window
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                primaryStage.close();
            }
        });
    }

    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Instructions");
        alert.initOwner(primaryStage); // Set the owner window

        TextArea textArea = new TextArea("This application allows you to visualize different types of trees.\n\n"
                + "BinaryTree: A simple binary tree.\n"
                + "GenericTree: A tree where nodes can have any number of children.\n"
                + "BalancedTree: A tree that self-balances.\n"
                + "BalancedBinaryTree: A binary tree that self-balances.\n\n"
                + "Use the buttons on the right to interact with the tree: insert nodes, delete nodes, search for nodes, etc.");
        textArea.setWrapText(true);
        textArea.setEditable(false);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
