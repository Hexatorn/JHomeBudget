package hexatorn.controler;

import hexatorn.App;
import hexatorn.util.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.net.MalformedURLException;

public class ControllerMenu_MainMenu {
    @FXML
    ImageView imageViewLogo;
    @FXML
    ImageView imageViewCashFlow;
    @FXML
    ImageView imageViewInwestycje;
    @FXML
    ImageView imageViewAnalityka;
    @FXML
    ImageView imageViewOpcje;
    @FXML
    ImageView imageViewExit;
    @FXML
    Button buttonCashFlow;
    @FXML
    Button buttonOpcje;

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
    private void initialize() throws MalformedURLException {


        Image image = new Image(Resources.getResources("resources/Icon/LogoJHomeBudget.png").toString());
        imageViewLogo.setImage(image);
        setIconI();

        buttonCashFlow.setOnAction(event -> btnCashFlowClickOnActionHandler());
        buttonOpcje.setOnAction(event -> btnOpcjeOnActionHandler());

    }

    /*
    * EN
    * Action doing after button click.
    * Change Center Panel contains
    * PL
    * Akcje wykonywane przez przyciski
    * Zmiana zawartości głównego Panelu.
    */

    private void btnOpcjeOnActionHandler(){
        try {
            mainApp.showSubMenuOption();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void btnCashFlowClickOnActionHandler() {
        try {
            mainApp.setCashFlowAtRootLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * EN
    * Inicialize Image in Menu
    * PL
    * Ustawianie obrazków obok przycisków
    */

    private void setIconII() throws MalformedURLException {
        Image image = new Image(Resources.getResources("resources/Icon/IcoCashFlow2.png").toString());
        imageViewCashFlow.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoMoneyBag2.png").toString());
        imageViewInwestycje.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoChart2.png").toString());
        imageViewAnalityka.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoSettings2.png").toString());
        imageViewOpcje.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoLogout2.png").toString());
        imageViewExit.setImage(image);
    }

    private void setIconI() throws MalformedURLException {
        Image image = new Image(Resources.getResources("resources/Icon/IcoCashFlow.png").toString());
        imageViewCashFlow.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoMoneyBag.png").toString());
        imageViewInwestycje.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoChart.png").toString());
        imageViewAnalityka.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoSettings.png").toString());
        imageViewOpcje.setImage(image);

        image = new Image(Resources.getResources("resources/Icon/IcoLogout.png").toString());
        imageViewExit.setImage(image);
    }


}
