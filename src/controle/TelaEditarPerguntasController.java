/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChip;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.skins.JFXChipViewSkin;
import dao.DisciplinaDAO;
import dao.PerguntaDAO;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import modelo.Alternativa;
import modelo.Dificuldade;
import modelo.Disciplina;
import modelo.Pergunta;
import org.hibernate.mapping.Collection;

/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaEditarPerguntasController implements Initializable {

    TelaListagemPerguntasController telaLPC;

    @FXML
    private JFXTextArea campoPergunta;

    @FXML
    private JFXRadioButton opcaoA;

    @FXML
    private ToggleGroup Alternativas;

    @FXML
    private JFXRadioButton opcaoB;

    @FXML
    private JFXRadioButton opcaoC;

    @FXML
    private JFXRadioButton opcaoD;

    @FXML
    private TextField campoAlternativaA;

    @FXML
    private TextField campoAlternativaB;

    @FXML
    private TextField campoAlternativaC;

    @FXML
    private TextField campoAlternativaD;

    @FXML
    private JFXComboBox<Dificuldade> selecaoDificuldadePergunta;

    @FXML
    private JFXComboBox<Disciplina> selecaoDisciplina;

    @FXML
    private JFXButton botaoEditar;

    @FXML
    private JFXChipView<String> campoTags;

    @FXML
    private JFXSlider sliderTempoPergunta;
    
    @FXML
    private Label labelCaracteres; 

    private List<Dificuldade> dificuldades = new ArrayList();
    private ObservableList<Dificuldade> obsDificuldade;
    private PerguntaDAO perguntaDAO;
    private List<Disciplina> displinas = new ArrayList<>();
    private ObservableList<Disciplina> obsDisciplinas;
    private Pergunta perguntaNova = new Pergunta();
    public int contadorCaracteres = 100;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        carregarDificuldades();
        carregarDisciplinas();
        
        labelCaracteres.setText("Caracteres: 700");
        limitarCampos(campoAlternativaA, 100);
        limitarCampos(campoAlternativaB, 100);
        limitarCampos(campoAlternativaC, 100);
        limitarCampos(campoAlternativaD, 100);
        limitarCampos(campoPergunta, 700);
    }

    public void receberParametros(TelaListagemPerguntasController tela, Pergunta pergunta) {

        //estou setando o id por aqui 
        perguntaNova.setId(pergunta.getId());
        perguntaNova.setAlternativas(pergunta.getAlternativas());
        perguntaNova.setHabilitar(pergunta.isHabilitar());
        
        campoPergunta.setText(pergunta.getDescricao());

        campoAlternativaA.setText(pergunta.getAlternativas().get(0).getDescricao());
        if (pergunta.getAlternativas().get(0).isCorreto() == true) {
            opcaoA.setSelected(true);
        }

        campoAlternativaB.setText(pergunta.getAlternativas().get(1).getDescricao());
        if (pergunta.getAlternativas().get(1).isCorreto() == true) {
            opcaoB.setSelected(true);
        }

        campoAlternativaC.setText(pergunta.getAlternativas().get(2).getDescricao());
        if (pergunta.getAlternativas().get(2).isCorreto() == true) {
            opcaoC.setSelected(true);
        }

        campoAlternativaD.setText(pergunta.getAlternativas().get(3).getDescricao());
        if (pergunta.getAlternativas().get(3).isCorreto() == true) {
            opcaoD.setSelected(true);
        }

        selecaoDificuldadePergunta.getSelectionModel().select(pergunta.getDificuldade());

        /*só assim para conseguir mostrar a disciplina, 
        mas acho que tem algum jeito mais fácil de se fazer isso*/
        for (int i = 0; i < displinas.size(); i++) {
            if (displinas.get(i).getDescricao().equals(pergunta.getDisciplina().getDescricao())) {
                selecaoDisciplina.getSelectionModel().select(i);
            }
        }

        sliderTempoPergunta.setValue(pergunta.getTempo());
        
        campoTags.getChips().addAll(pergunta.getTags());
                
        telaLPC = tela;
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

    @FXML
    public void editarPergunta(ActionEvent event) {

        perguntaDAO = new PerguntaDAO();
        List<String> listTags = new ArrayList<>();

        Disciplina disciplina = selecaoDisciplina.getSelectionModel().getSelectedItem();

        perguntaNova.setDescricao(campoPergunta.getText());
        perguntaNova.setDificuldade(selecaoDificuldadePergunta.getSelectionModel().getSelectedItem());
        perguntaNova.setDisciplina(disciplina);
        perguntaNova.setTempo(converterDoubleParaInteger(sliderTempoPergunta.getValue()));

        perguntaNova.getAlternativas().get(0).setDescricao(campoAlternativaA.getText());
        perguntaNova.getAlternativas().get(1).setDescricao(campoAlternativaB.getText());
        perguntaNova.getAlternativas().get(2).setDescricao(campoAlternativaC.getText());
        perguntaNova.getAlternativas().get(3).setDescricao(campoAlternativaD.getText());

        perguntaNova.getAlternativas().get(0).setCorreto(opcaoA.isSelected());
        perguntaNova.getAlternativas().get(1).setCorreto(opcaoB.isSelected());
        perguntaNova.getAlternativas().get(2).setCorreto(opcaoC.isSelected());
        perguntaNova.getAlternativas().get(3).setCorreto(opcaoD.isSelected());
        
        listTags.addAll(campoTags.getChips());
        perguntaNova.setTags(listTags);

        if (campoPergunta.getText().isEmpty() || (campoAlternativaA.getText().isEmpty()
                || campoAlternativaB.getText().isEmpty() || campoAlternativaC.getText().isEmpty()
                || campoAlternativaD.getText().isEmpty())
                || selecaoDisciplina.getSelectionModel().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pergunta");
            alert.setHeaderText("Editar pergunta");
            alert.setContentText("Não foi editar pergunta! Por favor, verifique se todos os campos estão preenchidos.");
            alert.show();
            
        } else {

            perguntaDAO.atualizarComAlternativas(perguntaNova);

            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setContentText("Pergunta editada com sucesso!");
            mensagem.showAndWait();
            Stage stage = new Stage();
            stage = (Stage) botaoEditar.getScene().getWindow();
            stage.close();
            
        }
    }

    public Integer converterDoubleParaInteger(Double valor) {

        Integer tempo = valor.intValue();
        return tempo;
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
        displinas = disciplinaDAO.listarDisciplinasAtivasOuDesativadas(true);
        obsDisciplinas = FXCollections.observableArrayList(displinas);
        selecaoDisciplina.getItems().addAll(obsDisciplinas);
    }

    public Alternativa editarAlternativa(TextField alternativa, JFXRadioButton opcao) {
        Alternativa a = new Alternativa();
        a.setDescricao(alternativa.getText());

        if (opcao.isSelected()) {
            a.setCorreto(true);
        }
        return a;
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
}
