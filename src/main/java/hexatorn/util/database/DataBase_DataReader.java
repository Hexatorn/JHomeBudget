package hexatorn.util.database;

import hexatorn.data.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class DataBase_DataReader {
    private static Connection connection;


    /*
    * EN
    * Get data on receipts / invoices / payments for a period of one month from the database.
    * Becouse SQLite stores date as text. I get Yer, Month and Day seperately from the database and I create date with them.
    * PL
    * Pobranie danych o paragonach/rachunkach/płatnościach za okres jednego miesiąca z bazy danych.
    * Ze względu na to że SQLite przechowuje date jako tekst zamiast pomierać daty z bazy danych jest pobierany odzielnie rok, miesiąc i dzień i z nich jest składana Data
    */

    static public ObservableList<Bill> readBillsFromOneMonthFromDataBase(String yer,String month){
        ObservableList<Bill> listOfBills = FXCollections.observableArrayList();
        connection = DataBase_CreateConnection.connect();

        ResultSet result;
        try {
            PreparedStatement selectBills = connection.prepareStatement(
                    "Select bill.id as Id, place.name as Place, goods.name as Gods, bill.amount as Amount, cat.name as Category, subcat.name as SubCategory, " +
                         "strftime('%Y',bill.transaction_date) as Yer, strftime('%m',bill.transaction_date) as Month ,strftime('%d',bill.transaction_date) as Day "+
                         "  from Bills as bill " +
                         "  join "+Enum_TableName.Dictionary_Places+" as place on bill.id_place = place.id " +
                         "  join "+Enum_TableName.Dictionary_GoodsAndServices+" as goods on bill.id_GoodsOrServices = goods.id " +
                         "  join "+Enum_TableName.Categorys+" as cat on bill.id_category = cat.id " +
                         "  join "+Enum_TableName.Categorys+" as subcat on bill.id_sub_category = subcat.id "+
                         "where strftime('%m',bill.transaction_date) = ?"+
                         "and strftime('%Y',bill.transaction_date) = ?"
            );
            selectBills.setString(1,month);
            selectBills.setString(2,yer);

            result = selectBills.executeQuery();

            while (result.next()){

                listOfBills.add(new Bill(
                        result.getString("Place"),
                        result.getString("Gods"),
                        result.getDouble("Amount"),
                        textToDate(
                                result.getInt("Yer"),
                                result.getInt("Month"),
                                result.getInt("Day")
                        ),
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

    /*
    * EN
    * Its a part of convert string date to Date Object
    * PL
    * Część mechanizmu konwersji z daty zapisanej jako string na obiekt Date
    */
    static private Date textToDate(int yer,int month, int day ){
        Date outDate;
        Calendar calendar = Calendar.getInstance();
        /*
        * EN
        * SQLite counts months from 1 to 12. 1 is January
        * Class Date created by Calendar class counts months from 0 to 11. 0 is January
        * PL
        * Sqlite liczy miesiące od 1-12. 1 to styczeń itd.
        * klasa Date stworzona za pomocą klasy Calendar liczy miesiące od 0-11. 0 to styczeń.
        */
        calendar.set(yer,month-1,day);
        outDate = calendar.getTime();
        return outDate;
    }

}
