package views;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.shape.Line;
import javafx.animation.FillTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tree.*;
import javafx.scene.shape.Circle;

import javafx.scene.shape.Line;
import javafx.animation.FillTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;




import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.Label;
import java.io.PrintStream;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import tree.*;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Pos;

public class TreeVisualizer {
    private Pane treePane;
    private Tree tree;
    private String treeType;
    private BorderPane mainLayout;
    private Stage primaryStage;
    private Scene mainMenuScene;
    private TreeNode highlightedNode; // New attribute to store the node to be highlighted
    private List<TreeNode> highlightedNodes = new ArrayList<>(); // List of nodes to highlight
    private Timeline timeline; // Timeline for the traversal animation
    private Set<TreeNode> permanentlyHighlightedNodes = new HashSet<>();
    private Set<Line> highlightedEdges = new HashSet<>();
    private Stack<TreeMemento> undoStack = new Stack<>();
    private Stack<TreeMemento> redoStack = new Stack<>();
    private List<TreeMemento> history = new ArrayList<>();
    private int currentHistoryIndex = -1;
    private boolean isPaused = false;
    private TreeNode targetNode = null;
    private Button forwardButton = new Button("⏩");
    private Button backwardButton = new Button("⏪");
    private Button continueButton = new Button("▶");
    private Button pauseButton = new Button("⏸"); // Pause symbol
    private Button undoButton = new Button("↺"); // Undo symbol
    private Button redoButton = new Button("↻"); // Redo symbol
    
    private Text notificationText;
    private Text nodeText;
    private StackPane notificationPane;
    private StackPane nodePane;
    private Set<TreeNode> visitedNodes;

    
  
    public TreeVisualizer(String treeType, Stage primaryStage, Scene mainMenuScene) {
    	this.primaryStage = primaryStage;
        this.mainMenuScene = mainMenuScene;
        this.treeType = treeType;
        initializeTree();
        setupUI();
        
     // Redirect System.out to the notification area
        notificationText = new Text();
        PrintStream customPrintStream = new CustomPrintStream(System.out, notificationText);
        System.setOut(customPrintStream);
        System.setErr(customPrintStream); // Optionally redirect System.err too
    }

    private void initializeTree() {
        switch (treeType) {
            case "BinaryTree":
                tree = new BinaryTree();
                break;
            case "GenericTree":
                tree = new GenericTree();
                break;
            case "BalancedTree":
                tree = new BalancedTree(0); // Assume this is a balanced tree implementation
                break;
            case "BalancedBinaryTree":
                tree = new BalancedBinaryTree(0);
                break;
        }
    }

    private void setupUI() {
        treePane = new Pane();
        treePane.setPrefSize(800, 600);
        treePane.setStyle("-fx-background-color: #F5F5DC");

        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.TOP_LEFT);
        controls.setStyle("-fx-background-color: #98FB98;");
        
        Button createButton = new Button("Create");
        createButton.setOnAction(e -> createRandomTree());

        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> insertNode());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteNode());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchNode());
        
        Button traverseButton = new Button("Traverse");
        traverseButton.setOnAction(e -> traverseNode());
        
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateNode());
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> back());

        // Styling the buttons
        for (Button button : new Button[]{createButton, insertButton, deleteButton, searchButton, traverseButton, updateButton,backButton}) {
            button.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 16;");
            button.setPadding(new Insets(20)); // Increased padding
            button.setMinHeight(50); // Set minimum height
            button.setMaxWidth(Double.MAX_VALUE);
            button.setAlignment(Pos.CENTER);
        }

        controls.getChildren().addAll(createButton, insertButton, deleteButton, searchButton,traverseButton, updateButton,backButton);

        // Create the bottom bar with Undo and Redo buttons
        HBox bottomBar = new HBox(20); // Spacing between buttons
        bottomBar.setPadding(new Insets(5)); // Padding around the HBox
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: lightgray;");
        
        undoButton.setStyle("-fx-font-size: 20;");
        undoButton.setPadding(new Insets(5));
        undoButton.setMinHeight(20);
        undoButton.setMinWidth(100); // Minimum width for the button
        undoButton.setAlignment(Pos.CENTER);
        undoButton.setOnAction(e -> undo());
        
        redoButton.setStyle("-fx-font-size: 20;");
        redoButton.setPadding(new Insets(5));
        redoButton.setMinHeight(20);
        redoButton.setMinWidth(100); // Minimum width for the button
        redoButton.setAlignment(Pos.CENTER);
        redoButton.setOnAction(e -> redo());

        pauseButton.setStyle("-fx-font-size: 20;");
        pauseButton.setPadding(new Insets(5));
        pauseButton.setMinHeight(20);
        pauseButton.setMinWidth(100); // Minimum width for the button
        pauseButton.setAlignment(Pos.CENTER);
        pauseButton.setOnAction(e -> pause());

        continueButton.setStyle("-fx-font-size: 20;");
        continueButton.setPadding(new Insets(5));
        continueButton.setMinHeight(20);
        continueButton.setMinWidth(100);
        continueButton.setAlignment(Pos.CENTER);
        continueButton.setOnAction(e -> resume());
        continueButton.setDisable(true); // Initially disabled

        backwardButton.setStyle("-fx-font-size: 20;");
        backwardButton.setPadding(new Insets(5));
        backwardButton.setMinHeight(20);
        backwardButton.setMinWidth(100);
        backwardButton.setAlignment(Pos.CENTER);
        backwardButton.setOnAction(e -> backward());
        backwardButton.setDisable(true); // Initially disabled

        forwardButton.setStyle("-fx-font-size: 20;");
        forwardButton.setPadding(new Insets(5));
        forwardButton.setMinHeight(20);
        forwardButton.setMinWidth(100);
        forwardButton.setAlignment(Pos.CENTER);
        forwardButton.setOnAction(e -> forward());
        forwardButton.setDisable(true); // Initially disabled

     // Add the buttons to the bottom bar
        bottomBar.getChildren().addAll(undoButton, redoButton, pauseButton, continueButton, backwardButton, forwardButton);

        mainLayout = new BorderPane();
        mainLayout.setCenter(treePane);
        mainLayout.setRight(controls);
        mainLayout.setBottom(bottomBar);
        
     // Create rectangle box for notifications
        Rectangle notificationBox = new Rectangle(400, 200); // Adjust width and height as needed
        notificationBox.setFill(Color.WHITE);
        notificationBox.setStroke(Color.BLACK);
        notificationBox.setStrokeWidth(2);
        
        
        // Create StackPane to hold the Rectangle and Text
        notificationPane = new StackPane(notificationBox);
        StackPane.setAlignment(notificationPane, Pos.BOTTOM_LEFT); // Align to bottom-left corner
        StackPane.setMargin(notificationPane, new Insets(10)); // Add margin

        // Set the desired position
        notificationPane.setLayoutX(5); // Set x-coordinate
        notificationPane.setLayoutY(513); // Set y-coordinate
        
        
        //Create rectangle box for nodeTraverse
        Rectangle nodeTraverse = new Rectangle(400,100);
        nodeTraverse.setFill(Color.WHITE);
        nodeTraverse.setStroke(Color.BLACK);
        nodeTraverse.setStrokeWidth(2);
        
     // Create StackPane to hold the Rectangle and Text
        nodePane = new StackPane(nodeTraverse);
        StackPane.setAlignment(nodePane, Pos.BOTTOM_LEFT); // Align to bottom-left corner
        StackPane.setMargin(nodePane, new Insets(10)); // Add margin
        
     // Set the desired position
        nodePane.setLayoutX(5); // Set x-coordinate
        nodePane.setLayoutY(410); // Set y-coordinate
        
        
     // Add both panes to treePane
        treePane.getChildren().addAll(notificationPane, nodePane);
    }
    
    private void pause() {
        if (!isPaused && timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            isPaused = true;
            forwardButton.setDisable(false);
            backwardButton.setDisable(false);
            continueButton.setDisable(false);
            pauseButton.setDisable(true);
            timeline.pause();
        }
    }

    private void resume() {
        if (isPaused == true && timeline != null && timeline.getStatus() == Timeline.Status.PAUSED) {
            isPaused = false;
            forwardButton.setDisable(true);
            backwardButton.setDisable(true);
            continueButton.setDisable(true);
            pauseButton.setDisable(false);
            timeline.play();
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(saveState());
            restoreState(undoStack.pop());
            currentHistoryIndex--;
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(saveState());
            restoreState(redoStack.pop());
            currentHistoryIndex++;
        }
    }

    private void forward() {
        if (isPaused && currentHistoryIndex < history.size() - 1) {
            currentHistoryIndex++;
            restoreState(history.get(currentHistoryIndex));
        }
    }

    private void backward() {
        if (isPaused && currentHistoryIndex > 0) {
            currentHistoryIndex--;
            restoreState(history.get(currentHistoryIndex));
        }
    }
    
    private void updateButtonStates() {
        backwardButton.setDisable(currentHistoryIndex <= 0);
        forwardButton.setDisable(currentHistoryIndex >= history.size() - 1);
        continueButton.setDisable(!isPaused);
        pauseButton.setDisable(isPaused);
    }

    private void back() {
    	
        primaryStage.setScene(mainMenuScene);
        primaryStage.setFullScreen(true); // Set full screen mode

    }

	public BorderPane getView() {
        return mainLayout;
    }
    
	private void createRandomTree() {
        if (tree instanceof BalancedTree || tree instanceof BalancedBinaryTree) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(primaryStage);
            dialog.setTitle("Maximum Distance");
            dialog.setHeaderText("Enter the maximum difference in distance:");
            dialog.setContentText("Maximum difference:");
            dialog.showAndWait().ifPresent(maxDiff -> {
                try {
                    int maxDifference = Integer.parseInt(maxDiff);
                    if (tree instanceof BalancedTree) {
                        ((BalancedTree) tree).setMaximumDifference(maxDifference);
                    } else if (tree instanceof BalancedBinaryTree) {
                        ((BalancedBinaryTree) tree).setMaximumDifference(maxDifference);
                    }

                    createRandomTreeWithMaxDifference();
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid integer.");
                }
            });
        } else {
            // For other types of trees, simply create a random tree without asking for the maximum difference
            createRandomTreeWithoutMaxDifference();
        }
    }

    private void createRandomTreeWithMaxDifference() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Create Random Tree");
        dialog.setHeaderText("Enter number of values:");
        dialog.setContentText("Number of values:");

        dialog.showAndWait().ifPresent(numValues -> {
            try {
                int numberOfValues = Integer.parseInt(numValues);
                tree.createRandomTree(numberOfValues);
                drawTree();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer.");
            }
        });
    }

    private void createRandomTreeWithoutMaxDifference() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Create Random Tree");
        dialog.setHeaderText("Enter number of values:");
        dialog.setContentText("Number of values:");

        dialog.showAndWait().ifPresent(numValues -> {
            try {
                int numberOfValues = Integer.parseInt(numValues);
                tree.createRandomTree(numberOfValues);
                drawTree();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer.");
            }
        });
    }



    private void insertNode() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Insert Node");
        dialog.setHeaderText("Insert a new node");

        // Check if the root node exists
        if (tree.getRoot() == null) {
            if (tree instanceof BalancedTree || tree instanceof BalancedBinaryTree) {
                TextInputDialog maxDiffDialog = new TextInputDialog();
                maxDiffDialog.initOwner(primaryStage);                
                maxDiffDialog.setTitle("Maximum Difference");
                maxDiffDialog.setHeaderText("Enter the maximum difference in distance:");

                maxDiffDialog.showAndWait().ifPresent(maxDiffInput -> {
                    try {
                        int maxDifference = Integer.parseInt(maxDiffInput.trim());

                        if (tree instanceof BalancedTree) {
                            ((BalancedTree) tree).setMaximumDifference(maxDifference);
                        } else if (tree instanceof BalancedBinaryTree) {
                            ((BalancedBinaryTree) tree).setMaximumDifference(maxDifference);
                        }

                        dialog.setContentText("Enter value for the root node:");
                        dialog.showAndWait().ifPresent(rootValueInput -> {
                            try {
                                int rootValue = Integer.parseInt(rootValueInput.trim());
                                saveStateForUndo();
                                if (tree instanceof BalancedTree) {
                                    ((BalancedTree) tree).insert(0, rootValue); // Inserting as root
                                } else if (tree instanceof BalancedBinaryTree) {
                                    ((BalancedBinaryTree) tree).insert(0, rootValue); // Inserting as root
                                }
                                drawTree();
                            } catch (NumberFormatException e) {
                                showAlert("Invalid Input", "Please enter a valid integer value for the root node.");
                            }
                        });

                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid integer value for the maximum difference.");
                    }
                });
            } else {
                dialog.setContentText("Enter value for the root node:");
                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int newValue = Integer.parseInt(input.trim());
                        saveStateForUndo();
                        if (tree instanceof GenericTree) {
                            ((GenericTree) tree).insert(0, newValue); // Inserting as root
                        } else if (tree instanceof BinaryTree) {
                            ((BinaryTree) tree).insert(0, newValue); // Inserting as root
                        } else {
                            showAlert("Unsupported Operation", "Insert operation is not supported for this tree type.");
                        }
                        drawTree();
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid integer value for the root node.");
                    }
                });
            }
        } else {
            dialog.setContentText("Enter parent value and new value (comma-separated):");
            dialog.showAndWait().ifPresent(input -> {
                try {
                    String[] values = input.split(",");
                    if (values.length == 2) {
                        int parentValue = Integer.parseInt(values[0].trim());
                        saveStateForUndo();
                        int newValue = Integer.parseInt(values[1].trim());
                        TreeNode nodeToInsert = searchNodeForDeletion(parentValue);
                        if (tree instanceof GenericTree) {
                        	
                        	if (nodeToInsert != null) {
                                // Add a delay to ensure the animation completes before deletion
                                if (timeline != null) {
                                    timeline.setOnFinished(e -> {
                                    	((GenericTree) tree).insert(parentValue, newValue);
                                        highlightedNodes.clear();
                                        permanentlyHighlightedNodes.clear();
                                        highlightedEdges.clear();
                                        drawTree();
                                    });
                                } else {
                                	((GenericTree) tree).insert(parentValue, newValue);
                                    
                                }
                            } else {
                                showAlert("Node Not Found", "The node was not found.");
                            }
                            
                        } else if (tree instanceof BinaryTree) {
                        	if (nodeToInsert != null) {
                                // Add a delay to ensure the animation completes before deletion
                                if (timeline != null) {
                                    timeline.setOnFinished(e -> {
                                    	((BinaryTree) tree).insert(parentValue, newValue);
                                        highlightedNodes.clear();
                                        permanentlyHighlightedNodes.clear();
                                        highlightedEdges.clear();
                                        
                                        drawTree();
                                    });
                                } else {
                                	((BinaryTree) tree).insert(parentValue, newValue);
                                    
                                }
                            } else {
                                showAlert("Node Not Found", "The node was not found.");
                            }
                            
                        } else if (tree instanceof BalancedTree) {
                        	if (nodeToInsert != null) {
                                // Add a delay to ensure the animation completes before deletion
                                if (timeline != null) {
                                    timeline.setOnFinished(e -> {
                                    	((BalancedTree) tree).insert(parentValue, newValue);
                                        highlightedNodes.clear();
                                        permanentlyHighlightedNodes.clear();
                                        highlightedEdges.clear();
                                        drawTree();
                                    });
                                } else {
                                	((BalancedTree) tree).insert(parentValue, newValue);
                                    
                                }
                            } else {
                                showAlert("Node Not Found", "The node was not found.");
                            }
                            
                        } else if (tree instanceof BalancedBinaryTree) {
                        	if (nodeToInsert != null) {
                                // Add a delay to ensure the animation completes before deletion
                                if (timeline != null) {
                                    timeline.setOnFinished(e -> {
                                    	((BalancedBinaryTree) tree).insert(parentValue, newValue);
                                        highlightedNodes.clear();
                                        permanentlyHighlightedNodes.clear();
                                        highlightedEdges.clear();
                                        drawTree();
                                    });
                                } else {
                                	((BalancedBinaryTree) tree).insert(parentValue, newValue);
                                    
                                }
                            } else {
                                showAlert("Node Not Found", "The node was not found.");
                            }
                            
                        } else {
                            showAlert("Unsupported Operation", "Insert operation is not supported for this tree type.");
                        }
                        for (int i = 0; i < highlightedNodes.size(); i++) {
                            TreeNode node = highlightedNodes.get(i);
                            if(node.getValue()==newValue) {
                            	targetNode = node;
                            }
                        }
                        drawTree();
                    } else {
                        showAlert("Invalid Input", "Please enter two comma-separated values.");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid integer values.");
                }
            });
        }
        saveCurrentState();
    }
    private double getNodePositionX(TreeNode node) {
        return node.getX();
    }

    private double getNodePositionY(TreeNode node) {
        return node.getY();
    }

    private void updateNode() {
        TextInputDialog oldDialog = new TextInputDialog();
        oldDialog.setTitle("Update Node");
        oldDialog.setHeaderText("Enter the value to be updated:");
        oldDialog.setContentText("Old Value:");

        oldDialog.showAndWait().ifPresent(oldValue -> {
            try {
                int oldIntValue = Integer.parseInt(oldValue);

                // Check if the value exists in the tree before proceeding
                TreeNode existingNode = tree.search(oldIntValue);
                if (existingNode == null) {
                    showAlert("Node Not Found", "Value " + oldIntValue + " not found in the tree.");
                    return;
                }

                TextInputDialog newDialog = new TextInputDialog();
                newDialog.setTitle("Update Node");
                newDialog.setHeaderText("Enter the new value:");
                newDialog.setContentText("New Value:");

                newDialog.showAndWait().ifPresent(newValue -> {
                    try {
                        int newIntValue = Integer.parseInt(newValue);
                        saveStateForUndo();
                        tree.update(oldIntValue, newIntValue);
                        drawTree();
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid integer for the new value.");
                    }
                });
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer for the old value.");
            }
        });
        saveCurrentState();
    }

    private TreeNode searchNodeForDeletion(int targetValue) {
        ChoiceDialog<String> searchTypeDialog = new ChoiceDialog<>("BFS", "DFS", "IDDFS");
        searchTypeDialog.setTitle("Search Nodes");
        searchTypeDialog.setHeaderText("Select search method");
        searchTypeDialog.setContentText("Choose search method:");

        final TreeNode[] foundNode = {null};
        
     // Set dialog as non-modal
        searchTypeDialog.initOwner(primaryStage); // Set the owner of the dialog to the main application window
        searchTypeDialog.initModality(Modality.NONE); // Make the dialog non-modal

        searchTypeDialog.showAndWait().ifPresent(selected -> {
            highlightedNodes.clear();
            permanentlyHighlightedNodes.clear();
            highlightedEdges.clear();

            boolean found = false;
            if (selected.equals("BFS")) {
                found = bfsSearch(tree.getRoot(), targetValue);
            } else if (selected.equals("DFS")) {
                found = dfsSearch(tree.getRoot(), targetValue);
            } else if (selected.equals("IDDFS")) {
                found = iddfsSearch(tree.getRoot(), targetValue);
            }

            if (found) {
                animateSearch(targetValue);
                foundNode[0] = highlightedNodes.get(highlightedNodes.size() -1 );
            } else {
                showAlert("Search Result", "Node not found.");
            }
        });

        return foundNode[0];
    }

    private void deleteNode() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Delete Node");
        dialog.setHeaderText("Delete a node");
        dialog.setContentText("Please enter a value:");

        dialog.showAndWait().ifPresent(value -> {
            try {
                int intValue = Integer.parseInt(value);

                // Search for the node first with animation
                TreeNode nodeToDelete = searchNodeForDeletion(intValue);
                if (nodeToDelete != null) {
                    // Add a delay to ensure the animation completes before deletion
                    if (timeline != null) {
                        timeline.setOnFinished(e -> {
                            tree.delete(nodeToDelete.getValue());
                            highlightedNodes.clear();
                            permanentlyHighlightedNodes.clear();
                            highlightedEdges.clear();
                            drawTree();
                        });
                    } else {
                        tree.delete(nodeToDelete.getValue());
                        drawTree();
                    }
                } else {
                    showAlert("Node Not Found", "The node was not found.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer.");
            }
        });
        saveCurrentState();
    }

    private void searchNode() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Node");
        dialog.setHeaderText("Search for a node");
        dialog.setContentText("Please enter a value:");
        
     // Set dialog as non-modal
        dialog.initOwner(primaryStage); // Set the owner of the dialog to the main application window
        dialog.initModality(Modality.NONE); // Make the dialog non-modal

        dialog.showAndWait().ifPresent(value -> {
            try {
                int targetValue = Integer.parseInt(value);
                
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("BFS", "DFS","IDDFS");
                choiceDialog.setTitle("Search Method");
                choiceDialog.setHeaderText("Select search method");
                choiceDialog.setContentText("Choose search method:");

                choiceDialog.showAndWait().ifPresent(selected -> {
                    highlightedNodes.clear();
                    permanentlyHighlightedNodes.clear();
                    highlightedEdges.clear();
                    boolean found = false;
                    
                    if (selected.equals("BFS")) {
                        found = bfsSearch(tree.getRoot(), targetValue);
                    } else if (selected.equals("DFS")) {
                        found = dfsSearch(tree.getRoot(), targetValue);
                    }else if (selected.equals("IDDFS")) {
                        found = iddfsSearch(tree.getRoot(), targetValue);
                    }
                    
                    if (found) {
                        animateSearch(targetValue);
                    } else {
                        showAlert("Search Result", "Node not found.");
                    }
                });
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer.");
            }
        });
    }
    private boolean iddfsSearch(TreeNode root, int value) {
        if (root == null) return false;
        int depth = 0;
        while (true) {
            boolean found = dlsSearch(root, value, depth);
            if (found) return true;
            depth++;
        }
    }

    private boolean dlsSearch(TreeNode node, int value, int depth) {
        if (depth == 0) {
            highlightedNodes.add(node);
            return node.getValue() == value;
        }
        if (depth > 0) {
            for (TreeNode child : node.getChildren()) {
                if (dlsSearch(child, value, depth - 1)) {
                    highlightedNodes.add(node);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean bfsSearch(TreeNode root, int targetValue) {
        if (root == null) return false;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            highlightedNodes.add(node);
            if (node.getValue() == targetValue) {
                return true;
            }
            queue.addAll(node.getChildren());
        }
        return false;
    }

    private boolean dfsSearch(TreeNode root, int targetValue) {
        if (root == null) return false;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            highlightedNodes.add(node);
            if (node.getValue() == targetValue) {
                return true;
            }
            Collections.reverse(node.getChildren()); // To maintain correct order
            stack.addAll(node.getChildren());
        }
        return false;
    }
    
    private void animateSearch(int targetValue) {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        targetNode = null;
        for (int i = 0; i < highlightedNodes.size(); i++) {
            TreeNode node = highlightedNodes.get(i);
            
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i/1.5), e -> {
            	System.out.print(node.getValue() + " ");
                if (node.getValue() == targetValue) {
                	permanentlyHighlightedNodes.add(node);
                	
                    targetNode = node; // Set the target node
                    drawTree();
                } else {
                    permanentlyHighlightedNodes.add(node);
                    drawTree();
                }
            });
            timeline.getKeyFrames().add(keyFrame);

            if (i > 0) {
                TreeNode parentNode = highlightedNodes.get(i - 1);
                TreeNode childNode = highlightedNodes.get(i);
                double parentX = getNodePositionX(parentNode);
                double parentY = getNodePositionY(parentNode);
                double childX = getNodePositionX(childNode);
                double childY = getNodePositionY(childNode);
                Line edge = new Line(parentX, parentY, childX, childY);
                KeyFrame edgeKeyFrame = new KeyFrame(Duration.seconds(i), e -> {
                    if (permanentlyHighlightedNodes.contains(parentNode) && permanentlyHighlightedNodes.contains(childNode)) {
                        edge.setStroke(Color.RED); // Highlight the edge if both nodes are highlighted
                        highlightedEdges.add(edge);
                    }
                    drawTree();
                });
                timeline.getKeyFrames().add(edgeKeyFrame);
            }
        }
        timeline.play();
    }




    
    private void traverseNode() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("BFS", "DFS", "IDDFS");
        dialog.setTitle("Traverse Nodes");
        dialog.setHeaderText("Select traversal method");
        dialog.setContentText("Choose traversal method:");

        // Set dialog as non-modal
        dialog.initOwner(primaryStage); // Set the owner of the dialog to the main application window
        dialog.initModality(Modality.NONE); // Make the dialog non-modal

        dialog.showAndWait().ifPresent(selected -> {
            targetNode = null;
            highlightedNodes.clear();
            permanentlyHighlightedNodes.clear();
            highlightedEdges.clear(); // Clear highlighted edges
            if (selected.equals("BFS")) {
                bfsTraversal(tree.getRoot());
            } else if (selected.equals("DFS")) {
                dfsTraversal(tree.getRoot());
            } else if (selected.equals("IDDFS")) {
                iddfsTraversal(tree.getRoot());
            }
            animateTraversal();
        });
    }


    private void bfsTraversal(TreeNode root) {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            
            highlightedNodes.add(node);
            queue.addAll(node.getChildren());
        }
    }

    private void dfsTraversal(TreeNode root) {
        if (root == null) return;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            highlightedNodes.add(node);
            Collections.reverse(node.getChildren()); // To maintain correct order
            stack.addAll(node.getChildren());
        }
    }
    private void iddfsTraversal(TreeNode root) {
        if (root == null) return;
        int maxDepth = getMaxDepth(root);
        for (int depth = 0; depth <= maxDepth; depth++) {
            dfsTraversal(root, depth);
        }
    }

    private int getMaxDepth(TreeNode node) {
        if (node == null) return 0;
        int maxChildDepth = 0;
        for (TreeNode child : node.getChildren()) {
            maxChildDepth = Math.max(maxChildDepth, getMaxDepth(child));
        }
        return 1 + maxChildDepth;
    }
    private void dfsTraversal(TreeNode root, int depth) {
        dfsTraversalRecursive(root, depth, 0);
    }

    private void dfsTraversalRecursive(TreeNode node, int depth, int currentDepth) {
        if (node == null || currentDepth > depth) return;

        highlightedNodes.add(node);
        for (TreeNode child : node.getChildren()) {
            dfsTraversalRecursive(child, depth, currentDepth + 1);
        }
    }
    
    private void animateTraversal() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        for (int i = 0; i < highlightedNodes.size(); i++) {
            TreeNode node = highlightedNodes.get(i);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i), e -> {
                permanentlyHighlightedNodes.add(node);
                System.out.print(node.getValue() + " ");
                drawTree(); // Redraw tree to highlight nodes
            });
            timeline.getKeyFrames().add(keyFrame);

            if (i > 0) {
                TreeNode parentNode = highlightedNodes.get(i - 1);
                TreeNode childNode = highlightedNodes.get(i);
                KeyFrame edgeKeyFrame = new KeyFrame(Duration.seconds(i), e -> {
                    Line edge = new Line(getNodePositionX(parentNode), getNodePositionY(parentNode), getNodePositionX(childNode), getNodePositionY(childNode));
                    highlightedEdges.add(edge);
                    drawTree(); // Redraw tree to highlight edges
                });
                timeline.getKeyFrames().add(edgeKeyFrame);
            }
        }
        timeline.play();
    }
    
    private void drawTree() {
        // Clear the existing elements
        treePane.getChildren().clear();
        visitedNodes = new HashSet<>(); // Initialize the visitedNodes set

        // Clear the notification pane
        if (notificationPane != null) {
            notificationPane.getChildren().clear();
        }

        TreeNode root = tree.getRoot();
        if (root != null) {
            if (tree instanceof BinaryTree || tree instanceof BalancedBinaryTree) {
                drawTreeRecursive(root, treePane.getWidth() / 2, 30, treePane.getWidth() / 4, 50);
            } else if (tree instanceof GenericTree || tree instanceof BalancedTree) {
                drawGenericTree(root, treePane.getWidth() / 2, 30, treePane.getWidth() / 4, 50);
            }
        }

        // Create rectangle box for notifications
        if (notificationPane != null) {
            // Update the existing notificationPane with the latest content
            if (notificationText != null) {
                Rectangle notificationBox = new Rectangle(400, 200); // Adjust width and height as needed
                notificationBox.setFill(Color.WHITE);
                notificationBox.setStroke(Color.BLACK);
                notificationBox.setStrokeWidth(2);

                // Set the desired position
                notificationPane.setLayoutX(10); // Set x-coordinate
                notificationPane.setLayoutY(treePane.getHeight() - 210); // Set y-coordinate

                // Add notification text to the box
                notificationText.setFill(Color.BLACK);

                // Add new content
                notificationPane.getChildren().addAll(notificationBox, notificationText);

                // Add notificationPane to treePane if it's not already added
                if (!treePane.getChildren().contains(notificationPane)) {
                    treePane.getChildren().add(notificationPane);
                }
            }
        }
    }


    private void drawTreeRecursive(TreeNode node, double x, double y, double xOffset, double yOffset) {
        if (node == null) return;

        if (node.getX() == 0 && node.getY() == 0) {
            node.setX(x); // Set initial position if not already set
            node.setY(y);
        } else {
            x = node.getX(); // Use stored position
            y = node.getY();
        }

        for (TreeNode child : node.getChildren()) {
            double newX = x + (child == node.getChildren().get(0) ? -xOffset : xOffset);
            double newY = y + yOffset;
            if (child.getX() == 0 && child.getY() == 0) {
                child.setX(newX); // Set initial position if not already set
                child.setY(newY);
            } else {
                newX = child.getX(); // Use stored position
                newY = child.getY();
            }
            Line line = new Line(x, y, newX, newY);
            line.setStrokeWidth(3);
            if (highlightedEdges.contains(line) || 
                (permanentlyHighlightedNodes.contains(node) && permanentlyHighlightedNodes.contains(child))) {
                line.setStroke(Color.RED); // Keep the edge highlighted
            }
            treePane.getChildren().add(line);
            drawTreeRecursive(child, newX, newY, xOffset / 2, yOffset);
        }

        Circle circle = new Circle(x, y, 15);
        if (permanentlyHighlightedNodes.contains(node)) {
            circle.setFill(Color.YELLOW); // Keep the node highlighted
        }  else {
            circle.setFill(Color.WHITE);
        }
        if (node == targetNode) {
            circle.setFill(Color.AQUAMARINE); // Highlight the target node in aquamarine
        }
        circle.setStroke(Color.BLACK);
        treePane.getChildren().add(circle);

        Text text = new Text(x - 5, y + 5, String.valueOf(node.getValue()));
        treePane.getChildren().add(text);
    }
    

    private void drawGenericTree(TreeNode node, double x, double y, double xOffset, double yOffset) {
        if (node == null || visitedNodes.contains(node)) return;

        visitedNodes.add(node);  // Mark this node as visited

        if (node.getX() == 0 && node.getY() == 0) {
            node.setX(x); // Set initial position if not already set
            node.setY(y);
        } else {
            x = node.getX(); // Use stored position
            y = node.getY();
        }

        double step = 2 * xOffset / Math.max(1, node.getChildren().size() - 1);
        double currentX = x - xOffset;
        for (TreeNode child : node.getChildren()) {
            double newX = currentX;
            double newY = y + yOffset;
            if (child.getX() == 0 && child.getY() == 0) {
                child.setX(newX); // Set initial position if not already set
                child.setY(newY);
            } else {
                newX = child.getX(); // Use stored position
                newY = child.getY();
            }
            Line line = new Line(x, y, newX, newY);
            line.setStrokeWidth(3);
            if (highlightedEdges.contains(line) || 
                (permanentlyHighlightedNodes.contains(node) && permanentlyHighlightedNodes.contains(child))) {
                line.setStroke(Color.RED); // Keep the edge highlighted
            }
            treePane.getChildren().add(line);
            drawGenericTree(child, newX, newY, xOffset / 2, yOffset);
            currentX += step;
        }

        Circle circle = new Circle(x, y, 15);
        if (permanentlyHighlightedNodes.contains(node)) {
            circle.setFill(Color.YELLOW); // Keep the node highlighted
        } else {
            circle.setFill(Color.WHITE);
        }
        if (node == targetNode) {
            circle.setFill(Color.AQUAMARINE); // Highlight the target node in aquamarine
        }
        circle.setStroke(Color.BLACK);
        treePane.getChildren().add(circle);

        Text text = new Text(x - 5, y + 5, String.valueOf(node.getValue()));
        treePane.getChildren().add(text);
    }



    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage); // Set the owner to the primary stage
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
 // Save the current state for undo
    private void saveStateForUndo() {
        TreeMemento memento = saveState();
        undoStack.push(memento);
        redoStack.clear();
        history.add(memento);
        currentHistoryIndex++;
    }

    // Save the current state
    private void saveCurrentState() {
        if (!isPaused) {
            TreeMemento memento = saveState();
            if (currentHistoryIndex < history.size() - 1) {
                history = history.subList(0, currentHistoryIndex + 1);
            }
            history.add(memento);
            currentHistoryIndex++;
            updateButtonStates();
        }
    }

    // Save the state and return a TreeMemento object
    private TreeMemento saveState() {
        return new TreeMemento(tree.cloneTree(), new ArrayList<>(highlightedNodes), new HashSet<>(permanentlyHighlightedNodes));
    }

    // Method to restore a previous state of the tree
    private void restoreState(TreeMemento memento) {
        this.tree = memento.getTree();
        this.highlightedNodes = new ArrayList<>(memento.getHighlightedNodes());
        this.permanentlyHighlightedNodes = new HashSet<>(memento.getPermanentlyHighlightedNodes());
        drawTree();
    }

    private static class TreeMemento {
        private Tree tree;
        private List<TreeNode> highlightedNodes;
        private Set<TreeNode> permanentlyHighlightedNodes;

        public TreeMemento(Tree tree, List<TreeNode> highlightedNodes, Set<TreeNode> permanentlyHighlightedNodes) {
            this.tree = tree;
            this.highlightedNodes = highlightedNodes;
            this.permanentlyHighlightedNodes = permanentlyHighlightedNodes;
        }

        public Tree getTree() {
            return tree;
        }

        public List<TreeNode> getHighlightedNodes() {
            return highlightedNodes;
        }

        public Set<TreeNode> getPermanentlyHighlightedNodes() {
            return permanentlyHighlightedNodes;
        }
    }
    
    
}
