package hexatorn.util;

import hexatorn.data.Bill;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Iterator;

public class ReadXLSX {

    static public void ReadXLSX(File file, ObservableList<Bill> listOfBils) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = null;

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
                                ,(row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                                //category
                                ,row.getCell(4).getStringCellValue()
                                //subcategory
                                ,row.getCell(5).getStringCellValue()));
            }
        }
    }


}
