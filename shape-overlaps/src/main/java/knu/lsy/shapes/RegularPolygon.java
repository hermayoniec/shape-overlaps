package knu.lsy.shapes;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class RegularPolygon extends Shape {
    private int sides;
    private double rotationAngle;
    private List<Point> vertices;

    public RegularPolygon(Point center, double radius, int sides, double rotationAngle) {
        super(center, radius);
        this.sides = sides;
        this.rotationAngle = rotationAngle;
        this.vertices = generateVertices();
    }

    private List<Point> generateVertices() {
        List<Point> points = new ArrayList<>();
        double angleStep = 2 * Math.PI / sides;

        for (int i = 0; i < sides; i++) {
            double angle = angleStep * i + rotationAngle;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            points.add(new Point(x, y));
        }

        return points;
    }

    @Override
    public boolean overlaps(Shape other) {
        // Si el otro también es un polígono, usamos SAT
        List<Point> thisVertices = this.getVertices();
        List<Point> otherVertices = other.getVertices();

        // SAT: usar normales (vectores perpendiculares a los lados) como ejes
        List<Point> axes = getNormals(thisVertices);
        axes.addAll(getNormals(otherVertices));

        for (Point axis : axes) {
            Projection p1 = projectOntoAxis(thisVertices, axis);
            Projection p2 = projectOntoAxis(otherVertices, axis);
            if (!p1.overlaps(p2)) {
                return false; // Separa → no hay colisión
            }
        }

        return true; // No hubo separación → colisión
    }


    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "regularPolygon");
        json.put("id", id);
        json.put("center", center.toJSON());
        json.put("radius", radius);
        json.put("sides", sides);
        json.put("rotationAngle", rotationAngle);
        json.put("color", color);

        JSONArray verticesArray = new JSONArray();
        for (Point vertex : vertices) {
            verticesArray.put(vertex.toJSON());
        }
        json.put("vertices", verticesArray);

        return json;
    }

    @Override
    public String getShapeType() {
        return "regularPolygon";
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }
    // Proyección en un eje (usado en SAT)
    private static class Projection {
        double min, max;

        Projection(double min, double max) {
            this.min = min;
            this.max = max;
        }

        boolean overlaps(Projection other) {
            return !(this.max < other.min || other.max < this.min);
        }
    }

    // Devuelve una lista de vectores normales (ejes) para SAT
    private List<Point> getNormals(List<Point> vertices) {
        List<Point> normals = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % vertices.size());

            // Vector del lado
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();

            // Vector normal (perpendicular)
            Point normal = new Point(-dy, dx);
            normals.add(normalize(normal));
        }
        return normals;
    }

    // Proyecta un polígono sobre un eje
    private Projection projectOntoAxis(List<Point> vertices, Point axis) {
        double min = dotProduct(vertices.get(0), axis);
        double max = min;

        for (int i = 1; i < vertices.size(); i++) {
            double projection = dotProduct(vertices.get(i), axis);
            if (projection < min) min = projection;
            if (projection > max) max = projection;
        }

        return new Projection(min, max);
    }

    // Producto punto
    private double dotProduct(Point p1, Point p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    // Normaliza un vector
    private Point normalize(Point p) {
        double length = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(p.getX() / length, p.getY() / length);
    }
}