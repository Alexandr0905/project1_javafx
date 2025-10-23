package com.example.lab1.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Shape {
    private double x2, y2, x3, y3;
    private boolean filled;

    public Triangle(double x, double y, double x2, double y2, double x3, double y3, Color color, boolean filled) {
        super(x, y, color, filled);
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.filled = filled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        if (filled) {
            gc.fillPolygon(new double[]{x, x2, x3}, new double[]{y, y2, y3}, 3);
        }
        else {
            gc.strokePolygon(
                    new double[]{x, x2, x3},
                    new double[]{y, y2, y3},
                    3
            );
        }
    }

    @Override
    public boolean contains(double px, double py) {
        double areaOrig = Math.abs((x2 - x) * (y3 - y) - (x3 - x) * (y2 - y));
        double area1 = Math.abs((x - px) * (y2 - py) - (x2 - px) * (y - py));
        double area2 = Math.abs((x2 - px) * (y3 - py) - (x3 - px) * (y2 - py));
        double area3 = Math.abs((x3 - px) * (y - py) - (x - px) * (y3 - py));

        return Math.abs(area1 + area2 + area3 - areaOrig) < 0.01;
    }

    @Override
    public String getType() {
        return "Треугольник";
    }

    public double getX2()
    {
        return x2;
    }
    public double getY2()
    {
        return y2;
    }
    public double getX3()
    {
        return x3;
    }
    public double getY3()
    {
        return y3;
    }
}