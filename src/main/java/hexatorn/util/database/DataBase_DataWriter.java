package hexatorn.util.database;

import hexatorn.data.Bill;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import java.sql.*;


public class DataBase_DataWriter {

    private static Connection connection;
    private static Statement query;

    /*
    * EN
    * Main method realize writing receipts to the database.
    * PL
    * Główna metoda realizująca zapisa paragonów/płatności/operacji finansowych do bazy danych.
    */
    static public void writeToBase(ObservableList<Bill> listOfBills, ProgressBar progressBar, double step){

        /*
        * EN
        * Open connection with database
        * PL
        * Otwarcie połączenia z bazą danych
        */
        connection = DataBase_CreateConnection.connect();
        /*
         * EN
         * Create Statment -> Object allows communication with a database
         * PL
         * Stworzenie obiektu umożliwiającego komunikację z bazą danych.
         */
        query = DataBase_CreateConnection.getQueryStatment();

        for (Bill bill:listOfBills) {
            addBill(bill);
            progressBar.setProgress(progressBar.getProgress()+step);
            progressBar.setProgress(progressBar.getProgress()+step);
        }
        /*
        * EN
        * Close connection with database
        * PL
        * Zamknięcie połączenia z bazą danych
        */
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Nieudane zamknięcie połączenia z bazą danych");
            e.printStackTrace();
        }
    }

    /*static public void writeToBase (ObservableList<Bill> listOfBill){
        ProgressBar fakeProgressBar  = new ProgressBar();
        double fakeStep = 0;
        writeToBase(listOfBill,fakeProgressBar,fakeStep);
    }*/

    /*
     * EN
     * Write data to data base
     * PL
     * Zapis danych do bazy danych
     */
    private static int addBill(Bill bill){

        int placeID, goodID, catID, subCatID;
        
        placeID = addToDictionary(bill.getPlace(), Enum_DictionaryTableName.Dictionary_Places );
        goodID = addToDictionary(bill.getGoodsOrServices(), Enum_DictionaryTableName.Dictionary_GoodsAndServices);
        catID = addCategory(bill.getCategory(),0);
        //todo sprawdzić czy kat ID jest rootem
        subCatID = addCategory(bill.getSubcategory(),catID);

        System.out.println(bill.getStringDate());

        try {
            PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO "+ Enum_TableName.Bills +" (id_place,id_GoodsOrServices,amount,id_category,id_sub_category,transaction_date) VALUES (?,?,?,?,?,?)");
            //id_place
            insert.setInt(1,placeID);
            //id_GoodsOrServices
            insert.setInt(2,goodID);
            //amount
            insert.setDouble(3,bill.getAmount());
            //id_category
            insert.setInt(4,catID);
            //id_sub_category
            insert.setInt(5,subCatID);
            //transaction_data
            //System.out.println(bill.getDate());
            insert.setString(6,bill.getStringDate());
            //create_data

            insert.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /*
     * EN
     * Check can value exist in database. Exacly in category table.
     * If value exist in data base, returned its id.
     * If value not exist in data base, add it to database and return its id.
     * As parent should be set anther category id for subcategory or 0 for main category
     * PL
     * Sprawdza czy w tabeli słownikowej jest wartość.
     * Jeżeli jet to zwraca jej id z bazy danych.
     * Jeżeli nie to dodaje do bazy danych i zwraca jej id.
     * Jako rodzic (parent) powinno zostać podane id innej kategori jeżeli chcemy stworzyć podkategorię lub 0 dla głównej kategori.
     */
    private static  int addCategory (String value,int parent){
        ResultSet result;
        try {
            PreparedStatement selectCategory = connection.prepareStatement(
                    "SELECT id FROM "+ Enum_TableName.Categorys +" WHERE name = ?" );
            selectCategory.setString(1,value);
            result = selectCategory.executeQuery();

            if(!result.next()){
                PreparedStatement insertCategory;
                if (parent == 0){
                    insertCategory = connection.prepareStatement(
                            "INSERT INTO "+ Enum_TableName.Categorys.toString()+" (name) VALUES (?)");
                }
                else {
                    insertCategory = connection.prepareStatement(
                            "INSERT INTO "+ Enum_TableName.Categorys.toString()+" (name, parent) VALUES (?,?)");
                    insertCategory.setInt(2,parent);
                }

                insertCategory.setString(1,value);
                insertCategory.execute();

                result = selectCategory.executeQuery();
                return result.getInt("id");
            }
            else {
                return result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
    }

    /*
    * EN
    * Check can value exist in database. Exacly in dictionary table.
    * If value exist in data base, returned its id.
    * If value not exist in data base, add it to database and return its id.
    * PL
    * Sprawdza czy w tabeli słownikowej jest wartość.
    * Jeżeli jet to zwraca jej id z bazy danych.
    * Jeżeli nie to dodaje do bazy danych i zwraca jej id.
    */
    private static int addToDictionary(String value, Enum_DictionaryTableName table_name) {
        try {
            PreparedStatement select = connection.prepareStatement(
                    "SELECT id FROM "+ table_name.toString()+" WHERE name = ?");
            select.setString(1,value);
            ResultSet result = select.executeQuery();
            if (!result.next()){
                PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO "+ table_name.toString()+" (name) VALUES (?)");
                insert.setString(1,value);
                insert.execute();
                result = select.executeQuery();
            }
            return result.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
