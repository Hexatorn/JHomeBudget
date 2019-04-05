package hexatorn.util.database;

import java.sql.*;

public class DataBase_CreateDataBase {

    private static Connection connection;
    private static Statement query;

    public static void create(){
        connection = DataBase_CreateConnection.connect();
        query = DataBase_CreateConnection.getQueryStatment();

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
            createTrigerAfterInsert(Enum_TrigerName.afterInsertToDictionaryPlace , Enum_TableName.Dictionary_Places);
            createTrigerAfterInsert(Enum_TrigerName.afterInsertToDictionaryGoodsAndServices, Enum_TableName.Dictionary_GoodsAndServices);
            createTrigerAfterInsert(Enum_TrigerName.afterInsertToCategory, Enum_TableName.Categorys);
            createTrigerAfterInsert(Enum_TrigerName.afterInsertToBills, Enum_TableName.Bills);

            createTrigerAfterUpdate(Enum_TrigerName.afterUpdateDictionaryPlace, Enum_TableName.Dictionary_Places);
            createTrigerAfterUpdate(Enum_TrigerName.afterUpdateDictionaryGoodsAndServices, Enum_TableName.Dictionary_GoodsAndServices);
            createTrigerAfterUpdate(Enum_TrigerName.afterUpdateCategory, Enum_TableName.Categorys);
            createTrigerAfterUpdate(Enum_TrigerName.afterUpdateBills, Enum_TableName.Bills);

        } catch (SQLException e) {
            System.err.println("Bład przy tworzeniu Trigerów");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean createTables()  {

        String createTableList = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.TableList +" ( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(255))";

        String createSync = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.Sync +" ( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "table_id Integer not null, " +
                "row_id Integer not null, " +
                "create_date INTEGER NOT NULL, " +
                "last_edit_date INTEGER)";

        String createDictionaryPlaces = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.Dictionary_Places+" (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " name varchar(255) NOT NULL)";

        String createDictionaryGoodsAndServices = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.Dictionary_GoodsAndServices+" (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " name varchar(255) NOT NULL)";

        String createCategory = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.Categorys +" ("+
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "name varchar(255) NOT NULL,"+
                "parent INTEGER)";
        String createBill = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.Bills +" ("+
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                "id_place INTEGER NOT NULL , "+
                "id_GoodsOrServices INTEGER NOT NULL , "+
                "amount REAL NOT NULL, "+
                "id_category INTEGER NOT NULL , "+
                "id_sub_category INTEGER NOT NULL , "+
                "transaction_date INTEGER NOT NULL)";

        String createLocalVariable = "CREATE TABLE IF NOT EXISTS "+ Enum_TableName.LocalVariable +" ( " +
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

    private static void fillTabelList(){
        for (Enum_TableName tb : Enum_TableName.values()) {
            try {
                PreparedStatement select = connection.prepareStatement("select id from "+ Enum_TableName.TableList +" where name = ?");
                select.setString(1,tb.toString());
                ResultSet result = select.executeQuery();
                if(!result.next()){
                    PreparedStatement insert = connection.prepareStatement(
                            "INSERT INTO "+ Enum_TableName.TableList+"(name) VALUES (?) ");
                    insert.setString(1,tb.toString());
                    insert.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private static void createTrigerAfterInsert(Enum_TrigerName trigerName, Enum_TableName tableName) throws SQLException {

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

    private static void createTrigerAfterUpdate(Enum_TrigerName trigerName, Enum_TableName tableName) throws SQLException {

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
