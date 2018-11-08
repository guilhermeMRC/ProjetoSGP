/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaSobreController implements Initializable {
    
    @FXML
    private JFXTextArea textoSobre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textoSobre.setText("Orgulhosamente Desenvolvido pela turma de Sistemas de Informacão do Instituto Federal Fluminense campus Itaperuna."
                + "\n Equipe de Desenvolvimento da Unity: Délio Marcos, Gabriel Faria, Sávio Siqueira "
                + "\n Equipe de Desenvolvimento do SGP: Joáo Vitor Oliveira, Guilherme Muniz, Gustavo Nunes"
                + "\n Equipe de Design e Sonoplastia: Leonardo Cabral, Kissila Calidoni, Erich Braganca"
                + "\n Equipe de Documentacao: Vinicius Cabral, Matheus Justino, Lucas Ribeiro, Kelly Medeiros, Jeferson Silva");
    }    
    
}
