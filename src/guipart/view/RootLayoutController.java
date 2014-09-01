/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart.view;

import guipart.GUIPart;
import guipart.model.Person;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.Dialogs;


/**
 *
 * @author ivan
 */
public class RootLayoutController {
    
    private GUIPart guiPart;
    
    public void setMainApp(GUIPart mainApp) {
        this.guiPart = mainApp;
  
    }
    
    @FXML
    private void handelExit(Event e)
    {
        Platform.exit();
    }
    
    @FXML 
    private void clearTable(Event e){
        this.guiPart.clearTable();
    }
    
    @FXML 
    private void saveAsCSV(Event e) throws IOException{
        FileChooser fileChooser = new FileChooser();
  
              //Set extension filter
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
              fileChooser.getExtensionFilters().add(extFilter);
              
              //Show save file dialog
              File file = fileChooser.showSaveDialog(null);
              //String s = guiPart.getPerson();
              
              BufferedWriter bw = new BufferedWriter(new FileWriter(file));
              ListIterator<Person> listIterator = this.guiPart.getPersonData().listIterator();
              String s;
              
              
              if(file != null && listIterator.hasNext()){
                  String header = "ID Gender Cardhodlers Balance Transactions IntlTrans CreditLine Fraud \n";
                  bw.write(header);
                  Person onePerson = listIterator.next();
                  
                  while(listIterator.hasNext()){
                      s = onePerson.getID().toString();
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getGender());
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getCardholders());
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getBalance().toString());
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getTransactions());
                      s = s.concat(",");
                       
                      s = s.concat(onePerson.getIntlTransactions());
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getCreditLine().toString());
                      s = s.concat(",");
                      
                      s = s.concat(onePerson.getFraud().toString());
                      s = s.concat("\n");
                      
                      bw.write(s);
                      onePerson = listIterator.next();
                  }
                  
              }else{
                  Dialogs.create()
                    .owner(guiPart.getPrimaryStage())
                    .title("Error Dialog")
                    .masthead("Look, an Error Dialog")
                    .message("Classify table is empty or file name wasn't given")
                    .showError();
              }
    }
    
     private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
             
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            //Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     @FXML
     private void about(Event e){
         Dialogs.create()
                    .owner(guiPart.getPrimaryStage())
                    .title("About")
                    .masthead("Super program 3300 delux")
                    .message("Created by Ivan")
                    .showInformation();
     }
     
     @FXML
     private void deleteRow(Event e){
         
         
     }
}
