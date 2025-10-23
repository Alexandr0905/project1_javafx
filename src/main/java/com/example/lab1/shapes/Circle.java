package com.example.lab1.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Shape {
    private double radius;
    private boolean filled;

    public Circle(double x, double y, double radius, Color color, boolean filled) {
        super(x, y, color, filled);
        this.radius = radius;
        this.filled = filled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        if (filled) {
            gc.fillOval(x, y, radius * 2, radius * 2);
        } else {
            gc.strokeOval(x, y, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean contains(double px, double py)
    {
        double dx = px - x;
        double dy = py - y;
        return dx * dx + dy * dy <= radius *radius;
    }

    @Override
    public String getType() {
        return "Круг";
    }

    public double getRadius()
    {
        return radius;
    }
}