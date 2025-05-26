package knu.lsy.shapes;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class Circle extends Shape {

    public Circle(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public boolean overlaps(Shape other) {
        if (other instanceof Circle c) {
            // Circle–Circle detection
            double distance = this.center.distanceTo(c.center);
            return distance < (this.radius + c.radius);
        }

        // Circle–Polygon detection
        List<Point> vertices = other.getVertices();
        for (Point p : vertices) {
            if (this.center.distanceTo(p) < this.radius) {
                return true; // vertex inside circle
            }
        }

        // Optional: check for edge intersections (not required yet)
        return false;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "circle");
        json.put("id", id);
        json.put("center", center.toJSON());
        json.put("radius", radius);
        json.put("color", color);
        return json;
    }

    @Override
    public String getShapeType() {
        return "circle";
    }

    @Override
    public List<Point> getVertices() {
        List<Point> vertices = new ArrayList<>();
        int numPoints = 32;
        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            vertices.add(new Point(x, y));
        }
        return vertices;
    }
}
