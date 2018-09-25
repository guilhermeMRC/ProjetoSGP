/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private MenuItem MIPerguntas;
    
    @FXML
    public void carregarCadastroPerguntas(ActionEvent event) {
        
        carregarTela("/visao/TelaCadastroPergunta.fxml");
    }
    
    @FXML
    public void carregarCadastroDisciplinas(ActionEvent event) {
        carregarTela("/visao/TelaCadastroDisciplinas.fxml");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    //Método para carregar outras telas
    public void carregarTela(String arq) {
        
        try {
            
            Parent root = FXMLLoader.load(getClass().getResource(arq));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
