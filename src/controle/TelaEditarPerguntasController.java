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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import modelo.Alternativa;
import modelo.Dificuldade;
import modelo.Disciplina;
import modelo.Pergunta;

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
    
    private List<Dificuldade> dificuldades = new ArrayList();
    private ObservableList<Dificuldade> obsDificuldade;
    //private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private PerguntaDAO perguntaDAO = new PerguntaDAO();
    private List<Disciplina> displinas = new ArrayList<>();
    private ObservableList<Disciplina> obsDisciplinas;
    
    public void receberParametros(TelaListagemPerguntasController tela, Pergunta pergunta){
        
        //DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        //Disciplina disciplina = new Disciplina();
        //disciplina = pergunta.getDisciplina();
        
        //System.out.println("aaa: " + disciplina);
        campoPergunta.setText(pergunta.getDescricao());
        
        for(Alternativa a : pergunta.getAlternativas()){
            
            switch(a.getLetraAlternativa()){
                case A: 
                    System.out.println("aaa");
                    campoAlternativaA.setText(a.getDescricao());
                    if(a.isCorreto() == true) opcaoA.setSelected(true);
                    
                    break;
                case B:
                    System.out.println("bbb");
                    campoAlternativaB.setText(a.getDescricao());
                    if(a.isCorreto() == true) opcaoB.setSelected(true);
                    
                    break;
                case C:
                    System.out.println("ccc");
                    campoAlternativaC.setText(a.getDescricao());
                    if(a.isCorreto() == true) opcaoC.setSelected(true);
                    
                    break;
                case D:
                    System.out.println("ddd");
                    campoAlternativaD.setText(a.getDescricao());
                    if(a.isCorreto() == true) opcaoD.setSelected(true);
                   
                    break;        
            }
        }
        
        selecaoDificuldadePergunta.getSelectionModel().select(pergunta.getDificuldade());
        
        /*só assim para conseguir mostrar a disciplina, 
        mas acho que tem algum jeito mais fácil de se fazer isso*/
        
        for(int i = 0; i < displinas.size(); i++){
            if(displinas.get(i).getDescricao().equals(pergunta.getDisciplina().getDescricao())){
                selecaoDisciplina.getSelectionModel().select(i);
            }
        }
        
        sliderTempoPergunta.setValue(pergunta.getTempo());
        
        telaLPC = tela;
    }
    
    public Integer converterDoubleParaInteger(Double valor){
        
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
        displinas = disciplinaDAO.listar();
        obsDisciplinas = FXCollections.observableArrayList(displinas);
        selecaoDisciplina.getItems().addAll(obsDisciplinas);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        carregarDificuldades();
        carregarDisciplinas();
    }    

}
