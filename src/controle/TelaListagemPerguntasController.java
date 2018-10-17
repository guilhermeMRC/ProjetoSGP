/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.PerguntaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Pergunta;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaListagemPerguntasController implements Initializable {

    @FXML
    private TableView<Pergunta> tvPergunta;

    @FXML
    private TableColumn<Pergunta, String> tcolSelect;

    @FXML
    private TableColumn<Pergunta, String> tcolPergunta;
    
    @FXML
    private TableColumn<Pergunta, String> tcolHabilitar;
    
    private ObservableList<Pergunta> observableListPergunta;
    
    PerguntaDAO perguntaDAO = new PerguntaDAO();
    
    //evento do togllebutton
    private void handleButtonAction(ActionEvent event){
       
        System.out.println("cliquei");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        carregartableViewPergunta();
        tvPergunta.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewPergunta(newValue));
        
        halitarDesabilitarPergunta();
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
    
    //método para carregar tabela
    public void carregartableViewPergunta(){
        
        tcolSelect.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        tcolPergunta.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tcolHabilitar.setCellValueFactory(new PropertyValueFactory<>("togglebutton"));
        
        observableListPergunta = FXCollections.observableArrayList(perguntaDAO.listar());
        tvPergunta.setItems(observableListPergunta);
    }
    
    public void selecionarItemTableViewPergunta(Pergunta pergunta){
        
        System.out.println("ok");
        
    }
    
    //faz com que o togllebutton tenha um evento
    public void halitarDesabilitarPergunta(){
        
        for(int i = 0; i<observableListPergunta.size(); i++){
            observableListPergunta.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }
        
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
