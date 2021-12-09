//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javaapplication1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ticketsJTable {
    private final DefaultTableModel tableModel = new DefaultTableModel();

    public ticketsJTable() {
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector();
        int columnCount = metaData.getColumnCount();

        for(int column = 1; column <= columnCount; ++column) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector data = new Vector();

        while(rs.next()) {
            Vector<Object> vector = new Vector();

            for(int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                vector.add(rs.getObject(columnIndex));
            }

            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
