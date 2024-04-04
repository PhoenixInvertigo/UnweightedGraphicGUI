package Project4;

import java.util.Objects;

/**
 * Author: Ryo Kilgannon
 * Date: 2/28/24
 *
 * Class for storing vertex information. Contains
 * the x and y coordinates, as well as the name of
 * the vertex.
 */
public class Vertex {

    private double x;
    private double y;

    private String name;

    public Vertex(double x, double y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Double.compare(x, vertex.x) == 0 && Double.compare(y, vertex.y) == 0 && Objects.equals(name, vertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }
}
