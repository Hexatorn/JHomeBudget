package hexatorn.util;

import hexatorn.data.Bill;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;


public class WriteToDataBase {

    /*public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:baza.db";*/
    private static Connection connection;
    private static Statement query;



    static public void WriteToBase(ObservableList<Bill> listOfBills){

        connection = CreateConnectionToDatabase.connect();
        query = CreateConnectionToDatabase.getQueryStatment();



        for (Bill bill:listOfBills) {
            addBill(bill);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Nieudane zamknięcie połączenia z bazą danych");
            e.printStackTrace();
        }

    }

    private static int addBill(Bill bill){

        int placeID, goodID, catID, subCatID;
        
        placeID = addToDictionary(bill.getPlace(), Table_Name.Dictionary_Places );
        goodID = addToDictionary(bill.getGoodsOrServices(), Table_Name.Dictionary_GoodsAndServices);
        catID = addCategory(bill.getCategory(),0);
        //todo sprawdzić czy kat ID jest rootem
        subCatID = addCategory(bill.getSubcategory(),catID);

        try {
            PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO "+Table_Name.Bills +" (id_place,id_GoodsOrServices,amount,id_category,id_sub_category,transaction_date) VALUES (?,?,?,?,?,?)");
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
            insert.setString(6,bill.getDate().toString());
            //create_data

            insert.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }



    private static  int addCategory (String value,int parent){
        ResultSet result;
        try {
            PreparedStatement selectCategory = connection.prepareStatement(
                    "SELECT id FROM "+Table_Name.Categorys +" WHERE name = ?" );
            selectCategory.setString(1,value);
            result = selectCategory.executeQuery();

            if(!result.next()){
                PreparedStatement insertCategory;
                if (parent == 0){
                    insertCategory = connection.prepareStatement(
                            "INSERT INTO "+Table_Name.Categorys.toString()+" (name) VALUES (?)");
                }
                else {
                    insertCategory = connection.prepareStatement(
                            "INSERT INTO "+Table_Name.Categorys.toString()+" (name, parent) VALUES (?,?)");
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

    private static int addToDictionary(String value, Table_Name table_name) {
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
            else{
            }
            return result.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }







}
