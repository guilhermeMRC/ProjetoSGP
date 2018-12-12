/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.DisciplinaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import modelo.Disciplina;

/**
 * FXML Controller class
 *
 * @author gnunes
 */
public class TelaCadastroDisciplinasController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private TableView<Disciplina> tabelaDisciplinas;
    @FXML
    private TableColumn<Disciplina, Long> colunaId;
    @FXML
    private TableColumn<Disciplina, String> colunaDisciplina;
    @FXML
    private TableColumn<Disciplina, ToggleButton> colunaHabilitarDesabilitar;
    @FXML
    private TextField campoPesquisar;
    @FXML
    private JFXButton botaoIncluir, botaoAlterar;
    @FXML
    private JFXComboBox<String> comboListagem;
    @FXML
    private AnchorPane ParenteContainer;

    private Disciplina disciplina = new Disciplina();
    private ObservableList<String> obsOpcaoListagem;
    private ObservableList<Disciplina> observableListDisciplina;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //carrega todas as opções no combobox (comboListagem)
        carregarOpcaoListar();
        
        //chamo esse método para que por padrão venha no listar todos
        voltarEmListarTodos();
        
        /*inicia o campoPesquisa com uma espécie de envento 
        que propicia usar o método trazerPesquisa. Esse método 
        pede uma String que no caso é o texto o próprio campo*/
        campoPesquisar.addEventFilter(KeyEvent.ANY, eve -> {

            trazerPesquisa(campoPesquisar.getText());

        });
    }

    public AnchorPane getAnchorPane() {
        return this.ParenteContainer;
    }

    /*Esse evento serve para eu fazer com que tenha um evento 
    em cada togglebutton da tabela. Ou seja cada togglebutton 
    terá esse evento implementado*/
    private void handleButtonAction(ActionEvent event) {
        //chamo a conexão com o banco através da Classe DisciplinaDAO 
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

        /*Faço um for na tabela e comparo cada objeto Disciplina
        com da tabela com o alvo do evento (getTarget) para saber 
        qual togglebutton eu estou acionando*/
        for (Disciplina d : tabelaDisciplinas.getItems()) {

            if (d.getTogglebutton() == event.getTarget()) {
                disciplina = d;
                break;
            }

        }

        /*Esse ponto eu checo os status. Como é um evento que reage
        ao toque, ele nesse caso reagirá de forma que quando a disciplina 
        estiver habilitada ela receberá um false e aparecerá como desabilitada
        e vice versa se caso o status for false.*/
        boolean status = disciplina.isHabilitar();
        if (status == true) {

            disciplina.setHabilitar(false);
            disciplinaDAO.atualizar(disciplina);

        } else {

            disciplina.setHabilitar(true);
            disciplinaDAO.atualizar(disciplina);

        }

        /*Chama o método para que a cada vez que eu usar o togglebutton
        eu possa atualizar a tebela através do que está escolhido no comboBox
        Se caso estiver trazendo a tabela com as habilitadas e desabilito alguma
        disciplina pelo togglebutton ele acaba saindo da tabela e vice e versa.
        Quando eu troco de tabela aquela disciplina que eu desabilitei ela volta 
        quando eu carrego as desabilitadas e vice e versa. Fazendo com que a cada 
        escolha a tebela seja atualizada*/
        escolherOpcao(event);

    }

    /*Esse método pega os togglebuttons da classe e implementa 
    um método de evento neles. Vide o evento acima*/
    public void habilitarDesabilitarDisciplina() {

        for (int i = 0; i < observableListDisciplina.size(); i++) {
            observableListDisciplina.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }

    }

    //Método que insere a disciplina. Grava no banco
    @FXML
    public void inserirDisciplina(ActionEvent event) {

        disciplina = new Disciplina();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de disciplina");
        dialog.setHeaderText("Cadastrar disciplina");
        dialog.setContentText("Informe o nome da disciplina:");

        Optional<String> resultado = dialog.showAndWait();

        //checa se apertou o botão ok (retorna um true) ou se apertou cancel(retorna false) 
        if (resultado.isPresent()) {

            /*checa se o textImput esta vindo vazio. Aqui diz que não 
            é possível salvar vazio*/
            if (resultado.get().equals("")) {

                menssagem(Alert.AlertType.ERROR, "Disciplina", 
                          "Cadastrar Disciplina", 
                          "Não foi póssivel cadastrar disciplina sem nome!");

            } else {
                
                disciplina.setDescricao(resultado.get());
                
                try {

                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

                    disciplinaDAO.incluir(disciplina);
                    
                    menssagem(Alert.AlertType.NONE, "Disciplina", 
                              "Cadastrar Disciplina", 
                              "Disciplina cadastrada com sucesso!");

                    dialog.close();
                    
                    voltarEmListarTodos();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void alterarDisciplina(ActionEvent event) {

        try {

            disciplina = tabelaDisciplinas.getSelectionModel().getSelectedItem();

            TextInputDialog dialogAlteracao = new TextInputDialog(disciplina.getDescricao());
            dialogAlteracao.setTitle("Disciplina");
            dialogAlteracao.setHeaderText("Alterar disciplina");
            dialogAlteracao.setContentText("Informe o novo nome da disciplina");

            Optional<String> resultado = dialogAlteracao.showAndWait();
            if (resultado.isPresent()) {

                if (resultado.get().equals("")) {

                    menssagem(Alert.AlertType.ERROR, "Disciplina", 
                            "Alterar disciplina", 
                            "Não é possivel alterar uma disciplina sem nome!");
                    
                    
                } else {

                    disciplina.setDescricao(resultado.get());

                    try {

                        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                        disciplinaDAO.atualizar(disciplina);

                        menssagem(Alert.AlertType.NONE, "Disciplina", 
                                    "Alterar disciplina", 
                                    "Disciplina alterada com sucesso!");
                        
                        //carregarTabelaComTodos();
                        voltarEmListarTodos();

                    } catch (Exception ex) {

                        ex.getStackTrace();
                    }
                }

            } else {
                dialogAlteracao.close();
            }
        } catch (NullPointerException ex) {
            
            menssagem(Alert.AlertType.INFORMATION, 
                    "Disciplina", 
                    "Alterar disciplina", 
                    "Nenhuma disciplina foi selecionada");
            
        }
    }

    public void carregarTabelaComTodos() {

        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaId.setStyle("-fx-alignment: CENTER;");
        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER");
        colunaHabilitarDesabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitarDesabilitar.setStyle("-fx-alignment: CENTER");

        observableListDisciplina = FXCollections.observableArrayList(disciplinaDAO.listar());

        habilitarDesabilitarDisciplina();
        tabelaDisciplinas.setItems(observableListDisciplina);

    }

    public void trazerDisciplinasOnOuOff(boolean status) {

        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

        observableListDisciplina = FXCollections.observableArrayList(disciplinaDAO.listarDisciplinasAtivasOuDesativadas(status));
        habilitarDesabilitarDisciplina();
        tabelaDisciplinas.setItems(observableListDisciplina);
        //tabelaDisciplinas.refresh();

    }

    //carrega as opções para o CheckBox
    public void carregarOpcaoListar() {

        List<String> opcoes = new ArrayList<>();
        opcoes.add("Listar Todas");
        opcoes.add("Listar Habilitadas");
        opcoes.add("Listar Desabilitadas");

        obsOpcaoListagem = FXCollections.observableArrayList(opcoes);
        comboListagem.getItems().setAll(obsOpcaoListagem);

    }

    /*Método que seleciona os filtros específicos
    Listar todos (vem por padrão) e as outras opções
    são listar as disciplinas habilitadas e as desabilitadas*/
    @FXML
    public void escolherOpcao(ActionEvent event) {

        /*um boolean que vai servir para trazer a lista 
        que eu quero que é a lista de disciplinas ativadas
        ou desativadas*/
        boolean status;

        String escolha = comboListagem.getSelectionModel().getSelectedItem();

        switch (escolha) {

            case "Listar Todas":
                carregarTabelaComTodos();
                break;

            case "Listar Habilitadas":
                status = true;
                trazerDisciplinasOnOuOff(status);
                break;

            case "Listar Desabilitadas":
                status = false;
                trazerDisciplinasOnOuOff(status);
                break;
        }

    }

    /*Metodo usado para capturar o filtro que é uma String passado por um TextField.
    Esse filtro é importante porque se ele for diferente de vazio ("")
    ele vai trazer as disciplinas pela pesquisa*/
    public void trazerPesquisa(String filtro) {

        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

        if (filtro.equals("")) {
            
            voltarEmListarTodos();
            //carregarTabelaComTodos();
        } else {

            observableListDisciplina = FXCollections.observableArrayList(disciplinaDAO.listarDisciplinasPorDescricao(filtro));
            tabelaDisciplinas.getItems().setAll(observableListDisciplina);
        }

    }
    
    public void voltarEmListarTodos(){
        
        //estancio o evento para chamar no comboListagem por padrão
        ActionEvent event = new ActionEvent();
        
        /*Pega a primeira opção para que venha por padrão 
        a primeira opção que é a de listar todas*/
        comboListagem.getSelectionModel().select(0);
        
        //chama o evento do comboBox para checar qual é a escolha
        escolherOpcao(event);
        
    }
    
    public void menssagem(Alert.AlertType tipo, String title, String header, String Content){
        
        Alert mensagem = new Alert(tipo);
        
        if(tipo == Alert.AlertType.NONE){
            
            FontAwesomeIconView icone = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE_ALT);
            icone.setGlyphSize(50);

            Paint paint = new Color(0.0, 0.7, 0.0, 1.0);
            icone.setFill(paint);

            mensagem.setGraphic(icone);
            mensagem.setTitle(title);
            mensagem.setHeaderText(header);
            mensagem.setContentText(Content);
            mensagem.getOnCloseRequest();
            mensagem.getButtonTypes().add(ButtonType.OK);
            mensagem.showAndWait();
            
            
        }else {
            
            mensagem.setTitle(title);
            mensagem.setHeaderText(header);
            mensagem.setContentText(Content);
            mensagem.showAndWait();
        }
        
    }

}
