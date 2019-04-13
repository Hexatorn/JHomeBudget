package hexatorn.controler;

import hexatorn.data.Bill;
import hexatorn.util.gui.Progress;
import hexatorn.util.XLSXReader;
import hexatorn.util.WarningAlert;
import hexatorn.util.database.DataBase_DataWriter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import java.io.File;

public class ControllerOption_ImportFromExcel {

    @FXML
    Button buttonChoseFile;
    @FXML
    ProgressBar progressBar;
    @FXML
    TextField textFieldChosenFile;
    @FXML
    Button buttonImport;
    @FXML
    Label labelProgres;
    @FXML
    Label labelInfo;

    private File file = null;

    /*
    * EN
    * The flag indicates how the file to import was indicated
    * True -> The file indicated by the FileChooser class and the File object contains an indication of the file
    * False -> The file was not indicated or the file path entered manually into the text field and the Fiole object does not indicate the location of the file
    * PL
    * Flaga wskazuje w jaki sposób został wzkazany plik do zaimportowania
    * True -> Plik wskazany za pomocą klasy FileChooser i obiekt File zawiera wskazanie na plik
    * False -> Plik nie został wskazany lub ścieżka do pliku wprowadzona ręcznie do pola tekstowego a obiekt Fiole nie wskazuje lokalizacji pliku.
    */
    private Boolean fileIsChosen = false;

    @FXML
    private void initialize(){

        buttonChoseFile.setOnAction(event -> handleChoseFile());
        buttonImport.setOnAction(event ->handleImport());
        textFieldChosenFile.setOnKeyPressed(event -> handleClickTextFieldChoseFile());
    }

    /*
    * EN
    * Change the flag when typing the filepath to textfield
    * PL
    * Zmiana flagi podczas ręcznego wprowaszania sciężki do pliku
    */
    private void handleClickTextFieldChoseFile(){
        fileIsChosen = false;
        resetGUIProgresComponent();
    }

    /*
    * EN
    * Start import data from exel to data base in new thred
    * PL
    * Rozpączecie importu danych z pliku excel do bazy danych w nowym wątku
    */
    private void handleImport(){

        Progress progress = new Progress();
        labelProgres.textProperty().bind(progress.getProgresTask().messageProperty());
        progressBar.progressProperty().bind(progress.getProgresTask().progressProperty());

        buttonImport.setDisable(true);
        buttonChoseFile.setDisable(true);
        textFieldChosenFile.setDisable(true);

        Thread ImportThread = new Thread(()->{
            ObservableList<Bill> listOfBils;
            /*
             * EN
             * if flag = false
             * file validation and create a file based on the value from the text field
             * PL
             * jeżeli flaga = false
             * walidacja i utwórzenie pliku na podstawie wartości z pola tekstowego
             */

            if(!fileIsChosen){
                String filename = textFieldChosenFile.getText();
                String[] extensions = {"xlsx", "xls"};
                if(!FilenameUtils.isExtension(filename,extensions)){
                    WarningAlert.show("Niepoprawny plik","Niepoprawny plik","Proszę wskazać poprawny plik excel");
                    return ;
                }
                file = new File(textFieldChosenFile.getText());
            }
            if (!file.exists()){
                WarningAlert.show("Niepoprawny plik","Plik nie istnieje","Proszę wskazać istniejący plik excel");
                return ;
            }

            XLSXReader xlsxReader = new XLSXReader(file,progress);
            progress.setMaxProgress(xlsxReader.getRowsCount()*2);

            progress.run();

            listOfBils=xlsxReader.readXLSX();
            DataBase_DataWriter.writeToBase(listOfBils,progress);

            progress.stop();
            labelProgres.setTextFill(Color.GREEN);
            buttonImport.setDisable(false);
            buttonChoseFile.setDisable(false);
            textFieldChosenFile.setDisable(false);
        });
        ImportThread.setName("Import Thred");
        ImportThread.setDaemon(true);
        ImportThread.start();
    }


    /*
    * EN
    * Choosing file to be imported
    * PL
    * Wskazanie pliku do importu
    */
    private void handleChoseFile(){
        resetGUIProgresComponent();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zaczytywanie danych z pliku excel");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File","*.xlsx","*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(null);
        if (file != null){
            textFieldChosenFile.setText(file.getAbsolutePath());
        }

        fileIsChosen = true;
    }

    private void resetGUIProgresComponent(){
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        labelProgres.textProperty().unbind();
        labelProgres.setText("Wybierz plik i rozpocznij import");
        labelProgres.setTextFill(Color.BLACK);
    }
}
