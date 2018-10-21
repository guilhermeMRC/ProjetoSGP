/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.PerguntaDAO;
import dao.SalaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private AnchorPane ParenteContainer;

    @FXML
    private TableView<Pergunta> tabelaPerguntas;
    @FXML
    private TableColumn<Pergunta, Long> colunaId;
    @FXML
    private TableColumn<Pergunta, String> colunaPergunta;
    @FXML
    private TableColumn<Pergunta, ToggleButton> colunaHabilitar;
    @FXML
    private TableColumn<Pergunta, JFXCheckBox> colunaSala;
    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldade;

    @FXML
    private JFXButton botaoIncluir, botaoAlterar, botaoExcluir, criarSala;

    @FXML
    private TextField campoPesquisar;

    private PerguntaDAO perguntaDAO;
    private Pergunta pergunta;
    private TelaListagemPerguntasController telaListarPerguntas;

    private ObservableList<Pergunta> obsPergunta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        carregarTabela();

    }

    @FXML
    public void inserirPergunta(ActionEvent event) {

        carregarTela("/visao/TelaCadastroPerguntas.fxml");
    }

    @FXML
    public void alterarPergunta(ActionEvent event) {

        passarParametroTelaDisciplinaEdicao();

    }

    @FXML
    public void excluirPergunta(ActionEvent event) {

    }

    public void carregarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaId.setStyle("-fx-alignment: CENTER;");

        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaHabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitar.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSala.setStyle("-fx-alignment: CENTER;");

        tabelaPerguntas.setItems(getPerguntas());
    }
    
    public ObservableList<Pergunta> getPerguntas(){
        perguntaDAO = new PerguntaDAO();
        obsPergunta = FXCollections.observableArrayList();
        
        for(Pergunta p:perguntaDAO.listar()){
            obsPergunta.add(p);
        }
        
        return obsPergunta;
    }

    public void carregarTela(String arq) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource(arq));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @FXML
    void criarSala(ActionEvent event) {
       
        obsPergunta = FXCollections.observableArrayList();
        
    }

    /*Método para passar uma pergunta de uma tela para outra*/
    @FXML
    public void passarParametroTelaDisciplinaEdicao() {
        try {
            pergunta = new Pergunta();
            pergunta.setId(tabelaPerguntas.getSelectionModel().getSelectedItem().getId());
            pergunta.setAlternativas(tabelaPerguntas.getSelectionModel().getSelectedItem().getAlternativas());
            pergunta.setDescricao(tabelaPerguntas.getSelectionModel().getSelectedItem().getDescricao());
            pergunta.setDisciplina(tabelaPerguntas.getSelectionModel().getSelectedItem().getDisciplina());
            pergunta.setDificuldade(tabelaPerguntas.getSelectionModel().getSelectedItem().getDificuldade());
            pergunta.setTempo(tabelaPerguntas.getSelectionModel().getSelectedItem().getTempo());
            pergunta.setTags(tabelaPerguntas.getSelectionModel().getSelectedItem().getTags());

            Stage stage2 = new Stage();
            FXMLLoader loader = new FXMLLoader();

            try {
                StackPane root = (StackPane) loader.load(getClass().getResource("/visao/TelaEditarPerguntas.fxml").openStream());
                TelaEditarPerguntasController controller = (TelaEditarPerguntasController) loader.getController(); //carregando instancia da outra tela

                //esta acessando um método da outra tela.
                controller.receberParametros(telaListarPerguntas, pergunta);

                Scene scene = new Scene(root);
                stage2.setScene(scene);
                stage2.alwaysOnTopProperty();
                stage2.initModality(Modality.APPLICATION_MODAL);
                stage2.show();
                
            } catch (IOException ex) {

            }
            
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar editar pergunta");
            alert.setContentText("Nenhuma pergunta foi selecionada");
            alert.show();
        }
        
        carregarTabela();
    }
}
