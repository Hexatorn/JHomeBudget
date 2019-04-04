package hexatorn.controler;

import hexatorn.App;
import hexatorn.data.Bill;
import hexatorn.data.Person;
import hexatorn.util.Resources;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


public class Controller_CashFlow {
    private ArrayList<Double> percentColumnWidthArrayList = new ArrayList<>();
    private ObservableList<Bill> listOfBills;
    private App app;
    private LocalDateTime date = LocalDateTime.now();

    @FXML
    TableView tbBills;
    @FXML
    HBox hBoxWithTextFields;
    @FXML
    TableColumn<Bill, String> tbColPlace;
    @FXML
    TableColumn<Bill, Double> tbColAmount;
    @FXML
    TableColumn<Bill, LocalDate> tbColData;
    @FXML
    TableColumn<Bill, String> tbColCategory;
    @FXML
    TableColumn<Bill, String> tbColSubCategory;
    @FXML
    TableColumn<Bill, String> tbColDescription;
    @FXML
    TableColumn<Bill, String> tbColPerson;
    @FXML
    TextField tfPlace;
    @FXML
    TextField tfAmount;
    @FXML
    TextField tfData;
    @FXML
    TextField tfCategory;
    @FXML
    TextField tfSubCategory;
    @FXML
    TextField tfDescription;
    @FXML
    AnchorPane aCashFlowView;
    @FXML
    ImageView imCalendar;
    @FXML
    Button btnNextMonth;
    @FXML
    Button btnPreviousMonth;
    @FXML
    Button btnNextYer;
    @FXML
    Button btnPreviousYer;

    @FXML
    Label lblMonth;
    @FXML
    Label lblYer;


    private boolean finishInitialize = false;

    @FXML
    private void initialize(){
        tbColPlace.setCellValueFactory(new PropertyValueFactory<Bill,String>("place"));
        tbColAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("amount"));
        tbColData.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date"));
        tbColCategory.setCellValueFactory(new PropertyValueFactory<Bill,String>("category"));
        tbColSubCategory.setCellValueFactory(new PropertyValueFactory<Bill,String>("subcategory"));
        tbColDescription.setCellValueFactory(new PropertyValueFactory<Bill,String>("description"));
        tbColPerson.setCellValueFactory( cellData -> cellData.getValue().getPerson().toStringPropherty());


        SetPercentColumnWidth();
        SetColumnResize();

        try {
            Image image = new Image(Resources.getResources("resources/Icon/IcoCalendar.png").toString());
            imCalendar.setImage(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        lblMonth.setText(getPLMonthString());
        lblYer.setText(String.valueOf(getYer()));
        btnNextMonth.setOnAction(event -> handleNextMonth());
        btnPreviousMonth.setOnAction(event -> handlePreviousMonth());
        btnNextYer.setOnAction(event -> handleNextYer());
        btnPreviousYer.setOnAction(event -> handlePreviousYer());
    }

    private void handlePreviousYer(){
        date = date.minusYears(1);
        lblYer.setText(String.valueOf(getYer()));
    }

    private void handleNextYer(){
        date = date.plusYears(1);
        lblYer.setText(String.valueOf(getYer()));
    }

    private void handlePreviousMonth() {
        date = date.minusMonths(1);
        lblMonth.setText(getPLMonthString());
        lblYer.setText(String.valueOf(getYer()));
    }

    private void handleNextMonth() {
        date = date.plusMonths(1);
        lblMonth.setText(getPLMonthString());
        lblYer.setText(String.valueOf(getYer()));
    }

    private String getPLMonthString(){
        String month = Month.of(date.getMonth().getValue()).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("pl-PL"));
        month = month.substring(0,1).toUpperCase() + month.substring(1);
        return month;
    }

    private int getYer(){
        return date.getYear();
    }

    private void SetPercentColumnWidth(){
        double sumPrefWidth = 0;
        /* Start Create Percent Table
         * EN
         * Create Percent Table. 0.1 at first index means first column have 10% width of table.
         * PL
         * Utworzenie tabeli z procentową wielkością kolumn. 0.1 na pierwszej pozycji oznacza że pierwsza kolumna ma 10% szerokości tabeli.
         * */
        ArrayList<Double> doubleArrayList = new ArrayList<Double>();
        for (Object columnOb : tbBills.getColumns() ) {

            TableColumn column = (TableColumn) columnOb;
            doubleArrayList.add(( column.getPrefWidth()));

            sumPrefWidth += (column).getPrefWidth();
        }


        for (Double d : doubleArrayList) {

            double percent = 0;
            percent = d/sumPrefWidth;
            percentColumnWidthArrayList.add(percent);
        }
    }

    private void SetColumnResize() {

        /* End Create Percent Table */
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
                    double newWidth = 0;
                    newWidth = newValue.doubleValue() * percentColumnWidthArrayList.get(i);

                    column = (TableColumn) tbBills.getColumns().get(i);
                    column.setPrefWidth(newWidth);
                    textField = (TextField) hBoxWithTextFields.getChildren().get(i);
                    textField.setPrefWidth(column.getWidth());
                    sumColumnWidth += newWidth;
                }
            }

            if(finishInitialize){
                tbColDescription.setPrefWidth(
                        tbColDescription.getPrefWidth() - (tbBills.getWidth() -  sumColumnWidth) -3
                );
            }
            finishInitialize = true;

        };
        tbBills.widthProperty().addListener(changeSizeListener);
        /* End Resize Column and Text Field*/

    }

    public void setApp(App app) {
        this.app = app;
        tbBills.setItems(app.getListOfBill());
    }
}
