package hexatorn.util.database;

import hexatorn.data.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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

        try {
            ResultSet result;
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
    * Get years from stored data
    * PL
    * Zwraca lata z których są dane w bazie
    */
    static public ArrayList<String> readYers(){
        ArrayList<String> arrayList = new ArrayList<>();
        connection = DataBase_CreateConnection.connect();

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
    /*
     * EN
     * Get months and years from stored data
     * PL
     * Zwraca miesiące i lata z których są dane w bazie
     */
    static public ArrayList<String> readMonthAndYersAsString(){
        ArrayList<String> arrayList = new ArrayList<>();
        connection = DataBase_CreateConnection.connect();

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

    /*
     * EN
     * Get max date
     * PL
     * Zwraca największą date zbazy
     */
    static public LocalDate getMaxDate(){
        LocalDate localDate = null;
        connection = DataBase_CreateConnection.connect();
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

    /*
     * EN
     * Get min date
     * PL
     * Zwraca najmniejszą date zbazy
     */
    static public LocalDate getMinDate(){
        LocalDate localDate = null;
        connection = DataBase_CreateConnection.connect();
        try {
            ResultSet result;

            PreparedStatement selectMonthsAndYers = connection.prepareStatement(
            "SELECT  strftime('%Y',transaction_date) as Yer, strftime('%m',transaction_date) as Month ,strftime('%d',transaction_date) as Day " +
                "from Bills " +
                "order by transaction_date ASC " +
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
