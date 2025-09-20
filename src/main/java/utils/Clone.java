package utils;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

public class Clone {
    public void runClone(String[] args) {
        if (args.length < 2 || !args[0].equals("clone")) {
            System.out.println("Usage: clone <repository-url> [directory]");
            System.out.println("Example: clone https://github.com/user/repo.git my-project");
            return;
        }

        String repoURL = args[1];
        String targetDir = args.length > 2 ? args[2] : ".";

        try {
            System.out.println("Cloning repository...");
            Git.cloneRepository()
            .setURI(repoURL)
            .setDirectory(new File(targetDir))
            .setCloneAllBranches(false)
            .setNoCheckout(false)
            .call();
            System.out.println("Clone completed successfully");
        }
        catch (JGitInternalException e) {
            System.err.println("Error: Invalid repository URL or local directory issues");
            System.err.println("Details: " + e.getMessage());
        }
        catch (GitAPIException e) {
            System.err.println("Error: Failed to clone repository");
            System.err.println("Possible reasons:");
            System.err.println("1. Repository doesn't exist or is private");
            System.err.println("2. Network connectivity issues");
            System.err.println("3. Disk permissions");
            System.err.println("Technical details: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Unexpected error during clone: " + e.getMessage());
        }
    }
}