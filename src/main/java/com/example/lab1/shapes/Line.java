package com.example.lab1.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape
{
    private double x2;
    private double y2;

    public Line(double x, double y, double x2, double y2, Color color) {
        super(x, y, color);
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        gc.strokeLine(x, y, x2, y2);
    }

    @Override
    public boolean contains(double px, double py) {
        double dx = x2 - x;
        double dy = y2 - y;
        double lengthSquared = dx * dx + dy * dy;

        if (lengthSquared == 0) return false;

        double t = ((px - x) * dx + (py - y) * dy) / lengthSquared;
        if (t < 0 || t > 1) return false;

        double projX = x + t * dx;
        double projY = y + t * dy;

        double distSq = (px - projX) * (px - projX) + (py - projY) * (py - projY);
        return distSq <= 25;
    }
}
