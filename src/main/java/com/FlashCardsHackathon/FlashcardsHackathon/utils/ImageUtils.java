package com.FlashCardsHackathon.FlashcardsHackathon.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;

public class ImageUtils {

    public static final int BITE_SIZE = 4 * 1024;

    // Compress the image data
    public static byte[] compressImage(byte[] data) throws IOException {
        try {
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[BITE_SIZE];

            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }

            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Error while compressing image data", e);
        }
    }

    // Decompress the image data
    public static byte[] decompressImage(byte[] data) throws DataFormatException, IOException {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[BITE_SIZE];

            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }

            outputStream.close();
            return outputStream.toByteArray();
        } catch (DataFormatException | IOException e) {
            throw new IOException("Error while decompressing image data", e);
        }
    }
}
