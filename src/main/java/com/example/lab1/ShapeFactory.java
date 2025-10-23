package com.example.lab1;

import com.example.lab1.shapes.*;
import javafx.scene.paint.Color;

public class ShapeFactory {

    public static Shape createShape(String name, double startX, double startY, double endX, double endY, Color color, boolean filled)
    {
        if (name.equals("Треугольник"))
        {
            double baseX = Math.min(startX, endX);
            double baseY = Math.max(startY, endY);
            double w = Math.abs(endX - startX);
            return new Triangle(startX, startY, baseX, baseY, baseX + w, baseY, color, filled);        }
        else if (name.equals("Отрезок"))
        {
            return new Line(startX, startY, endX, endY, color, filled);
        }
        else if (name.equals("Круг"))
        {
            double radius = Math.hypot(endX - startX, endY - startY) / 2;
            double cx = Math.min(startX, endX);
            double cy = Math.min(startY, endY);
            return new Circle(cx, cy, radius, color, filled);
        }
        else if (name.equals("Прямоугольник"))
        {
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            double rx = Math.min(startX, endX);
            double ry = Math.min(startY, endY);
            return new Rectangle(rx, ry, width, height, color, filled);
        }
        else
        {
            return new Line(startX, startY, endX, endY, color, filled);
        }
    }
}