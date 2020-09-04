package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileManager {

    public String baseDirectory;
    public String baseDataDirectory;
    public String fileName;
    private final BufferedReader reader;

    public FileManager(){
        baseDirectory = System.getProperty("user.dir") + "\\";
        baseDataDirectory = "data" + "\\";
        fileName = "fileName.csv";
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void selectBaseDirectory() throws IOException {
        System.out.println("Current base directory is : \"" + baseDirectory + "\"");
        readFilesInDirectory(baseDirectory);

        System.out.println("New base directory : ");
        String input = reader.readLine();

        if(!input.equals("")) baseDirectory = input;
    }

    private void selectFileDirectory() throws IOException {
        System.out.println("Current data directory is : \"" + baseDirectory + "\"");
        readFilesInDirectory(baseDirectory);

        System.out.println("New data directory : ");
        String input = reader.readLine();

        if(!input.equals("")) baseDataDirectory = input;
    }

    public void setupDirectories() throws IOException {
        String input = "";

        System.out.println("Current base directory is : \"" + baseDirectory + "\"");
        System.out.println("Current data directory is : \"" + baseDirectory + baseDataDirectory);

        System.out.println("Would you like to change the base directory? {Y/N}");
        input = reader.readLine().toUpperCase();

        if(input.equals("Y")){
            selectBaseDirectory();
        }

        System.out.println("Would you like to change the data directory? {Y/N}");
        input = reader.readLine().toUpperCase();

        if(input.equals("Y")){
            selectFileDirectory();
        }

        System.out.println("Please select the target file number: ");
        List<String> files = getFilesInDirectory(baseDirectory + baseDataDirectory);

        for (int i = 0; i < files.size(); i++) {
            System.out.println("[" + (i + 1) +"]" + " " + files.get(i));
        }

        int fileNumber = Integer.parseInt(reader.readLine());

        fileName = files.get( fileNumber - 1);

        System.out.println("Using source : " + baseDirectory + baseDataDirectory + fileName);
    }


    private List<String> getFilesInDirectory(String directory) {

        List<String> files = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(x -> files.add(x.toString().replace(directory,"")) );
        } catch (IOException e) {
            System.out.println("Unable to preview directory:" + e.getMessage());
        }

        return files;
    }

    private void readFilesInDirectory(String directory) {

        System.out.println("Files in " + directory);

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(x -> System.out.println(x.toString().replace(directory,"")));
        } catch (IOException e) {
            System.out.println("Unable to preview directory:" + e.getMessage());
        }
    }

    public String getCountry() throws IOException {

        System.out.println("Please enter the name of the country you would like to load data on :");
        return reader.readLine();
    }
}
