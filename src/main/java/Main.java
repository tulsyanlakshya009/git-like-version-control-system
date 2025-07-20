import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Main {
  public static void main(String[] args){
    System.err.println("Logs from your program will appear here!");
    final String command = args[0];
    
    switch (command) {
      case "init" -> {
        final File root = new File(".git");
        new File(root, "objects").mkdirs();
        new File(root, "refs").mkdirs();
        final File head = new File(root, "HEAD");
    
        try {
          head.createNewFile();
          Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
          System.out.println("Initialized git directory");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      case "cat-file" -> {
        // get the object hash from the command line arguments
        final String objectHash = args[2];
        final String objectFolder = objectHash.substring(0, 2);
        final String objectFile = objectHash.substring(2);
        try {
          // read the object file from the .git/objects directory
          byte[] data = Files.readAllBytes(new File(".git/objects/" + objectFolder + "/" + objectFile).toPath());
          // decompress the data using zlib
          Inflater inflater = new Inflater();
          inflater.setInput(data);
          // create a ByteArrayOutputStream to hold the decompressed data
          try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)){
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
              int count = inflater.inflate(buffer);
              outputStream.write(buffer, 0, count);
            }
            // print the decompressed data
            String decompressedData = outputStream.toString("UTF-8");
            int start = decompressedData.indexOf('\0');
            System.out.print(decompressedData.substring(start+1));
          } catch (DataFormatException e) {
            throw new RuntimeException(e);
          } finally {
            inflater.end();
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      default -> System.out.println("Unknown command: " + command);
    }
  }
}
