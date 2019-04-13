package hexatorn.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DBBillsReader_FromDateToDate extends DBBillsReader {
    private LocalDate fromDate;
    private LocalDate toDate;

    public DBBillsReader_FromDateToDate(LocalDate fromDate,LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected PreparedStatement buildPrepatedStatment() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(select+
                "where transaction_date between ? AND ? ");
            preparedStatement.setString(1,fromDate.toString());
            preparedStatement.setString(2,toDate.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }
}
