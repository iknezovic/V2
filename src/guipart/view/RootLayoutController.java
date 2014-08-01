/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart.view;

import guipart.GUIPart;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;


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
    
}
