package utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
/**
 * This class is responsible for handling the 'cat-file' command in a Git-like system.
 * It retrieves and decompresses the content of a Git object file based on its hash.
 */
public class CatFile {
    public void runCatFile(String[] args) {
        // Ensure the command is 'cat-file' and the correct number of arguments are provided
        if (args.length < 3 || !args[0].equals("cat-file") || !args[1].equals("-p") || args[1].equals("-help")) {
            System.out.println("Usage: cat-file -p <object-hash>");
            return;
        }
        try {
            final String path = getFilePath(args[3]);
            // Read the object file from the .git/objects directory
            byte[] data = Files.readAllBytes(new File(path).toPath());
            String decompressedData = inflateData(data);
            int start = decompressedData.indexOf('\0');
            System.out.print(decompressedData.substring(start + 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataFormatException e) {
            throw new RuntimeException("Error decompressing data: " + e.getMessage(), e);
        }
    }

    private String getFilePath(String objectHash) {
        String objectFolder = objectHash.substring(0, 2);
        String objectFile = objectHash.substring(2);
        return ".git/objects/" + objectFolder + "/" + objectFile;
    }

    private String inflateData(byte[] data) throws DataFormatException {
        // Decompress the data using zlib
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        // Create a ByteArrayOutputStream to hold the decompressed data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        // Ensure the inflater is properly ended
        inflater.end();
        return outputStream.toString(java.nio.charset.StandardCharsets.UTF_8);
    }
}
