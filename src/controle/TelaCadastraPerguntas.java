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
import com.jfoenix.controls.JFXTimePicker;
import dao.DisciplinaDAO;
import dao.PerguntaDAO;
import modelo.Dificuldade;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import modelo.Alternativa;
import modelo.Disciplina;
import modelo.Pergunta;
import org.eclipse.persistence.jpa.rs.exceptions.JPARSException;

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

    private List<Dificuldade> dificuldades = new ArrayList();
    private ObservableList<Dificuldade> obsDificuldade;
    //private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    //private PerguntaDAO perguntaDAO = new PerguntaDAO();
    private ObservableList<Disciplina> obsDisciplinas;

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

    }
    
    
    public void CadastrarPergunta() {
       
        /*
        O cadastro do tempo da pergunta ainda não esta funcionando, estou verificando o erro 
        no componente de hora, pois ele pega o formato em 24hrs mas quando ele insere no editor
        é infomado somente os valores de 12horas. Ou seja, não tem como simular minutos. 
        */
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        try {
            
            campoTags = new JFXChipView();
            Disciplina disciplina = selecaoDisciplina.getSelectionModel().getSelectedItem();
            List<String> listTags = new ArrayList();
            Pergunta pergunta = new Pergunta();
            
            pergunta.setDescricao(campoPergunta.getText());
            pergunta.setDificuldade(selecaoDificuldadePergunta.getSelectionModel().getSelectedItem());
            
            pergunta.setTempo(converterDoubleParaInteger(sliderTempoPergunta.getValue()));
            
            pergunta.setDisciplina(disciplina);
            pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaA,opcaoA));
            pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaB,opcaoB));
            pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaC,opcaoC));
            pergunta.addAlternativa(cadastrarAlternativa(campoAlternativaD,opcaoD));
            campoTags.getChips().forEach((tags) -> {
                listTags.add(tags);
            });
            
            
            try {
                perguntaDAO.incluirComAlternativas(pergunta);
                Alert mensagemErro = new Alert(Alert.AlertType.WARNING);
                mensagemErro.setTitle("Erro do sistema");
                mensagemErro.setContentText("Pergunta cadastrada com sucesso!");
                mensagemErro.showAndWait();

                limparCampos();

            } catch (Exception ex) {
                Alert mensagemErro = new Alert(Alert.AlertType.WARNING);
                mensagemErro.setTitle("Erro do sistema");
                mensagemErro.setHeaderText("Erro ao cadastrar pergunta");
                mensagemErro.setContentText("Não é possivel cadastrar uma pergunta!"
                                          + "Verifique se todos os dados foram preenchidos corretamente!");
                mensagemErro.showAndWait();
            }

        } catch (Exception ex) {
            Alert mensagemAviso = new Alert(Alert.AlertType.WARNING);
            mensagemAviso.setTitle("Aviso do sistema");
            mensagemAviso.setHeaderText("Erro ao cadastrar pergunta");
            mensagemAviso.setContentText("Não é possivel cadastrar uma pergunta sem informar seus dados!");
            mensagemAviso.showAndWait();
            
        }
    }

    public Alternativa cadastrarAlternativa(TextField alternativa, JFXRadioButton opcao) {
        //List<Alternativa> alternativas = new ArrayList();
        Alternativa a = new Alternativa();
        
        a.setDescricao(alternativa.getText());
    
        
        if(opcao.isSelected()){
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
        obsDisciplinas = FXCollections.observableArrayList(disciplinaDAO.listar());
        selecaoDisciplina.getItems().addAll(obsDisciplinas);
    }
    
    /*Método para converter o valor do slider que vem o double 
    em Integer que foi mapeado no banco*/
    public Integer converterDoubleParaInteger(Double valor){
        
        Integer tempo = valor.intValue();
        return tempo;
    }
    
    
}
