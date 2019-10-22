import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BruteForceSearch {


    private Scanner scanner = new Scanner(System.in);


    public long searchStringInFiles(String sensor, String searchStr, int numberOfDays) throws FileNotFoundException{
        long startTime = System.nanoTime();

        File dir = new File(sensor);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child
                searchStringInFile(child, searchStr);
                //System.out.println(child.getName());
                numberOfDays--;
                if(numberOfDays == 0){
                    break;
                }
            }
        }
        long endTime = System.nanoTime();

        return endTime - startTime;
    }


    private void searchStringInFile(File file, String searchStr) throws FileNotFoundException {
        // given the search txt file name, search in the file for the search String
        // print the line if it contains the search string
        Scanner scan = new Scanner(file);
        while (scan.hasNext()) {
            final String line = scan.nextLine().toLowerCase();
            if (line.contains(searchStr)) {
            }
        }
    }

}
