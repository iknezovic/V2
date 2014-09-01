/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart.view;

import com.google.common.collect.Lists;
import guipart.GUIPart;
import guipart.model.Person;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.transform.ScaleBuilder;
import javafx.stage.FileChooser;
import net.sf.cglib.transform.AbstractClassFilterTransformer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.RegressionResultAnalyzer;
import org.apache.mahout.classifier.ResultAnalyzer;
import org.apache.mahout.classifier.df.DFUtils;
import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.data.DataConverter;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.CsvRecordFactory;
import org.apache.mahout.classifier.sgd.LogisticModelParameters;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author ivan
 */
public class GUIOverviewController {
    /*******************************************************************
        Table fields
    *********************************************************************/
    @FXML private TableView<Person> personTable;
    @FXML private TableColumn<Person,String> id;
    @FXML private TableColumn<Person,String> balance;
    @FXML private TableColumn<Person,String> creditLine;
    @FXML private TableColumn<Person,String> fraud;
    
    /***************************************************************
     *  Label fields
     ****************************************************************/
    
    @FXML private Label gender;
    @FXML private Label transaction;
    @FXML private Label intlTransaction;
    @FXML private Label cardholders;
    
    @FXML private Label gender1;
    @FXML private Label transaction1;
    @FXML private Label intlTransaction1;
    @FXML private Label cardholders1;
    /*****************************************************************
     * Input fields forest and log.reg.
     *****************************************************************/
   
    @FXML private TextField textFieldCSV;
    @FXML private TextField textFieldModel;
    
    @FXML private TextField textFieldCSVRF;
    @FXML private TextField textFieldModelRF;
    @FXML private TextField textFieldDatasetRF;
    /*************************************************************
     *  Input fields single classify
     ***********************************************************/
    @FXML private TextField scID;
    @FXML private TextField scGender;
    @FXML private TextField scState;
    @FXML private TextField scBalance;       
    @FXML private TextField scTrans;
    @FXML private TextField scIntlTrans;
    @FXML private TextField scCreditLine;
    @FXML private TextField scCardholders;
            
    //output fields
    @FXML private TextArea textAnalyze;
    @FXML private TextArea textAnalyze2;
    
    /****************************************************************
     * path to csv file and model
     *****************************************************************/
    private String pathCSV = null;
    private String pathModel = null;
    
    private String pathCSVRF = null;
    private String pathModelRF = null;
    private String pathDatasetRF = null;
    
    //reference to GUIPart;
    private GUIPart guiPart;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public GUIOverviewController(){
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    
    @FXML
    private void initialize() {
        
        id.setCellValueFactory(cellData -> cellData.getValue().getIDProperty().asString());
        balance.setCellValueFactory(cellData -> cellData.getValue().getBalanceProperty().asString());
        creditLine.setCellValueFactory(cellData -> cellData.getValue().getCreditLineProperty().asString());
        fraud.setCellValueFactory(cellData -> cellData.getValue().getFraudProperty().asString());
        
        
        // Clear person details.
        setPersonDetails(null);
            
        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> setPersonDetails(newValue));
    }
    
    
    public void setMainApp(GUIPart mainApp) {
        this.guiPart = mainApp;

        // Add observable list data to the table
        personTable.setItems(guiPart.getPersonData());
    }
    
    public void setPersonDetails(Person person){
        
        if(person != null){
            
            gender.setText(person.getGender());
            transaction.setText(person.getTransactions());
            intlTransaction.setText(person.getIntlTransactions());
            cardholders.setText(person.getCardholders());
            
            gender1.setText(person.getGender());
            transaction1.setText(person.getTransactions());
            intlTransaction1.setText(person.getIntlTransactions());
            cardholders1.setText(person.getCardholders());
        }else{
        
            gender.setText("");
            transaction.setText("");
            intlTransaction.setText("");
            cardholders.setText("");
        }
    
    }
    
    
    @FXML void handleOpenFile(ActionEvent event){
        
        final FileChooser fileChooser = new FileChooser();
        Utils.configureFileChooserCSV(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pathCSV = file.getPath();
            
            textFieldCSV.setText(pathCSV);
        }
                
    }
    
    @FXML void handleOpenModel(ActionEvent event){
        
        final FileChooser fileChooser = new FileChooser();
        Utils.configureFileChooserModel(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pathModel = file.getPath();
            
            textFieldModel.setText(pathModel);
        }
                
    }
    
    @FXML void handleClassifyModel(ActionEvent event) throws IOException{
        
        if(pathModel != null && pathCSV != null){
            
            Auc collector = new Auc();
            LogisticModelParameters lmp = LogisticModelParameters.loadFrom(new File(pathModel));

            CsvRecordFactory csv = lmp.getCsvRecordFactory();
            OnlineLogisticRegression lr = lmp.createRegression();

            BufferedReader in = Utils.open(pathCSV);

            String line = in.readLine();
            csv.firstLine(line);
            line = in.readLine();
            
            int correct = 0;
            int wrong = 0;
            Boolean booltemp;
            String gender;
            
            while(line != null){
            
                Vector v = new SequentialAccessSparseVector(lmp.getNumFeatures());
                int target = csv.processLine(line, v);
                String [] split = line.split(",");
               
                double score = lr.classifyFull(v).maxValueIndex();
                if(score == target)
                   correct++;
                else
                   wrong++;

                System.out.println("Target is: "+target+" Score: "+score);
                
                booltemp = score != 0;
               
                if(split[1].contentEquals("1"))
                    gender = "male";
                else    
                    gender = "female";
                    
                
                Person temp = new Person(Integer.parseInt(split[0]),Integer.parseInt(split[4]),Integer.parseInt(split[7]),booltemp, 
                        gender,Integer.parseInt(split[5]),Integer.parseInt(split[6]),Integer.parseInt(split[3]));
                
                guiPart.addPerson(temp);
                
                line = in.readLine();
                collector.add(target, score);

            }
            double posto = ((double)wrong/(double)(correct+wrong))*100;
            System.out.println("Total: "+(correct+wrong)+" Correct: "+correct+" Wrong: "+wrong+" Wrong pct: "+posto +"%");
            //PrintWriter output = null;
            Matrix m = collector.confusion();
            //output.printf(Locale.ENGLISH, "confusion: [[%.1f, %.1f], [%.1f, %.1f]]%n",m.get(0, 0), m.get(1, 0), m.get(0, 1), m.get(1, 1));
            System.out.println("Confusion:"+m.get(0, 0)+" "+m.get(1, 0)+"\n \t   "+m.get(0, 1)+" "+m.get(1, 1)+" ");
    //        m = collector.entropy();
            //output.printf(Locale.ENGLISH, "entropy: [[%.1f, %.1f], [%.1f, %.1f]]%n",m.get(0, 0), m.get(1, 0), m.get(0, 1), m.get(1, 1));
            textAnalyze2.setText("Confusion:"+m.get(0, 0)+" "+m.get(1, 0)+"\n \t \t   "+m.get(0, 1)+" "+m.get(1, 1)+"\n"+
                                "Total: "+(correct+wrong)+" Correct: "+correct+" Wrong: "+wrong+" Wrong pct: "+posto +"%");
        }else{
            
            
            Dialogs.create()
        .owner(guiPart.getPrimaryStage())
        .title("Error Dialog")
        .masthead("Look, an Error Dialog")
        .message("One or more files aren't selected")
        .showError();
            
            
        
        }   
    }
    
    @FXML void handleClassifyRF(ActionEvent event) throws IOException{
        
        String outputFile = "data/out";
        
        Path dataPath = new Path(textFieldCSVRF.getText());         // test data path
        Path datasetPath = new Path(textFieldDatasetRF.getText());  //info file about data set
        Path modelPath = new Path(textFieldModelRF.getText());      // path where the forest is stored
        Path outputPath = new Path(outputFile);                     // path to predictions file, if null do not output the predictions

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        FileSystem outFS = FileSystem.get(conf);


        System.out.println("Loading the forest");
        DecisionForest forest = DecisionForest.load(conf, modelPath);

        if (forest == null) 
            System.err.println("No decision forest found!");
            
         // load the dataset
        Dataset dataset = Dataset.load(conf, datasetPath);
        DataConverter converter = new DataConverter(dataset);

        System.out.println("Sequential classification");
        long time = System.currentTimeMillis();

        Random rng = RandomUtils.getRandom();

        List<double[]> resList = Lists.newArrayList();
        if (fs.getFileStatus(dataPath).isDir()) {
         //the input is a directory of files
         Utils.rfTestDirectory(outputPath, converter, forest, dataset, resList, rng,fs,dataPath,outFS,guiPart);
         } else {
        // the input is one single file
         Utils.rfTestFile(dataPath, outputPath, converter, forest, dataset, resList, rng, outFS ,fs,guiPart);
        }

        time = System.currentTimeMillis() - time;
        //log.info("Classification Time: {}", DFUtils.elapsedTime(time));
        System.out.println("Classification time: "+DFUtils.elapsedTime(time));

        if (dataset.isNumerical(dataset.getLabelId())) {

            RegressionResultAnalyzer regressionAnalyzer = new RegressionResultAnalyzer();
            double[][] results = new double[resList.size()][2];
            regressionAnalyzer.setInstances(resList.toArray(results));
            //log.info("{}", regressionAnalyzer);
            System.out.println(regressionAnalyzer.toString());


        } else {
            ResultAnalyzer analyzer = new ResultAnalyzer(Arrays.asList(dataset.labels()), "unknown");
            for (double[] r : resList) {
              analyzer.addInstance(dataset.getLabelString(r[0]),
                new ClassifierResult(dataset.getLabelString(r[1]), 1.0));
            }
            //log.info("{}", analyzer);
            System.out.println(analyzer.toString());
            textAnalyze.setText(analyzer.toString());
        }   

    }
    
    @FXML void handelOpenFileRF(ActionEvent event){
        
        final FileChooser fileChooser = new FileChooser();
        Utils.configureFileChooserCSV(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pathCSVRF = file.getPath();
            
            textFieldCSVRF.setText(pathCSVRF);
        }
    }
    
    @FXML void handleOpenModelRF(ActionEvent event){
        
        final FileChooser fileChooser = new FileChooser();
        Utils.configureFileChooserModel(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pathModelRF = file.getPath();
            
            textFieldModelRF.setText(pathModelRF);
        }
    
    }
    
    @FXML void handleOpenDatasetRF(ActionEvent event){
        
        final FileChooser fileChooser = new FileChooser();
        Utils.configureFileChooserModel(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pathDatasetRF = file.getPath();
            
            textFieldDatasetRF.setText(pathDatasetRF);
        }
    }
    
    @FXML void singlClassify(ActionEvent e) throws IOException{
      
            
            LogisticModelParameters lmp = LogisticModelParameters.loadFrom(new File(pathModel));

            CsvRecordFactory csv = lmp.getCsvRecordFactory();
            OnlineLogisticRegression lr = lmp.createRegression();
            csv.firstLine("custID,gender,state,cardholder,balance,numTrans,numIntlTrans,creditLine,fraudRisk");
            
            String line;
            
            line = scID.getText();
            line = line.concat(","+scGender.getText());
            line = line.concat(","+scState.getText());
            line = line.concat(","+scCardholders.getText());
            line = line.concat(","+scBalance.getText());
            line = line.concat(","+scTrans.getText());
            line = line.concat(","+scIntlTrans.getText());
            line = line.concat(","+scCreditLine.getText());
            line = line.concat(",0 \n");
            
            Vector v = new SequentialAccessSparseVector(lmp.getNumFeatures());
                int target = csv.processLine(line, v);
                String [] split = line.split(",");
               
                double score = lr.classifyFull(v).maxValueIndex();
                boolean booltemp = score != 0;
                
                String gender;
                
                if(split[1].contentEquals("1"))
                    gender = "male";
                else    
                    gender = "female";
                
                Person temp = new Person(Integer.parseInt(split[0]),Integer.parseInt(split[4]),Integer.parseInt(split[7]),booltemp, 
                        gender,Integer.parseInt(split[5]),Integer.parseInt(split[6]),Integer.parseInt(split[3]));
                
                guiPart.addPerson(temp);
    }
    
}
