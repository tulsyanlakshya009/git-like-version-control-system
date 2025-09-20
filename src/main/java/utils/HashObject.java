package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HashObject {
    public void runHashObject(String[] args) {
        if (args.length < 3 || !args[0].equals("hash-object") || !args[1].equals("-w")) {
            System.out.println("Usage: hash-object -w <file>");
            return;
        }
        try {
            final String fileName = args[2];
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("File not found: " + fileName);
                return;
            }
            
            byte[] content = Files.readAllBytes(file.toPath());
            byte[] header = GitUtils.createGitHeader("blob", content.length);
            byte[] fullContent = GitUtils.concatenateBytes(header, content);
            
            String sha1 = GitUtils.computeSHA1(fullContent);
            GitUtils.writeGitObject(sha1, fullContent);
            
            System.out.println(sha1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}