package hexatorn.util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * EN
 * Get months and years from stored data
 * PL
 * Zwraca miesiące i lata z których są dane w bazie
 */
public class DBReader_MonthsAndYersAsString {
    public ArrayList<String> read(){
        ArrayList<String> arrayList = new ArrayList<>();
        Connection connection = DataBase_CreateConnection.connect();

        try {
            ResultSet result;

            PreparedStatement selectMonthsAndYers = connection.prepareStatement(
                    "Select strftime('%Y',transaction_date) as Yer, strftime('%m',transaction_date) as Month, "+
                            "strftime('%Y-%m' ,transaction_date) as 'Month and Yer' " +
                            "from Bills " +
                            "group by Yer, Month " +
                            "order by Yer desc, Month desc");
            result = selectMonthsAndYers.executeQuery();

            while (result.next()){
                arrayList.add(result.getString("Month and Yer"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
