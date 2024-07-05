package com.example.lab10;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RectangleController {
    private final List<Rectangle> rectangleList = new ArrayList<>();

    // Zadanie 2
    @GetMapping("/rectangle")
    public Rectangle getRectangle() {
        return new Rectangle(10, 20, 30, 40, "blue");
    }

    // Zadanie 3, 4
    @PostMapping("/add")
    public void addRectangle(@RequestBody Rectangle rectangle) {
        rectangleList.add(rectangle);
    }
    @GetMapping("/get")
    public List<Rectangle> getRectangles() {
        return rectangleList;
    }
    @GetMapping("/svg")
    public String getSvg() {
        StringBuilder svg = new StringBuilder();
        svg.append("<svg width=\"500\" height=\"500\" xmlns=\"http://www.w3.org/2000/svg\">");
        for (Rectangle rectangle : rectangleList) {
            svg.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" />",
                    rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), rectangle.getColor()
            ));
        }
        svg.append("</svg>");
        return svg.toString();
    }

    // Zadanie 5
    @GetMapping("/{index}")
    public Rectangle getRectangleIndex(@PathVariable int index) {
        return rectangleList.get(index);
    }
    @PutMapping("/{index}")
    public void editRectangle(@PathVariable int index, @RequestBody Rectangle rectangle) {
        rectangleList.set(index, rectangle);
    }
    @DeleteMapping("/{index}")
    public void deleteRectangle(@PathVariable int index) {
        rectangleList.remove(index);
    }
}
