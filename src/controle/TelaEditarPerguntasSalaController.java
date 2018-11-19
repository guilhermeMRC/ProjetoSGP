/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.PerguntaDAO;
import dao.SalaDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelo.Dificuldade;
import modelo.Pergunta;
import modelo.Sala;

/**
 *
 * @author gnunes
 */
public class TelaEditarPerguntasSalaController implements Initializable{
   
    @FXML
    private BorderPane borderPanePrincipal;
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
    private TableColumn<Pergunta, ToggleButton> colunaHabilitar;
    @FXML
    private TableColumn<Pergunta, CheckBox> colunaSelecionar;
    @FXML
    private TableView<Pergunta> tabelaPerguntasNaoEscolhidas;
    @FXML
    private TableColumn<Pergunta, String> colunaPerguntaTD;
    @FXML
    private TableColumn<Pergunta, String> colunaDisciplinaTD;
    @FXML
    private TableColumn<Pergunta, Dificuldade> colunaDificuldadeTD;
    @FXML
    private TableColumn<Pergunta, Integer> colunaTempoTD;
    @FXML
    private TableColumn<Pergunta, CheckBox> colunaSelecionarTD;
    @FXML
    private JFXCheckBox checkboxSelecionarTodosEsquerda;
    @FXML
    private JFXCheckBox checkboxSelecionarTodosDireita;
    @FXML
    private JFXButton botaoSalvar;
    @FXML
    private TextField campoNomeSala;
    
    private TelaListagemSalaController telaLSC;
    private ObservableList<Pergunta> perguntas = FXCollections.observableArrayList();
    private ObservableList<Pergunta> perguntasNaoEscolhidas = FXCollections.observableArrayList();
    private PerguntaDAO perguntaDAO;
    private Pergunta pergunta;
 
    private Sala novaSala = new Sala();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    /*Esse método é responsavel por iniciar o evento dos 
    togglesbuttons da tabela ele pega qual a pergunta daquele
    togglebutton específio e quando eu tenho esses dados 
    é só dar um merge pelo método atualizar no PerguntaDAO*/
    private void handleButtonAction(ActionEvent event) {

        //seto o PerguntaDAO que faz a conexão com o banco de dados
        PerguntaDAO perguntaDAO = new PerguntaDAO();

        //esse for varre a tabela atrás do togglebutton que eu cliquei
        for (Pergunta p : tabelaSalas.getItems()) {

            //aqui faz a checagem de qual tooglebutton eu apertei
            if (p.getTogglebutton() == event.getTarget()) {
                //aqui joga as informações para uma pergunta instaciada como atributo la em cima na classe
                pergunta = p;
                break;
            }

        }

        //aqui pega o valor do atributo habilitar 
        boolean status = pergunta.isHabilitar();
        
        //aqui ele checa e atualiza no banco
        if (status == true) {

            pergunta.setHabilitar(false);
            perguntaDAO.atualizarComAlternativas(pergunta);

        } else {

            pergunta.setHabilitar(true);
            perguntaDAO.atualizarComAlternativas(pergunta);

        }
        
        gerarTabela();
        //escolherOpcao(event);
    }

    /*Método que faz o togglebuttons aparecerem na
    tabela ele pega e faz um for no obsrvablelist e 
    depois seta um evento em cada togglebutton*/
    public void habilitarDesabilitarPergunta() {

        for (int i = 0; i < perguntas.size(); i++) {
            perguntas.get(i).getTogglebutton().setOnAction(this::handleButtonAction);
        }

    }
    
    //aqui está projetando a tabela do lado esquerdo
    public void gerarTabela(){
        
        colunaPergunta.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplina.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplina.setStyle("-fx-alignment: CENTER;");

        colunaDificuldade.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldade.setStyle("-fx-alignment: CENTER;");

        colunaTempo.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempo.setStyle("-fx-alignment: CENTER;");
        
        colunaHabilitar.setCellValueFactory(new PropertyValueFactory("togglebutton"));
        colunaHabilitar.setStyle("-fx-alignment: CENTER;");
        
        colunaSelecionar.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSelecionar.setStyle("-fx-alignment: CENTER;");
        
        habilitarDesabilitarPergunta();
        tabelaSalas.setItems(perguntas);
        
    }
    
    /*Gera a tabela com Perguntas que podem ser escolhidas 
    ela é projetada na direita da tela*/
    public void gerarTabelaComPerguntasNaoEscolhidas(){
        
        colunaPerguntaTD.setCellValueFactory(new PropertyValueFactory("descricao"));

        colunaDisciplinaTD.setCellValueFactory(new PropertyValueFactory("disciplina"));
        colunaDisciplinaTD.setStyle("-fx-alignment: CENTER;");

        colunaDificuldadeTD.setCellValueFactory(new PropertyValueFactory("dificuldade"));
        colunaDificuldadeTD.setStyle("-fx-alignment: CENTER;");

        colunaTempoTD.setCellValueFactory(new PropertyValueFactory("tempo"));
        colunaTempoTD.setStyle("-fx-alignment: CENTER;");
        
        colunaSelecionarTD.setCellValueFactory(new PropertyValueFactory("checkbox"));
        colunaSelecionarTD.setStyle("-fx-alignment: CENTER;");
        
        tabelaPerguntasNaoEscolhidas.setItems(perguntasNaoEscolhidas);
    }
   
    @FXML
    public void EditarSala(ActionEvent event) {
        
        /*Chamo uma conexão com o banco de dados 
        para poder atualizar a sala*/
        SalaDAO salaDAO = new SalaDAO();
        
        novaSala.setDescricao(campoNomeSala.getText());
        novaSala.setPerguntas(perguntas);
        
        /*Checa se a sala tem um mínimo de salas cadastradas.
        Nesse caso coloquei menor que 5 para poder testar*/
        if(novaSala.getPerguntas().size() < 5){
            
            /*Mensagem que checa se a sala tiver menos que a
            quantidade estipulada, lança a mensagem e nada acontece*/
            Alert mensagem = new Alert(Alert.AlertType.ERROR);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Sala não pode ser editada com menos de 5 perguntas");
            mensagem.show();
            
        }else {
            
            /*Se caso passar a condição à cima, vai cair nesse 
            else que tem um contador que servirá para contar o
            número de perguntas com a dificuldade DIFICIL*/
            int contador = 0;
            
            for(Pergunta p : novaSala.getPerguntas()){
                
                /*Aqui o for corre cada pergunta. Se a pergunta 
                conter dificuldade DIFICIL o cantador contará mais um
                marcando assim as perguntas com a referida dificuldade*/
                if(p.getDificuldade().equals(Dificuldade.DIFICIL)){
                    
                    contador ++;
                }
            }
            
            /*Se o contador conter um número menor que o determinado
            (Aqui determinei 3 para testar) o programa exibirá uma 
            mensagem dizendo que não é possivel editar a sala e nada acontece*/
            if(contador < 3){
                
                Alert mensagem = new Alert(Alert.AlertType.ERROR);
                mensagem.setTitle("Mensagem do sistema");
                mensagem.setHeaderText("Atualizar sala");
                mensagem.setContentText("Sala não pode ser editada com menos de 3 perguntas dificeis!");
                mensagem.show();
                
            }else {
                
                /*Se chegar aqui o programa vai atualizar 
                normalmente*/
                try {
                
                    salaDAO.atualizar(novaSala);

                    Alert mensagem = new Alert(Alert.AlertType.NONE);
                    mensagem.setTitle("Mensagem do sistema");
                    mensagem.setHeaderText("Atualizar sala");
                    mensagem.setContentText("sala cadastrada com sucesso!");
                    mensagem.getOnCloseRequest();
                    mensagem.getButtonTypes().add(ButtonType.OK);
                    mensagem.showAndWait();
                    
                    /*Seta um stage e com o getWindow pega a sena da tela
                    ai é só chamar o close() para fechar a tela*/
                    Stage stage = (Stage) borderPanePrincipal.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
    }
    
    /*Esse método é o evento do botão que adiciona perguntas 
    a tabela da esquerda (perguntas na sala). Basicamente, o usuário 
    seleciona as perguntas que ele quer na tabela da direita e 
    joga para a tabela da esquerda*/
    @FXML
    public void addPergunta(ActionEvent event) {
       
        /*Esse contador servirá para checar alguma pergunta
        foi marcada na tabela.*/
        int contador = 0;
        for(Pergunta p : perguntasNaoEscolhidas){
            
            /*ao passar por cada item, esse if checa se tem algum 
            checkbox marcado, caso tenha, ele adiciona esse elemento a 
            lista que é usada para gerar a tabela da esquerda*/
            if(p.getCheckbox().isSelected()){
                
                p.getCheckbox().setSelected(false);
                perguntas.add(p);
                
            }else {
                
                contador++;
            }
        }
        
        /*Aqui está checando se o cantador for igual a lista do lado direito
        quer dizer que não houve nenhum checbox marcado*/
        if(contador == tabelaPerguntasNaoEscolhidas.getItems().size()){
            
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Por favor marque as perguntas!");
            mensagem.show();
        }
        
        //Daqui para baixo atualiza as tabelas
        atualizarTabelas();
        /*gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();*/
    }

    @FXML
    public void removerPergunta(ActionEvent event) {
        
        ObservableList<Pergunta> perguntasAuxiliar = FXCollections.observableArrayList();
        perguntasAuxiliar.setAll(perguntas);
        int contador = 0;
        
        for(Pergunta p : perguntasAuxiliar){
            
            if(p.getCheckbox().isSelected()){
                
                perguntas.remove(p);
                
            }else {
                
                contador++;
            }
           
        }
        
        if(contador == perguntasAuxiliar.size()){
            
            Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
            mensagem.setTitle("Mensagem do sistema");
            mensagem.setHeaderText("Atualizar sala");
            mensagem.setContentText("Por favor marque as perguntas!");
            mensagem.show();
        }
        
        atualizarTabelas();
        /*
        gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();*/
    }
    
    public void trazerPerguntasNaoSelecionadas() {
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        perguntasNaoEscolhidas.setAll(perguntaDAO.listar());
        
        for(Pergunta ps : perguntas){
            
            for(Pergunta pt : perguntasNaoEscolhidas){
                
                if(ps.getId().equals(pt.getId())){
                    perguntasNaoEscolhidas.remove(pt);
                    break;
                }
                
            }
        }
        
    }
    
    public void setNomeSala(String nomeSala){
        this.campoNomeSala.setText(nomeSala);
    }
    
    public String getNomeSala(){
        return this.campoNomeSala.getText();
    }
    
    public void setSala(TelaListagemSalaController TelaListagemSala, Sala sala){
        
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        
        novaSala.setId(sala.getId());
        novaSala.setDescricao(sala.getDescricao());
        novaSala.setPerguntas(sala.getPerguntas());
        
        perguntas.setAll(novaSala.getPerguntas());
        
        atualizarTabelas();
        /*gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();*/
        
        telaLSC = TelaListagemSala;
    }
    
    public void setPerguntas(ObservableList<Pergunta> perguntas){
        this.perguntas.addAll(perguntas);
    }
    
    public ObservableList<Pergunta> getPerguntas(){
        return this.perguntas;
    }
    
    public void atualizarTabelas() {
        
        gerarTabela();
        trazerPerguntasNaoSelecionadas();
        gerarTabelaComPerguntasNaoEscolhidas();
        
    }
    
    @FXML
    public void selecionarTodasPerguntasSala(ActionEvent event) {
        
        if(checkboxSelecionarTodosEsquerda.isSelected()){
            
            for(Pergunta p : perguntas){
                p.getCheckbox().setSelected(true);
            }
            
        }else {
            
            for(Pergunta p : perguntas){
                p.getCheckbox().setSelected(false);
            }
        }
        
    }
    
    @FXML
    public void selecionarTodasPerguntasNaoSala(ActionEvent event) {

        if(checkboxSelecionarTodosDireita.isSelected()){
            
            for(Pergunta p : perguntasNaoEscolhidas){
                p.getCheckbox().setSelected(true);
            }
            
        }else {
            
            for(Pergunta p : perguntasNaoEscolhidas){
                p.getCheckbox().setSelected(false);
            }
        }
    }


}
