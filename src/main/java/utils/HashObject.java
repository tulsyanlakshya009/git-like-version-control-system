package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;

/**
 * This class is responsible for handling the 'hash-object' command in a Git-like system.
 * It computes the SHA-1 hash of a file's content and stores it in the Git object store.
 */
public class HashObject {
    public void runHashObject(String[] args) {
        // Ensure the command is 'hash-object' and the correct number of arguments are provided
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
            String header = "blob " + content.length + "\0";  // Git blob format
            byte[] fullContent = concatenate(header.getBytes(), content);
            
            String sha1 = computeHash(fullContent);  // Compute SHA-1 of the correct format
        
            String objectFolder = ".git/objects/" + sha1.substring(0, 2);
            String objectFile = objectFolder + "/" + sha1.substring(2);
            new File(objectFolder).mkdirs();
            try (FileOutputStream fos = new FileOutputStream(objectFile);
                DeflaterOutputStream dos = new DeflaterOutputStream(fos)) {
                dos.write(fullContent);  // Store the correct content
            }
            System.out.println(sha1);  // Output only SHA-1
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String computeHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }

    private static byte[] concatenate(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
