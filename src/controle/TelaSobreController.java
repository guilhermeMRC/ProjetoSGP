package controle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TelaSobreController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView logocw;

    @FXML
    private ImageView linkProjeto;

    @FXML
    private ImageView linkSGP;

    @FXML
    private ImageView linkJogo;

    @FXML
    void abrePaginaProjeto(MouseEvent event) {
        try {
                try {
                    java.awt.Desktop.getDesktop().browse( new java.net.URI( "https://github.com/guilhermeMRC/ProjetoSGP"  ) );
                } catch (IOException ex) {
                    Logger.getLogger(TelaSobreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                System.out.println(ex);
            }
    }

    @FXML
    void abreRepositorioJogo(MouseEvent event) {
        try {
                try {
                    java.awt.Desktop.getDesktop().browse( new java.net.URI( "https://github.com/saviosiqueira/ProjetoVemProIFF"  ) );
                } catch (IOException ex) {
                    Logger.getLogger(TelaSobreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                System.out.println(ex);
            }
    }

    @FXML
    void abreRepositorioSGP(MouseEvent event) {
        try {
                try {
                    java.awt.Desktop.getDesktop().browse( new java.net.URI( "https://github.com/guilhermeMRC/ProjetoSGP"  ) );
                } catch (IOException ex) {
                    Logger.getLogger(TelaSobreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                System.out.println(ex);
            }
    }

    @FXML
    void initialize() {
        assert logocw != null : "fx:id=\"logocw\" was not injected: check your FXML file 'TelaSobre.fxml'.";
        assert linkProjeto != null : "fx:id=\"linkProjeto\" was not injected: check your FXML file 'TelaSobre.fxml'.";
        assert linkSGP != null : "fx:id=\"linkSGP\" was not injected: check your FXML file 'TelaSobre.fxml'.";
        assert linkJogo != null : "fx:id=\"linkJogo\" was not injected: check your FXML file 'TelaSobre.fxml'.";

    }
}
