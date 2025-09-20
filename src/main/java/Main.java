import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import utils.CatFile;
import utils.HashObject;
import utils.LsTree;
import utils.WriteTree;
import utils.CommitTree;
import utils.Clone;

public class Main {
  static {
        // Configure logging BEFORE any classes load
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.eclipse.jgit", "error");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");
        System.setProperty("org.slf4j.simpleLogger.showShortLogName", "true");
    }
  public static void main(String[] args){
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info"); // Set default log level
        
    // Optional: Additional SLF4J SimpleLogger configurations
    System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
    System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "yyyy-MM-dd HH:mm:ss");

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
      } case "cat-file" -> {
        CatFile catFile = new CatFile();
        catFile.runCatFile(args);
      } case ("hash-object") -> {
        HashObject hashObject = new HashObject();
        hashObject.runHashObject(args);
      } case ("ls-tree") -> {
        LsTree lsTree = new LsTree();
        lsTree.runLsTree(args);
      } case ("write-tree") -> {
        WriteTree writeTree = new WriteTree();
        try {
          String treeHash = writeTree.runWriteTree(new File("."));
          System.out.println(treeHash);
        } catch (IOException e) {
          System.err.println("An error occurred: " + e.getMessage());
        }
      } case ("commit-tree") -> {
        CommitTree commitTree = new CommitTree();
        commitTree.runCommitTree(args);
      } case ("clone") -> {
        Clone clone = new Clone();
        clone.runClone(args);
      }
      default -> System.out.println("Unknown command: " + command);
    }
  }
}
