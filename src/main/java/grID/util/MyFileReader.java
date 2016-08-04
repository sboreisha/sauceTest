package grID.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sboreisha on 11/27/2015.
 */
public class MyFileReader {

    public static List<String> readFromFile(String fullPath) {

        Path path = Paths.get(fullPath);
        try {
            return Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException ex) {
            System.out.println("File read exception - " + path);
        }
        return new ArrayList<>();
    }
}
