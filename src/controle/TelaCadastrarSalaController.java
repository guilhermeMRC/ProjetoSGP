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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    private Label labelNumero;
    @FXML
    private TextField campoPesquisar;
    @FXML
    private JFXButton botaoCadastrar;
    @FXML
    private TextField campoNomeSala;
    @FXML
    private JFXCheckBox checkboxSelecionarTodos;

    private ObservableList<Pergunta> obsPerguntas = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
    private SalaDAO salaDAO;
    private TelaListagemSalaController telaListagemSalaController;
    private int cont = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gerarTabela();
        
        //Ta dando uns problemas tem que ajustar isso
        campoPesquisar.addEventFilter(KeyEvent.ANY, eve -> {
            
            trazerPesquisa(campoPesquisar.getText());
            
        });
    }

    /*public void receberParametros(String nomeSala) {
        campoNomeSala.setText(nomeSala);
    }*/

    public void gerarTabela() {
        
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaPergunta.setStyle("-fx-alignment: LEFT;");
   
        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaTempo.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempo.setStyle("-fx-alignment: CENTER;");

        colunaSala.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSala.setStyle("-fx-alignment: CENTER;");

        //obsPerguntas.addAll(perguntaDAO.listarPerguntasAtivasOuDesativadas(true));
        //contarPerguntasSelecionadas();
        tabelaSalas.setItems(carregarPerguntas());

    }

    public ObservableList<Pergunta> carregarPerguntas() {
        PerguntaDAO perguntaDAO = new PerguntaDAO();

        obsPerguntas = FXCollections.observableArrayList(perguntaDAO.listar());
        contarPerguntasSelecionadas();
        
        return obsPerguntas;
    }

    @FXML
    void cadastrarSala(ActionEvent event) {
        
        SalaDAO salaDAO = new SalaDAO();
        List<Pergunta> perguntas = new ArrayList();
        
        int contador = 0;
        
        for (Pergunta p : obsPerguntas) {
            if (p.getCheckbox().isSelected()) {
                perguntas.add(p);
            }
            
        }
        
        /*Valida se o usuário não esta tentando cadastrar uma 
        sala sem nome ou sem alguma pergunta marcada
        como ainda está em teste deixar assim por enquanto 
        porque depois vamos ter que verificar o numero de questões mínimas*/
        if(campoNomeSala.getText().isEmpty()){
            
            menssagem(Alert.AlertType.ERROR, 
                      "Sala", 
                      "Cadastrar sala", 
                      "Não é possível cadastrar sala sem nome!");
            
        }else {
            
            boolean validacao = false;
            
            for(Sala s : salaDAO.listar()){
                
                if(s.getDescricao().equalsIgnoreCase(campoNomeSala.getText())){
                    validacao = true;
                    break;
                }
                
            }
            
            for(Pergunta per : perguntas){
                
                if(per.getDificuldade().equals(Dificuldade.DIFICIL)) {
                    contador ++;
                }
                  
            }
            
            if((perguntas.size() < 90) || (contador < 25) || validacao == true){
                
                menssagem(Alert.AlertType.ERROR, 
                          "Sala", 
                          "Cadastrar sala", 
                          "Não é possivel cadastrar! A sala deve conter um nome diferente das outras "
                          + "já cadastradas e no minimo 90 perguntas das quais 25 devem ser Dificeis.");
                
            }else {
                
                salaDAO = new SalaDAO();
                Sala novaSala = new Sala();
                novaSala.setDescricao(campoNomeSala.getText());
                novaSala.setPerguntas(perguntas);
                
                try {
                    
                    salaDAO.incluir(novaSala);
                    
                    menssagem(Alert.AlertType.NONE, 
                              "Sala", 
                              "Cadastrar sala", 
                              "Sala cadastrada com sucesso!");
                    
                    limparCampos();

                } catch (Exception ex) {
                    
                    menssagem(Alert.AlertType.ERROR, 
                              "Sala", 
                              "Cadastrar sala", 
                              ex.getLocalizedMessage());
                    
                }
            
            }
            
        }
 
    }
    
    //usado para fazer o lance do contador 
    public void contarPerguntasSelecionadas() {

        for (int i = 0; i < obsPerguntas.size(); i++) {
            obsPerguntas.get(i).getCheckbox().setOnAction(this::handleButtonAction);
        }

    }
    
    public void handleButtonAction(ActionEvent event){
        
        for (Pergunta p : tabelaSalas.getItems()) {

            if (p.getCheckbox() == event.getTarget()) {
                
                if(p.getCheckbox().isSelected() == true){
                    cont = cont + 1;
                    labelNumero.setText("N = " + cont);
                    
                }else {
                    
                    cont = cont - 1;
                    labelNumero.setText("N = " + cont);
                }
                
                break;
            }

        }
        
    }
    
    //método para limpar os campos após cadastrar
    public void limparCampos(){
        
        campoNomeSala.clear();
        cont = 0;
        labelNumero.setText("N = " + cont);
        
        for(Pergunta p : tabelaSalas.getItems()){
            
            if(p.getCheckbox().isSelected()){
                
                p.getCheckbox().setSelected(false);
            }
        }
        checkboxSelecionarTodos.setSelected(false);
    }
    
    public void trazerPesquisa(String filtro){
        
        //PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        if(filtro.equals("")){
            
            //gerarTabela();
            tabelaSalas.setItems(obsPerguntas);
            
        }else{
            
            ObservableList<Pergunta> pergs = FXCollections.observableArrayList();
            
            for(Pergunta p : obsPerguntas){
                
                if(p.getDescricao().toLowerCase().contains(filtro.toLowerCase())){
                    pergs.add(p);
                }
                
            }
            
            tabelaSalas.setItems(pergs);
        }
        
    }
    
    @FXML
    public void selecionarTodasPerguntas(ActionEvent event) {

        if(checkboxSelecionarTodos.isSelected()){
            
            for(Pergunta p : obsPerguntas){
                p.getCheckbox().setSelected(true);
                
            }
            cont = obsPerguntas.size();
            labelNumero.setText("N = " + cont);
            
        }else {
            
            for(Pergunta p : obsPerguntas){
                p.getCheckbox().setSelected(false);
            }
            cont = 0;
            labelNumero.setText("N = " + cont);
        }
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
