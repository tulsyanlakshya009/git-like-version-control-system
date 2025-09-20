package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.DataFormatException;

public class LsTree {
    public void runLsTree(String[] args) {
        if (args.length < 3 || !args[0].equals("ls-tree") || !args[1].equals("--name-only")) {
            System.out.println("Usage: ls-tree --name-only <tree-sha1>");
            return;
        }
        
        try {
            String treeHash = args[2];
            String filePath = GitUtils.getObjectPath(treeHash);
            byte[] data = Files.readAllBytes(new File(filePath).toPath());
            String decompressedData = GitUtils.decompressData(data);
            String[] splitPermissionsAndFiles = decompressedData.split("\0");
            for (int i = 1; i < splitPermissionsAndFiles.length; i++) {
                int start = splitPermissionsAndFiles[i].indexOf(' ');
                if (start != -1) {
                    String fileName = splitPermissionsAndFiles[i].substring(start + 1);
                    System.out.println(fileName);
                }
            }
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Tree object not found: ", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading tree object: " + e);
        }
    }
}