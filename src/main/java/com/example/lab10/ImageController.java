package com.example.lab10;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class ImageController {
    // Zadanie 6
    @GetMapping("/brighten")
    public String brightenImage(@RequestParam String image64, @RequestParam int brightness) {
        try {
            byte[] imageBytes1 = Base64.decodeBase64(image64);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes1));
            modifyBrightness(image, brightness);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            byte[] imageBytes2 = stream.toByteArray();
            stream.close();
            return Base64.encodeBase64String(imageBytes2);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Zadanie 7
    @GetMapping("/brighten2")
    public ResponseEntity<byte[]> brightenImageUncoded(@RequestParam String image64, @RequestParam int brightness) {
        try {
            byte[] imageBytes1 = Base64.decodeBase64(image64);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes1));
            modifyBrightness(image, brightness);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            byte[] imageBytes2 = stream.toByteArray();
            stream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            headers.set("Content-Disposition", "inline; filename=\"brightened_image.png\"");
            return new ResponseEntity<>(imageBytes2, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void modifyBrightness(BufferedImage image, int brightness) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                int alpha = (rgb >> 24) & 0xFF;
                r = clamp(r + brightness);
                g = clamp(g + brightness);
                b = clamp(b + brightness);
                rgb = (alpha << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
            }
        }
    }
    private int clamp(int a) {
        return Math.min(Math.max(a, 0), 255);
    }

}

