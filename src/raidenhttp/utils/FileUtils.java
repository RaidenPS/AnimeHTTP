package raidenhttp.utils;

// Imports
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {
    /**
     *  Returns a file input stream for the specified file path without throws an exception
     * @param filePath The file path.
     * @return The input stream.
     */
    public static InputStream getFileInputStream(String filePath) {
        try {
            return new FileInputStream(filePath);
        } catch (IOException ignored) {
            return null;
        }
    }

    /**
     * Retrieves a list of all paths in a directory and its subdirectories.
     * @param folder Folder the path to the directory from which to retrieve paths
     * @return List representing all files/subfolders.
     * @throws IOException Directory/Folder is not found.
     */
    public static List<Path> getPathsFromResource(String folder) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            return paths.collect(Collectors.toList());
        }
    }

    /**
     * Reads the content of a file into a bytearray.
     * @param filePath The path to the file to be read.
     * @return The bytes of given file.
     * @throws IOException The file is not found.
     */
    public static byte[] readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    /**
     * Retrieves a string from an input stream.
     *
     * @param stream The input stream.
     * @return The string.
     */
    public static String readFromInputStream(InputStream stream) {
        if (stream == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            stream.close();
        } catch (Exception ignored) {
            return null;
        }
        return stringBuilder.toString();
    }
}
