/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart.view;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import guipart.GUIPart;
import guipart.model.Person;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;
import javafx.stage.FileChooser;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.df.DFUtils;
import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.data.DataConverter;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;

/**
 *
 * @author ivan
 */
public class Utils {
    
    static void configureFileChooserCSV(
        final FileChooser fileChooser) {      
            fileChooser.setTitle("Select CSV");
            /*fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            ); */                
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }
    
    static void configureFileChooserModel(
        final FileChooser fileChooser) {      
            fileChooser.setTitle("Select model");
        
    }
    
 
     
     static BufferedReader open(String inputFile) throws IOException {
        InputStream in;
        
        try{
          in = Resources.getResource(inputFile).openStream();
        } catch (IllegalArgumentException e) {
          in = new FileInputStream(new File(inputFile));
        }
        
        return new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
      }
     
      static void rfTestDirectory(Path outPath,
                             DataConverter converter,
                             DecisionForest forest,
                             Dataset dataset,
                             Collection<double[]> results,
                             Random rng,
                             FileSystem dataFS, 
                             Path dataPath,
                             FileSystem outFS,
                             GUIPart guiPart) throws IOException {
    Path[] infiles = DFUtils.listOutputFiles(dataFS, dataPath);

    for (Path path : infiles) {
      //log.info("Classifying : {}", path);
        System.out.println("Classifying "+path);
      Path outfile = outPath != null ? new Path(outPath, path.getName()).suffix(".out") : null;
      rfTestFile(path, outfile, converter, forest, dataset, results, rng,outFS,dataFS,guiPart);
    }
  }
        
   static void rfTestFile(Path inPath,
                        Path outPath,
                        DataConverter converter,
                        DecisionForest forest,
                        Dataset dataset,
                        Collection<double[]> results,
                        Random rng,
                        FileSystem outFS,
                        FileSystem dataFS,
                        GUIPart guiPart) throws IOException {
    // create the predictions file
    FSDataOutputStream ofile = null;
    String gender;

    if (outPath != null) {
      ofile = outFS.create(outPath);
    }

    FSDataInputStream input = dataFS.open(inPath);
    try {
      Scanner scanner = new Scanner(input, "UTF-8");

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isEmpty()) {
          continue; // skip empty lines
        }
          System.out.println(line);
          
          String [] split = line.split(",");
          
          if(split[1].contentEquals("1"))
                    gender = "male";
                else    
                    gender = "female";
          

        Instance instance = converter.convert(line);
        
        double prediction = forest.classify(dataset, rng, instance);
        
        boolean booltemp = prediction != 0;
        
        System.out.println("Preditction: "+ prediction);
          
        if (ofile != null) {
          ofile.writeChars(Double.toString(prediction)); // write the prediction
          ofile.writeChar('\n');
        }
        
        Person temp = new Person(Integer.parseInt(split[0]),Integer.parseInt(split[4]),Integer.parseInt(split[7]),booltemp, 
                        gender,Integer.parseInt(split[5]),Integer.parseInt(split[6]),Integer.parseInt(split[3]));
          
        
        guiPart.addPerson(temp);
        
        results.add(new double[] {dataset.getLabel(instance), prediction});
      }

      scanner.close();
    } finally {
      Closeables.close(input, true);
    }
  }    
     
    
}
