package hexatorn.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBBillsReader_PerYer extends DBBillsReader{

    private String yer;
    public DBBillsReader_PerYer(String yer) {
        this.yer = yer;
    }


    @Override
    protected PreparedStatement buildPrepatedStatment() {
        PreparedStatement selectBills = null;
        try {
            selectBills = connection.prepareStatement(
                    select+ "where strftime('%Y',bill.transaction_date) = ?");
            selectBills.setString(1,yer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectBills;
    }
}
