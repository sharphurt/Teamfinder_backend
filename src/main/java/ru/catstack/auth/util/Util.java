package ru.catstack.auth.util;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Util {
    public static byte[] compressBytes(byte[] data) {
        var deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        var outputStream = new ByteArrayOutputStream(data.length);
        var buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        var inflater = new Inflater();
        inflater.setInput(data);
        var outputStream = new ByteArrayOutputStream(data.length);
        var buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }

    public static BufferedImage cropAndResizeImage(MultipartFile data) throws IOException {
        var image = ImageIO.read(data.getInputStream());
        var minSide = Math.min(image.getHeight(), image.getWidth());
        Scalr.crop(image, (image.getWidth() - minSide) / 2, (image.getHeight() - minSide) / 2, minSide, minSide);
        return Scalr.resize(image, 512);
    }

    public static byte[] encodeToByteArray(BufferedImage image) throws IOException {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}
