package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

public class WriteTree {
    public String runWriteTree(File dir) throws IOException {
        File[] files = dir.listFiles((d, name) -> !name.equals(".git"));
        if (files == null) return null;
        Arrays.sort(files, Comparator.comparing(File::getName));
        
        ByteArrayOutputStream treeContent = new ByteArrayOutputStream();

        for (File file : files) {
            String mode;
            String sha;
            String name = file.getName();

            if (file.isDirectory()) {
                mode = "40000";
                sha = runWriteTree(file);
            } else {
                byte[] content = Files.readAllBytes(file.toPath());
                byte[] header = GitUtils.createGitHeader("blob", content.length);
                byte[] blob = GitUtils.concatenateBytes(header, content);
                sha = GitUtils.computeSHA1(blob);

                GitUtils.writeGitObject(sha, blob);
                mode = "100644";
            }

            treeContent.write((mode + " " + name).getBytes(StandardCharsets.UTF_8));
            treeContent.write(0);
            treeContent.write(GitUtils.hexToBytes(sha));
        }

        byte[] raw = treeContent.toByteArray();
        byte[] header = GitUtils.createGitHeader("tree", raw.length);
        byte[] fullTree = GitUtils.concatenateBytes(header, raw);
        String treeSha = GitUtils.computeSHA1(fullTree);

        GitUtils.writeGitObject(treeSha, fullTree);
        return treeSha;
    }
}