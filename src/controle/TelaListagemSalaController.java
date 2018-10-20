/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import dao.SalaDAO;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Sala;

/**
 * FXML Controller class
 * 
 * @author gnunes
 */
public class TelaListagemSalaController implements Initializable{
    
    @FXML private AnchorPane ParenteContainer;

    @FXML private TableView<Sala> tabelaSalas;
    @FXML private TableColumn<Sala, Long> colunaId;
    @FXML private TableColumn<Sala, String> colunaSala;
    
    @FXML private JFXButton botaoIncluir, botaoAlterar, botaoExcluir, botaoVisualizarSala;
    
    private Sala sala;
    private SalaDAO salaDAO;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       inicializarTabela();
    }
    
    @FXML
    void inserirSala(ActionEvent event) {
        
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/visao/TelaCadastarSala.fxml"));
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Cadastro de salas");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            inicializarTabela();
            
            
        } catch (IOException ex) {
            Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void alterarSala(ActionEvent event) {

    }

    @FXML
    void excluirSala(ActionEvent event) {
        sala = tabelaSalas.getSelectionModel().getSelectedItem();
        
        Alert telaDeletarSala = new Alert(Alert.AlertType.CONFIRMATION);
        telaDeletarSala.setTitle("Deletar Sala");
        telaDeletarSala.setContentText("Tem certeza que deseja deletar essa sala?");
        
        Optional<ButtonType> resultado = telaDeletarSala.showAndWait();
        if(resultado.isPresent()){
            try{
                salaDAO = new SalaDAO();
                salaDAO.excluir(Sala.class, sala.getId());
                inicializarTabela();
            } catch(Exception ex){
                Alert mensagemErro = new Alert(Alert.AlertType.ERROR);
                mensagemErro.setTitle("Mensagem de erro do sistema");
                mensagemErro.setHeaderText("Erro ao deletar sala");
                mensagemErro.setContentText("Verique se:\n"
                        + "1 - Todos os campos foram preenchidos corretamente\n"
                        + "2 - O serviço do banco de dados está funcionando");
            }
        }
        
    }

    @FXML
    void visualizarSala(ActionEvent event) {

    }
    
    public void inicializarTabela(){
         colunaId.setCellValueFactory(new PropertyValueFactory("id"));
         colunaId.setStyle("-fx-alignment: CENTER;");
         
         colunaSala.setCellValueFactory(new PropertyValueFactory("descricao"));
         
         tabelaSalas.setItems(atualizarSala());
    }
    
    public ObservableList<Sala> atualizarSala(){
        salaDAO = new SalaDAO();
        return FXCollections.observableArrayList(salaDAO.listar());   
    }
}
