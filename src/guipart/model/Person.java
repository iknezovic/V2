/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guipart.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ivan
 */
public class Person {
    private  IntegerProperty id;
    private  IntegerProperty balance;
    private  IntegerProperty creditLine;   
    private  BooleanProperty fraud;
    
    private  StringProperty gender; 
    private  IntegerProperty trans;
    private  IntegerProperty intlTrans;
    private  IntegerProperty cardholders; 
    
    public Person(){
      
    }
    
   /* public Person(Integer id,Integer balance,Integer creditLine,Boolean fraud){
        this.id = new SimpleIntegerProperty(id);
        this.balance = new SimpleIntegerProperty(balance);
        this.creditLine = new SimpleIntegerProperty(creditLine);
        this.fraud = new SimpleBooleanProperty(fraud);
        
        //dummy data to populate fields
        this.gender = new SimpleStringProperty("unknown");
        this.trans = new SimpleIntegerProperty(0);
        this.intlTrans = new SimpleIntegerProperty(0);
        this.cardholders = new SimpleIntegerProperty(1);
                
    }*/
    
    public Person(Integer id,Integer balance,Integer creditLine,Boolean fraud,
                            String gender,Integer trans,Integer intlTrans,Integer cardholders){
    
        this.id = new SimpleIntegerProperty(id);
        this.balance = new SimpleIntegerProperty(balance);
        this.creditLine = new SimpleIntegerProperty(creditLine);
        this.fraud = new SimpleBooleanProperty(fraud);
    
        this.gender = new SimpleStringProperty(gender);
        this.trans = new SimpleIntegerProperty(trans);
        this.intlTrans = new SimpleIntegerProperty(intlTrans);
        this.cardholders = new SimpleIntegerProperty(cardholders);
        
    }
    
    
    public Integer getID(){
        return this.id.get();
    }
    
    public void setID(Integer id){
        this.id.set(id);
    }
    
    public IntegerProperty getIDProperty(){
        return this.id;
    }
    
    
    public Integer getBalance(){
        return this.balance.get();
    }
    
    public void setBalance(Integer balance){
        this.balance.set(balance);
    }
    
    public IntegerProperty getBalanceProperty(){
        return this.balance;
    }
    
    
    public Integer getCreditLine(){
        return this.creditLine.get();
    }
    
    public void setCreditLine(Integer creditLine){
        this.creditLine.set(creditLine);
    }
    
    public IntegerProperty getCreditLineProperty(){
        return this.creditLine;
    }
    
    
    public Boolean getFraud(){
        return this.fraud.get();
    }
    
    public void setFraud(Boolean fraud){
        this.fraud.set(fraud);
    }
    
    public BooleanProperty getFraudProperty(){
        return this.fraud;
    }
    
    public void setGender(String gender){
        this.gender.set(gender);
    }
    
    public void setTransactions(Integer transaction){
        this.trans.set(transaction);
    }
    
    
    public void setIntlTransactions(Integer transaction){
        this.intlTrans.set(transaction);
    }
    
    public void setCardholders(Integer cardholders){
        this.cardholders.set(cardholders);
    }
    
    public String getGender(){
        return this.gender.get();
    }
    
    public String getTransactions(){
        return Integer.toString(this.trans.get());
    }
    
    public String getIntlTransactions(){
        return Integer.toString(this.intlTrans.get());
    }
    
    public String getCardholders(){
        return Integer.toString(this.cardholders.get());
    }
    
    
    
}
