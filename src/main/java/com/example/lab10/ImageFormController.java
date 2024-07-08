package com.example.lab10;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
public class ImageFormController {
    private BufferedImage image;
    private String base;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("image")
    public String show(Model model) {
        model.addAttribute("image", base);
        return "image";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("factor") int factor, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "An error occurred");
            return "index";
        }
        try {
            byte[] imageBytes1 = file.getBytes();
            BufferedImage bf = ImageIO.read(new ByteArrayInputStream(imageBytes1));
            incBrightness(bf, factor);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bf, "png", baos);
            byte[] imageBytes2 = baos.toByteArray();

            base = Base64.getEncoder().encodeToString(imageBytes2);
            model.addAttribute("image", base);
            return "image";
        } catch (IOException e) {
            model.addAttribute("message","An error occurred: " + e.getMessage());
            return "index";
        }
    }

    public void incBrightness(BufferedImage image, int factor) {
        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                int pixel = image.getRGB(x, y);
                int mask = 255;
                int blue = pixel & mask;
                int green = (pixel >> 8) & mask;
                int red = (pixel >> 16) & mask;
                blue = Math.min(255, blue + factor);
                green = Math.min(255, green + factor);
                red = Math.min(255, red + factor);
                int newPixel = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newPixel);
            }
        }
    }


}
