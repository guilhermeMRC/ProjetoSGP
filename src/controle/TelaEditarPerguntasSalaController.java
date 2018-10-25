/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import dao.PerguntaDAO;
import dao.SalaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Dificuldade;
import modelo.Pergunta;
import modelo.Sala;

/**
 *
 * @author gnunes
 */
public class TelaEditarPerguntasSalaController implements Initializable{
   
    @FXML
    private TableView<Pergunta> tabelaSalas;
    @FXML
    private TableColumn<Pergunta, String> colunaPergunta;
    @FXML
    private TableColumn<Pergunta, String> colunaDisciplina;
    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldade;
    @FXML
    private TableColumn<Pergunta, Integer> colunaTempo;
    @FXML
    private TableColumn<Pergunta, ToggleButton> colunaHabilitar;
    @FXML
    private JFXButton botaoSalvar;
    @FXML
    private TextField campoNomeSala;
    @FXML
    private JFXButton botaoIncluir;

    private ObservableList<Pergunta> perguntas = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
    private SalaDAO salaDAO;
    private Sala sala;
    
    
    public void setNomeSala(String nomeSala){
        this.campoNomeSala.setText(nomeSala);
    }
    
    public String getNomeSala(){
        return this.campoNomeSala.getText();
    }
    
    public void setSala(Sala sala){
        this.sala = sala;
    }
    
    public Sala getSala(){
        return this.sala;
    }
    
    public void setPerguntas(ObservableList<Pergunta> perguntas){
        this.perguntas.addAll(perguntas);
    }
    
    public ObservableList<Pergunta> getPerguntas(){
        return this.perguntas;
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gerarTabela();
        setNomeSala(campoNomeSala.getText());
    }
    
    public void gerarTabela(){
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaTempo.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempo.setStyle("-fx-alignment: CENTER;");
        
        colunaHabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitar.setStyle("-fx-alignment: CENTER;");
        
        tabelaSalas.setItems(getPerguntas());
    }
    
    @FXML
    void EditarSala(ActionEvent event) {
        try{
            salaDAO = new SalaDAO();
            
            getSala().setDescricao(campoNomeSala.getText());
            getSala().setPerguntas(perguntas);
            
            salaDAO.atualizar(getSala());
            
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Sala atualizada com sucesso!");
            mensagem.show();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    void inserirPergunta(ActionEvent event) {
        
        FXMLLoader loader = new FXMLLoader();

        try {
            AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/visao/TelaPesquisarPergunta.fxml").openStream());
            TelaPesquisarPerguntaController controller = (TelaPesquisarPerguntaController) loader.getController();
             
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            ObservableList listaVelha = this.getPerguntas();
            ObservableList listaSelecionada = controller.getPerguntas();
            ObservableList listaNova = null;
            
            boolean teste = false;
            for (Object object : listaSelecionada) {
                if(!listaVelha.contains(object)){
                    listaNova.add(object);
                }else{
                    teste=true;
                }
            }
            if(teste){
              Alert erro = new Alert(Alert.AlertType.ERROR);
              erro.setHeaderText("Uma ou mais perguntas não foram adicionadas pois ja estava na sala");  
            }
            
            
            perguntas.addAll(listaNova);
            
        } catch (IOException ex) {
            Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
