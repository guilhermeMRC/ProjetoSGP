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
import java.util.Collections;
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
    
    private List<Dificuldade> dificuldades = new ArrayList();
    private ObservableList<Dificuldade> obsDificuldade;
    //private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private PerguntaDAO perguntaDAO = new PerguntaDAO();
    private List<Disciplina> displinas = new ArrayList<>();
    private ObservableList<Disciplina> obsDisciplinas;
    Pergunta perguntaNova = new Pergunta();
    //List<Long> listIdAlternativas = new ArrayList<>();
    
    public void receberParametros(TelaListagemPerguntasController tela, Pergunta pergunta){
        
        //estou setando o id por aqui 
        perguntaNova.setId(pergunta.getId());
        perguntaNova.setAlternativas(pergunta.getAlternativas());
        //System.out.println("aaa: " + disciplina);
        campoPergunta.setText(pergunta.getDescricao());
        
        campoAlternativaA.setText(pergunta.getAlternativas().get(0).getDescricao());
        if(pergunta.getAlternativas().get(0).isCorreto() == true) opcaoA.setSelected(true);

        campoAlternativaB.setText(pergunta.getAlternativas().get(1).getDescricao());
        if(pergunta.getAlternativas().get(1).isCorreto() == true) opcaoB.setSelected(true);

        campoAlternativaC.setText(pergunta.getAlternativas().get(2).getDescricao());
        if(pergunta.getAlternativas().get(2).isCorreto() == true) opcaoC.setSelected(true);

        campoAlternativaD.setText(pergunta.getAlternativas().get(3).getDescricao());
        if(pergunta.getAlternativas().get(3).isCorreto() == true) opcaoD.setSelected(true);

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
    
    @FXML
    public void editarPergunta(ActionEvent event) {
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
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
        
        /*perguntaNova.addAlternativa(editarAlternativa(campoAlternativaA,opcaoA));
        perguntaNova.addAlternativa(editarAlternativa(campoAlternativaB,opcaoB));
        perguntaNova.addAlternativa(editarAlternativa(campoAlternativaC,opcaoC));
        perguntaNova.addAlternativa(editarAlternativa(campoAlternativaD,opcaoD));*/
        //pergunta.setTags(tags);
        try {
            
            perguntaDAO.atualizarComAlternativas(perguntaNova);
            Alert mensagemErro = new Alert(Alert.AlertType.WARNING);
            mensagemErro.setTitle("Erro do sistema");
            mensagemErro.setContentText("Pergunta editada com sucesso!");
            mensagemErro.showAndWait();
            Stage stage = new Stage();
            stage = (Stage) botaoEditar.getScene().getWindow();
            stage.close();
                
        } catch (Exception e) {
            
            System.out.println("Erro: " + e);
        }
            
        /*campoTags = new JFXChipView();
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
            
        }*/
        
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
    
    public Alternativa editarAlternativa(TextField alternativa, JFXRadioButton opcao) {
        //List<Alternativa> alternativas = new ArrayList();
        Alternativa a = new Alternativa();
        a.setDescricao(alternativa.getText());
       
        if(opcao.isSelected()){
            a.setCorreto(true);
        }
        return a;
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
