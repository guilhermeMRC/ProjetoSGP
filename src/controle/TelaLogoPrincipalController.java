package controle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author gnunes
 */
public class TelaLogoPrincipalController implements Initializable, CallBack {
    
    @FXML AnchorPane rootPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @Override
    public void changeScenes(String tela) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(tela));
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(TelaLogoPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
