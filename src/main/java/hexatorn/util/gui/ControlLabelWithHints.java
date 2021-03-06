package hexatorn.util.gui;
import hexatorn.util.database.DBReader_GetMaxDate;
import hexatorn.util.database.DBReader_GetMinDate;
import javafx.scene.control.Label;
import java.time.LocalDate;

/*
* EN
* This class validate date in DataPickers and show Message in Hiden Label. Empty message means, data has passed the validation
* PL
* Klasa sprzwdza datę w kontrolkach DataPicker i używa ukrytej labelki do wyświetlenia wiadomosci. Pusta wiadomość -> "" oznacza poprawną validacje.
*/
public class ControlLabelWithHints {
    private Label labelWithHints;
    private boolean clerDataPicker = false;
    private boolean createBillReader = false;
    private LocalDate maxDate = new DBReader_GetMaxDate().read();
    private LocalDate minDate = new DBReader_GetMinDate().read();

    public ControlLabelWithHints(Label labelWithHints){
        this.labelWithHints = labelWithHints;
    }

    public void updateLabel(LocalDate fromDate, LocalDate toDate, WhoCall caller){
        clerDataPicker = false;
        createBillReader = false;
        String message = "";
        if (fromDate!=null && toDate!=null && fromDate.isAfter(toDate) && caller==WhoCall.FromeDate){
            clerDataPicker = true;
            message +="Data od musi być wcześniejsza niż data do";
        }
        if (fromDate!=null && toDate!=null && fromDate.isAfter(toDate) && caller==WhoCall.ToDate){
            clerDataPicker = true;
            message += "Data do musi być pniejsza niż data od";
        }
        if (fromDate!=null && fromDate.isAfter(maxDate)){
            message += "\nZmień Date od. Największa wprowadzona data do aplikacji to "+ maxDate;
        }
        if (toDate!=null && toDate.isBefore(minDate)){
            message +="\nZmień Date do. Najmniejsza wprowadzona data do aplikacji to "+ minDate;
        }
        if(message.equals("") && fromDate!=null && toDate!=null){
            createBillReader = true;
        }
        labelWithHints.setText(message);
    }
    /*
    * EN
    * Returns information about  to clean or not clean the DatePicker control.
    * PL
    * Zwraca informacje o tym czy należy wyczyścić kontrolkę DataPicker
    */
    public boolean isClerDataPicker() {
        return clerDataPicker;
    }
    public boolean isCreateBillReader() {
        return createBillReader;
    }

    public enum WhoCall{
        FromeDate, ToDate
    }
}
