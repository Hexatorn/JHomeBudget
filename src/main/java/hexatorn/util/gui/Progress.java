package hexatorn.util.gui;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class Progress {
    private int workDone = 0;
    private int maxProgress = 100;
    private boolean run =true;

    /*
    * EN
    * This Instance of class Task is used to comunication with LabelField
    * Exacly update its status/text from other thread
    * This is the way to not block/freez user interface podczas importu.
    * For use shuld be bind with LabelField
    * PL
    * Poniższy Obiekt klasy Task służy do komunikacji z LabelField i
    * aktualizowania jego stanu (tekstu) z innego wątku.
    * Dzięki temu nie jest blokowany/zamrażany interfejs użytkownika
    * W celu uzycia powinno być powiązane (zbindowane) z LabelField
    */
    private Task progresTask = new Task() {
        @Override
        protected Object call()  {
        do {
            String s = String.format("%.0f %%",(double) workDone / maxProgress *100);
            updateProgress(workDone, maxProgress);
            updateMessage(s);
        }while (run);
        updateProgress(maxProgress, maxProgress);
        updateMessage("100% Import zakończony");
        return null;
        }
    };

    /*
    * EN
    * Gether and Seter Method
    * PL
    * Metody Get i Set
    */
    public Task getProgresTask() {
        return progresTask;
    }
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
    /*
    * EN
    * Description EN
    * PL
    * Opis PL
    */

    public void incrementProgresByOnePoint(){
        workDone++;
    }

    /*
    * EN
    * Constructors
    * PL
    * Konstruktory
    */
    public Progress() {
    }

    public void run(){
        Thread thread = new Thread(progresTask);
        thread.setName("Update Label");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop(){
        run = false;
    }
}
