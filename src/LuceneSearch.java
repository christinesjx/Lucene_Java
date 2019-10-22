import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Scanner;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class LuceneSearch {


    public String INDEX_PATH;
    public String SCAN_PATH;
    public int NUMBER_OF_DAYS;
    StandardAnalyzer analyzer = new StandardAnalyzer();


    public LuceneSearch(String INDEX_PATH, String SCAN_PATH, int NUMBER_OF_DAYS) {
        this.INDEX_PATH = INDEX_PATH;
        this.SCAN_PATH = SCAN_PATH;
        this.NUMBER_OF_DAYS = NUMBER_OF_DAYS;
    }


    public void createIndex(){
        IndexWriter w = null;
        try {
            Directory index = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_PATH));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            w = new IndexWriter(index, config);
            w.deleteAll();
            addToLucene(w);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
                try
                {
                    if(w != null) w.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }

    }

    private void addToLucene(IndexWriter w){

        File[] files = new File(SCAN_PATH).listFiles();

        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            addToLucene(w, files[i]);

        }
    }



    private void addToLucene(IndexWriter w, File fileName){

        JSONParser parser = new JSONParser();
        try {
            //scan through each line of the .json file
            Scanner scan = new Scanner(fileName);

            while (scan.hasNext()) {

                final String line = scan.nextLine().toLowerCase();


                if(SCAN_PATH.equals("HeartRate")){

                    Object obj = parser.parse(line);
                    JSONObject jsonObject = (JSONObject) obj;
                    String sensorName = (String) jsonObject.get("sensor_name");
                    String timestamp = (String) jsonObject.get("timestamp").toString();
                    String sensorData = jsonObject.get("sensor_data").toString();

                    String[] heartRateSensorData = sensorData.split(":");
                    String heartRateSensorData1 = heartRateSensorData[0].replaceAll("[^A-Za-z]+", "");
                    String heartRateSensorData2 = heartRateSensorData[1].replaceAll("[^0-9.]", "");
                    String newStr = heartRateSensorData1 +""+heartRateSensorData2;
                    addHeartRateDoc(w, sensorName, timestamp, newStr);

                }else if(SCAN_PATH.equals("ActivFit")){

                    Object obj = parser.parse(line);
                    JSONObject jsonObject = (JSONObject) obj;
                    String sensorName = (String) jsonObject.get("sensor_name");
                    String timestamp = (String) jsonObject.get("timestamp").toString();
                    String sensorData = jsonObject.get("sensor_data").toString();

                    Object activFitObj = parser.parse(sensorData);
                    JSONObject activFitJsonObject = (JSONObject) activFitObj;

                    String activFitData1 = activFitJsonObject.get("duration").toString();
                    String activFitData2 = activFitJsonObject.get("activity").toString();

                    addActivFitDoc(w, sensorName, timestamp, activFitData1,activFitData2);

                }else if(SCAN_PATH.equals("SA/LightSensor")){

                    String[] lightSensorData = line.split(",");
                    if(lightSensorData.length == 5){
                        addLightSensorDoc(w, lightSensorData[0], lightSensorData[2], lightSensorData[3]);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public long searching(String searchStr){
        long startTime = 0;
        long endTime = 0;

        DirectoryReader directoryReader = null;
        try {
            Directory index = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_PATH));
            directoryReader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

            TopDocs topDocs = null;
            ScoreDoc[] scoreDocs = null;
            if(SCAN_PATH.equals("HeartRate")){
                Query q = new QueryParser("bpmData", analyzer).parse(searchStr);
                startTime = System.nanoTime();

                topDocs = indexSearcher.search(q, 100); // 搜索前100条结果
                scoreDocs = topDocs.scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    //System.out.println(document.get("sensor_name") + ", " + document.get("timestamp") + ", " + document.get("bpmData"));
                }


            }else if(SCAN_PATH.equals("ActivFit")){
                Query q = new QueryParser("activity", analyzer).parse(searchStr);
                startTime = System.nanoTime();

                topDocs = indexSearcher.search(q, 100); // 搜索前100条结果
                scoreDocs = topDocs.scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    //System.out.println(document.get("sensor_name") + ", " + document.get("timestamp") + ", " + document.get("duration") +", "+ document.get("activity"));
                }


            }else if(SCAN_PATH.equals("SA/LightSensor")) {
                Query q = new QueryParser("light", analyzer).parse(searchStr);
                startTime = System.nanoTime();

                topDocs = indexSearcher.search(q, 100); // 搜索前100条结果
                scoreDocs = topDocs.scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    //System.out.println(document.get("sensor_name") + ", " + document.get("timestamp") + ", " + document.get("light"));
                }
            }

            endTime = System.nanoTime();
            System.out.println("total hits：" + topDocs.totalHits);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(directoryReader != null) directoryReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return endTime - startTime;

    }




    private void addHeartRateDoc(IndexWriter w, String sensorName, String timestamp, String bpmData) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("sensor_name", sensorName, Field.Store.YES));
        doc.add(new TextField("timestamp", timestamp, Field.Store.YES));
        doc.add(new TextField("bpmData", bpmData, Field.Store.YES));
        w.addDocument(doc);
    }

    private void addActivFitDoc(IndexWriter w, String sensorName, String timestamp, String duration, String activity) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("sensor_name", sensorName, Field.Store.YES));
        doc.add(new TextField("timestamp", timestamp, Field.Store.YES));
        doc.add(new TextField("duration", duration, Field.Store.YES));
        doc.add(new TextField("activity", activity, Field.Store.YES));
        w.addDocument(doc);
    }

    private void addLightSensorDoc(IndexWriter w, String sensorName, String timestamp, String light) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("sensor_name", sensorName, Field.Store.YES));
        doc.add(new TextField("timestamp", timestamp, Field.Store.YES));
        doc.add(new TextField("light", light, Field.Store.YES));
        w.addDocument(doc);
    }
}
