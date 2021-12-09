package javaapplication1;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Dao {
    static Connection connect = null;
    Statement statement = null;

    public Dao() {
    }

    //Connects the Program to the external Database
    public Connection getConnection() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false&user=fp411&password=411");
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        return connect;
    }

    //Creates the new table in the external Database
    public void createTables() {
        String createTicketsTable = "CREATE TABLE apavl1_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date VARCHAR(30), end_date VARCHAR(30)";
        String var2 = "CREATE TABLE apavl_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

        //Creates both a users table and a tickets table
        try {
            this.statement = this.getConnection().createStatement();
            this.statement.executeUpdate("CREATE TABLE apavl1_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date VARCHAR(30), end_date VARCHAR(30))");
            this.statement.executeUpdate("CREATE TABLE apavl_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)");
            System.out.println("Created tables in given database...");
            this.statement.close();
            connect.close();
        }

        catch (Exception var4) {
            System.out.println(var4.getMessage());
        }

        this.addUsers();
    }

    //Adds users that will be able to login with information from a separate CSV File
    public void addUsers() {

        ArrayList array = new ArrayList();

        //Accesses the CSV File
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("./userlist.csv")));

            String line;
            while((line = br.readLine()) != null) {
                array.add(Arrays.asList(line.split(",")));
            }
        }
        catch (Exception var8) {
            System.out.println("There was a problem loading the file");
        }

        //Grabs the date from the file and inserts it
        try {

            Statement statement = this.getConnection().createStatement();
            Iterator var6 = array.iterator();

            while(var6.hasNext()) {
                List<String> rowData = (List)var6.next();
                String sql = "insert into apavl_users(uname,upass,admin) values('" + (String)rowData.get(0) + "'," + " '" + (String)rowData.get(1) + "','" + (String)rowData.get(2) + "');";
                statement.executeUpdate(sql);
            }

            System.out.println("Inserts completed in the given database...");
            statement.close();
        }
        catch (Exception var7) {
            System.out.println(var7.getMessage());
        }

    }

    //Inserts all the information into the tickets table along with returning the tables ID
    public int insertRecords(String ticketName, String ticketDesc, String sDate, String eDate) {
        int id = 0;

        try {

            this.statement = this.getConnection().createStatement();
            this.statement.executeUpdate("Insert into apavl1_tickets(ticket_issuer, ticket_description, start_date, end_date) values( '" + ticketName + "','" + ticketDesc + "','" + sDate + "','" + eDate + "')", 1);
            ResultSet resultSet = null;
            resultSet = this.statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        catch (SQLException var5) {
            var5.printStackTrace();
        }

        return id;
    }

    //Deletes a ticket from the table.
    public void deleteRecords(String id){

        //String sql = "DELETE FROM table WHERE ID = ?";

        try{

            statement = getConnection().createStatement();
            statement.executeUpdate("DELETE FROM apavl1_tickets WHERE ticket_id = " + id);
            System.out.println("Ticket" + id + "is now deleted from table");
        }
        catch (SQLException var5){
            var5.printStackTrace();
        }
    }

    //Reads the information that is in the table.
    public ResultSet readRecords() {
        ResultSet results = null;

        try {
            this.statement = connect.createStatement();
            results = this.statement.executeQuery("SELECT * FROM apavl1_tickets");
        }
        catch (SQLException var3) {
            var3.printStackTrace();
        }

        return results;
    }

    //Updates a ticket allowing your to modify an already created ticket.
    public void updateRecords(String id, String oldName, String newName, String oldDesc, String newDesc){

        try {

            statement = getConnection().createStatement();
            statement.executeUpdate("UPDATE apavl1_tickets SET " + oldName + " = '" + newName + "' WHERE ticket_id = " + id + ";");
            statement.executeUpdate("UPDATE apavl1_tickets SET " + oldDesc + " = '" + newDesc + "' WHERE ticket_id = " + id + ";");

            System.out.println("Record " + id + " is now updated to reflect changes");
        }

        catch (SQLException se){
            se.printStackTrace();
        }
    }

    //Closes a ticket which signifies that the ticket has been resolved.
    public void closeRecords(String id){

        try {
            statement = getConnection().createStatement();

            statement.executeUpdate("UPDATE tickets.apavl1_tickets SET end_date = current_timestamp() WHERE ticket_id = " + id + ";");
            System.out.println("Ticket id: " + id + " is now closed");
        }
        catch (SQLException se){
            se.printStackTrace();
        }
    }
}
