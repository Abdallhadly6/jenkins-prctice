package utility;


import java.io.File;

public class FileUtils {

    public static void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    public static void deleteDirectoryIfExists(File dir) {
        if (dir.exists()) {
            deleteDirectory(dir);
            System.out.println("Directory deleted: " + dir.getAbsolutePath());
        } else {
            System.out.println("Directory not found: " + dir.getAbsolutePath());
        }
    }
}

