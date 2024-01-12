/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbproject;
import java.awt.BorderLayout;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Eren
 */
public class DbObject {
    

    
    
    public DbObject() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            
        }catch(Exception ex){
            System.out.println("Problem has occured when creating DbObject");
        }
        
    }
    
    
    
    //Db de bulunan bütün table isimleri
    public ArrayList<String> getTables() throws SQLException {
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");

        DatabaseMetaData dbMetaData = connection.getMetaData();

        ResultSet rsTables = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});

        ArrayList<String> tableList = new ArrayList<String>();

        while (rsTables.next()) {

            tableList.add(rsTables.getString("TABLE_NAME"));
        }

        // Close the connection
        connection.close();
        return tableList;
       
    }
    
    //Bir tablenin bütün elemanları
    public ArrayList<String[]> getATable(String tableName) throws SQLException{
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        
         // Create a statement
        Statement statement = connection.createStatement();

        // Execute a statement
        ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + tableName + "`");
        
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        

        
        ArrayList<String[]> returner = new ArrayList<String[]>();
        while (resultSet.next()) {
            String[] row = new String[columnsNumber];
            for(int i = 1; i <= columnsNumber;i++){
                row[i-1] = resultSet.getString(i).toString();
            }
            returner.add(row);
        }

        // Close the connection
        connection.close();
        return returner;
    
    }
    
    
    public ArrayList<String[]> executeQuery(String query) throws SQLException{
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        
        // Create a statement
        Statement statement = connection.createStatement();
        //Execute statement
        ResultSet resultSet = statement.executeQuery(query);
        
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        
         ArrayList<String[]> returner = new ArrayList<String[]>();
        while (resultSet.next()) {
            String[] row = new String[columnsNumber];
            for(int i = 1; i <= columnsNumber;i++){
                row[i-1] = resultSet.getString(i).toString();
            }
            returner.add(row);
        }

        // Close the connection
        connection.close();
        return returner;
        
        
    }
    
    //DELETE FROM course WHERE `course`.`courseID` = '11111'
    //DELETE FROM enrollment WHERE `enrollment`.`ssn` = '444111110' AND `enrollment`.`courseID` = '11111
    
    //DELETE FROM `student` WHERE `student`.`ssn` = '444111118' - 
    
    
    
    
    public void deleteStudent(String ssn) throws SQLException{
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "delete from student where ssn = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, ssn);
        
        preparedStmt.execute();
      
        connection.close();
        
    }
    
    
    public void deleteEnrollment(String ssn ,String courseID ) throws SQLException{
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "delete from enrollment where ssn = ? and courseID = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, ssn);
        preparedStmt.setString(2, courseID);
        
        preparedStmt.execute();
      
        connection.close();
    }
    
    
    public void deleteCourse(String courseID) throws SQLException{
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "delete from course where courseID = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, courseID);
        
        preparedStmt.execute();
      
        connection.close();
    }
    

    public void updateStudent(String row,String changedRow) throws SQLException{
        String[] rowArr = row.split(",");
        String[] chRowArr = changedRow.split(",");
        String ssn = rowArr[0];
                
        String c1 = "ssn = ";
        String c2 = "fName = ";
        String c3 = "mi = ";
        String c4 = "lName = ";
        String c5 = "bDate = ";
        String c6 = "street = ";
        String c7 = "phone = ";
        String c8 = "zipCode = ";
        String c9 = "deptId = ";
        String[] columnNames = {c1,c2,c3,c4,c5,c6,c7,c8,c9};
        
        
        ArrayList<String> x = new ArrayList<String>();
        
        for(int i = 0;i<rowArr.length;i++){
            //Hücre değiştirilmişse
            if(!rowArr[i].equals(chRowArr[i])){
                x.add(columnNames[i] + "'" + chRowArr[i] + "'" );
            }
        }
        //fname "akjbfkajsn" , sname = "alksflkasf" 
        String afterSetQuery = "";
        
        for(int i = 0; i<x.size();i++){
            if(i == x.size()-1){
                afterSetQuery += x.get(i);
            }else{
                afterSetQuery += x.get(i) + " , ";
            }
        }
        
        System.out.println(afterSetQuery);
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "update student set " + afterSetQuery + " where ssn = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, ssn);
        
        preparedStmt.execute();
      
        connection.close();
    
    }
    public void updateEnrollment(String row,String changedRow) throws SQLException{
        String[] rowArr = row.split(",");
        String[] chRowArr = changedRow.split(",");
        String ssn = rowArr[0];
        String courseID = rowArr[1];
                
        String c1 = "ssn   = ";
        String c2 = "courseID  = ";
        String c3 = "dateReg = ";
        String c4 = "grade = ";
        
        
        String[] columnNames = {c1,c2,c3,c4};
        
        
        ArrayList<String> x = new ArrayList<String>();
        
        for(int i = 0;i<rowArr.length;i++){
            //Hücre değiştirilmişse
            if(!rowArr[i].equals(chRowArr[i])){
                x.add(columnNames[i] + "'" + chRowArr[i] + "'" );
            }
        }
        //fname "akjbfkajsn" , sname = "alksflkasf" 
        String afterSetQuery = "";
        
        for(int i = 0; i<x.size();i++){
            if(i == x.size()-1){
                afterSetQuery += x.get(i);
            }else{
                afterSetQuery += x.get(i) + " , ";
            }
        }
        
        System.out.println(afterSetQuery);
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "update enrollment set " + afterSetQuery + " where ssn = ? and courseID = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, ssn);
        preparedStmt.setString(2, courseID);
        
        preparedStmt.execute();
      
        connection.close();
    
    
    }
    
    
    public void updateCourse(String row,String changedRow) throws SQLException{
        String[] rowArr = row.split(",");
        String[] chRowArr = changedRow.split(",");
        String courseID = rowArr[0];
                
        String c1 = "courseID  = ";
        String c2 = "subjectID = ";
        String c3 = "courseNum = ";
        String c4 = "title = ";
        String c5 = "numCredit = ";
        
        String[] columnNames = {c1,c2,c3,c4,c5};
        
        
        ArrayList<String> x = new ArrayList<String>();
        
        for(int i = 0;i<rowArr.length;i++){
            //Hücre değiştirilmişse
            if(!rowArr[i].equals(chRowArr[i])){
                x.add(columnNames[i] + "'" + chRowArr[i] + "'" );
            }
        }
        //fname "akjbfkajsn" , sname = "alksflkasf" 
        String afterSetQuery = "";
        
        for(int i = 0; i<x.size();i++){
            if(i == x.size()-1){
                afterSetQuery += x.get(i);
            }else{
                afterSetQuery += x.get(i) + " , ";
            }
        }
        
        System.out.println(afterSetQuery);
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "update course set " + afterSetQuery + " where courseID = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, courseID);
        
        preparedStmt.execute();
      
        connection.close();
    }
    
    
    
    
    public void addCourse(String row) throws SQLException{
        
        String[] values = row.split(",");
        
        
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "INSERT INTO course (courseID, subjectID, courseNum, title, numCredit) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        
       
        preparedStmt.setString(1, values[0] );
        preparedStmt.setString(2, values[1] );
        preparedStmt.setString(3, values[2] );
        preparedStmt.setString(4, values[3] );
        preparedStmt.setString(5, values[4] );
        
        
        preparedStmt.execute();
      
        connection.close();
        
    }
    public void addStudent(String row) throws SQLException{
        String[] values = row.split(",");
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "INSERT INTO student (ssn, fName, mi, lName , bDate , street , phone , zipCode , deptId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        
       
        preparedStmt.setString(1, values[0] );
        preparedStmt.setString(2, values[1] );
        preparedStmt.setString(3, values[2] );
        preparedStmt.setString(4, values[3] );
        preparedStmt.setString(5, values[4] );
        preparedStmt.setString(6, values[5] );
        preparedStmt.setString(7, values[6] );
        preparedStmt.setString(8, values[7] );
        preparedStmt.setString(9, values[8] );
        
        
        preparedStmt.execute();
      
        connection.close();
    
    }
    public void addEnrollment(String row) throws SQLException{
        String[] values = row.split(",");
        
        // Connect to a database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/courseregistration", "root", "");
        System.out.println("Database connected");
        //String query = "DELETE FROM `student` WHERE `student`.`ssn` = '"+ssn+"'";
        String query = "INSERT INTO enrollment (ssn, courseID, dateReg, grade) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        
       
        preparedStmt.setString(1, values[0] );
        preparedStmt.setString(2, values[1] );
        preparedStmt.setString(3, values[2] );
        preparedStmt.setString(4, values[3] );
        
        
        
        preparedStmt.execute();
      
        connection.close();
    }
    
    
    public static void main(String[] args) throws SQLException {
        DbObject a = new DbObject();
        a.addCourse("1122,ASD,1305,Java I,5");
    }
    
    
}
