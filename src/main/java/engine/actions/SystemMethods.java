package engine.actions;

import engine.reporters.Loggers;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SystemMethods {
    private static final List<Process> runningProcesses = Collections.synchronizedList(new ArrayList<>());
private SystemMethods(){}
    public static void deleteDirectory(String path) {
        File directory = new File(path);
        try {
            FileUtils.deleteDirectory(directory);
         Loggers.getLogger().info("Deleted the directory {}", path);
        } catch (Exception e) {
         Loggers.getLogger().info("Couldn't delete directory {}", path);
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
         Loggers.getLogger().warn("File does not exist: {}", path);
            return;
        }
        try {
            FileUtils.delete(file);
         Loggers.getLogger().info("Deleted the file: {}", path);
        } catch (Exception e) {
         Loggers.getLogger().error("Couldn't delete file: {}.", path);
        }
    }
    public static Process startBatAsync(String batPath) {
        CompletableFuture<Process> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                File batFile = new File(batPath);
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", batFile.getAbsolutePath());
                builder.directory(batFile.getParentFile());
                builder.inheritIO();

                Process process = builder.start();
                runningProcesses.add(process);
                future.complete(process);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();

        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while starting process", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to start process: " + batPath, e);
        }
    }
    public static void killProcessesByPort(int... ports) {
        try {
            for (int port : ports) {
                // Find PID using port
                ProcessBuilder findPid = new ProcessBuilder(
                        "cmd.exe", "/c",
                        "netstat -ano | findstr :" + port + " | findstr LISTENING"
                );

                Process process = findPid.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("LISTENING")) {
                        String[] parts = line.trim().split("\\s+");
                        String pid = parts[parts.length - 1];

                        // Kill the process
                        ProcessBuilder killPb = new ProcessBuilder("taskkill", "/F", "/PID", pid);
                        killPb.start();
                        Loggers.getLogger().info("Killed process {} using port {}",pid,  port);
                    }
                }
                process.waitFor();
            }
        } catch (Exception e) {
            Loggers.getLogger().error("Error killing processes by port: " + e.getMessage());
        }
    }


    public static void runFile(String path) {
        File file = new File(path);
        if (file.canExecute()) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", file.getAbsolutePath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Loggers.getLogger().info(line);
                    }
                }

                 process.waitFor();
             Loggers.getLogger().info("File executed: {}", path);
            } catch (IOException | InterruptedException e) {
             Loggers.getLogger().info("File isn't executed: {}", path);
            }
        } else {
         Loggers.getLogger().info("File {} isn't executable type", path);
        }
    }

    public static boolean checkExistenceOfFile(String path) {
        File file = new File(path);
     Loggers.getLogger().info("Check if file exists {}", file.exists());
        return file.exists();
    }

    public static String readFileContent(String path) {
        Path pth = Paths.get(path);
        String lines = "";
        try {
            lines = String.valueOf(Files.readAllLines(pth));
        } catch (IOException ex) {
         Loggers.getLogger().error("Error reading file: {}", ex.getMessage());
        }
     Loggers.getLogger().info("Get file contents: {}", lines);
        return lines;
    }

}