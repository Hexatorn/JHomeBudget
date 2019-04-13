package hexatorn.util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * EN
 * Get years from stored data
 * PL
 * Zwraca lata z których są dane w bazie
 */
public  class DBReader_YersReader {
    public ArrayList<String> read(){
        ArrayList<String> arrayList = new ArrayList<>();
        Connection connection = DataBase_CreateConnection.connect();

        try {
            ResultSet result;
            PreparedStatement selectYers = connection.prepareStatement(
                    "Select strftime('%Y',transaction_date) as Yer " +
                            "from Bills " +
                            "group by Yer " +
                            "order by Yer desc");
            result = selectYers.executeQuery();

            while (result.next()){
                arrayList.add(result.getString("Yer"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
