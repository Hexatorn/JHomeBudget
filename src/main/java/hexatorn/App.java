package hexatorn;

import hexatorn.controler.CashFlowController;
import hexatorn.controler.MenuController;
import hexatorn.data.Bill;
import hexatorn.util.ReadXLSX;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;



import static hexatorn.util.Resources.getResources;


public class App extends Application
{
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Bill> listOfBills = FXCollections.observableArrayList();

    /*
    * EN
    * Allows get object from external child class
    * PL
    * Pozwala uzyskać dostęp do obiektów z poziomu klas dzieci.
    */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public ObservableList<Bill> getListOfBill() {
        return listOfBills;
    }



    public App() {

        File file1 = new File("Import2018.xlsx");
        File file2 = new File("Import2019.xlsx");
        try {
            ReadXLSX.ReadXLSX(file1,listOfBills);
            ReadXLSX.ReadXLSX(file2,listOfBills);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ){
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JHomeBudget");

        initRootLayout();
        setMenuAtRootLayout();
        //TODO zmienić ładowaną scenę
        setSplashAtRootLayout();
        //setCashFlowAtRootLayout();
    }

    /*
    * EN
    * Initialize Root Layout
    * PL
    * Inicjalizacja Głównego Layoutu -> Root Layout
    * Opis PL
    */
    private void initRootLayout() throws Exception {
        rootLayout = FXMLLoader.load(getResources("resources/FXML View/RootView.fxml"));
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }

    /*
    * EN
    * Add Menu to Root Layout
    * Intentionally pernamettly added menu to left side of Root Layout - > left side of Border Pane
    * PL
    * Dodanie Menu do Głównego Layoutu
    * Intencionalnie pernamentne dodanie menu do Głównego Leyautu po lewej stronie.
    */

    private void setMenuAtRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/Menu.fxml"));
        VBox menu = loader.load();
        rootLayout.setLeft(menu);
        rootLayout.setMinSize(
                menu.getPrefWidth(),
                menu.getHeight()
        );
        MenuController controller = loader.getController();
        controller.setMainApp(this);
    }
    /*
    * EN
    * Set the Center Panel contents in the Border Panel
    * PL
    * Metody ustawiające zawartość Center Panel w Border Panel
    * 1 Splash Layout
    * 2 CashFlow Layout
    * 3 Option Layout
    */
    public void setSplashAtRootLayout() throws IOException {
        AnchorPane splash = FXMLLoader.load(getResources("resources/FXML View/SplashView.fxml"));
        rootLayout.setCenter(splash);
        rootLayout.setMinWidth(
                rootLayout.getMinWidth() + splash.getPrefWidth()
        );
    }

    public void setCashFlowAtRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/CashFlow.fxml"));
        AnchorPane anchorPane = loader.load();
        rootLayout.setCenter(anchorPane);
        CashFlowController cashFlowController = loader.getController();
        cashFlowController.setApp(this);
    }

    public void setOptionViewAtRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/OptionView.fxml"));
        BorderPane borderPane = loader.load();
        rootLayout.setCenter(borderPane);
    }
}
