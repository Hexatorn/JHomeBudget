package hexatorn.controler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.MalformedURLException;

import static hexatorn.util.Resources.getResources;
public class Menu_SubMenu_Option {

    @FXML
    Label lbl_import;
    @FXML
    Label lbl_export;
    @FXML
    Button btn_import;
    @FXML
    Button btn_export;
    @FXML
    private BorderPane bp_Option;


    @FXML
    private void initialize(){

        btn_import.setOnAction(event -> btnImportHandle());

    }


    private void btnImportHandle() {


        try {
            FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/OptionView_Import.fxml"));
            AnchorPane importPane = loader.load();
            bp_Option.setCenter(importPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
