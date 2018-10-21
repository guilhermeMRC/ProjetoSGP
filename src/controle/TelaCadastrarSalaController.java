package controle;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.jfoenix.controls.JFXCheckBox;
import dao.PerguntaDAO;
import dao.SalaDAO;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Dificuldade;
import modelo.Pergunta;
import modelo.Sala;

/**
 *
 * @author gnunes
 */
public class TelaCadastrarSalaController implements Initializable {

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
    private TableColumn<Pergunta, JFXCheckBox> colunaSala;

    @FXML
    private JFXButton botaoCadastrar;
    @FXML
    private TextField campoNomeSala;

    private ObservableList<Pergunta> obsPerguntas = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
    private SalaDAO salaDAO;
    private TelaListagemSalaController telaListagemSalaController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gerarTabela();
    }

    public void receberParametros(String nomeSala) {
        campoNomeSala.setText(nomeSala);
    }

    public void gerarTabela() {
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaTempo.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempo.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSala.setStyle("-fx-alignment: CENTER;");

        tabelaSalas.setItems(carregarPerguntas());

    }

    public ObservableList<Pergunta> carregarPerguntas() {
        perguntaDAO = new PerguntaDAO();

        for (Pergunta p : perguntaDAO.listar()) {
            obsPerguntas.add(p);
        }

        return obsPerguntas;
    }

    @FXML
    void cadastrarSala(ActionEvent event) {
        List<Pergunta> perguntas = new ArrayList();

        for (Pergunta p : obsPerguntas) {
            if (p.getCheckbox().isSelected()) {
                perguntas.add(p);
            }
        }

        try {
            Sala novaSala = new Sala();
            novaSala.setDescricao(campoNomeSala.getText());
            novaSala.setPerguntas(perguntas);

            salaDAO = new SalaDAO();
            salaDAO.incluir(novaSala);

            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Cadastro de sala");
            mensagem.setContentText("Sala cadastrada com sucesso!");
            mensagem.show();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Aviso do sistema");
            alert.setHeaderText("Erro ao tentar cadastrar sala");
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
}
