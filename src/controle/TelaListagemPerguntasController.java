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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaListagemPerguntasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void inserirPergunta(ActionEvent event) {
        
        carregarTela("/visao/TelaCadastroPerguntas.fxml");
    }
    
    @FXML
    public void alterarPergunta(ActionEvent event) {
        
    }
    
    @FXML
    public void excluirPergunta(ActionEvent event) {
        
    }
    
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
