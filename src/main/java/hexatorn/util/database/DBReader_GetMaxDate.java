package hexatorn.util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/*
 * EN
 * Get max date
 * PL
 * Zwraca największą date zbazy
 */
public class DBReader_GetMaxDate {

    public LocalDate read(){
        LocalDate localDate = null;
        Connection connection = DataBase_CreateConnection.connect();
        try {
            ResultSet result;

            PreparedStatement selectMonthsAndYers = connection.prepareStatement(
                    "SELECT  strftime('%Y',transaction_date) as Yer, strftime('%m',transaction_date) as Month ,strftime('%d',transaction_date) as Day " +
                            "from Bills " +
                            "order by transaction_date DESC " +
                            "limit 1");
            result = selectMonthsAndYers.executeQuery();

            /*
             * EN
             * Tjis loop will only be done once. But it checks can result is not empty
             * PL
             * Ta pętla wykona się tylko raz. Ale sprawdza czy result nie jest pusty
             */
            while (result.next()){
                localDate = LocalDate.of(
                        result.getInt("Yer"),
                        result.getInt("Month"),
                        result.getInt("Day")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localDate;
    }
}
