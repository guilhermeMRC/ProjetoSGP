/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.PerguntaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Dificuldade;
import modelo.Pergunta;
import modelo.Sala;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaListagemPerguntasController implements Initializable {

    TelaListagemPerguntasController telaLPC;
    
   @FXML
    private TextField campoPesquisar;

    @FXML
    private TableView<Pergunta> tabelaPerguntas;

    @FXML
    private TableColumn<Pergunta, String> colunaPergunta;
    
    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldade;

    @FXML
    private TableColumn<Pergunta, ToggleButton> colunaHabilitar;

    @FXML
    private TableColumn<Pergunta, JFXCheckBox> colunaSala;

    @FXML
    private JFXButton botaoGerarSala;
    
    private ObservableList<Pergunta> observableListPergunta;
    
    Pergunta pergunta;
    PerguntaDAO perguntaDAO;
    
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
        /*tabelaPerguntas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewPergunta(newValue));*/
        
        halitarDesabilitarPergunta();
    }    
    
    @FXML
    public void inserirPergunta(ActionEvent event) {
        
        carregarTela("/visao/TelaCadastroPerguntas.fxml");
    }
    
    @FXML
    public void alterarPergunta(ActionEvent event) {
        
        try {
            passarParametroTelaDisciplinaEdicao();
        } catch (IOException ex) {
            Logger.getLogger(TelaListagemPerguntasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void excluirPergunta(ActionEvent event) {
        
    }
    
    //método para carregar tabela
    public void carregartableViewPergunta(){
        
        perguntaDAO = new PerguntaDAO();
        
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaHabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaSala.setCellValueFactory(new PropertyValueFactory("checkbox"));
        
        observableListPergunta = FXCollections.observableArrayList(perguntaDAO.listar());
        tabelaPerguntas.setItems(observableListPergunta);
    }
    
    /*public void selecionarItemTableViewPergunta(Pergunta pergunta){
        
    }*/
    
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
    
    @FXML
    void gerarSala(ActionEvent event) {
        List<Pergunta> perguntas = new ArrayList();
        
        perguntaDAO = new PerguntaDAO();
        
        for(Pergunta p:perguntaDAO.listar()){
            if(tabelaPerguntas.getSelectionModel().getSelectedItem().getCheckbox().isSelected()){
                perguntas.add(p);
            }
        }
        
        Sala sala = new Sala();
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gerar ");
        dialog.setHeaderText("Cadastrar disciplina");
        dialog.setContentText("Informe o nome da disciplina:");
        
    }
    
    
    /*Método para passar uma pergunta de uma tela para outra*/
    @FXML
    public void passarParametroTelaDisciplinaEdicao() throws IOException {
        
        Pergunta pergunta = new Pergunta();
        pergunta.setId(tabelaPerguntas.getSelectionModel().getSelectedItem().getId());
        pergunta.setAlternativas(tabelaPerguntas.getSelectionModel().getSelectedItem().getAlternativas());
        pergunta.setDescricao(tabelaPerguntas.getSelectionModel().getSelectedItem().getDescricao());
        pergunta.setDisciplina(tabelaPerguntas.getSelectionModel().getSelectedItem().getDisciplina());
        pergunta.setDificuldade(tabelaPerguntas.getSelectionModel().getSelectedItem().getDificuldade());
        pergunta.setTempo(tabelaPerguntas.getSelectionModel().getSelectedItem().getTempo());
        pergunta.setTags(tabelaPerguntas.getSelectionModel().getSelectedItem().getTags());
        
        Stage stage2 = new Stage();
        FXMLLoader loader = new FXMLLoader();
        
        StackPane root = (StackPane) loader.load(getClass().getResource("/visao/TelaEditarPerguntas.fxml").openStream());
        TelaEditarPerguntasController TelaEditarPerguntasControllerInstancia = (TelaEditarPerguntasController) loader.getController(); //carregando instancia da outra tela
        
        //esta acessando um método da outra tela.
        TelaEditarPerguntasControllerInstancia.receberParametros(telaLPC, pergunta);
        
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        stage2.alwaysOnTopProperty();
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
          
    }
}
