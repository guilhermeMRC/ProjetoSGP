/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

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
import javafx.scene.AccessibleAttribute;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Disciplina;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaCadastroDisciplinaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Disciplina disciplina;
    
    @FXML
    private TextField tfDisciplina;
    
    
    @FXML
    private TableView<Disciplina> tvDisciplina;
    
    @FXML // fx:id="id"
    private TableColumn<Disciplina, Long> id; // Value injected by FXMLLoader

    @FXML // fx:id="descricao"
    private TableColumn<Disciplina, String> descricao; // Value injected by FXMLLoader
    
    @FXML
    private Button btnEditar;
    
    private ObservableList<Disciplina> observableListDisciplinas;
    
    private List<Disciplina> listaDisciplinas = new ArrayList<>();
    
    @FXML
    private void editarDisciplina(ActionEvent event) {
        
        try{
            
            disciplina = tvDisciplina.getSelectionModel().getSelectedItem();
            disciplina.setDescricao(tfDisciplina.getText());
            System.out.println("ID " + disciplina.getId());
            DisciplinaDAO disciplinadao = new DisciplinaDAO();
        
            disciplinadao.editar(disciplina);
            
        }catch(NullPointerException n){
            
            n.getStackTrace();
            
        }finally{
            
            tfDisciplina.setText("");
            carregarTableViewDisciplina("");
            
        }
        
        
        
    }

    @FXML
    public void pesquisarDisciplina(ActionEvent event) {
        
        carregarTableViewDisciplina(tfDisciplina.getText());
        
    }

    @FXML
    public void salvarDisciplina(ActionEvent event) {
    
        //seta valores na classe
        disciplina.setDescricao(tfDisciplina.getText());
        //System.out.println(disciplina.getDescricao());
            
        //chama a classe DAO que tem o método incluir
        DisciplinaDAO disciplinadao = new DisciplinaDAO();

        //salva no Banco
        try {    
            
            disciplinadao.incluir(disciplina);
            System.out.println("Cadastrado com Sucesso!");
            
        }catch(Exception e) {
            
            System.out.println("ERRO: "+e.getMessage());
        }
        
        carregarTableViewDisciplina("");
       
    }
    
    public void selecionarItensTableViewDisciplina(Disciplina disciplina){
        
        tfDisciplina.setText(disciplina.getDescricao());
        
    }
    
    public void carregarTableViewDisciplina(String filtro) {
        
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        DisciplinaDAO dao = new DisciplinaDAO();
        
        try {
            
            if(filtro.equals("")){
                
                listaDisciplinas = dao.listar();
                
            }else {
                
                listaDisciplinas = dao.listarPorDisciplina(filtro);
                
            }
            
            observableListDisciplinas = FXCollections.observableArrayList(listaDisciplinas);
            tvDisciplina.setItems(observableListDisciplinas);
            
        } catch (Exception e) {
            System.out.println("\n\n-------------> " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        disciplina = new Disciplina();
        carregarTableViewDisciplina("");
        
        tvDisciplina.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItensTableViewDisciplina(newValue));
    }  
    
}
