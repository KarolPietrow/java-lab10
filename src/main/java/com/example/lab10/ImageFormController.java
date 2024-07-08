package com.example.lab10;

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
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes1);
            byte[] imageBytes2 = Base64.getDecoder().decode(encodedImage);
            BufferedImage bf = ImageIO.read(new ByteArrayInputStream(imageBytes2));
            incBrightness(bf, factor);
            base = Base64.getEncoder().encodeToString(imageBytes1);
            model.addAttribute("image", base);
            return "image";
        } catch (IOException e) {
            model.addAttribute("message","An error occurred: " + e.getMessage());
            return "index";
        }
    }

//    public void incBrightness(BufferedImage image, int factor) {
//        for (int x = 0; x < image.getHeight(); x++) {
//            for (int y = 0; y < image.getWidth(); y++) {
//                int pixel = image.getRGB(x, y);
//                int mask = 255;
//                int blue = pixel & mask;
//                int green = (pixel >> 8) & mask;
//                int red = (pixel >> 16) & mask;
//                blue = Math.min(255, blue + factor);
//                green = Math.min(255, green + factor);
//                red = Math.min(255, red + factor);
//                int newPixel = (red << 16) | (green << 8) | blue;
//                image.setRGB(x, y, newPixel);
//            }
//        }
//    }

    public void incBrightness(BufferedImage image, int factor) {
        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                int pixel = image.getRGB(x, y);
                pixel = brightenPixel(pixel, factor);
                image.setRGB(x, y, pixel);
            }
        }
    }


    private int brightenPixel(int pixel, int factor) {
        int mask = 255;
        int blue = pixel & mask;
        int green = (pixel >> 8) & mask;
        int red = (pixel >> 16) & mask;
        blue = brightenPixelPart(blue, factor);
        green = brightenPixelPart(red, factor);
        red = brightenPixelPart(red, factor);
        return blue + (green << 8) + (red << 16);
    }

    private int brightenPixelPart(int color, int factor) {
        color += factor;
        if (color > 255) {
            return 255;
        } else {
            return color;
        }
    }


}
