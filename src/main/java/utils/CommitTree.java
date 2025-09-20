package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommitTree {
    public void runCommitTree(String[] args) {
        if (args.length < 3 || !args[0].equals("commit-tree") || !args[1].equals("-p") || args[1].equals("-help")) {
            System.out.println("Usage: commit-tree -p <object-hash>");
            return;
        }

        String treeSha = args[1];
        List<String> parentShas = new ArrayList<>();
        for (int i = 2; i < args.length - 1; i++) {
            if (args[i].equals("-p")) {
                parentShas.add(args[i + 1]);
            }
        }
        String commitMessage = args[args.length - 1];

        String authorName = "sabya";
        String authorEmail = "sabya@sachi.com";
        String committerName = "Committer Name";
        String committerEmail = "sabya@abcd.com";
        long timestamp = System.currentTimeMillis() / 1000;

        String parentCommitShas = String.join(" ", parentShas);
        String commitContent = String.format("tree %s\n", treeSha);
        if (!parentCommitShas.isEmpty()) {
            commitContent += String.format("parent %s\n", parentCommitShas);
        }
        commitContent += String.format("author %s <%s> %d +0000\n", authorName, authorEmail, timestamp);
        commitContent += String.format("committer %s <%s> %d +0000\n", committerName, committerEmail, timestamp);
        commitContent += "\n" + commitMessage + "\n";

        byte[] commitBytes = commitContent.getBytes(StandardCharsets.UTF_8);
        byte[] header = GitUtils.createGitHeader("commit", commitBytes.length);
        byte[] commitObject = GitUtils.concatenateBytes(header, commitBytes);
        String commitHash = GitUtils.computeSHA1(commitObject);
        
        try {
            GitUtils.writeGitObject(commitHash, commitObject);
            System.out.println(commitHash);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}