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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Dificuldade;
import modelo.Pergunta;
import modelo.Sala;

/**
 *
 * @author gnunes
 */
public class TelaPesquisarPerguntaController implements Initializable{
    
    @FXML
    private TableView<Pergunta> tabelaPerguntas;
    @FXML
    private TableColumn<Pergunta, String> colunaPergunta;
    @FXML
    private TableColumn<Pergunta, String> colunaDisciplina;
    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldade;
    @FXML
    private TableColumn<Pergunta, Integer> colunaTempo;
    @FXML
    private TableColumn<Pergunta, JFXCheckBox> colunaSelecionar;

    @FXML
    private JFXButton botaoSalvar;
    @FXML
    private TextField campoNomeSala;
    
    private ObservableList<Pergunta> obsPerguntas  = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
    private ObservableList<Pergunta> perguntas;
    
    public void setPerguntas(ObservableList<Pergunta> perguntas){
        this.perguntas = perguntas;
    }
    
    public ObservableList<Pergunta> getPerguntas(){
        return this.perguntas;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gerarTabela();
    }
    
    public void gerarTabela(){
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaTempo.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempo.setStyle("-fx-alignment: CENTER;");
        
        colunaSelecionar.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSelecionar.setStyle("-fx-alignment: CENTER;");
        
        tabelaPerguntas.setItems(carregarPerguntas());
    }
    
    //mexi aqui dia 16/11/2018 00:45
    public ObservableList<Pergunta> carregarPerguntas(){
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        SalaDAO salaDAO = new SalaDAO();
        Sala sala = salaDAO.listarPorId(1L);
        
        List<Pergunta> listaTotalPerguntas = new ArrayList<>();
        listaTotalPerguntas.addAll(perguntaDAO.listar());
        List<Pergunta> listaPerguntaSala = new ArrayList<>();
        listaPerguntaSala.addAll(sala.getPerguntas());
        
        for(Pergunta p : listaPerguntaSala){
            for(Pergunta ps : listaTotalPerguntas){
                if(p.getId().equals(ps.getId())){
                    
                    listaTotalPerguntas.remove(ps);
                    break;
                }
            }
        }
        
        System.out.println(listaTotalPerguntas);
        
        obsPerguntas.setAll(listaTotalPerguntas);
        return obsPerguntas;
    }
    
    @FXML
    public void salvarPeguntas(ActionEvent event) {
        perguntas = FXCollections.observableArrayList();
        
        for(Pergunta p : obsPerguntas){
            if(p.getCheckbox().isSelected()){
                perguntas.add(p);
            }
        }
        
        setPerguntas(perguntas);
    }
}
