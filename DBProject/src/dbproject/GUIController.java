/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Zehra
 */
public class GUIController implements Initializable {

    @FXML
    private AnchorPane layer0;
    @FXML
    private Button connectBtn;
    @FXML
    private AnchorPane layer1;
    @FXML
    private ListView<String> tableList;
    @FXML
    private AnchorPane layer2_1;
    @FXML
    private AnchorPane layer2_2;
    
    @FXML
    private Label warnLabel;
    
    @FXML
    private ListView<String> tableDatas;
    
    @FXML
    private TextArea queryArea;

    @FXML
    private TextArea resultArea;
    @FXML
    private AnchorPane layer3_1;

    @FXML
    private TextField layer3TextField;
    
    
    DbObject db ;
    //Selected table
    String selectedTable = "";
    
    //Ekranda anlık gösterilen table
    String showingTable = "";
    
    //Selected student enrollment or course
    String selectedRow = "";
    
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new DbObject();
        layer1.setVisible(false);
        layer2_1.setVisible(false);
        layer2_2.setVisible(false);
        layer3_1.setVisible(false);
        
        
        
        //ListView da seçilen ismi kaydediyoruz
        tableList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
             @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    String listviewValue = tableList.getSelectionModel().getSelectedItem();
                    selectedTable = listviewValue;
                    warnLabel.setText("");
                    

                }catch(Exception ex){
                    System.out.println("Changing event problem");
                } 
       
            }
            
        });
        
        
        tableDatas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
             @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    String listviewValue = tableDatas.getSelectionModel().getSelectedItem();
                    selectedRow = listviewValue;
                    layer3TextField.setText(selectedRow);
                }catch(Exception ex){
                    System.out.println("Tabel datas problem");
                } 
       
            }
            
        });
        
        
    }    
    //Dbye bağlanıyoruz
    @FXML
    private void connectBtnOnAction(ActionEvent event) {
        try{
            
            layer1.setVisible(true);
            ArrayList<String> tables = db.getTables();
            tableList.getItems().setAll(tables);
            connectBtn.setStyle("-fx-background-color: green;");
            connectBtn.setText("Connected");
 
        }catch(Exception ex){
            System.out.println("Problem occured on connectBtnOnAction");
        }
        
    }
    
    //Table listesini yeniliyor
    @FXML
    void refOnAction(ActionEvent event) {
        try{
            ArrayList<String> tables = db.getTables();
            tableList.getItems().setAll(tables);
        }catch(Exception ex){
            System.out.println("Problem occured when refresh");
        }
    }
    
    
    
    //ListViewda Seçilen tabloyu gösteriyoruz
    @FXML
    private void displayContentOnClick(ActionEvent event) {
        try{
            layer2_1.setVisible(true);
            layer2_2.setVisible(false);
            layer3_1.setVisible(true);
            
            if(selectedTable.equals("")){
                warnLabel.setText("Select a table!!!");

            }else{
               showingTable = selectedTable;
                ArrayList<String[]> table = db.getATable(selectedTable);
                String[] printer = new String[table.size()];
                for(int i = 0; i < table.size();i++){
                    String[] row = table.get(i);
                    String strRow = "";
                    for(int j = 0; j<row.length;j++){
                        if(j == row.length-1){
                            strRow += row[j];
                        }else{
                            strRow +=row[j] + ",";
                        }
                    }
                    System.out.println(strRow);
                    printer[i] = strRow; 
                }
                
                //-----------------------lw update
            tableDatas.getItems().setAll(printer);
                    
                
            }

        }catch(Exception ex){
            //System.out.println("Problem on displayContentOnClick");
        }  
    }
    
    
    
    @FXML
    void addNewOnAction(ActionEvent event) {
        try{
            if( !selectedRow.equals("") ){
                if(showingTable.equals("course")){
                    db.addCourse(layer3TextField.getText());
                }if(showingTable.equals("enrollment")){
                   db.addEnrollment(layer3TextField.getText());
                }if(showingTable.equals("student")){
                    db.addStudent(layer3TextField.getText());
                }
            }
            
        }catch(Exception ex){
            System.out.println("Problem on add new");
        }
        
    }
    
    @FXML
    void updateOnAction(ActionEvent event) {
        try{
            if( !selectedRow.equals("") ){
                if(showingTable.equals("course")){
                    if(!selectedRow.equals(layer3TextField.getText()) && !layer3TextField.getText().equals("")){
                        warnLabel.setText("");
                        db.updateCourse(selectedRow, layer3TextField.getText());
                    
                    }else{
                        warnLabel.setText("Values must be diffrent and filled for update");
                    }

                }if(showingTable.equals("enrollment")){
                    if(!selectedRow.equals(layer3TextField.getText()) && !layer3TextField.getText().equals("")){
                        warnLabel.setText("");
                        db.updateEnrollment(selectedRow, layer3TextField.getText());
                    
                    }else{
                        warnLabel.setText("Values must be diffrent and filled for update");
                    }

                }if(showingTable.equals("student")){
                    if(!selectedRow.equals(layer3TextField.getText()) && !layer3TextField.getText().equals("")){
                        warnLabel.setText("");
                        db.updateStudent(selectedRow, layer3TextField.getText());
                    
                    }else{
                        warnLabel.setText("Values must be diffrent and filled for update");
                    }

                }

            }
            
        }catch(Exception ex){
            System.out.println("Problem on update on action");
        }
        
        

    }
    
    @FXML
    void deleteOnAction(ActionEvent event)  {
        try{
            if( !selectedRow.equals("") ){
                if(showingTable.equals("course")){
                    System.out.println(selectedRow);
                    String[] x = selectedRow.split(",");
                    db.deleteCourse(x[0]);

                }if(showingTable.equals("enrollment")){
                    System.out.println(selectedRow);
                    String[] x = selectedRow.split(",");
                    db.deleteEnrollment(x[0],x[1]);

                }if(showingTable.equals("student")){
                    System.out.println(selectedRow);
                    String[] x = selectedRow.split(",");
                    db.deleteStudent(x[0]);

                }

            }
        }catch(Exception ex){
            System.out.println("Problem on delete on action");
        }
    }
  
    //DELETE FROM course WHERE `course`.`courseID` = '11111'
    //DELETE FROM enrollment WHERE `enrollment`.`ssn` = '444111110' AND `enrollment`.`courseID` = '11111
    //DELETE FROM `student` WHERE `student`.`ssn` = '444111118'
    
    

    @FXML
    private void customQueryOnClick(ActionEvent event) {
        layer2_1.setVisible(false);
        layer2_2.setVisible(true);
    }
    
     @FXML
    void executequeryOnAction(ActionEvent event) {
        try{
            
            

            ArrayList<String[]> queryResult = db.executeQuery(queryArea.getText());
            queryArea.setText("");
            resultArea.setText("");
            
            String[] printer = new String[queryResult.size()];
                for(int i = 0; i < queryResult.size();i++){
                    String[] row = queryResult.get(i);
                    String strRow = "";
                    for(int j = 0; j<row.length;j++){
                        if(j == row.length-1){
                            strRow += row[j];
                        }else{
                            strRow +=row[j] + ",";
                        }
                    }
                    System.out.println(strRow);
                    resultArea.appendText(strRow + "\n");
                    
                    printer[i] = strRow; 
                }
             
            
            
            
            
            
        }catch(Exception ex){
            //System.out.println("Problem on executequeryOnAction");
        }
    }
    
}
