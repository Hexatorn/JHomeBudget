package hexatorn;

import hexatorn.controler.CashFlowController;
import hexatorn.controler.Menu_MainMenuController;
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
    private BorderPane menuPanel;
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
        setMenuPanelAtRootLayout();
        //setMainMenuAtMenuPanel();
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
    * Add Menu Panel to Root Layout
    * Intentionally pernamettly added Menu Panel to left side of Root Layout - > left side of Border Pane
    * PL
    * Dodanie Panelu Menu do Głównego Layoutu
    * Intencionalnie pernamentne dodanie panelu menu do Głównego Leyautu po lewej stronie.
    */

    private void setMenuPanelAtRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/Manu_MenuPanel.fxml"));
        menuPanel = loader.load();
        rootLayout.setLeft(menuPanel);
        rootLayout.setMinSize(
                menuPanel.getPrefWidth(),
                menuPanel.getHeight()
        );
        setMainMenuAtMenuPanel();
    }

    /*
    * EN
    * Add Main Menu to Menu Panel
    * Intentionally pernamettly added Main Menu to center Menu Panel - > center of Border Pane
    * PL
    * Dodanie Głównego Menu do Menu Panel
    * Intencionalnie pernamentne dodanie Głownego Menu do Menu Panelu -> Centrala część Border Panel.
    */


    private void setMainMenuAtMenuPanel() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/Manu_MainMenu.fxml"));
        VBox menu = loader.load();
        menuPanel.setCenter(menu);
        Menu_MainMenuController controller = loader.getController();
        controller.setMainApp(this);
    }

    /*
    * EN
    * Show Submenu
    * Change the width of the Panel Menu and Main Menu and adding the Sub Menu to the right side of the Panel Menu (Border Panel)
    * PL
    * Pokazanie podmenu
    * Zmiana szerokości Panel Menu i Main Menu i dodanie Sub Menu do prawej strony Menu Panel (Border Panel)
    */

    public void showSubMenuOption() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/Menu_SubMenu_Option.fxml"));
        VBox subMenu = loader.load();
        menuPanel.setRight(subMenu);
        menuPanel.setMinWidth(340);
        VBox mainMenu = (VBox) menuPanel.getCenter();
        mainMenu.setMinWidth(210);
    }

    /*
     * EN
     * Hide Submenu
     * Change the width of the Panel Menu and Main Menu and remove the Sub Menu from the Panel Menu.
     * PL
     * Ukrucie podmenu
     * Zmiana szerokości Panel Menu i Main Menu i usunięcie Sub Menu z Menu Panel
     */

    private void hideSubMenu(){
        menuPanel.setMinWidth(300);
        menuPanel.setRight(null);
        VBox mainMenu = (VBox) menuPanel.getCenter();
        mainMenu.setMinWidth(300);
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
    private void setSplashAtRootLayout() throws IOException {
        AnchorPane splash = FXMLLoader.load(getResources("resources/FXML View/SplashView.fxml"));
        rootLayout.setCenter(splash);
        rootLayout.setMinWidth(
                rootLayout.getMinWidth() + splash.getPrefWidth()
        );
    }

    public void setCashFlowAtRootLayout() throws IOException {
        hideSubMenu();
        FXMLLoader loader = new FXMLLoader(getResources("resources/FXML View/CashFlow.fxml"));
        AnchorPane anchorPane = loader.load();
        rootLayout.setCenter(anchorPane);
        CashFlowController cashFlowController = loader.getController();
        cashFlowController.setApp(this);
    }



}
