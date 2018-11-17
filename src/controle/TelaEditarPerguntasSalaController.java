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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
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
    private TableColumn<Pergunta, CheckBox> colunaSelecionar;
    
    @FXML
    private TableView<Pergunta> tabelaPerguntasNaoEscolhidas;

    @FXML
    private TableColumn<Pergunta, String> colunaPerguntaTD;

    @FXML
    private TableColumn<Pergunta, String> colunaDisciplinaTD;

    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldadeTD;

    @FXML
    private TableColumn<Pergunta, Integer> colunaTempoTD;

    @FXML
    private TableColumn<Pergunta, CheckBox> colunaSelecionarTD;
    
    @FXML
    private JFXButton botaoSalvar;
    @FXML
    private TextField campoNomeSala;
    @FXML
    private JFXButton botaoIncluir;
    
    private TelaListagemSalaController telaLSC;
    private ObservableList<Pergunta> perguntas = FXCollections.observableArrayList();
    private ObservableList<Pergunta> perguntasNaoEscolhidas = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
 
    private Sala novaSala = new Sala();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    //aqui está projetando a tabela do lado esquerdo
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
        
        colunaSelecionar.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSelecionar.setStyle("-fx-alignment: CENTER;");
        
        tabelaSalas.setItems(perguntas);
        
    }
    
    public void gerarTabelaComPerguntasNaoEscolhidas(){
        
        colunaPerguntaTD.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplinaTD.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplinaTD.setStyle("-fx-alignment: CENTER;");

        colunaDificuldadeTD.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldadeTD.setStyle("-fx-alignment: CENTER;");

        colunaTempoTD.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempoTD.setStyle("-fx-alignment: CENTER;");
        
        colunaSelecionarTD.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSelecionarTD.setStyle("-fx-alignment: CENTER;");
        
        tabelaPerguntasNaoEscolhidas.setItems(perguntasNaoEscolhidas);
    }
   
    @FXML
    public void EditarSala(ActionEvent event) {
        SalaDAO salaDAO = new SalaDAO();
        //List<Pergunta> listaPergunta = new ArrayList<>();
        novaSala.setDescricao(campoNomeSala.getText());
        novaSala.setPerguntas(perguntas);
        
        try {
            salaDAO.atualizar(novaSala);
            System.out.println("Sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
    
    @FXML
    public void inserirPergunta(ActionEvent event) {
        
    }

    @FXML
    public void addPergunta(ActionEvent event) {
       
        int contador = 0;
        for(Pergunta p : perguntasNaoEscolhidas){
            
            if(p.getCheckbox().isSelected()){
                
                p.getCheckbox().setSelected(false);
                perguntas.add(p);
                
            }else {
                
                contador++;
            }
        }
        
        if(contador == tabelaPerguntasNaoEscolhidas.getItems().size()){
            
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Por favor marque as perguntas!");
            mensagem.show();
        }
        
        gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();
    }

    @FXML
    public void removerPergunta(ActionEvent event) {
        
        ObservableList<Pergunta> perguntasAuxiliar = FXCollections.observableArrayList();
        perguntasAuxiliar.setAll(perguntas);
        int contador = 0;
        
        for(Pergunta p : perguntasAuxiliar){
            
            if(p.getCheckbox().isSelected()){
                
                perguntas.remove(p);
                
            }else {
                
                contador++;
            }
           
        }
        
        if(contador == perguntasAuxiliar.size()){
            
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Por favor marque as perguntas!");
            mensagem.show();
        }
        
        gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();
    }
    /*
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
    }*/

    public void setNomeSala(String nomeSala){
        this.campoNomeSala.setText(nomeSala);
    }
    
    public String getNomeSala(){
        return this.campoNomeSala.getText();
    }
    
    public void setSala(TelaListagemSalaController TelaListagemSala, Sala sala){
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        novaSala.setId(sala.getId());
        novaSala.setDescricao(sala.getDescricao());
        novaSala.setPerguntas(sala.getPerguntas());
        
        perguntas.setAll(novaSala.getPerguntas());
        gerarTabela();
        
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();
        
        telaLSC = TelaListagemSala;
    }
    
    public void trazerPerguntasNaoSelecionadas() {
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        perguntasNaoEscolhidas.setAll(perguntaDAO.listar());
        
        for(Pergunta ps : perguntas){
            
            for(Pergunta pt : perguntasNaoEscolhidas){
                
                if(ps.getId().equals(pt.getId())){
                    perguntasNaoEscolhidas.remove(pt);
                    break;
                }
                
            }
        }
        
    }
    /*
    public Sala getSala(){
        return this.sala;
    }*/
    
    public void setPerguntas(ObservableList<Pergunta> perguntas){
        this.perguntas.addAll(perguntas);
    }
    
    public ObservableList<Pergunta> getPerguntas(){
        return this.perguntas;
    }
}
