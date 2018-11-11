/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import dao.SalaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Pergunta;
import modelo.Sala;

/**
 * FXML Controller class
 *
 * @author gnunes
 */
public class TelaListagemSalaController implements Initializable {

    @FXML
    private AnchorPane ParenteContainer;
    @FXML
    private TableView<Sala> tabelaSalas;
    @FXML
    private TableColumn<Sala, Long> colunaId;
    @FXML
    private TableColumn<Sala, String> colunaSala;
    @FXML
    private TableColumn<Sala, ToggleButton> colunaIHabilitarDesabilitar;
    @FXML
    private JFXComboBox<String> comboListagem;
    @FXML
    private TextField campoPesquisar;
    
    @FXML
    private JFXButton botaoIncluir, botaoAlterar, botaoExcluir, botaoVisualizarSala;

    private Sala sala;
    private SalaDAO salaDAO;
    private ObservableList<Pergunta> obsPerguntas;
    private TelaListagemSalaController telaListagemSalaController;
    private ObservableList<Sala> obsSalas;
    private ObservableList<String> obsOpcaoListagem;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        carregarOpcaoListar();
        voltarEmListarTodos();
        
        campoPesquisar.addEventFilter(KeyEvent.ANY, eve -> {
            
            trazerPesquisa(campoPesquisar.getText());
            
        });
    }

    /*Esse método é responsavel por iniciar o evento dos 
    togglesbuttons da tabela ele pega qual a pergunta daquele
    togglebutton específio e quando eu tenho esses dados 
    é só dar um merge pelo método atualizar no PerguntaDAO*/
    private void handleButtonAction(ActionEvent event) {

        //seto o SalaDAO que faz a conexão com o banco de dados
        SalaDAO salaDAO = new SalaDAO();

        //esse for varre a tabela atrás do togglebutton que eu cliquei
        for (Sala s : tabelaSalas.getItems()) {

            //aqui faz a checagem de qual tooglebutton eu apertei
            if (s.getTogglebutton() == event.getTarget()) {
                //aqui joga as informações para uma sala instaciada como atributo la em cima na classe
                sala = s;
                break;
            }

        }
        
        //aqui pega o valor do atributo habilitar 
        boolean status = sala.isHabilitar();
        
        //aqui ele checa e atualiza no banco
        if (status == true) {

            sala.setHabilitar(false);
            salaDAO.atualizar(sala);

        } else {

            sala.setHabilitar(true);
            salaDAO.atualizar(sala);

        }
        
        escolherOpcao(event);
    }
    
    public void habilitarDesabilitarSala() {

        for (int i = 0; i < obsSalas.size(); i++) {
            obsSalas.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }

    }
    
    @FXML
    public void inserirSala(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();

        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Cadastro de sala");
        inputDialog.setHeaderText("Cadastrar sala");
        inputDialog.setContentText("Informe o nome da sala: ");

        Optional<String> resultado = inputDialog.showAndWait();

        if (resultado.isPresent() && !resultado.get().isEmpty()) {
            
            try {
                
                AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/visao/TelaCadastroSalas.fxml").openStream());
                TelaCadastrarSalaController controller = (TelaCadastrarSalaController) loader.getController();

                controller.receberParametros(resultado.get());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                voltarEmListarTodos();
                
            } catch (IOException ex) {
                Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
                voltarEmListarTodos();
            }
        } else {
            inputDialog.close();
        }
        voltarEmListarTodos();
    }

    @FXML
    public void alterarSala(ActionEvent event) {
        
        try {
            
            salaDAO = new SalaDAO();
            sala = salaDAO.listarPorId(tabelaSalas.getSelectionModel().getSelectedItem().getId());
            obsPerguntas = FXCollections.observableArrayList();

            for (Pergunta p : sala.getPerguntas()) {
                obsPerguntas.add(p);
            }

            FXMLLoader loader = new FXMLLoader();

            try {
                
                AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/visao/TelaEditarPerguntasSala.fxml").openStream());
                TelaEditarPerguntasSalaController controller = (TelaEditarPerguntasSalaController) loader.getController();
                
                controller.setPerguntas(obsPerguntas);
                controller.setNomeSala(sala.getDescricao());
                controller.setSala(sala);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Editar Sala");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar editar sala");
            alert.setContentText("Nenhuma sala foi selecionada");
            alert.show();
        }
    }

    @FXML
    public void visualizarSala(ActionEvent event) {
        
        try{
            
            SalaDAO salaDAO = new SalaDAO();
            sala = salaDAO.listarPorId(tabelaSalas.getSelectionModel().getSelectedItem().getId());
            obsPerguntas = FXCollections.observableArrayList();

            for (Pergunta p : sala.getPerguntas()) {
                obsPerguntas.add(p);
            }

            
            FXMLLoader loader = new FXMLLoader();

            try {
                AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/visao/TelaVisualizarPerguntasSala.fxml").openStream());
                TelaVisualizarPerguntasSalaController controller = (TelaVisualizarPerguntasSalaController) loader.getController();

                controller.setPerguntas(obsPerguntas);
                controller.setNomeSala(sala.getDescricao());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Visulaizar Sala");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
            } catch (IOException ex) {
                Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch(NullPointerException ex){
            
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar visualizar sala");
            alert.setContentText("Nenhuma sala foi selecionada");
            alert.show();
            
        }

    }

    public void inicializarTabela() {
        SalaDAO salaDAO = new SalaDAO();
        
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaId.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaSala.setStyle("-fx-alignment: CENTER;");
        
        colunaIHabilitarDesabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaIHabilitarDesabilitar.setStyle("-fx-alignment: CENTER;");
        
        obsSalas = FXCollections.observableArrayList(salaDAO.listar());
        
        habilitarDesabilitarSala();
        tabelaSalas.setItems(obsSalas);
    }

    public void carregarOpcaoListar() {
        
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Listar Todas");
        opcoes.add("Listar Habilitadas");
        opcoes.add("Listar Desabilitadas");
        
        obsOpcaoListagem = FXCollections.observableArrayList(opcoes);
        comboListagem.getItems().setAll(obsOpcaoListagem);
        
    }
    
    @FXML
    public void escolherOpcao(ActionEvent event) {
        
        /*um boolean que vai servir para trazer a lista 
        que eu quero que é a lista de disciplinas ativadas
        ou desativadas*/ 
        boolean status;
        
        String escolha = comboListagem.getSelectionModel().getSelectedItem();
        
        switch(escolha){
            
            case "Listar Todas":
                inicializarTabela();
                break;
            
            case "Listar Habilitadas":
                status = true;
                trazerSalasOnOuOff(status); 
                break;
                
            case "Listar Desabilitadas":
                status = false;
                trazerSalasOnOuOff(status);
                break;
            
        }
    }    
        
    public void trazerSalasOnOuOff(boolean status) {
        
        SalaDAO salaDAO = new SalaDAO();

        obsSalas = FXCollections.observableArrayList(salaDAO.listarSalasAtivasOuDesativadas(status));
        habilitarDesabilitarSala();
        tabelaSalas.setItems(obsSalas);
        
    }
    
    public void trazerPesquisa(String filtro){
        
        SalaDAO salaDAO = new SalaDAO();
        
        if(filtro.equals("")){
            
            voltarEmListarTodos();
            
        }else{
            
            obsSalas  = FXCollections.observableArrayList(salaDAO.listarSalasPorDescricao(filtro));
            tabelaSalas.getItems().setAll(obsSalas);
        }
        
    }
    
    public void voltarEmListarTodos(){
        
        ActionEvent event = new ActionEvent();
        comboListagem.getSelectionModel().select(0);
        escolherOpcao(event);
        
    }
    
}
