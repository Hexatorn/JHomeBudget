package hexatorn.controler;

import hexatorn.data.Bill;
import hexatorn.util.WriteToDataBase;
import hexatorn.util.XLSXReader;
import hexatorn.util.WarningAlert;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;


public class ControllerOption_ImportFromExcel {

    @FXML
    Button btn_chose_file;
    @FXML
    ProgressBar progres_bar;
    @FXML
    Button btn_test_minus;
    @FXML
    Button btn_test_plus;
    @FXML
    TextField tf_chosen_file;
    @FXML
    Button btn_Import;


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

        btn_chose_file.setOnAction(event -> handleChoseFile());
        btn_Import.setOnAction(event ->handleImport());
        tf_chosen_file.setOnKeyPressed(event -> handleClickTextFieldChoseFile());

    }

    /*
    * EN
    * Change the flag when typing the filepath to textfield
    * PL
    * Zmiana flagi podczas ręcznego wprowaszania sciężki do pliku
    */
    private void handleClickTextFieldChoseFile(){
        fileIsChosen = false;
        progres_bar.setProgress(0);
    }

    /*
    * EN
    * Start import data from exel to data base in new thred
    * PL
    * Rozpączecie importu danych z pliku excel do bazy danych w nowym wątku
    */
    private void handleImport(){
        Thread importThread = new Thread(() -> {
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
                String filename = tf_chosen_file.getText();
                String[] extensions = {"xlsx", "xls"};
                if(!FilenameUtils.isExtension(filename,extensions)){
                    WarningAlert.show("Niepoprawny plik","Niepoprawny plik","Proszę wskazać poprawny plik excel");
                    return;
                }
                file = new File(tf_chosen_file.getText());
            }
            if (!file.exists()){
                WarningAlert.show("Niepoprawny plik","Plik nie istnieje","Proszę wskazać istniejący plik excel");
                return;
            }
            XLSXReader xlsxReader = new XLSXReader();

            xlsxReader.open(file);
            int count = xlsxReader.getRowsCount();
            double progressStep = (double) 1/(2*count);
            listOfBils = xlsxReader.readXLSX(progres_bar,progressStep);
            WriteToDataBase.writeToBase(listOfBils,progres_bar,progressStep);
        });
        importThread.start();
    }

    /*
    * EN
    * Choosing file to be imported
    * PL
    * Wskazanie pliku do importu
    */
    private void handleChoseFile(){
        progres_bar.setProgress(0);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zaczytywanie danych z pliku excel");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File","*.xlsx","*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(null);
        if (file != null){
            tf_chosen_file.setText(file.getAbsolutePath());
        }

        fileIsChosen = true;
    }
}