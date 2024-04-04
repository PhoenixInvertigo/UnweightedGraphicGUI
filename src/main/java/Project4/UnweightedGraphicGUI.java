package Project4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Author: Ryo Kilgannon
 * Date: 2/28/24
 *
 * This is a class for generating the GUI of this
 * application. It allows for the clicking of the
 * central pane to add vertices, allows edges to
 * be added between vertices, then runs calculations
 * about the created graph: whether it has cycles and
 * is connected, and what order the nodes are returned
 * in a depth-first search or a breadth-first search,
 * starting from Vertex A.
 */
public class UnweightedGraphicGUI extends Application {

    @Override
    public void start(Stage primaryStage){

        //The necessary panes
        BorderPane mainPane = new BorderPane();
        FlowPane topPane = new FlowPane();
        FlowPane bottomPane = new FlowPane();
        GraphPane centerPane = new GraphPane();

        //Setup for the topPane
        Button addEdge = new Button("Add Edge");
        Text v1 = new Text("Vertex 1");
        TextField v1Field = new TextField();
        Text v2 = new Text("Vertex 2");
        TextField v2Field = new TextField();

        v1Field.setPrefColumnCount(2);
        v2Field.setPrefColumnCount(2);

        topPane.getChildren().addAll(addEdge, v1, v1Field, v2, v2Field);
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(15);
        topPane.setPadding(new Insets(20, 0, 0, 0));

        //Setup for the bottomPane
        Button isConnected = new Button("Is Connected?");
        Button hasCycles = new Button("Has Cycles?");
        Button dfSearch = new Button("Depth-First Search");
        Button bfSearch = new Button("Breadth-First Search");

        TextField response = new TextField();
        response.setPrefColumnCount(35);

        bottomPane.getChildren().addAll(isConnected, hasCycles, dfSearch, bfSearch, response);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setHgap(15);
        bottomPane.setVgap(15);
        bottomPane.setPadding(new Insets(0, 0, 20, 0));

        //EventHandlers for the buttons
        addEdge.setOnMouseClicked(e -> {
            try {
                centerPane.addEdge(v1Field.getText(), v2Field.getText());
                response.setText("Edge Added");
            } catch(IllegalArgumentException ex){
                response.setText(ex.getMessage());
            }
        });

        isConnected.setOnMouseClicked(e -> {
            if(centerPane.getGraph().isConnected()){
                response.setText("The graph is connected");
            } else {
                response.setText("The graph is not connected");
            }
        });

        hasCycles.setOnMouseClicked(e -> {
            if(centerPane.getGraph().getHasCycles()){
                response.setText("The graph has cycles");
            } else {
                response.setText("The graph doesn't have cycles");
            }
        });

        dfSearch.setOnMouseClicked(e -> {
            ArrayList<Vertex> list = centerPane.getGraph().depthFirstSearch();

            String result = "";

            for(Vertex v : list){
                result += v.getName() + " ";
            }

            response.setText(result);
        });

        bfSearch.setOnMouseClicked(e -> {
            ArrayList<Vertex> list = centerPane.getGraph().breadthFirstSearch();

            String result = "";

            for(Vertex v : list){
                result += v.getName() + " ";
            }

            response.setText(result);
        });


        //Combination of the parts
        mainPane.setTop(topPane);
        mainPane.setBottom(bottomPane);
        mainPane.setCenter(centerPane);

        //Display of the program
        Scene scene = new Scene(mainPane, 500, 500);
        primaryStage.setTitle("UnweightedGraphGUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}