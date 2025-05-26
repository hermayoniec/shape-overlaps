package knu.lsy.shapes;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IrregularPolygon extends Shape {
    private List<Point> vertices;

    public IrregularPolygon(Point center, double radius, int numVertices) {
        super(center, radius);
        this.vertices = generateIrregularVertices(numVertices);
    }

    private List<Point> generateIrregularVertices(int numVertices) {
        List<Point> points = new ArrayList<>();

        List<Double> angles = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            angles.add(Math.random() * 2 * Math.PI);
        }
        Collections.sort(angles);

        for (int i = 0; i < numVertices; i++) {
            double angle = angles.get(i);
            double r = radius * (0.5 + Math.random() * 0.5);
            double x = center.getX() + r * Math.cos(angle);
            double y = center.getY() + r * Math.sin(angle);
            points.add(new Point(x, y));
        }

        return createSimpleConvexHull(points);
    }

    private List<Point> createSimpleConvexHull(List<Point> points) {
        if (points.size() <= 3) return points;

        points.sort(Comparator.comparingDouble(Point::getX));
        List<Point> hull = new ArrayList<>();

        for (Point p : points) {
            while (hull.size() >= 2 && orientation(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        int lowerSize = hull.size();
        for (int i = points.size() - 2; i >= 0; i--) {
            Point p = points.get(i);
            while (hull.size() > lowerSize && orientation(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }

        if (hull.size() > 1) hull.remove(hull.size() - 1);
        return hull;
    }

    private double orientation(Point p, Point q, Point r) {
        return (q.getX() - p.getX()) * (r.getY() - p.getY()) -
                (q.getY() - p.getY()) * (r.getX() - p.getX());
    }

    @Override
    public boolean overlaps(Shape other) {
        List<Point> vertsA = this.getVertices();
        List<Point> vertsB = other.getVertices();

        List<Point> axes = getNormals(vertsA);
        axes.addAll(getNormals(vertsB));

        for (Point axis : axes) {
            Projection pA = projectOntoAxis(vertsA, axis);
            Projection pB = projectOntoAxis(vertsB, axis);
            if (!pA.overlaps(pB)) return false;
        }
        return true;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "irregularPolygon");
        json.put("id", id);
        json.put("center", center.toJSON());
        json.put("radius", radius);
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
        return "irregularPolygon";
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    // ===================== 👇 Métodos auxiliares ========================

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

    private List<Point> getNormals(List<Point> vertices) {
        List<Point> normals = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % vertices.size());
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();
            Point normal = new Point(-dy, dx);
            normals.add(normalize(normal));
        }
        return normals;
    }

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

    private double dotProduct(Point p1, Point p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    private Point normalize(Point p) {
        double length = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(p.getX() / length, p.getY() / length);
    }
}
