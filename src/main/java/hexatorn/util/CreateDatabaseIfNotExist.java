package hexatorn.util;

import java.sql.*;

public class CreateDatabaseIfNotExist {

    private static Connection connection;
    private static Statement query;

    public static void create(){
        connection = CreateConnectionToDatabase.connect();
        query = CreateConnectionToDatabase.getQueryStatment();

        if(!createTables()){
            System.err.println("Koniec Programu z powodu nie utworzenia Tabel");
            return;
        }

        fillTabelList();

        if(!createTrigers()){
            System.err.println("Koniec Programu z powodu nie utworzenia Trigerów");
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static boolean createTrigers(){
        try {
            createTrigerAfterInsert(Triger_Name.afterInsertToDictionaryPlace , Table_Name.Dictionary_Places);
            createTrigerAfterInsert(Triger_Name.afterInsertToDictionaryGoodsAndServices,Table_Name.Dictionary_GoodsAndServices);
            createTrigerAfterInsert(Triger_Name.afterInsertToCategory,Table_Name.Categorys);
            createTrigerAfterInsert(Triger_Name.afterInsertToBills,Table_Name.Bills);

            createTrigerAfterUpdate(Triger_Name.afterUpdateDictionaryPlace,Table_Name.Dictionary_Places);
            createTrigerAfterUpdate(Triger_Name.afterUpdateDictionaryGoodsAndServices,Table_Name.Dictionary_GoodsAndServices);
            createTrigerAfterUpdate(Triger_Name.afterUpdateCategory,Table_Name.Categorys);
            createTrigerAfterUpdate(Triger_Name.afterUpdateBills,Table_Name.Bills);

        } catch (SQLException e) {
            System.err.println("Bład przy tworzeniu Trigerów");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean createTables()  {

        String createTableList = "CREATE TABLE IF NOT EXISTS "+ Table_Name.TableList +" ( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(255))";

        String createSync = "CREATE TABLE IF NOT EXISTS "+ Table_Name.Sync +" ( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "table_id Integer not null, " +
                "row_id Integer not null, " +
                "create_date INTEGER NOT NULL, " +
                "last_edit_date INTEGER)";

        String createDictionaryPlaces = "CREATE TABLE IF NOT EXISTS "+Table_Name.Dictionary_Places+" (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " name varchar(255) NOT NULL)";

        String createDictionaryGoodsAndServices = "CREATE TABLE IF NOT EXISTS "+Table_Name.Dictionary_GoodsAndServices+" (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " name varchar(255) NOT NULL)";

        String createCategory = "CREATE TABLE IF NOT EXISTS "+Table_Name.Categorys +" ("+
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "name varchar(255) NOT NULL,"+
                "parent INTEGER)";
        String createBill = "CREATE TABLE IF NOT EXISTS "+Table_Name.Bills +" ("+
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "id_place INTEGER NOT NULL , "+
                "id_GoodsOrServices INTEGER NOT NULL , "+
                "amount REAL NOT NULL, "+
                "id_category INTEGER NOT NULL , "+
                "id_sub_category INTEGER NOT NULL , "+
                "transaction_date INTEGER NOT NULL)";

        String createLocalVariable = "CREATE TABLE IF NOT EXISTS "+ Table_Name.LocalVariable +" ( " +
                "local_variable INTEGER)";


        try{
            query.execute(createTableList);
            query.execute(createSync);
            query.execute(createDictionaryPlaces);
            query.execute(createDictionaryGoodsAndServices);
            query.execute(createCategory);
            query.execute(createBill);
            query.execute(createLocalVariable);

        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void fillTabelList(){
        for (Table_Name tb : Table_Name.values()) {
            try {
                PreparedStatement select = connection.prepareStatement("select id from "+ Table_Name.TableList +" where name = ?");
                select.setString(1,tb.toString());
                ResultSet result = select.executeQuery();
                if(!result.next()){
                    PreparedStatement insert = connection.prepareStatement(
                            "INSERT INTO "+ Table_Name.TableList+"(name) VALUES (?) ");
                    insert.setString(1,tb.toString());
                    insert.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private static void createTrigerAfterInsert(Triger_Name trigerName, Table_Name tableName) throws SQLException {

        PreparedStatement triger = connection.prepareStatement(
                "Create TRIGGER IF NOT EXISTS "+trigerName+" after insert on "+ tableName +" "+
                        "begin "+
                        "INSERT INTO LocalVariable (local_variable) values(datetime('now'));"+
                        "Insert into Sync (table_id , row_id , create_date) "+
                        "values( "+
                        "(Select id from TableList "+
                        "where name = \'"+ tableName + "\' "+
                        "limit 1), "+
                        "NEW.id, "+
                        "(Select local_variable from LocalVariable order by local_variable desc limit 1 ) "+
                        ");"+
                        "delete from LocalVariable;"+
                        "end ");
        triger.execute();
    }

    private static void createTrigerAfterUpdate(Triger_Name trigerName, Table_Name tableName) throws SQLException {

        PreparedStatement triger = connection.prepareStatement(
                "Create TRIGGER IF NOT EXISTS "+trigerName+" after update on "+ tableName +" "+
                        "begin "+
                        "INSERT INTO LocalVariable (local_variable) values(datetime('now'));"+
                        "Insert into Sync (table_id , row_id , create_date, last_edit_date) "+
                        "values( "+
                        //table_id
                        "(Select id from TableList "+
                        "where name = \'"+ tableName + "\' "+
                        "limit 1), "+
                        //row_id
                        "NEW.id, "+
                        //create_date
                        "(Select create_date from Sync "+
                        "where "+
                        "table_id = (Select id from TableList "+
                        "where name = \'"+ tableName + "\' "+
                        "limit 1) and "+
                        "row_id = OLD.id "+
                        "limit 1), "+
                        //last_edit_date
                        "(Select local_variable from LocalVariable "+
                        "order by local_variable desc "+
                        " limit 1 ) "+
                        "); "+
                        "delete from LocalVariable; "+
                        "end ");
        triger.execute();
    }

}
