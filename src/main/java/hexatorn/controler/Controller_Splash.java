package hexatorn.controler;

import hexatorn.util.Resources;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;

public class Controller_Splash {
    @FXML
    ImageView imageViewSplash;

    @FXML
    private void initialize() throws MalformedURLException {
        Image image = new Image(Resources.getResources("resources/Icon/LogoJHomeBudget.png").toString());
        imageViewSplash.setImage(image);
    }

}
