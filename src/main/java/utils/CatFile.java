package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.DataFormatException;

public class CatFile {
    public void runCatFile(String[] args) {
        if (args.length < 3 || !args[0].equals("cat-file") || !args[1].equals("-p") || args[1].equals("-help")) {
            System.out.println("Usage: cat-file -p <object-hash>");
            return;
        }
        try {
            final String path = GitUtils.getObjectPath(args[2]);
            byte[] data = Files.readAllBytes(new File(path).toPath());
            String decompressedData = GitUtils.decompressData(data);
            int start = decompressedData.indexOf('\0');
            System.out.print(decompressedData.substring(start + 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataFormatException e) {
            throw new RuntimeException("Error decompressing data: " + e.getMessage(), e);
        }
    }
}