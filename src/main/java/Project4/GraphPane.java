package Project4;

import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Author: Ryo Kilgannon
 * Date: 2/28/24
 *
 * This is a class for housing the vertices and
 * edges of its backing UnweightedGraph so that
 * they can be displayed graphically.
 */
public class GraphPane extends Pane {

    UnweightedGraph graph = new UnweightedGraph();

    public GraphPane(){

        this.setOnMouseClicked(e -> {

            if(e.getButton() == MouseButton.PRIMARY){
                double x = e.getX();
                double y = e.getY();

                String vertexName = graph.addVertex(x, y);

                getChildren().add(new Circle(x, y, 5));
                getChildren().add(new Text(x - (3*vertexName.length()), y - 10, vertexName));
            }
        });
    }

    /**
     * Passes the edge vertex names down to its
     * graph, receiving back the coordinates from
     * which to draw the line.
     *
     * @param v1Name Vertex 1's name.
     * @param v2Name Vertex 2's name.
     */
    public void addEdge(String v1Name, String v2Name){
        double[] edge = graph.addEdge(v1Name, v2Name);

        getChildren().add(new Line(edge[0], edge[1], edge[2], edge[3]));
    }

    public UnweightedGraph getGraph(){
        return graph;
    }
}
