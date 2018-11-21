/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import dao.DisciplinaDAO;
import dao.PerguntaDAO;
import modelo.Dificuldade;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import modelo.Alternativa;
import modelo.Disciplina;
import modelo.Pergunta;

/**
 *
 * @author Turma de Sistemas 6° periodo
 */
public class TelaCadastraPerguntas implements Initializable {

    @FXML
    private JFXTextArea campoPergunta;

    @FXML
    private ToggleGroup Alternativas;

    @FXML
    private TextField campoAlternativaA, campoAlternativaB, campoAlternativaC, campoAlternativaD;

    @FXML
    private JFXComboBox<Dificuldade> selecaoDificuldadePergunta;

    @FXML
    private JFXComboBox<Disciplina> selecaoDisciplina;

    @FXML
    private JFXButton botaoCadastrar, botaoCancelar, botaoDiminuir, botaoAumentar;

    @FXML
    private JFXChipView<String> campoTags;

    @FXML
    private JFXRadioButton opcaoA, opcaoB, opcaoC, opcaoD;

    @FXML
    private JFXSlider sliderTempoPergunta;

    @FXML
    private Label labelCaracteres;

    private List<Dificuldade> dificuldades = new ArrayList();
    private ObservableList<Dificuldade> obsDificuldade;
    //private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    //private PerguntaDAO perguntaDAO = new PerguntaDAO();
    private ObservableList<Disciplina> obsDisciplinas;
    private int contadorCaracteres = 100;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
        Esse campoTags.getSuggestions()..addAll("HELLO", "TROLL", "WFEWEF", "WEF")
        funciona para dar uma sugestão quando a pessoa for digitar a tag. 
        Se puderem colocar algumas tags dentro do método addAll sobre as disciplinas, vai ficar massa.
        Se puderem ver um jeito mais eficiente de informar as tags sem ter que ficar carregando um momento de
        mensagem na memoria do sistema vai ficar bem melhor.
         */
        campoTags.getSuggestions().addAll("Baskara", "Tomas Edson", "Matemática Financeira", "Literatura");
        carregarDificuldades();
        carregarDisciplinas();
        labelCaracteres.setText("Caracteres: 100");
        contatCaracteresPergunta();
    }

    @FXML
    public void inserirDisciplina(ActionEvent event) {

        Disciplina disciplina = new Disciplina();

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

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Disciplina");
                alert.setHeaderText("Cadastro de disciplina");
                alert.setContentText("Não é possivel cadastrar uma disciplina sem nome!");
                alert.show();

            } else {
                disciplina.setDescricao(resultado.get());
                try {

                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

                    disciplinaDAO.incluir(disciplina);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Disciplina");
                    alert.setHeaderText("Cadastro de disciplina");
                    alert.setContentText("Disciplina cadastrada com sucesso!");
                    alert.show();

                    dialog.close();
                    carregarDisciplinas();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public void CadastrarPergunta() {

        PerguntaDAO perguntaDAO = new PerguntaDAO();
        //campoTags = new JFXChipView();
        Disciplina disciplina = selecaoDisciplina.getSelectionModel().getSelectedItem();
        List<String> listTags = new ArrayList();
        Pergunta pergunta = new Pergunta();

        pergunta.setDescricao(campoPergunta.getText());
        pergunta.setDificuldade(selecaoDificuldadePergunta.getSelectionModel().getSelectedItem());

        pergunta.setTempo(converterDoubleParaInteger(sliderTempoPergunta.getValue()));

        pergunta.setDisciplina(disciplina);
        pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaA, opcaoA));
        pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaB, opcaoB));
        pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaC, opcaoC));
        pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaD, opcaoD));

        listTags.addAll(campoTags.getChips());
        pergunta.setTags(listTags);
        
        /*Esse if serve para verificar os campos obrigatórios*/
        if (campoPergunta.getText().isEmpty()
                || selecaoDificuldadePergunta.getSelectionModel().isEmpty()
                || selecaoDisciplina.getSelectionModel().isEmpty()
                || (opcaoA.isSelected() == false
                && opcaoB.isSelected() == false
                && opcaoC.isSelected() == false
                && opcaoD.isSelected() == false)
                || (campoAlternativaA.getText().isEmpty()
                || campoAlternativaB.getText().isEmpty()
                || campoAlternativaC.getText().isEmpty()
                || campoAlternativaD.getText().isEmpty())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pergunta");
            alert.setHeaderText("Cadastro de pergunta");
            alert.setContentText("Não foi possível cadastrar a pergunta. Por favor, verifique se todos os campos estão preenchidos");
            alert.show();

        } else {

            perguntaDAO.incluirComAlternativas(pergunta);
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Cadastro de pergunta");
            mensagem.setContentText("Pergunta cadastrada com sucesso!");
            mensagem.showAndWait();

            limparCampos();

        }
    }

    public Alternativa cadastrarAlternativa(TextField alternativa, JFXRadioButton opcao) {
        //List<Alternativa> alternativas = new ArrayList();
        Alternativa a = new Alternativa();

        a.setDescricao(alternativa.getText());

        if (opcao.isSelected()) {
            a.setCorreto(true);
        }

        return a;
    }

    public void limparCampos() {
        
        campoPergunta.clear();
        campoAlternativaA.clear();
        campoAlternativaB.clear();
        campoAlternativaC.clear();
        campoAlternativaD.clear();
        Alternativas.getToggles().clear();
        sliderTempoPergunta.setValue(30);
        selecaoDificuldadePergunta.getSelectionModel().clearSelection();
        selecaoDisciplina.getSelectionModel().clearSelection();
        campoTags.getChips().clear();
        
    }

    public void carregarDificuldades() {
        dificuldades.add(Dificuldade.FACIL);
        dificuldades.add(Dificuldade.MEDIO);
        dificuldades.add(Dificuldade.DIFICIL);

        obsDificuldade = FXCollections.observableArrayList(dificuldades);
        selecaoDificuldadePergunta.getItems().addAll(obsDificuldade);

    }

    public void carregarDisciplinas() {

        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        selecaoDisciplina.getItems().clear();
        obsDisciplinas = FXCollections.observableArrayList(disciplinaDAO.listarDisciplinasAtivasOuDesativadas(true));
        selecaoDisciplina.getItems().addAll(obsDisciplinas);
    }

    /*Método para converter o valor do slider (que vem como double) 
    em Integer porque foi mapeado no banco como Integer*/
    public Integer converterDoubleParaInteger(Double valor) {

        Integer tempo = valor.intValue();
        return tempo;
    }

    private void contatCaracteresPergunta() {

        campoPergunta.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            
            //Trabalhando nessa função---- ainda esta bugando
            System.out.println(campoPergunta.getText().length());
            
            if (event.getCode() == KeyCode.BACK_SPACE || 
                event.getCode() == KeyCode.DELETE ||
                event.getCode() == KeyCode.TAB ||
                event.getCode() == KeyCode.ENTER   
                ) {
                
                if (campoPergunta.getText().isEmpty()) {
                    contadorCaracteres = 100;
                    labelCaracteres.setText("Caracteres: " + contadorCaracteres);
                } else {
                    contadorCaracteres = 100;
                    contadorCaracteres = contadorCaracteres - (campoPergunta.getText().length()-1);
                    labelCaracteres.setText("Caracteres: " + contadorCaracteres);
                }
            } else if(event.getCode() == KeyCode.F1 ||
                      event.getCode() == KeyCode.F2 ||
                      event.getCode() == KeyCode.F4 ||
                      event.getCode() == KeyCode.F5 ||
                      event.getCode() == KeyCode.F6 ||
                      event.getCode() == KeyCode.F7 ||
                      event.getCode() == KeyCode.F8 ||
                      event.getCode() == KeyCode.F9 ||
                      event.getCode() == KeyCode.F10 ||
                      event.getCode() == KeyCode.F11 ||
                      event.getCode() == KeyCode.F12 ||
                      event.getCode() == KeyCode.ALT ||
                      event.getCode() == KeyCode.CAPS ||
                      event.getCode() == KeyCode.ALT_GRAPH ||
                      event.getCode() == KeyCode.ESCAPE || 
                      event.getCode() == KeyCode.LEFT ||
                      event.getCode() == KeyCode.RIGHT ||
                      event.getCode() == KeyCode.UP ||
                      event.getCode() == KeyCode.DOWN ||
                      event.getCode() == KeyCode.CONTROL ||
                      event.getCode() == KeyCode.SHIFT ||
                      event.getCode() == KeyCode.INSERT || 
                      event.getCode() == KeyCode.PAGE_UP ||
                      event.getCode() == KeyCode.PAGE_DOWN ||
                      event.getCode() == KeyCode.HOME ||
                      event.getCode() == KeyCode.END ||
                      event.getCode() == KeyCode.NUM_LOCK ||
                      event.getCode() == KeyCode.PRINTSCREEN ||
                      event.getCode() == KeyCode.COPY ||
                      event.getCode() == KeyCode.PASTE) {
                      
                      
            }else {
                
                if (contadorCaracteres == 0) {
                    contadorCaracteres = 0;
                } else {
                    contadorCaracteres = 100;
                    contadorCaracteres = contadorCaracteres - (campoPergunta.getText().length() + 1);
                    labelCaracteres.setText("Caracteres: " + contadorCaracteres);
                }
            }
        });
    }

}
