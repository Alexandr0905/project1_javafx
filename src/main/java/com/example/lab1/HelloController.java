package com.example.lab1;

import com.example.lab1.Tool;
import com.example.lab1.shapes.*;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private double startX, startY;
    private boolean drawing = false;
    private List<Shape> shapes = new ArrayList<>(); // список нарисованных фигур

    @FXML
    private Canvas canvass;

    @FXML
    private ComboBox<String> cmb_figures;

    @FXML
    private ComboBox<String> type_figures;

    @FXML
    private ColorPicker clr;

    @FXML
    private ToggleButton btnCursor;

    @FXML
    private ToggleButton btnBrush;

    @FXML
    private Shape selectedShape = null;
    private double offsetX, offsetY;

    @FXML
    private Tool currentTool = Tool.BRUSH; // по умолчанию кисть

    @FXML
    public void initialize() {
        cmb_figures.getItems().addAll("Отрезок", "Круг", "Прямоугольник", "Треугольник");
        type_figures.getItems().addAll("Заполненный", "Пустой");

        ToggleGroup toolsGroup = new ToggleGroup();
        btnCursor.setToggleGroup(toolsGroup);
        btnBrush.setToggleGroup(toolsGroup);
        btnBrush.setSelected(true);

        toolsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle == btnCursor) {
                        currentTool = Tool.CURSOR;
                    } else if (newToggle == btnBrush) {
                        currentTool = Tool.BRUSH;
                    }
        });

        GraphicsContext gc = canvass.getGraphicsContext2D();

        canvass.setOnMousePressed(e -> {
            if (currentTool == Tool.BRUSH) {
                startX = e.getX();
                startY = e.getY();
                drawing = true;
            } else if (currentTool == Tool.CURSOR) {
                for (Shape s : shapes)
                {
                    if (s.contains(e.getX(), s.getX()))
                    {
                        selectedShape = s;
                        offsetX = e.getX() - s.getX();
                        offsetY = e.getY() - s.getY();
                        break;
                    }
                }
            }
        });

        canvass.setOnMouseDragged(e -> {
            if (currentTool == Tool.BRUSH) {
                if (!drawing) return;

                double endX = e.getX();
                double endY = e.getY();
                Color selectedColor = clr.getValue();
                String selected = cmb_figures.getValue();
                String type_selected = type_figures.getValue();

                Shape previewShape = null;

                if (selected == null) return;

                switch (selected) {
                    case "Отрезок" -> {
                        if (type_selected == "Заполненный") {
                            previewShape = new Line(startX, startY, endX, endY, selectedColor);
                        }
                    }
                    case "Круг" -> {
                        double radius = Math.hypot(endX - startX, endY - startY) / 2;
                        double cx = Math.min(startX, endX);
                        double cy = Math.min(startY, endY);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        previewShape = new Circle(cx, cy, radius, selectedColor, filled);
                    }
                    case "Прямоугольник" -> {
                        double width = Math.abs(endX - startX);
                        double height = Math.abs(endY - startY);
                        double rx = Math.min(startX, endX);
                        double ry = Math.min(startY, endY);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        previewShape = new Rectangle(rx, ry, width, height, selectedColor, filled);
                    }
                    case "Треугольник" -> {
                        double baseX = Math.min(startX, endX);
                        double baseY = Math.max(startY, endY);
                        double width = Math.abs(endX - startX);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        previewShape = new Triangle(startX, startY, baseX, baseY, baseX + width, baseY, selectedColor, filled);
                    }
                }

                gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());

                for (Shape s : shapes) {
                    s.draw(gc);
                }

                if (previewShape != null) {
                    previewShape.draw(gc);
                }
            }
            else if (currentTool == Tool.CURSOR)
            {
                if (selectedShape != null) {
                    selectedShape.setX(e.getX() - offsetX);
                    selectedShape.setY(e.getY() - offsetY);

                    gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());
                    for (Shape s : shapes) {
                        s.draw(gc);
                    }
                }
            }
        });

        canvass.setOnMouseReleased(e -> {
            if (currentTool == Tool.BRUSH) {
                drawing = false;

                double endX = e.getX();
                double endY = e.getY();
                Color selectedColor = clr.getValue();
                String selected = cmb_figures.getValue();

                Shape finalShape = null;

                if (selected == null) return;

                switch (selected) {
                    case "Отрезок" -> {
                        finalShape = new Line(startX, startY, endX, endY, selectedColor);
                    }
                    case "Круг" -> {
                        double radius = Math.hypot(endX - startX, endY - startY) / 2;
                        double cx = Math.min(startX, endX);
                        double cy = Math.min(startY, endY);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        finalShape = new Circle(cx, cy, radius, selectedColor, filled);
                    }
                    case "Прямоугольник" -> {
                        double width = Math.abs(endX - startX);
                        double height = Math.abs(endY - startY);
                        double rx = Math.min(startX, endX);
                        double ry = Math.min(startY, endY);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        finalShape = new Rectangle(rx, ry, width, height, selectedColor, filled);
                    }
                    case "Треугольник" -> {
                        double baseX = Math.min(startX, endX);
                        double baseY = Math.max(startY, endY);
                        double width = Math.abs(endX - startX);

                        boolean filled = "Заполненный".equals(type_figures.getValue());
                        finalShape = new Triangle(startX, startY, baseX, baseY, baseX + width, baseY, selectedColor, filled);
                    }
                }

                if (finalShape != null) {
                    shapes.add(finalShape);
                }
            }
            else if (currentTool == Tool.CURSOR)
            {
                selectedShape = null;
            }
        });
    }

    @FXML
    protected void onClearCanvas() {
        GraphicsContext gc = canvass.getGraphicsContext2D();
        gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());
        shapes.clear();
    }
}