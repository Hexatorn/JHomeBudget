package hexatorn.util.gui;

import javafx.scene.control.Alert;

public class WarningAlert {
    static public  void show(String title,String head,String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.show();
    }
}
