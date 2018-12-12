/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import dao.DisciplinaDAO;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Dificuldade;
import modelo.Disciplina;
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
    private TableColumn<Pergunta, Disciplina> colunaDisciplina;
    @FXML
    private JFXButton botaoIncluir, botaoAlterar, botaoExcluir, criarSala;
    @FXML
    private JFXCheckBox checkboxSelecionarTodos;
    
    @FXML
    private JFXComboBox<String> comboListagem;

    @FXML
    private TextField campoPesquisar;

    //private PerguntaDAO perguntaDAO;
    private Pergunta pergunta;
    private TelaListagemPerguntasController telaListarPerguntas;

    private ObservableList<String> obsOpcaoListagem;// = FXCollections.observableArrayList();
    private ObservableList<Pergunta> obsPergunta; //= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        carregarOpcaoListar();
        voltarEmListarTodos();
        
        campoPesquisar.addEventFilter(KeyEvent.ANY, eve -> {
            
            trazerPesquisa(campoPesquisar.getText());
            
        });

    }
    
    @FXML
    public void inserirPergunta(ActionEvent event) {

        carregarTela("/visao/TelaCadastroPerguntas.fxml","Cadastro de Pergunta");
        voltarEmListarTodos();
        //carregarTabela();
        
    }

    @FXML
    public void alterarPergunta(ActionEvent event) {

        passarParametroTelaDisciplinaEdicao();
        voltarEmListarTodos();
        //carregarTabela();
        
    }

    /*Esse método é responsavel por iniciar o evento dos 
    togglesbuttons da tabela ele pega qual a pergunta daquele
    togglebutton específio e quando eu tenho esses dados 
    é só dar um merge pelo método atualizar no PerguntaDAO*/
    private void handleButtonAction(ActionEvent event) {

        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        for(Pergunta p : tabelaPerguntas.getItems()){
            
            if(p.getTogglebutton() == event.getTarget()) {
                
                pergunta = p;
                break;
            }
        }
        
        boolean status = pergunta.isHabilitar();
        
        if(status == true){
            
            pergunta.setHabilitar(false);
            perguntaDAO.atualizarComAlternativas(pergunta);
            
        }else {
            
            pergunta.setHabilitar(true);
            perguntaDAO.atualizarComAlternativas(pergunta);
            
        }
        
        escolherOpcao(event);
        
    }

    /*Método que faz o togglebuttons aparecerem na
    tabela ele pega e faz um for no obsrvablelist e 
    depois seta um evento em cada togglebutton*/
    public void habilitarDesabilitarPergunta() {

        for (int i = 0; i < obsPergunta.size(); i++) {
            obsPergunta.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }

    }

    public void carregarTabela() {
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaId.setStyle("-fx-alignment: CENTER;");

        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaPergunta.setStyle("-fx-alignment: CENTER;");
        
        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");
        
        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaHabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitar.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSala.setStyle("-fx-alignment: CENTER;");

        obsPergunta = FXCollections.observableArrayList(perguntaDAO.listar());
        habilitarDesabilitarPergunta();
        tabelaPerguntas.getItems().clear();
        tabelaPerguntas.setItems(obsPergunta);
        
    }
    
    public void trazerPerguntasOnOuOff(boolean status) {
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        obsPergunta = FXCollections.observableArrayList(perguntaDAO.listarPerguntasAtivasOuDesativadas(status));
        habilitarDesabilitarPergunta();
        tabelaPerguntas.setItems(obsPergunta);
        
    }
    
    public void carregarTela(String arq, String titulo) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource(arq));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @FXML
    public void criarSala(ActionEvent event) {
        
        int contador = 0;
        ObservableList<Pergunta> perguntas = FXCollections.observableArrayList();
        
        //Varre as perguntas atrás de qual está marcada pelo checkbox
        for (Pergunta p : obsPergunta) {
            if (p.getCheckbox().isSelected()) {
                perguntas.add(p);
            }
        }

        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Cadastro de sala");
        inputDialog.setHeaderText("Cadastrar sala");
        inputDialog.setContentText("Informe o nome da sala: ");

        Optional<String> resultado = inputDialog.showAndWait();
        if (resultado.isPresent()) {
            
            for(Pergunta pd : perguntas) {
                if(pd.getDificuldade().equals(Dificuldade.DIFICIL)) contador++;
            }
            
            /*Aqui checa se a sala tem nome e checa também se o usuário 
            marcou alguma pergunta... ou seja não é possivel cadastrar 
            sala sem pergunta e nem sem nome nessa tela*/
            if(resultado.get().equals("") || perguntas.size() < 5 || contador < 3){
                
                Alert mensagem = new Alert(Alert.AlertType.ERROR);
                mensagem.setTitle("Sala");
                mensagem.setHeaderText("Cadastro de Sala");
                mensagem.setContentText("Não é possivel cadastrar uma sala! Sala deve conter "
                                        + "ter um nome e no mínimo 5 perguntas das quais 3 difíceis.");
                mensagem.show();
                
            }else {
                
                try {

                    Sala novaSala = new Sala();
                    novaSala.setDescricao(resultado.get());
                    novaSala.setPerguntas(perguntas);

                    SalaDAO salaDAO = new SalaDAO();
                    salaDAO.incluir(novaSala);

                    Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
                    mensagem.setTitle("Mensagem do sistema");
                    mensagem.setHeaderText("Cadastrar sala");
                    mensagem.setContentText("Sala cadastrada com sucesso!");
                    mensagem.showAndWait();
               
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
        } else {
            
            inputDialog.close();
        }
        
        checkboxSelecionarTodos.setSelected(false);
        voltarEmListarTodos();
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
            pergunta.setHabilitar(tabelaPerguntas.getSelectionModel().getSelectedItem().isHabilitar());

            Stage stage2 = new Stage();
            FXMLLoader loader = new FXMLLoader();

            try {
                
                StackPane root = (StackPane) loader.load(getClass().getResource("/visao/TelaEditarPerguntas.fxml").openStream());
                TelaEditarPerguntasController controller = (TelaEditarPerguntasController) loader.getController(); //carregando instancia da outra tela

                //esta acessando um método da outra tela.
                controller.receberParametros(telaListarPerguntas, pergunta);

                Scene scene = new Scene(root);
                stage2.setScene(scene);
                stage2.setTitle("Editar Pergunta");
                stage2.alwaysOnTopProperty();
                stage2.initModality(Modality.APPLICATION_MODAL);
                stage2.showAndWait();
                
            } catch (IOException ex) {

            }
            
        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar editar pergunta");
            alert.setContentText("Nenhuma pergunta foi selecionada");
            alert.show();
        }
        
    }
    
    /*Método que carrega as opções para o checkBox*/
    public void carregarOpcaoListar() {
        
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Listar Todas");
        opcoes.add("Listar Habilitadas");
        opcoes.add("Listar Desabilitadas");
        
        obsOpcaoListagem = FXCollections.observableArrayList(opcoes);
        comboListagem.getItems().setAll(obsOpcaoListagem);
        
    }
    
    /*Método que pega a escolha do comboBox de filtros na tabela.
    Aqui o usuário pode trazer todos os registros os habilitados e 
    os desabilitados*/
    @FXML
    public void escolherOpcao(ActionEvent event) {
        
        /*um boolean que vai servir para trazer a lista 
        que eu quero que é a lista de disciplinas ativadas
        ou desativadas*/ 
        boolean status;
        
        String escolha = comboListagem.getSelectionModel().getSelectedItem();
        
        switch(escolha){
            
            case "Listar Todas":
                carregarTabela();
                break;
            
            case "Listar Habilitadas":
                status = true;
                trazerPerguntasOnOuOff(status); 
                break;
                
            case "Listar Desabilitadas":
                status = false;
                trazerPerguntasOnOuOff(status);
                break;
            
        }
        
    }
    
    /*Mesma ideia do método da Classe TelaCadastroDisciplinaController.
    Porém aqui implementei a pesquisa por descricao, disciplina e dificuldade*/
    public void trazerPesquisa(String filtro){
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        if(filtro.equals("")){
            
            voltarEmListarTodos();
            
        }else{
            
            obsPergunta  = FXCollections.observableArrayList(perguntaDAO.listarPerguntasPorDescricaoOuDificuldadeOuDisciplina(filtro));
            tabelaPerguntas.getItems().setAll(obsPergunta);
        }
        
    }
    
    public void voltarEmListarTodos(){
        
        ActionEvent event = new ActionEvent();
        comboListagem.getSelectionModel().select(0);
        escolherOpcao(event);
        
    }
    
    @FXML
    public void selecionarTodasPerguntas(ActionEvent event) {

        if(checkboxSelecionarTodos.isSelected()){
            
            for(Pergunta p : obsPergunta){
                p.getCheckbox().setSelected(true);
                
            }
            
        }else {
            
            for(Pergunta p : obsPergunta){
                p.getCheckbox().setSelected(false);
            }
            
        }
    }
}
