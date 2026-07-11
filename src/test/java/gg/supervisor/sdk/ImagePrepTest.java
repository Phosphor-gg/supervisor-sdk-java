package gg.supervisor.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.junit.jupiter.api.Test;

class ImagePrepTest {

    @Test
    void downscalesLandscapeRgbTo1280LongestEdge() throws IOException {
        String prepared = ImagePrep.prepareImage(pngBase64(newRgbImage(3000, 2000)));

        byte[] bytes = Base64.getDecoder().decode(prepared);
        BufferedImage result = ImageIO.read(new ByteArrayInputStream(bytes));
        assertNotNull(result);
        assertEquals(1280, Math.max(result.getWidth(), result.getHeight()));
        assertEquals(1280, result.getWidth());
        assertAspectPreserved(3000, 2000, result.getWidth(), result.getHeight());
        assertEquals("jpeg", formatName(bytes).toLowerCase());
    }

    @Test
    void downscalesWideArgbWithTransparencyTo1280LongestEdge() throws IOException {
        String prepared = ImagePrep.prepareImage(pngBase64(newArgbImageWithTransparency(4000, 1000)));

        byte[] bytes = Base64.getDecoder().decode(prepared);
        BufferedImage result = ImageIO.read(new ByteArrayInputStream(bytes));
        assertNotNull(result);
        assertEquals(1280, Math.max(result.getWidth(), result.getHeight()));
        assertEquals(1280, result.getWidth());
        assertAspectPreserved(4000, 1000, result.getWidth(), result.getHeight());
        assertEquals("jpeg", formatName(bytes).toLowerCase());
    }

    @Test
    void smallImageKeepsItsDimensions() throws IOException {
        String prepared = ImagePrep.prepareImage(pngBase64(newRgbImage(64, 64)));

        BufferedImage result = ImageIO.read(new ByteArrayInputStream(Base64.getMimeDecoder().decode(prepared)));
        assertNotNull(result);
        assertEquals(64, result.getWidth());
        assertEquals(64, result.getHeight());
    }

    @Test
    void nonBase64GarbageIsReturnedUnchanged() {
        String garbage = "###";
        assertSame(garbage, ImagePrep.prepareImage(garbage));
    }

    @Test
    void base64OfNonImageBytesIsReturnedUnchanged() {
        String notAnImage = Base64.getEncoder().encodeToString("hello".getBytes(StandardCharsets.UTF_8));
        assertSame(notAnImage, ImagePrep.prepareImage(notAnImage));
    }

    private static void assertAspectPreserved(int srcW, int srcH, int outW, int outH) {
        double expectedH = (double) srcH * outW / srcW;
        assertTrue(Math.abs(expectedH - outH) <= 1,
                "aspect not preserved: expected height ~" + expectedH + " but was " + outH);
    }

    private static BufferedImage newRgbImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(Color.BLUE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.ORANGE);
            graphics.fillOval(width / 4, height / 4, width / 2, height / 2);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static BufferedImage newArgbImageWithTransparency(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            // Left half stays fully transparent; right half is opaque red.
            graphics.setColor(Color.RED);
            graphics.fillRect(width / 2, 0, width - width / 2, height);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static String pngBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    private static String formatName(byte[] bytes) throws IOException {
        try (ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            assertTrue(readers.hasNext(), "no ImageReader recognises the encoded bytes");
            return readers.next().getFormatName();
        }
    }
}
