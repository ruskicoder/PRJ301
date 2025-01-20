/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class DBUtils {

    private static final String DB_Name = "Web_05_a";
    private static final String DB_Userame = "sa";
    private static final String DB_Password = "12345";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_Name;
        conn = DriverManager.getConnection(url, DB_Userame, DB_Password);
        return conn;
    }
    
    public static void main(String[] args) {
        try {
            Connection c = getConnection();
            System.out.println(c);
            
            String query = "INSERT INTO products (product_id, product_name, price, category, stock_quantity) VALUES\n" +
"(11, N'Canon EOS R', 28550000, N'Máy ảnh', 20)\n";
            
            Statement st = c.createStatement();
            int i = st.executeUpdate(query);
            System.out.println(i);
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
