package hexatorn.controler;

import hexatorn.util.Resources;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;

public class SplashController {
    @FXML
    ImageView imSplash;

    @FXML
    private void initialize() throws MalformedURLException {
        Image image = new Image(Resources.getResources("resources/Icon/LogoJHomeBudget.png").toString());
        imSplash.setImage(image);
    }

}
