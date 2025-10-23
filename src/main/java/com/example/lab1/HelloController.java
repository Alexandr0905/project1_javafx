package com.example.lab1;

import com.example.lab1.Tool;
import com.example.lab1.shapes.*;
import com.example.lab1.ShapeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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

                boolean filled = "Заполненный".equals(type_figures.getValue());
                previewShape = ShapeFactory.createShape(selected, startX, startY, endX, endY, selectedColor, filled);

//                switch (selected) {
//                    case "Отрезок" -> {
//                        if (type_selected == "Заполненный") {
//                            previewShape = new Line(startX, startY, endX, endY, selectedColor);
//                        }
//                    }
//                    case "Круг" -> {
//                        double radius = Math.hypot(endX - startX, endY - startY) / 2;
//                        double cx = Math.min(startX, endX);
//                        double cy = Math.min(startY, endY);
//
//                        boolean filled = "Заполненный".equals(type_figures.getValue());
//                        previewShape = new Circle(cx, cy, radius, selectedColor, filled);
//                    }
//                    case "Прямоугольник" -> {
//                        double width = Math.abs(endX - startX);
//                        double height = Math.abs(endY - startY);
//                        double rx = Math.min(startX, endX);
//                        double ry = Math.min(startY, endY);
//
//                        boolean filled = "Заполненный".equals(type_figures.getValue());
//                        previewShape = new Rectangle(rx, ry, width, height, selectedColor, filled);
//                    }
//                    case "Треугольник" -> {
//                        double baseX = Math.min(startX, endX);
//                        double baseY = Math.max(startY, endY);
//                        double width = Math.abs(endX - startX);
//
//                        boolean filled = "Заполненный".equals(type_figures.getValue());
//                        previewShape = new Triangle(startX, startY, baseX, baseY, baseX + width, baseY, selectedColor, filled);
//                    }
//                }

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
                        boolean filled = false;
                        finalShape = new Line(startX, startY, endX, endY, selectedColor, filled);
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

    @FXML
    protected void onSaveToJson()
    {
        List<ShapeData> shapeDataList = new ArrayList<>();

        for (Shape s : shapes)
        {
            ShapeData data = new ShapeData();
            data.type = s.getType();
            data.color = s.getColor().toString();
            data.filled = s.isFilled();

            if (s instanceof Line line)
            {
                data.x = line.getStartX();
                data.y = line.getY();
                data.x2 = line.getEndX();
                data.y2 = line.getEndY();
            }
            else if (s instanceof Circle c) {
                data.x = c.getX();
                data.y = c.getY();
                data.radius = c.getRadius();
            } else if (s instanceof Rectangle r) {
                data.x = r.getX();
                data.y = r.getY();
                data.width = r.getWidth();
                data.height = r.getHeight();
            } else if (s instanceof Triangle t) {
                data.x = t.getX();
                data.y = t.getY();
                data.x2 = t.getX2();
                data.y2 = t.getY2();
                data.x3 = t.getX3();
                data.y3 = t.getY3();
            }
            shapeDataList.add(data);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("shapes.json")) {
            gson.toJson(shapeDataList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoadFromJson()
    {
        try (FileReader reader = new FileReader("shapes.json"))
        {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ShapeData>>(){}.getType();
            List<ShapeData> shapeDataList = gson.fromJson(reader, listType);

            shapes.clear();

            for (ShapeData data : shapeDataList) {
                Color color = Color.valueOf(data.color);
                Shape s = null;

                switch (data.type) {
                    case "Отрезок" -> s = new Line(data.x, data.y, data.x2, data.y2, color, data.filled);
                    case "Круг" -> s = new Circle(data.x, data.y, data.radius, color, data.filled);
                    case "Прямоугольник" -> s = new Rectangle(data.x, data.y, data.width, data.height, color, data.filled);
                    case "Треугольник" -> s = new Triangle(data.x, data.y, data.x2, data.y2, data.x3, data.y3, color, data.filled);
                }

                if (s != null) shapes.add(s);
            }
            // перерисовываем
            GraphicsContext gc = canvass.getGraphicsContext2D();
            gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());
            for (Shape s : shapes) s.draw(gc);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}