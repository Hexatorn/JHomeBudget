package hexatorn.controler;

import hexatorn.data.Bill;
import hexatorn.util.database.*;
import hexatorn.util.gui.ControlLabelWithHints;
import hexatorn.util.gui.WarningAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class ControllerOption_ExportFromExcel {

    @FXML
    Button buttonChoseFile;
    @FXML
    Button buttonExport;
    @FXML
    RadioButton radioButtonAll;
    @FXML
    RadioButton radioButtonOneYer;
    @FXML
    RadioButton radioButtonOneMonth;
    @FXML
    RadioButton radioButtonFromDateToDate;
    @FXML
    ChoiceBox choiceBoxOneYer;
    @FXML
    ChoiceBox choiceBoxOneMonth;
    @FXML
    DatePicker datePickerFromDate;
    @FXML
    DatePicker datePickerToDate;
    @FXML
    TextField textFieldChosenFile;
    @FXML
    Label labelHints;
    @FXML
    ToggleGroup toggleGroupExportChoise;

    private DBBillsReader dbReaderBills = new DBBillsReader_All();

    private File file = null;
    private ControlLabelWithHints controlLabelWithHints;
    @FXML
    private void initialize(){
        controlLabelWithHints = new ControlLabelWithHints(labelHints);
        buttonChoseFile.setOnAction(event -> handleChoseFile());

        radioButtonAll.setOnAction(event -> radioButtonHandleGetAllData());
        radioButtonOneYer.setOnAction(event -> radioButtonHandleGetOneYerData());
        radioButtonOneMonth.setOnAction(event -> radioButtonHandleGetOneMonthData());
        radioButtonFromDateToDate.setOnAction(event -> radioButtonHandleGetFromDateToDateData());
        datePickerFromDate.setOnAction(event -> handleFromDatePick());
        datePickerToDate.setOnAction(event -> handleToDatePick());

        choiceBoxOneMonth.setOnAction(event -> choiceBoxHandleGetOneMonthData());
        choiceBoxOneYer.setOnAction(event -> choiceBoxHandleGetOneYerData());

        buttonExport.setOnAction(event -> handleExport());
    }

    private void handleExport() {
        ObservableList<Bill> listOfBills;
        if(dbReaderBills != null){
            listOfBills = dbReaderBills.read();
            System.out.println(listOfBills);
        }
        else {
            WarningAlert.show("Nie wybrano zakresu Dat",
           "Wybierz za jaki okres czasu mają być wyeksportowane dane !!","");
        }
    }

    private void choiceBoxHandleGetOneYerData() {
        String yer = (String) choiceBoxOneYer.getValue();
        dbReaderBills = new DBBillsReader_PerYer(yer);

    }

    private void choiceBoxHandleGetOneMonthData() {
        String chosenValue = (String) choiceBoxOneMonth.getValue();
        String yer = chosenValue.substring(0,4);
        String month = chosenValue.substring(5);
        dbReaderBills = new DBBillsReader_PerMonth(yer,month);
    }


    private void handleToDatePick() {
        dbReaderBills = null;
        controlLabelWithHints.updateLabel(
                datePickerFromDate.getValue(),
                datePickerToDate.getValue(),
                ControlLabelWithHints.WhoCall.ToDate);
        if (controlLabelWithHints.isClerDataPicker()){
            datePickerToDate.getEditor().clear();
        }
        if(controlLabelWithHints.isCreateBillReader()){
            dbReaderBills = new DBBillsReader_FromDateToDate(
                    datePickerFromDate.getValue(),
                    datePickerToDate.getValue()
            );
        }
    }

    private void handleFromDatePick() {
        dbReaderBills = null;
        controlLabelWithHints.updateLabel(
                datePickerFromDate.getValue(),
                datePickerToDate.getValue(),
                ControlLabelWithHints.WhoCall.FromeDate);
        if (controlLabelWithHints.isClerDataPicker()){
            datePickerFromDate.getEditor().clear();
        }
        if(controlLabelWithHints.isCreateBillReader()){
            dbReaderBills = new DBBillsReader_FromDateToDate(
                    datePickerFromDate.getValue(),
                    datePickerToDate.getValue()
            );
        }

    }

    private void radioButtonHandleGetFromDateToDateData() {
        disableDateSelectControls();
        datePickerFromDate.setDisable(false);
        datePickerToDate.setDisable(false);
        dbReaderBills = null;
    }

    private void radioButtonHandleGetOneMonthData() {
        disableDateSelectControls();
        choiceBoxOneMonth.setDisable(false);
        ArrayList<String> listOfMonthsAndYers = new DBReader_MonthsAndYersAsString().read();
        for (String s : listOfMonthsAndYers) {
            choiceBoxOneMonth.getItems().add(s);
        }
        dbReaderBills = null;
    }

    private void radioButtonHandleGetOneYerData() {
        disableDateSelectControls();
        choiceBoxOneYer.setDisable(false);
        ArrayList<String> listOfYers = new DBReader_YersReader().read();
        for (String s : listOfYers) {
            choiceBoxOneYer.getItems().add(s);
        }
        dbReaderBills = null;
    }

    private void radioButtonHandleGetAllData() {
        disableDateSelectControls();
        dbReaderBills = new DBBillsReader_All();
    }

    private void handleChoseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wskaż gdzie zapisać plik z danymi");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File","*.xlsx","*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showSaveDialog(null);
        if (file != null){
            textFieldChosenFile.setText(file.getAbsolutePath());
        }
    }

    private void disableDateSelectControls(){
        choiceBoxOneYer.setDisable(true);
        choiceBoxOneMonth.setDisable(true);
        datePickerFromDate.setDisable(true);
        datePickerToDate.setDisable(true);
    }
}
