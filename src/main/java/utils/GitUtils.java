package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.*;


// Common utilities for Git operations

public class GitUtils {
    
    // Prevent instantiation
    private GitUtils() {}

    // Hashing Utilities
    
    public static String computeSHA1(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    public static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return bytes;
    }
    
    // Compression Utilities
    
    public static byte[] compressData(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(bos)) {
            dos.write(data);
        }
        return bos.toByteArray();
    }
    
    public static String decompressData(byte[] compressedData) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        
        inflater.end();
        return outputStream.toString(StandardCharsets.UTF_8);
    }
    
    // File/Object Utilities
    
    public static String getObjectPath(String objectHash) {
        String objectFolder = objectHash.substring(0, 2);
        String objectFile = objectHash.substring(2);
        return ".git/objects/" + objectFolder + "/" + objectFile;
    }
    
    public static void writeGitObject(String objectHash, byte[] content) throws IOException {
        String objectDirPath = ".git/objects/" + objectHash.substring(0, 2);
        String objectFilePath = objectDirPath + "/" + objectHash.substring(2);
        
        File objectDir = new File(objectDirPath);
        if (!objectDir.exists()) {
            objectDir.mkdirs();
        }
        
        try (FileOutputStream fos = new FileOutputStream(objectFilePath);
            DeflaterOutputStream dos = new DeflaterOutputStream(fos)) {
            dos.write(content);
        }
    }
    
    public static byte[] readGitObject(String objectHash) throws IOException {
        String path = getObjectPath(objectHash);
        return Files.readAllBytes(new File(path).toPath());
    }
    
    //Data Manipulation
    public static byte[] concatenateBytes(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        
        byte[] result = new byte[totalLength];
        int currentPos = 0;
        
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentPos, array.length);
            currentPos += array.length;
        }
        
        return result;
    }
    
    public static byte[] createGitHeader(String type, int length) {
        String header = type + " " + length + "\0";
        return header.getBytes(StandardCharsets.UTF_8);
    }
}