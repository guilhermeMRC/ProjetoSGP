/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.DisciplinaDAO;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, Long> colunaId;
    @FXML private TableColumn<Disciplina, String> colunaDisciplina;
    @FXML private TextField campoPesquisar;
    @FXML private JFXButton botaoIncluir, botaoAlterar, botaoExcluir;
    @FXML private AnchorPane ParenteContainer;
    
    private ObservableList<Disciplina> observableList;
    
    public AnchorPane getAnchorPane() {
        return this.ParenteContainer;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabela();
        observableList = FXCollections.observableArrayList();
    }

    public void TelaCadastroDisciplinasController() {
    }

    @FXML
    public void inserirDisciplina(ActionEvent event) {

        Disciplina novaDisciplina = new Disciplina();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de disciplina");
        dialog.setHeaderText("Cadastrar disciplina");
        dialog.setContentText("Informe o nome da disciplina:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            try {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                novaDisciplina.setDescricao(resultado.get());
                disciplinaDAO.incluir(novaDisciplina);
                dialog.close();
                inicializarTabela();
            } catch (Exception e) {

            }
        }
    }

    @FXML
    public void alterarDisciplina(ActionEvent event) {
        try{  
            Disciplina disciplina = tabelaDisciplinas.getSelectionModel().getSelectedItem();

            TextInputDialog dialogAlteracao = new TextInputDialog(disciplina.getDescricao());
            dialogAlteracao.setTitle("Alterar disciplina");
            dialogAlteracao.setHeaderText("Alterar disciplina");
            dialogAlteracao.setContentText("Informe o novo nome da disciplina");

            Optional<String> resultado = dialogAlteracao.showAndWait();
            if (resultado.isPresent()) {
                try {
                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                    disciplina.setDescricao(resultado.get());
                    disciplinaDAO.atualizar(disciplina);
                    inicializarTabela();
                } catch (Exception ex) {

                }
            } else {
                dialogAlteracao.close();
            }
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar editar disciplina");
            alert.setContentText("Nenhuma disciplina foi selecionada");
            alert.show();
        }
    }

    @FXML
    public void excluirDisciplina(ActionEvent event) {
        try{
            Disciplina disciplina = tabelaDisciplinas.getSelectionModel().getSelectedItem();
            Alert telaDeletarDisciplina = new Alert(AlertType.CONFIRMATION);
            telaDeletarDisciplina.setTitle("Deletar disciplina");
            telaDeletarDisciplina.setContentText("Tem certeza que deseja deletar essa disciplina?");

            Optional<ButtonType > resultado = telaDeletarDisciplina.showAndWait();
            if (resultado.isPresent()) {
                try {
                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                    disciplinaDAO.excluir(Disciplina.class, disciplina.getId());
                    inicializarTabela();
                } catch (Exception ex) {

                }
            } else {
                telaDeletarDisciplina.close();
            }
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar excluir disciplina");
            alert.setContentText("Nenhuma disciplina selecionada");
            alert.show();
        }

    }

    public void inicializarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("descricao"));
        tabelaDisciplinas.setItems(atualizaTabela());
    }

    public ObservableList<Disciplina> atualizaTabela() {
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        return FXCollections.observableArrayList(disciplinaDAO.listar());
    }
}
