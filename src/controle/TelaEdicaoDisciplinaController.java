package controle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import modelo.Disciplina;

/**
 * FXML Controller class
 *
 * @author João Vitor Oliveira
 */
public class TelaEdicaoDisciplinaController{

    @FXML
    private TextField tfID;
    @FXML
    private TextField tfDescricao;
    @FXML
    private Button btSalvar;
    @FXML
    private Button btCancelar;

    private Disciplina disciplina;
    /**
     * Initializes the controller class.
     */
    
    public void initialize(URL url, ResourceBundle rb, Disciplina d ) {
        this.disciplina = d;
    }    

    @FXML
    private void SalvarEdit(ActionEvent event) {
    }

    @FXML
    private void CancelarEdit(ActionEvent event) {
    }
    
}
