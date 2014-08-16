/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart;

import guipart.model.Person;
import guipart.view.GUIOverviewController;
import guipart.view.RootLayoutController;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ivan
 */
public class GUIPart extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /*
        observable list
    */
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    
    //constructor
    public GUIPart(){
        /*for(int i = 0;i < 1000; i++){
            personData.add(new Person(5, 5, Integer.MIN_VALUE, Boolean.TRUE));
        }*/
        
    }
    
    public ObservableList<Person> getPersonData() {
        return personData;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Classification");

        initRootLayout();

        showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIPart.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIPart.class.getResource("view/GUIOverview.fxml"));
            AnchorPane guiOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(guiOverview);
            // Give the controller access to the main app.
            GUIOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void addPerson(Person person){
    
        this.personData.add(person);
        
    }
    
    public void clearTable(){
        this.personData.clear();
    }
    
    public GUIPart getMainApp(){
        return this;
    }
    
    public String getPerson()
    {
       return this.personData.toString();
    }
    
   
    
}
