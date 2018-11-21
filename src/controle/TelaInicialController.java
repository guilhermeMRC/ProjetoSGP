package controle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaInicialController implements Initializable,CallBack {
    
    @FXML private JFXHamburger hamburger;
    @FXML private AnchorPane rootPane;
    @FXML private JFXDrawer drawer;
    
    private TelaMenuLateralController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visao/TelaMenuLateral.fxml"));
            VBox box = loader.load();
            controller = loader.getController();
            controller.setCallBack(this);
            drawer.setSidePane(box);
        } catch (IOException ex) {
            Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (!drawer.isOpened()) {
                drawer.setVisible(true);
                drawer.open();
            } else {
                drawer.close();
                drawer.setVisible(false);
            }
        });
    }
    
    @FXML
    void minimizarAplicacao(ActionEvent event){
        
    }
    
    @Override
    public void changeScenes(String tela) {
        try {
            AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource(tela));
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void fecharMenuLateral(ActionEvent event) {
        drawer.close();
        drawer.setVisible(false);
    }
    
    @FXML
    void fecharAplicacao(ActionEvent event) {
        System.exit(0);
    }
}
