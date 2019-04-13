package hexatorn.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * EN
 * Get data on receipts / invoices / payments for a period of one month from the database.
 * Becouse SQLite stores date as text. I get Yer, Month and Day seperately from the database and I create date with them.
 * PL
 * Pobranie danych o paragonach/rachunkach/płatnościach za okres jednego miesiąca z bazy danych.
 * Ze względu na to że SQLite przechowuje date jako tekst zamiast pomierać daty z bazy danych jest pobierany odzielnie rok, miesiąc i dzień i z nich jest składana Data
 */
public class DBBillsReader_PerMonth extends DBBillsReader {

    private String yer;
    private String month;

    public DBBillsReader_PerMonth(String yer, String month) {
        this.month = month;
        this.yer = yer;
    }

    public DBBillsReader_PerMonth(int yer, int month) {
        this.month = String.format("%02d", month);
        this.yer = ""+yer;
    }

    @Override
    protected PreparedStatement buildPrepatedStatment() {
        PreparedStatement selectBills = null;
        try {
            selectBills = connection.prepareStatement(
            select+
                "where strftime('%m',bill.transaction_date) = ?"+
                "and strftime('%Y',bill.transaction_date) = ?");
            selectBills.setString(1,month);
            selectBills.setString(2,yer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectBills;
    }
}
