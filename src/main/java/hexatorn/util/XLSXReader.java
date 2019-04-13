package hexatorn.util;

import hexatorn.data.Bill;
import hexatorn.util.gui.Progress;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class XLSXReader {
    private XSSFWorkbook workbook = null;
    private FileInputStream fileInputStream = null;
    private Progress progress =null;

    public XLSXReader(File file) {
        read(file);
    }
    public XLSXReader(File file, Progress progress) {
        this.progress = progress;
        read(file);
    }


    /*
    * EN
    * Open connecting from file. And read it and save into memory.
    * Finally, close the connection to the file.
    * PL
    * Otwórz połączenie z plikiem i wczytaj go do pamięci.
    * Następnie zamkniej połączenie z plikiem.
    */
    private boolean read(File file){
        fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /*
    * EN
    * Return how many row is in XLSX file. All sheets.
    * Before this method, use the "read" method
    * PL
    * Zwraca ilość wierszy w pliku XLSX. Ze wszystkich skoroszytów.
    * Przed tą metodą należy użyć metody "read"
    */
    public int getRowsCount(){
        XSSFSheet sheet;
        int rowCount = 0;
        if(workbook ==null){
            return -1;
        }
        for (int i = 0; i < workbook.getNumberOfSheets() ; i++) {
            sheet = workbook.getSheetAt(i);
            rowCount += sheet.getLastRowNum();
        }
        return rowCount;
    }

    /*
    * EN
    * Return list of bils, they ale reding from XLSX file.
    * Before this method, use the "read" method
    * PL
    * Zwraca listę płatności/rachunków/operacji pienieżnych zaczytaną z pliku XLSX.
    * Przed tą metodą należy użyć metody "read"
    */
    public ObservableList<Bill> readXLSX()  {
        ObservableList<Bill> listOfBils = FXCollections.observableArrayList();
        XSSFSheet sheet;

        if(workbook ==null){
            return null;
        }
        for (int i = 0; i < workbook.getNumberOfSheets() ; i++) {
            sheet = workbook.getSheetAt(i);
            //System.out.println(sheet.getSheetName());
            Iterator<Row> rowIterator = sheet.iterator();
            if(rowIterator.hasNext()){
                rowIterator.next();
            }
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                row.getCell(1);
                if(row.getCell(0).getCellTypeEnum() == CellType.BLANK||row.getCell(1).getCellTypeEnum() == CellType.BLANK||row.getCell(3).getCellTypeEnum() == CellType.BLANK)
                    continue;
                listOfBils.add(
                    new Bill(
                        //place
                        row.getCell(1).getStringCellValue()
                        //goods or services
                        ,row.getCell(2).getStringCellValue()
                        //amount
                        ,row.getCell(3).getNumericCellValue()
                        //data
                        ,(row.getCell(0).getDateCellValue())
                        //category
                        ,row.getCell(4).getStringCellValue()
                        //subcategory
                        ,row.getCell(5).getStringCellValue()));
                progress.incrementProgresByOnePoint();
            }
        }
        return listOfBils;
    }
}
