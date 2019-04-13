package hexatorn.controler;

import hexatorn.util.gui.ControlLabelWithHints;
import hexatorn.util.database.DataBase_DataReader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
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

    private File file = null;
    private ControlLabelWithHints controlLabelWithHints;
    @FXML
    private void initialize(){
        controlLabelWithHints = new ControlLabelWithHints(labelHints);
        buttonChoseFile.setOnAction(event -> handleChoseFile());
        radioButtonAll.setOnAction(event -> handleGetAllData());
        radioButtonOneYer.setOnAction(event -> handleGetOneYerData());
        radioButtonOneMonth.setOnAction(event -> handleGetOneMonthData());
        radioButtonFromDateToDate.setOnAction(event -> handleGetFromDateToDateData());
        datePickerFromDate.setOnAction(event -> handleFromDatePick());
        datePickerToDate.setOnAction(event -> handleToDatePick());
    }

    private void handleToDatePick() {
        controlLabelWithHints.updateLabel(
                datePickerFromDate.getValue(),
                datePickerToDate.getValue(),
                ControlLabelWithHints.WhoCall.ToDate);
        if (controlLabelWithHints.isClerDataPicker()){
            datePickerToDate.getEditor().clear();
        }
    }

    private void handleFromDatePick() {
        controlLabelWithHints.updateLabel(
                datePickerFromDate.getValue(),
                datePickerToDate.getValue(),
                ControlLabelWithHints.WhoCall.FromeDate);
        if (controlLabelWithHints.isClerDataPicker()){
            datePickerFromDate.getEditor().clear();
        }
    }

    private void handleGetFromDateToDateData() {
        disableDateSelectControls();
        datePickerFromDate.setDisable(false);
        datePickerToDate.setDisable(false);
    }

    private void handleGetOneMonthData() {
        disableDateSelectControls();
        choiceBoxOneMonth.setDisable(false);
        ArrayList<String> listOfMonthsAndYers = DataBase_DataReader.readMonthAndYersAsString();
        for (String s : listOfMonthsAndYers) {
            choiceBoxOneMonth.getItems().add(s);
        }
    }

    private void handleGetOneYerData() {
        disableDateSelectControls();
        choiceBoxOneYer.setDisable(false);
        ArrayList<String> listOfYers = DataBase_DataReader.readYers();
        for (String s : listOfYers) {
            choiceBoxOneYer.getItems().add(s);
        }
    }

    private void handleGetAllData() {
        disableDateSelectControls();
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
