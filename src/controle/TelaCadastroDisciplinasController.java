/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import modelo.Disciplina;

/**
 * FXML Controller class
 *
 * @author gnunes
 */
public class TelaCadastroDisciplinasController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private StackPane rootpane;

    @FXML
    private TextField campoPesquisar;

    @FXML
    private JFXButton btnIncluir, btnAlterar, btnExcluir;

    @FXML
    private JFXTreeTableView<DisciplinaTableView> treeTableView;

    private ObservableList<Disciplina> observableListDisciplinas;
    private List<Disciplina> listaDisciplinas = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewDisciplinas();
    }

    public void TelaCadastroDisciplinasController() {
    }

    @FXML
    public void inserirDisciplina(ActionEvent event) {

        Disciplina novaDisciplina = new Disciplina();
        
        /*
        Cria uma caixa de dialogo perguntando o nome da disciplina a ser cadastrada
        */
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de disciplina");
        dialog.setHeaderText("Cadastrar disciplina");
        dialog.setContentText("Informe o nome da disciplina:");

        Optional<String> resultado = dialog.showAndWait(); //Utilizado para pegar o valor informando no campo e mostrar a caixa de dialogo

        if (resultado.isPresent()) { //Verifica se o botão OK foi pressionado
            try {
                novaDisciplina.setDescricao(resultado.get());
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

                disciplinaDAO.incluir(novaDisciplina);
                dialog.close();
                carregarTableViewDisciplinas();
            } catch (Exception e) {
                /*
                Cria uma caixa de dialogo exibindo uma mensagem de erro
                */
                Alert mensagemErro = new Alert(AlertType.ERROR);
                mensagemErro.setTitle("Mensagem de erro do sistema");
                mensagemErro.setHeaderText("Erro ao cadastrar disciplina");
                mensagemErro.setContentText("Verique se:\n"
                        + "1 - Todos os campos foram preenchidos corretamente\n"
                        + "2 - O serviço do banco de dados está funcionando");
            }
        }
    }

    @FXML
    public void alterarDisciplina(ActionEvent event) {
        /*
        Jão, para você que estava com duvida sobre como pegar a informação da tabela e informar na tela de edição de disciplina,
        esse é o pulo do Gato. Oque esta sendo feito aqui é justamente selecionar a tabela (Classe principal). Em seguida,
        é retornado a tabela em si e não a classe Principal, por fim, permitindo que eu possa pegar o item que eu selecionei da tabela.
        */
        TreeItem<DisciplinaTableView> selecionarDisciplina = treeTableView.getSelectionModel().getSelectedItem();
        Disciplina disciplina = new Disciplina();
        
        /*
        Cria uma caixa de dialogo perguntando o nome da nova da disciplina e ja inicializa o campo com o nome da disciplina selecionada.
        */
        
        TextInputDialog dialogAlteracao = new TextInputDialog(selecionarDisciplina.getValue().getDisciplina());
        dialogAlteracao.setTitle("Alterar disciplina");
        dialogAlteracao.setHeaderText("Alterar disciplina");
        dialogAlteracao.setContentText("Informe o novo nome da disciplina");

        Optional<String> resultado = dialogAlteracao.showAndWait();

        if (resultado.isPresent()) {
            try {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                //O método getValue() é uma propriedade do StringProprety, para que ele retorne uma String 
                disciplina.setId(selecionarDisciplina.getValue().getId());
                disciplina.setDescricao(resultado.get()); 
                disciplinaDAO.atualizar(disciplina);
                dialogAlteracao.close();
                carregarTableViewDisciplinas();
            } catch (Exception e) {
                
                 /*
                Cria uma caixa de dialogo exibindo uma mensagem de erro
                */
                Alert mensagemErro = new Alert(AlertType.ERROR);
                mensagemErro.setTitle("Mensagem de erro do sistema");
                mensagemErro.setHeaderText("Erro ao deletar a disciplinas");
                mensagemErro.setContentText("Verifique se o serviço do banco de dados está funcionando corretamente!");
            }
        }
    }

    @FXML
    public void excluirDisciplina(ActionEvent event) {
        TreeItem<DisciplinaTableView> selecionarDisciplina = treeTableView.getSelectionModel().getSelectedItem();
        Disciplina disciplina = new Disciplina();

        Alert mensagemConfirmacao = new Alert(AlertType.CONFIRMATION);
        mensagemConfirmacao.setTitle("Mensagem de confirmação");
        mensagemConfirmacao.setHeaderText("Exclusão de disciplina");
        mensagemConfirmacao.setContentText("Deseja excluir essa disciplina?");

        Optional<ButtonType> opcao = mensagemConfirmacao.showAndWait();

        if (opcao.get() == ButtonType.OK) {
            try {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                disciplinaDAO.excluir(Disciplina.class, selecionarDisciplina.getValue().getId());
                mensagemConfirmacao.close();
                carregarTableViewDisciplinas();
            } catch (Exception e) {
                Alert mensagemErro = new Alert(AlertType.ERROR);
                mensagemErro.setTitle("Mensagem de erro do sistema");
                mensagemErro.setHeaderText("Erro ao deletar a disciplinas");
                mensagemErro.setContentText("Verifique se o serviço do banco de dados está funcionando corretamente!");
            }
        }

    }

    public List<Disciplina> carregarDisciplinas() {
        DisciplinaDAO dao = new DisciplinaDAO();

        try {
            listaDisciplinas = dao.listar();
            return listaDisciplinas;

        } catch (Exception e) {

            Alert mensagemErro = new Alert(AlertType.ERROR);
            mensagemErro.setTitle("Mensagem de erro do sistema");
            mensagemErro.setHeaderText("Erro ao carregar a lista de disciplinas");
            mensagemErro.setContentText("Verifique se o serviço do banco de dados está funcionando corretamente!");

            return listaDisciplinas;
        }
    }

    public void carregarTableViewDisciplinas() {
        
        /*
        Cria uma coluna na tabela. São criados Callbacks para que ele pegue o valor da classe e informe na tabela
        */
        
        JFXTreeTableColumn<DisciplinaTableView, String> idDisciplina = new JFXTreeTableColumn<>("ID");
        idDisciplina.setPrefWidth(75);
        idDisciplina.setResizable(false);
        idDisciplina.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DisciplinaTableView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DisciplinaTableView, String> param) {
                return param.getValue().getValue().id;
            }
        });

        JFXTreeTableColumn<DisciplinaTableView, String> descricao = new JFXTreeTableColumn<>("DESCRICAO");
        descricao.setPrefWidth(567);
        descricao.setResizable(false);
        descricao.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DisciplinaTableView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DisciplinaTableView, String> param) {
                return param.getValue().getValue().disciplina;
            }
        });

        ObservableList<DisciplinaTableView> disciplinas = FXCollections.observableArrayList();

        for (int i = 0; i < carregarDisciplinas().size(); i++) {
            disciplinas.add(new DisciplinaTableView((String) carregarDisciplinas().get(i).getId().toString(), carregarDisciplinas().get(i).getDescricao()));
        }

        final TreeItem<DisciplinaTableView> root = new RecursiveTreeItem<DisciplinaTableView>(disciplinas, RecursiveTreeObject::getChildren);
        treeTableView.getColumns().setAll(idDisciplina, descricao);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
        
        /*
        Listener que ira verificar em tempo real se o nome digitado no campo pesquisa existe dentro da tabela.
        Ele fica escutando a cada caracter digitado e faz automaticamente uma busca pela disciplina digitada.
        */
        
        campoPesquisar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                treeTableView.setPredicate(new Predicate<TreeItem<DisciplinaTableView>>() {
                    @Override
                    public boolean test(TreeItem<DisciplinaTableView> disciplina) {
                        Boolean flag = disciplina.getValue().disciplina.getValue().contains(newValue);
                        return flag;
                    }
                });
            }
        });
    }
    
    /*
    Classe utilizada para manipular a tabela
    */
    
    class DisciplinaTableView extends RecursiveTreeObject<DisciplinaTableView> {

        StringProperty id;
        StringProperty disciplina;

        public DisciplinaTableView(String id, String disciplina) {
            this.id = new SimpleStringProperty(id);
            this.disciplina = new SimpleStringProperty(disciplina);
        }

        public Long getId() {
            Long id = Long.parseLong(this.id.get());
            return id;
        }

        public String getDisciplina() {
            return this.disciplina.get();
        }
    }

}
