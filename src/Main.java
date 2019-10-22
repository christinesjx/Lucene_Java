import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;



public class Main{


    BruteForceSearch bruteForceSearch = new BruteForceSearch();
    List<List<Long>> responseTimeHeartRate = new ArrayList<>();
    List<List<Long>> responseTimePhysicalActivity = new ArrayList<>();
    List<List<Long>> responseTimeLight = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        Main driver = new Main();

        System.out.println("ambient light: ");
        driver.ambientLight(100);
        System.out.println();

        System.out.println("heart rate：");
        driver.heartRate(100);
        System.out.println();

        System.out.println("physical activity: ");
        driver.physicalActivity(100);
        System.out.println();


    }


    public List<List<Long>> ambientLight(int num) throws FileNotFoundException {

        responseTimeLight = new ArrayList<>();
        List<Long> responseTimeBF = new ArrayList<>();
        List<Long> responseTimeLucene = new ArrayList<>();


        for(int i = 1; i < num; i++){

            responseTimeBF.add(bruteForceSearch.searchStringInFiles("SA/LightSensor", "less bright", i));

            LuceneSearch LightLS = new LuceneSearch("lucene", "SA/LightSensor", i);
            LightLS.createIndex();
            responseTimeLucene.add(LightLS.searching("less bright"));

            responseTimeLight.add(responseTimeBF);
            responseTimeLight.add(responseTimeLucene);

        }
        System.out.println("ambient light brute force: " + responseTimeBF.toString());
        System.out.println("ambient light lucene: "+responseTimeLucene.toString());

        return responseTimeLight;
    }



    public void physicalActivity(int num) throws FileNotFoundException{

        responseTimePhysicalActivity = new ArrayList<>();
        List<Long> responseTimeBF = new ArrayList<>();
        List<Long> responseTimeLucene = new ArrayList<>();


        for(int i = 1; i < num; i++){

            responseTimeBF.add(bruteForceSearch.searchStringInFiles("ActivFit", "running", i));

            LuceneSearch heartRateLS = new LuceneSearch("lucene", "ActivFit", i);
            heartRateLS.createIndex();
            responseTimeLucene.add(heartRateLS.searching("running"));

            responseTimePhysicalActivity.add(responseTimeBF);
            responseTimePhysicalActivity.add(responseTimeLucene);

        }
        System.out.println("physical activity brute force: " + responseTimeBF.toString());
        System.out.println("physical activity lucene: "+ responseTimeLucene.toString());
    }


    public void heartRate(int num) throws FileNotFoundException{

        responseTimeHeartRate = new ArrayList<>();
        List<Long> responseTimeBF = new ArrayList<>();
        List<Long> responseTimeLucene = new ArrayList<>();


        for(int i = 1; i < num; i++){

            responseTimeBF.add(bruteForceSearch.searchStringInFiles("HeartRate", "bpm:100", i));
            LuceneSearch heartRateLS = new LuceneSearch("lucene", "HeartRate", i);
            heartRateLS.createIndex();
            responseTimeLucene.add(heartRateLS.searching("bpm100"));

            responseTimeHeartRate.add(responseTimeBF);
            responseTimeHeartRate.add(responseTimeLucene);

        }
        System.out.println("heart rate brute force: "+responseTimeBF.toString());
        System.out.println("heart rate lucene: "+responseTimeLucene.toString());

    }


//
//
//    @Override public void start(Stage stage) throws Exception{
//
//        physicalActivity(100);
//
//        stage.setTitle("PhysicalActivity");
//        //defining the axes
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Days");
//        yAxis.setLabel("Response Time");
//
//        //creating the chart
//        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
//
//        lineChart.setTitle("PhysicalActivity");
//        //defining a series
//        XYChart.Series series = new XYChart.Series();
//        series.setName("brute force");
//        XYChart.Series series1 = new XYChart.Series();
//        series1.setName("lucene");
//
//        //populating the series with data
//
//        List<Long> responseTimeBF = responseTimePhysicalActivity.get(0);
//        List<Long> responseTimeLucene = responseTimePhysicalActivity.get(1);
//
//        System.out.println(responseTimePhysicalActivity.get(0));
//
//
//        for(int i = 0; i < responseTimeBF.size(); i++){
//            series.getData().add(new XYChart.Data(i, responseTimeBF.get(i)));
//            series1.getData().add(new XYChart.Data(i, responseTimeLucene.get(i)));
//        }
//
//        Scene scene = new Scene(lineChart, 800, 600);
//        lineChart.getData().add(series);
//        lineChart.getData().add(series1);
//
//
//        stage.setScene(scene);
//        stage.show();
//
//    }


//
//
//    @Override public void start(Stage stage) throws Exception{
//
//        heartRate(100);
//
//        stage.setTitle("HeartRate");
//        //defining the axes
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Days");
//        yAxis.setLabel("Response Time");
//
//        //creating the chart
//        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
//
//        lineChart.setTitle("Heart Rate");
//        //defining a series
//        XYChart.Series series = new XYChart.Series();
//        series.setName("brute force");
//        XYChart.Series series1 = new XYChart.Series();
//        series1.setName("lucene");
//
//        //populating the series with data
//
//        List<Long> responseTimeBF = responseTimeHeartRate.get(0);
//        List<Long> responseTimeLucene = responseTimeHeartRate.get(1);
//
//        System.out.println(responseTimeHeartRate.get(0));
//
//
//        for(int i = 0; i < responseTimeBF.size(); i++){
//            series.getData().add(new XYChart.Data(i, responseTimeBF.get(i)));
//            series1.getData().add(new XYChart.Data(i, responseTimeLucene.get(i)));
//        }
//
//        Scene scene = new Scene(lineChart, 800, 600);
//        lineChart.getData().add(series);
//        lineChart.getData().add(series1);
//
//
//        stage.setScene(scene);
//        stage.show();
//
//    }

//
//    @Override public void start(Stage stage) throws Exception{
//
//        ambientLight(100);
//
//        stage.setTitle("ambientLight");
//        //defining the axes
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Days");
//        yAxis.setLabel("Response Time");
//        //creating the chart
//        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
//
//        lineChart.setTitle("Ambient Light");
//        //defining a series
//        XYChart.Series series = new XYChart.Series();
//        series.setName("brute force");
//        XYChart.Series series1 = new XYChart.Series();
//        series1.setName("lucene");
//
//        //populating the series with data
//
//        List<Long> responseTimeBF = responseTimeLight.get(0);
//        List<Long> responseTimeLucene = responseTimeLight.get(1);
//
//        System.out.println(responseTimeLight.get(0));
//
//
//        for(int i = 0; i < responseTimeBF.size(); i++){
//            series.getData().add(new XYChart.Data(i, responseTimeBF.get(i)));
//            series1.getData().add(new XYChart.Data(i, responseTimeLucene.get(i)));
//        }
//
//        Scene scene = new Scene(lineChart, 800, 600);
//        lineChart.getData().add(series);
//        lineChart.getData().add(series1);
//
//
//        stage.setScene(scene);
//        stage.show();
//
//    }


    //    public static void main(String[] args) throws Exception {
//
//        launch(args);
//
//    }


}
