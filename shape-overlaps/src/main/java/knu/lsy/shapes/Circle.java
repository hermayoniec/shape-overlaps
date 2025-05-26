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
            // 🔵 Circle–Circle detection
            double distance = this.center.distanceTo(c.center);
            return distance < (this.radius + c.radius);
        }

        // 🔵 Circle–Polygon detection (RegularPolygon or IrregularPolygon)
        List<Point> vertices = other.getVertices();

        // Caso 1: algún vértice dentro del círculo
        for (Point p : vertices) {
            if (this.center.distanceTo(p) <= this.radius) {
                return true;
            }
        }

        // Caso 2: algún borde del polígono intersecta con el círculo
        for (int i = 0; i < vertices.size(); i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % vertices.size());
            if (edgeIntersectsCircle(p1, p2)) {
                return true;
            }
        }

        return false;
    }

    // Función auxiliar: detección de intersección entre un borde y el círculo
    private boolean edgeIntersectsCircle(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();

        double fx = p1.getX() - center.getX();
        double fy = p1.getY() - center.getY();

        double a = dx * dx + dy * dy;
        double b = 2 * (fx * dx + fy * dy);
        double c = fx * fx + fy * fy - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return false; // No intersection
        }

        discriminant = Math.sqrt(discriminant);
        double t1 = (-b - discriminant) / (2 * a);
        double t2 = (-b + discriminant) / (2 * a);

        return (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1);
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