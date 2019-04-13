package hexatorn.controler;

import hexatorn.App;
import hexatorn.data.Bill;
import hexatorn.util.Resources;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static hexatorn.util.database.DataBase_DataReader.readBillsFromOneMonthFromDataBase;


public class Controller_CashFlow {
    private ArrayList<Double> percentColumnWidthArrayList = new ArrayList<>();
    private ObservableList<Bill> listOfBills;
    private App app;
    private LocalDateTime date = LocalDateTime.now();

    @FXML
    TableView tableViewBills;
    @FXML
    HBox hBoxWithTextFields;
    @FXML
    TableColumn<Bill, String> tableColumnPlace;
    @FXML
    TableColumn<Bill, String> tableColumnGods;
    @FXML
    TableColumn<Bill, Double> tableColumnAmount;
    @FXML
    TableColumn<Bill, Date> tableColumnData;
    @FXML
    TableColumn<Bill, String> tableColumnCategory;
    @FXML
    TableColumn<Bill, String> tableColumnSubCategory;
    @FXML
    TableColumn<Bill, String> tableColumnDescription;
    @FXML
    TableColumn<Bill, String> tableColumnPerson;
    @FXML
    TextField textFieldPlace;
    @FXML
    TextField textFieldGoods;
    @FXML
    TextField textFieldAmount;
    @FXML
    TextField textFieldData;
    @FXML
    TextField textFieldCategory;
    @FXML
    TextField textFieldSubCategory;
    @FXML
    TextField textFieldDescription;
    @FXML
    AnchorPane anchorPaneCashFlowView;
    @FXML
    ImageView imageViewCalendar;
    @FXML
    Button buttonNextMonth;
    @FXML
    Button buttonPreviousMonth;
    @FXML
    Button buttonNextYer;
    @FXML
    Button buttonPreviousYer;
    @FXML
    Label labelMonth;
    @FXML
    Label labelYer;

    private boolean finishInitialize = false;

    @FXML
    private void initialize(){
        labelMonth.setText(getPLMonthString());
        labelYer.setText(String.valueOf(getYer()));

        reloadListOfBills();

        tableColumnPlace.setCellValueFactory(new PropertyValueFactory<Bill,String>("place"));
        tableColumnGods.setCellValueFactory(new PropertyValueFactory<Bill,String>("goodsOrServices"));
        tableColumnAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("amount"));

        tableColumnData.setCellValueFactory(new PropertyValueFactory<Bill, Date>("date"));
        tableColumnData.setCellFactory(new Controller_CashFlow.ColumnFormatter<>("dd-MM-yyyy"));

        tableColumnCategory.setCellValueFactory(new PropertyValueFactory<Bill,String>("category"));
        tableColumnSubCategory.setCellValueFactory(new PropertyValueFactory<Bill,String>("subcategory"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<Bill,String>("description"));
        tableColumnPerson.setCellValueFactory(cellData -> cellData.getValue().getPerson().toStringPropherty());

        SetPercentColumnWidth();
        SetColumnResize();

        try {
            Image image = new Image(Resources.getResources("resources/Icon/IcoCalendar.png").toString());
            imageViewCalendar.setImage(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        buttonNextMonth.setOnAction(event -> handleNextMonth());
        buttonPreviousMonth.setOnAction(event -> handlePreviousMonth());
        buttonNextYer.setOnAction(event -> handleNextYer());
        buttonPreviousYer.setOnAction(event -> handlePreviousYer());
    }

    /*
    * EN
    * This Class was injection to object Table Column to order formatting data in cells
    * PL
    * Klasa wstrzykiwana w obiekt TableColumn w celu formatowania zawartości komórek
    */
    private class ColumnFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

        SimpleDateFormat simpleDateFormat;

        ColumnFormatter(String formatPatern) {
            super();
            simpleDateFormat = new SimpleDateFormat(formatPatern);
        }

        @Override
        public TableCell<S, T> call(TableColumn<S, T> arg0) {
            return new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {

                        Date localValueDate = (Date) item;
                        String val = simpleDateFormat.format(localValueDate);

                        setGraphic(new Label(val));
                    }
                }
            };
        }
    }

    private void reloadListOfBills() {
        String month = String.format("%02d", date.getMonth().getValue());
        String yer = ""+ date.getYear();
        listOfBills = readBillsFromOneMonthFromDataBase(yer,month);
        tableViewBills.setItems(listOfBills);
    }

    private void handlePreviousYer(){
        date = date.minusYears(1);
        labelYer.setText(String.valueOf(getYer()));
        reloadListOfBills();
    }

    private void handleNextYer(){
        date = date.plusYears(1);
        labelYer.setText(String.valueOf(getYer()));
        reloadListOfBills();
    }

    private void handlePreviousMonth() {
        date = date.minusMonths(1);
        labelMonth.setText(getPLMonthString());
        labelYer.setText(String.valueOf(getYer()));
        reloadListOfBills();
    }

    private void handleNextMonth() {
        date = date.plusMonths(1);
        labelMonth.setText(getPLMonthString());
        labelYer.setText(String.valueOf(getYer()));
        reloadListOfBills();
    }

    private String getPLMonthString(){
        String month = Month.of(date.getMonth().getValue()).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("pl-PL"));
        month = month.substring(0,1).toUpperCase() + month.substring(1);
        return month;
    }

    private int getYer(){
        return date.getYear();
    }

    /*
    * EN
    * Corecrt Column Width during rezisable aplication Window
    * Prabably can be do beter
    * PL
    * Poprawia szerokość kolumn podczas zmiany rozmiaru okna.
    */
    /*TODO FIX
    * Nie działa z zmianą rozmiaru kolumn
    */
    private void SetPercentColumnWidth(){
        double sumPrefWidth = 0;
        /* Start Create Percent Table
         * EN
         * Create Percent Table. For example 0.1 at first index means first column have 10% width of table.
         * PL
         * Utworzenie tabeli z procentową wielkością kolumn. 0.1 na pierwszej pozycji oznacza że pierwsza kolumna ma 10% szerokości tabeli.
         * */
        ArrayList<Double> doubleArrayList = new ArrayList<Double>();
        for (Object columnOb : tableViewBills.getColumns() ) {

            TableColumn column = (TableColumn) columnOb;
            doubleArrayList.add(( column.getPrefWidth()));

            sumPrefWidth += (column).getPrefWidth();
        }


        for (Double d : doubleArrayList) {

            double percent;
            percent = d/sumPrefWidth;
            percentColumnWidthArrayList.add(percent);
        }
    }

    /*
    * EN
    * Together with the resize of the columns, resize of the text fields under the table
    * PL
    * Wraz ze mnaną wielkości kolumn zmienia się wielkość poł tekstowych pod tabelą
    */
    private void SetColumnResize() {
        /* EN
         * Resize Column and Text Field
         * PL
         * Zmiana rozmiaru kolumn i pól tekstowych*/
        ChangeListener<Number> changeSizeListener = (observable, oldValue, newValue) ->{
            double sumColumnWidth = 0;
            for (int i = 0; i < percentColumnWidthArrayList.size() ; i++) {
                TableColumn column;
                TextField textField;
                if(oldValue.doubleValue() != 0){
                    double newWidth;
                    newWidth = newValue.doubleValue() * percentColumnWidthArrayList.get(i);

                    column = (TableColumn) tableViewBills.getColumns().get(i);
                    column.setPrefWidth(newWidth);
                    textField = (TextField) hBoxWithTextFields.getChildren().get(i);
                    textField.setPrefWidth(column.getWidth());
                    sumColumnWidth += newWidth;
                }
            }

            if(finishInitialize){
                tableColumnDescription.setPrefWidth(
                        tableColumnDescription.getPrefWidth() - (tableViewBills.getWidth() -  sumColumnWidth) -3
                );
            }
            finishInitialize = true;

        };
        tableViewBills.widthProperty().addListener(changeSizeListener);
        /* End Resize Column and Text Field*/

    }

    public void setApp(App app) {
        this.app = app;
    }
}
