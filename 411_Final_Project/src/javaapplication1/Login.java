package javaapplication1;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
    Dao conn = new Dao();

    //This will log in a user to the program
    public Login() {
        super("IIT HELP DESK LOGIN");
        this.conn.createTables();
        this.setSize(400, 210);
        this.setLayout(new GridLayout(4, 2));
        this.setLocationRelativeTo((Component)null);
        JLabel lblUsername = new JLabel("Username", 2);
        JLabel lblPassword = new JLabel("Password", 2);
        final JLabel lblStatus = new JLabel(" ", 0);
        final JTextField txtUname = new JTextField(10);
        final JPasswordField txtPassword = new JPasswordField();
        JButton btn = new JButton("Submit");
        JButton btnExit = new JButton("Exit");
        lblStatus.setToolTipText("Contact help desk to unlock password");
        lblUsername.setHorizontalAlignment(0);
        lblPassword.setHorizontalAlignment(0);
        this.add(lblUsername);
        this.add(txtUname);
        this.add(lblPassword);
        this.add(txtPassword);
        this.add(btn);
        this.add(btnExit);
        this.add(lblStatus);
        btn.addActionListener(new ActionListener() {
            int count = 0;

            //Logs in the user with the information provided and checks if they are a valid login.
            public void actionPerformed(ActionEvent e) {
                boolean admin = false;
                ++this.count;
                String query = "SELECT * FROM apavl_users WHERE uname = ? and upass = ?;";

                try {
                    Throwable var4 = null;
                    Object var5 = null;

                    //Checks to see if the username and password are correct and lists remaining attempts.
                    try {
                        PreparedStatement stmt = Login.this.conn.getConnection().prepareStatement(query);

                        try {
                            stmt.setString(1, txtUname.getText());
                            stmt.setString(2, txtPassword.getText());
                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                admin = rs.getBoolean("admin");
                                new Tickets(admin);
                                Login.this.setVisible(false);
                                Login.this.dispose();
                            } else {
                                lblStatus.setText("Try again! " + (3 - this.count) + " / 3 attempt(s) left");
                            }
                        } finally {
                            if (stmt != null) {
                                stmt.close();
                            }

                        }
                    } catch (Throwable var15) {
                        if (var4 == null) {
                            var4 = var15;
                        } else if (var4 != var15) {
                            var4.addSuppressed(var15);
                        }


                        throw var4;
                    }
                } catch (Throwable var16) {
                    var16.printStackTrace();
                }

            }
        });
        btnExit.addActionListener((e) -> {
            System.exit(0);
        });
        this.setVisible(true);
    }

    //Runs the program.
    public static void main(String[] args) {
        new Login();
    }
}
