
package controle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Disciplina;


public class TelaCadastroDisciplinaController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewDisciplina();
    }  
        
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
    
    @FXML
    private Button btLimpar;
    
    @FXML
    private Button btExcluir;
    
    private ObservableList<Disciplina> observableListDisciplinas;
    private Disciplina disciplina = new Disciplina();
    private List<Disciplina> listaDisciplinas = new ArrayList<>();
    
    @FXML //-----------------------FALTA TERMINAR--------------------------------
    private void editarDisciplina(ActionEvent event) {
        
        try{
            disciplina = tvDisciplina.getSelectionModel().getSelectedItem();
            //CHAMAR A TELA DE EDITAR
            //PASSAR A DISCIPLINA COMO PARÂMETRO E ATUALIZAR LÁ
            // disciplina.setDescricao(tfDisciplina.getText());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            carregarTableViewDisciplina();
        }
    }

    @FXML //----------------------FUNCIONANDO-----------------------------------
    public void pesquisarDisciplina(ActionEvent event) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        DisciplinaDAO dao = new DisciplinaDAO();
        
        try {
            if(tfDisciplina.getText().equals("")){
                listaDisciplinas = dao.listar();
            }else {
                listaDisciplinas = dao.listarPorDisciplina(tfDisciplina.getText());
            }
            
            observableListDisciplinas = FXCollections.observableArrayList(listaDisciplinas);
            tvDisciplina.setItems(observableListDisciplinas);
            
        } catch (Exception e) {
            System.out.println("\n\n-------------> " + e.getMessage());
            e.printStackTrace();
        }
        
        
    }
    
    @FXML //----------------------FUNCIONANDO-----------------------------------
    void LimparDis(ActionEvent event) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        DisciplinaDAO dao = new DisciplinaDAO();
        listaDisciplinas = dao.listar();
        tfDisciplina.setText("");
        observableListDisciplinas = FXCollections.observableArrayList(listaDisciplinas);
        tvDisciplina.setItems(observableListDisciplinas);
    }

    @FXML //----------------------FUNCIONANDO-----------------------------------
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
        
        carregarTableViewDisciplina();
       
    }
    
    @FXML
    void ExcluirDisciplina(ActionEvent event) {
        try{
            Long id = tvDisciplina.getSelectionModel().getSelectedItem().getId();
            System.out.println("---------------------------> ID:"+id);
            DisciplinaDAO disciplinadao = new DisciplinaDAO();
            disciplinadao.excluir(disciplinadao.ListarDisciplinaPorID(id));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            carregarTableViewDisciplina();
        }
    }
    
    //----------------------FUNCIONANDO-----------------------------------    
    public void carregarTableViewDisciplina() {
        
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        DisciplinaDAO dao = new DisciplinaDAO();
        
        try {   
            listaDisciplinas = dao.listar();
            observableListDisciplinas = FXCollections.observableArrayList(listaDisciplinas);
            tvDisciplina.setItems(observableListDisciplinas);
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
        
}
