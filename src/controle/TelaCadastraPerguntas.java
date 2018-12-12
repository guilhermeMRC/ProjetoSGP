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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import modelo.Dificuldade;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    public int contadorCaracteres = 100;

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
        labelCaracteres.setText("Caracteres: 700");
        limitarCampos(campoAlternativaA, 100);
        limitarCampos(campoAlternativaB, 100);
        limitarCampos(campoAlternativaC, 100);
        limitarCampos(campoAlternativaD, 100);
        limitarCampos(campoPergunta, 700);
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

                menssagem(Alert.AlertType.ERROR, "Disciplina", 
                          "Cadastrar Disciplina", 
                          "Não foi póssivel cadastrar disciplina sem nome!");
                
            } else {
                
                disciplina.setDescricao(resultado.get());
                
                try {

                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

                    disciplinaDAO.incluir(disciplina);

                    menssagem(Alert.AlertType.NONE, "Disciplina", 
                              "Cadastrar Disciplina", 
                              "Disciplina cadastrada com sucesso!");
                    
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

            menssagem(Alert.AlertType.ERROR, 
                     "Pergunta", 
                     "Cadastrar pergunta", 
                     "Não foi possível cadastrar a pergunta! Por favor, "
                     + "verifique se todos os campos estão preenchidos.");
            
            /*Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pergunta");
            alert.setHeaderText("Cadastro de pergunta");
            alert.setContentText("Não foi possível cadastrar a pergunta. Por favor, verifique se todos os campos estão preenchidos");
            alert.show();*/

        } else {

            menssagem(Alert.AlertType.NONE, 
                    "Pergunta", 
                    "Cadastrar pergunta", 
                    "Pergunta cadastrada com sucesso!");
            
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
    
    private void limitarCampos(TextInputControl textAtual, int max){
        textAtual.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(textAtual.getText().length() >= max){
                    textAtual.setText(String.copyValueOf(textAtual.getText().toCharArray(), 0, max));
                }
                
                if(textAtual.getClass() == JFXTextArea.class){
                    contadorCaracteres = max - textAtual.getText().length();
                    labelCaracteres.setText("Caracteres: " + contadorCaracteres);
                }
            }
            
        });
    }
    
    public void menssagem(Alert.AlertType tipo, String title, String header, String Content){
        
        Alert mensagem = new Alert(tipo);
        
        if(tipo == Alert.AlertType.NONE){
            
            FontAwesomeIconView icone = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE_ALT);
            icone.setGlyphSize(50);

            Paint paint = new Color(0.0, 0.7, 0.0, 1.0);
            icone.setFill(paint);

            mensagem.setGraphic(icone);
            mensagem.setTitle(title);
            mensagem.setHeaderText(header);
            mensagem.setContentText(Content);
            mensagem.getOnCloseRequest();
            mensagem.getButtonTypes().add(ButtonType.OK);
            mensagem.showAndWait();
            
            
        }else {
            
            mensagem.setTitle(title);
            mensagem.setHeaderText(header);
            mensagem.setContentText(Content);
            mensagem.showAndWait();
        }
        
    }
}
