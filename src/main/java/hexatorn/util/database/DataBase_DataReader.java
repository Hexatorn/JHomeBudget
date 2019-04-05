package hexatorn.util.database;

import hexatorn.data.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DataBase_DataReader {
    private static Connection connection;


    static public ObservableList<Bill> readBillsFromDataBase(){
        ObservableList<Bill> listOfBills = FXCollections.observableArrayList();
        connection = DataBase_CreateConnection.connect();
        ResultSet result;
        try {
            PreparedStatement selectBills = connection.prepareStatement(
                    "Select bill.id as Id, place.name as Place, goods.name as Gods, bill.amount as Amount, cat.name as Category, subcat.name as SubCategory, bill.transaction_date as Date" +
                            "  from Bills as bill " +
                            "  join "+Enum_TableName.Dictionary_Places+" as place on bill.id_place = place.id " +
                            "  join "+Enum_TableName.Dictionary_GoodsAndServices+" as goods on bill.id_GoodsOrServices = goods.id " +
                            "  join "+Enum_TableName.Categorys+" as cat on bill.id_category = cat.id " +
                            "  join "+Enum_TableName.Categorys+" as subcat on bill.id_sub_category = subcat.id "
            );
            result = selectBills.executeQuery();
            while (result.next()){
                listOfBills.add(new Bill(
                        result.getString("Place"),
                        result.getString("Gods"),
                        result.getDouble("Amount"),
                        result.getDate("Date"),
                        result.getString("Category"),
                        result.getString("SubCategory")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBills;
    }
}
