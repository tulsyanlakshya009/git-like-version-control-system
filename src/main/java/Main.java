import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import utils.CatFile;
import utils.HashObject;

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
        CatFile catFile = new CatFile();
        catFile.runCatFile(args);
      }
      case ("hash-object") -> {
        HashObject hashObject = new HashObject();
        hashObject.runHashObject(args);
      }
      default -> System.out.println("Unknown command: " + command);
    }
  }
}
