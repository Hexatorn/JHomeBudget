package hexatorn.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBBillsReader_All extends DBBillsReader {
    @Override
    protected PreparedStatement buildPrepatedStatment() {
        PreparedStatement selectBills = null;
        try {
            selectBills = connection.prepareStatement(select);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectBills;
    }
}
