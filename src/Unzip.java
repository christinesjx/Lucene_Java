import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzip {


    public void mergeSensorFiles() throws IOException {

        // merge all sensor files into six different txt files

        // create a folder called data in the current working directory, the folder will save 6 .txt files.
        new File("data").mkdir();

        // files = all files in the current working directory
        File[] files = new File(".").listFiles();

        for (File file : files) {

            // search for 6 sensor folder
            if (file.getName().contains("ActivFit") || file.getName().contains("Bluetooth") || file.getName().contains("LightSensor")
                    || file.getName().contains("HeartRate") || file.getName().contains("Activity") || file.getName().contains("BatterySensor")) {


                // merge six sensor files into six txt files
                mergeSensorFiles(file.getName());

            }
        }
    }




    private void mergeSensorFiles(String folderName) throws IOException {

        // for all txt files in the same folder, merge them all into one single .txt file
        File[] files = new File(folderName).listFiles();
        File outFile = new File("data/" + folderName + ".txt");

        OutputStream out = new FileOutputStream(outFile);

        byte[] buf = new byte[1024];

        // write all the .txt files into a single .txt file
        for (File file : files) {
            InputStream in = new FileInputStream(file);
            int b;
            while ((b = in.read(buf)) >= 0)
                out.write(buf, 0, b);
            in.close();
        }
        out.close();
    }



    public void unzipSensorFiles(){

        // unzip all files in SampleUserSmartwatch

        // files = all files in the SampleUserSmartwatch folder
        File[] files = new File("SampleUserSmartwatch").listFiles();

        for (File file : files) {

            String zipFileName = "SampleUserSmartwatch/" + file.getName();
            // unzip one of the daily .zip file
            unzipSensorFiles(zipFileName);
        }
    }



    private void unzipSensorFiles(String zipFileName) {

        //unzip one of the .zip files in SampleUserSmartwatch

        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {

                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                String name = zipEntry.getName();

                // unzip the file, save only the sensor data file, discard other unnecessary files
                if(name.contains("ActivFit") || name.contains("Bluetooth") || name.contains("LightSensor")
                        || name.contains("HeartRate") || name.contains("Activity") || name.contains("BatterySensor")) {



                        File file = new File(name);

                        File parent = file.getParentFile();
                        if (parent != null) {
                            parent.mkdirs();
                        }

                        InputStream is = zipFile.getInputStream(zipEntry);
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = is.read(bytes)) >= 0) {
                            fos.write(bytes, 0, length);
                        }

                        is.close();
                        fos.close();


                }

            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
