package hexatorn.controler;

import hexatorn.App;
import hexatorn.util.gui.Resources;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import static hexatorn.util.gui.Resources.getResources;
public class ControllerMenu_SubMenuOption {

    @FXML
    ImageView imageViewLogo;
    @FXML
    Button buttonImport;
    @FXML
    Button buttonExport;

    /*
     * EN
     * Setting the reference to the parent
     * Used in parent class when initialized Menu
     * PL
     * Ustawienie odwołania do klasy rodzica
     * Wykozystane klasie rodzica podczas inicjalizacji menu
     */
    private App mainApp;
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize(){
        imageViewLogo.setImage(subImege());
        buttonImport.setOnAction(event -> btnImportHandle());
        buttonExport.setOnAction(event -> btnExportHandle());
    }

    /*
     * EN
     * Action doing after button click.
     * Change Center Panel contains
     * PL
     * Akcje wykonywane przez przyciski
     * Zmiana zawartości głównego Panelu.
     */
    private void btnImportHandle() {
        try {
            FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/ViewOption_ImportFromExcel.fxml"));
            AnchorPane importPane = loader.load();
            mainApp.getRootLayout().setCenter(importPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void btnExportHandle(){
        try {
            FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/ViewOption_ExportFromExcel.fxml"));
            AnchorPane importPane = loader.load();
            mainApp.getRootLayout().setCenter(importPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * EN
    * Cuting image for placement in submenu
    * PL
    * Przycięcie obrazka w celu umieszczenia w Submenu.
    */

    private Image subImege(){
        Image outImage = null;

        try {
            Image image = new Image(Resources.getResources("resources/Icon/LogoJHomeBudget.png").toString());
            PixelReader imageToCut = image.getPixelReader();
            outImage = new WritableImage(imageToCut,630, 0, 270, 650);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outImage;
    }
}
