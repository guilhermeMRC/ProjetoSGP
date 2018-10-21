/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.DisciplinaDAO;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    @FXML private TableColumn<Disciplina, ToggleButton> colunaHabilitarDesabilitar;
    @FXML private TextField campoPesquisar;
    @FXML private JFXButton botaoIncluir, botaoAlterar;
    @FXML private AnchorPane ParenteContainer;
    
    private Disciplina disciplina = new Disciplina();
    
    private ObservableList<Disciplina> observableListDisciplina;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTabela();
        //observableListDisciplina = FXCollections.observableArrayList();
        //habilitarDesabilitarDisciplina();
    }

    public AnchorPane getAnchorPane() {
        return this.ParenteContainer;
    }

    private void handleButtonAction(ActionEvent event){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        
        for(Disciplina d : tabelaDisciplinas.getItems()){
            
            if(d.getTogglebutton() == event.getTarget()){
                disciplina = d;
                break;
            }
    
        }
        
        boolean status = disciplina.isHabilitar();
        if(status == true){
            
            disciplina.setHabilitar(false);
            disciplinaDAO.atualizar(disciplina);
            
        }else {
            
            disciplina.setHabilitar(true);
            disciplinaDAO.atualizar(disciplina);
            
        }
        carregarTabela();
    }

    public void habilitarDesabilitarDisciplina(){
        
        for(int i = 0; i<observableListDisciplina.size(); i++){
            observableListDisciplina.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }
        
    }
    
    public void TelaCadastroDisciplinasController() {
    }

    @FXML
    public void inserirDisciplina(ActionEvent event) {

        disciplina = new Disciplina();
        //Disciplina novaDisciplina = new Disciplina();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de disciplina");
        dialog.setHeaderText("Cadastrar disciplina");
        dialog.setContentText("Informe o nome da disciplina:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            try {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                disciplina.setDescricao(resultado.get());
                disciplinaDAO.incluir(disciplina);
                dialog.close();
                carregarTabela();
            } catch (Exception e) {

            }
        }
    }

    @FXML
    public void alterarDisciplina(ActionEvent event) {
        try{  
            
            disciplina = tabelaDisciplinas.getSelectionModel().getSelectedItem();

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
                    carregarTabela();
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

    /*@FXML
    public void excluirDisciplina(ActionEvent event) {
        try{
            disciplina = tabelaDisciplinas.getSelectionModel().getSelectedItem();
            Alert telaDeletarDisciplina = new Alert(AlertType.CONFIRMATION);
            telaDeletarDisciplina.setTitle("Deletar disciplina");
            telaDeletarDisciplina.setContentText("Tem certeza que deseja deletar essa disciplina?");

            Optional<ButtonType > resultado = telaDeletarDisciplina.showAndWait();
            if (resultado.isPresent()) {
                try {
                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                    disciplinaDAO.excluir(Disciplina.class, disciplina.getId());
                    carregarTabela();
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

    }*/

    public void carregarTabela() {
        
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaHabilitarDesabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitarDesabilitar.setStyle("-fx-alignment: CENTER;");
        
        observableListDisciplina = FXCollections.observableArrayList(disciplinaDAO.listar());

        habilitarDesabilitarDisciplina();
        tabelaDisciplinas.setItems(observableListDisciplina);
   
    }

    /*public ObservableList<Disciplina> atualizaTabela() {
        
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        ob
        return FXCollections.observableArrayList(disciplinaDAO.listar());
        
    }*/
    
}
