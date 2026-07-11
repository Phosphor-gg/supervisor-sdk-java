package gg.supervisor.sdk;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/** Client-side preprocessing of base64 images before they are sent to the moderation API. */
public final class ImagePrep {
    private static final int MAX_EDGE = 1280;
    private static final float JPEG_QUALITY = 0.85f;

    private ImagePrep() {
    }

    /**
     * Prepare a base64-encoded image for upload: strip an optional {@code data:...;base64,}
     * prefix, downscale so the longest edge is at most 1280 pixels (aspect preserved, never
     * upscaled), flatten any transparency onto white, and re-encode as JPEG at quality 0.85.
     * <p>
     * If the input cannot be decoded (e.g. WebP, or not an image at all) it is returned
     * unchanged. If no resize was needed and the JPEG re-encode is not smaller than the
     * original bytes, the original input is returned unchanged.
     *
     * @param imageB64 the base64-encoded image, optionally with a data URI prefix
     * @return the processed image as raw standard base64, or the input unchanged
     */
    public static String prepareImage(String imageB64) {
        if (imageB64 == null || imageB64.isBlank()) {
            return imageB64;
        }
        try {
            String payload = imageB64;
            if (payload.startsWith("data:")) {
                int comma = payload.indexOf(',');
                if (comma >= 0) {
                    payload = payload.substring(comma + 1);
                }
            }

            byte[] original = Base64.getMimeDecoder().decode(payload);
            BufferedImage source = ImageIO.read(new ByteArrayInputStream(original));
            if (source == null) {
                return imageB64;
            }

            int width = source.getWidth();
            int height = source.getHeight();
            boolean resize = Math.max(width, height) > MAX_EDGE;
            int targetWidth = width;
            int targetHeight = height;
            if (resize) {
                if (width >= height) {
                    targetWidth = MAX_EDGE;
                    targetHeight = Math.max(1, Math.round((float) height * MAX_EDGE / width));
                } else {
                    targetHeight = MAX_EDGE;
                    targetWidth = Math.max(1, Math.round((float) width * MAX_EDGE / height));
                }
            }

            byte[] jpeg = encodeJpeg(render(source, targetWidth, targetHeight));
            if (!resize && jpeg.length >= original.length) {
                return imageB64;
            }
            return Base64.getEncoder().encodeToString(jpeg);
        } catch (Exception e) {
            return imageB64;
        }
    }

    /**
     * Render the source onto a white-filled RGB canvas at the target size, downscaling in
     * halving steps (progressive bilinear) when the reduction exceeds 2x.
     */
    private static BufferedImage render(BufferedImage source, int targetWidth, int targetHeight) {
        BufferedImage current = source;
        int width = source.getWidth();
        int height = source.getHeight();
        while (width > targetWidth * 2 || height > targetHeight * 2) {
            width = Math.max(targetWidth, width / 2);
            height = Math.max(targetHeight, height / 2);
            current = draw(current, width, height);
        }
        if (current == source || width != targetWidth || height != targetHeight) {
            current = draw(current, targetWidth, targetHeight);
        }
        return current;
    }

    private static BufferedImage draw(BufferedImage source, int width, int height) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        return target;
    }

    private static byte[] encodeJpeg(BufferedImage image) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(JPEG_QUALITY);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ImageOutputStream stream = ImageIO.createImageOutputStream(out)) {
            writer.setOutput(stream);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
        return out.toByteArray();
    }
}
