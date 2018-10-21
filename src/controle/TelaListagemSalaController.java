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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private JFXButton botaoIncluir, botaoAlterar, botaoExcluir, botaoVisualizarSala;

    private Sala sala;
    private SalaDAO salaDAO;
    private ObservableList<Pergunta> obsPerguntas;
    private TelaListagemSalaController telaListagemSalaController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarTabela();
    }

    @FXML
    void inserirSala(ActionEvent event) {

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
            } catch (IOException ex) {
                Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            inputDialog.close();
        }
    }

    @FXML
    void alterarSala(ActionEvent event) {
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
    void excluirSala(ActionEvent event) {

    }

    @FXML
    void visualizarSala(ActionEvent event) {
        try{
            salaDAO = new SalaDAO();
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
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(TelaListagemSalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar visualizar sala");
            alert.setContentText("Nenhuma sala foi selecionada");
            alert.show();
        }

    }

    public void inicializarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaId.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("descricao"));

        tabelaSalas.setItems(atualizarSala());
    }

    public ObservableList<Sala> atualizarSala() {
        salaDAO = new SalaDAO();
        return FXCollections.observableArrayList(salaDAO.listar());
    }
}
