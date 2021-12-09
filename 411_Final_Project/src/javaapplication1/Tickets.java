//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javaapplication1;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Tickets extends JFrame implements ActionListener {
    Dao dao = new Dao();
    Boolean chkIfAdmin = null;
    private JMenu mnuFile = new JMenu("File");
    private JMenu mnuAdmin = new JMenu("Admin");
    private JMenu mnuTickets = new JMenu("Tickets");
    JMenuItem mnuItemExit;
    JMenuItem mnuItemUpdate;
    JMenuItem mnuItemDelete;
    JMenuItem mnuItemOpenTicket;
    JMenuItem mnuItemViewTicket;
    JMenuItem mnuItemCloseTickets;

    //Creates the initial tickets
    public Tickets(Boolean isAdmin) {
        this.chkIfAdmin = isAdmin;
        this.createMenu();
        this.prepareGUI();
    }

    //Creates the menu that will be used for the entire program
    private void createMenu() {

        //Creates individual GUI elements and adds them to the ticket pogram
        this.mnuItemExit = new JMenuItem("Exit");
        this.mnuFile.add(this.mnuItemExit);
        this.mnuItemUpdate = new JMenuItem("Update Ticket");
        this.mnuAdmin.add(this.mnuItemUpdate);
        this.mnuItemDelete = new JMenuItem("Delete Ticket");
        this.mnuAdmin.add(this.mnuItemDelete);
        this.mnuItemOpenTicket = new JMenuItem("Open Ticket");
        this.mnuTickets.add(this.mnuItemOpenTicket);
        this.mnuItemViewTicket = new JMenuItem("View Ticket");
        this.mnuTickets.add(this.mnuItemViewTicket);
        this.mnuItemCloseTickets = new JMenuItem("Close Ticket");
        this.mnuTickets.add(this.mnuItemCloseTickets);

        //Adds actions such as buttons to the program
        this.mnuItemExit.addActionListener(this);
        this.mnuItemUpdate.addActionListener(this);
        this.mnuItemDelete.addActionListener(this);
        this.mnuItemOpenTicket.addActionListener(this);
        this.mnuItemViewTicket.addActionListener(this);
        this.mnuItemCloseTickets.addActionListener(this);
    }

    //Creates the table and makes certain elements only visible to those that are admins
    private void prepareGUI() {
        JMenuBar bar = new JMenuBar();
        bar.add(this.mnuFile);
        if (chkIfAdmin == true) {
            bar.add(this.mnuAdmin);
        }
        bar.add(this.mnuTickets);
        this.setJMenuBar(bar);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });
        this.setSize(400, 400);
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
    }

    //Creates different functions based on which button or element is interacted with.
    public void actionPerformed(ActionEvent e) {

        //Exits the program when exit button is clicked
        if (e.getSource() == this.mnuItemExit) {
            System.exit(0);

            //Opens a new ticket when button is clicked
        } else if (e.getSource() == this.mnuItemOpenTicket) {
            String ticketName = JOptionPane.showInputDialog((Component)null, "Enter your name");
            String ticketDesc = JOptionPane.showInputDialog((Component)null, "Enter a ticket description");
            String ticketStart = JOptionPane.showInputDialog((Component)null, "Enter the start date");
            String ticketEnd = JOptionPane.showInputDialog((Component)null, "Enter the end date");
            int id = this.dao.insertRecords(ticketName, ticketDesc, ticketStart, ticketEnd);
            if (id != 0) {
                System.out.println("Ticket ID : " + id + " created successfully!!!");
                JOptionPane.showMessageDialog((Component)null, "Ticket id: " + id + " created");
            } else {
                System.out.println("Ticket cannot be created!!!");
            }

            //Allows you to view all created tickets when button is clicked
        } else if (e.getSource() == this.mnuItemViewTicket) {
            try {
                JTable jt = new JTable(ticketsJTable.buildTableModel(this.dao.readRecords()));
                jt.setBounds(30, 40, 200, 400);
                JScrollPane sp = new JScrollPane(jt);
                this.add(sp);
                this.setVisible(true);
            } catch (SQLException var5) {
                var5.printStackTrace();
            }
        }

        //Deletes a ticket when the button is clicked
        else if (e.getSource() == this.mnuItemDelete){

            String ticketId = JOptionPane.showInputDialog((Component)null, "Please enter which ticket's id to be deleted");

            int resp = JOptionPane.showConfirmDialog(null, "Are you sure you wish to proceed?", "Delete ticket", JOptionPane.YES_NO_OPTION);

            if(resp == JOptionPane.YES_OPTION) {
                dao.deleteRecords(ticketId);
                JOptionPane.showMessageDialog(null, "Ticket: " + ticketId + " has been deleted");
                System.out.println("Record is gone");
            }
            else
                dispose();
        }

        //Updates an already created ticket when the button is clicked.
        else if (e.getSource() == this.mnuItemUpdate){

           try {

               String id = JOptionPane.showInputDialog(null, "Please enter which ticket ID you want updated");

               String oldName;
               String oldDesc;
               String newName;
               String newDesc;

               oldName = "ticket_issuer";
               newName = JOptionPane.showInputDialog(null, "Whats your new name?");

               oldDesc = "ticket_description";
               newDesc = JOptionPane.showInputDialog(null, "Whats the new description?");

               dao.updateRecords(id, oldName, newName, oldDesc, newDesc);
               System.out.println("Updated");
               JOptionPane.showMessageDialog(null, "Ticket " + id + " has been updated");

           }
           catch (Exception se){
               se.printStackTrace();
           }
        }

        //Closes the ticket when the button is clicked
        else if(e.getSource() == mnuItemCloseTickets){

            try {

                String id = JOptionPane.showInputDialog(null, "Please enter the ID of ticket that you want to close");

                int resp = JOptionPane.showConfirmDialog(null, "Are you sure you wish to proceed?", "Close", JOptionPane.YES_NO_OPTION);

                if (resp == JOptionPane.YES_OPTION){

                    dao.closeRecords(id);
                }
            }
            catch (Exception se){
                se.printStackTrace();
            }
        }
    }
}
