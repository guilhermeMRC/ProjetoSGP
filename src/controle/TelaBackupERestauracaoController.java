/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * FXML Controller class
 *
 * @author GuiGuizinho
 */
public class TelaBackupERestauracaoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    String pathExport = null;
    String pathImport = null;
    String nomeArquivo;
    
    @FXML
    private JFXButton botaoBrowseSalvar;

    @FXML
    private JFXButton botaoExportar;
    
    @FXML
    private JFXButton botaoBrowseProcurar;

    @FXML
    private JFXButton botaoImportar;
    
    @FXML
    private JFXTextField textFieldPathSalvar;
    
    @FXML
    private JFXTextField textFieldPathCarregar;
    
    @FXML
    private Label labelMensagemExport;
    
    @FXML
    private Label labelMensagemImport;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    /*Função que Seleciona onde vai salvar o arquivo do banco de Dados*/
    @FXML
    public void selecionarCaminhoBackup(ActionEvent event) {
        
        Window meuWindows = new Stage();
        FileChooser fc = new FileChooser();
        
        String data = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        
        try {
            File f = fc.showSaveDialog(meuWindows); 
            pathExport = f.getAbsolutePath();
            pathExport = pathExport.replace('\\', '/');
            pathExport = pathExport + "_" + data + ".sql"; 
            textFieldPathSalvar.setText(pathExport);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
    } 

    @FXML
    public void exportarBackup(ActionEvent event) {
        
        Process p = null;
        
        if (textFieldPathSalvar.getText().isEmpty()) {
            
            Alert mensagem = new Alert(Alert.AlertType.ERROR);
            mensagem.setTitle("Alerta");
            mensagem.setHeaderText("Erro ao exportar o Banco de Dados!");
            mensagem.setContentText("Não é possivel exportar um banco sem selecionar arquivo!");
            mensagem.show();
            labelMensagemExport.setText("Mensagem");
            
        } else {
            
            try {
            
                Runtime runtime = Runtime.getRuntime();
                //p=runtime.exec("C:/Program Files/MySQL/MySQL Workbench 6.3 CE/mysqldump.exe -uroot --add-drop-database -B bancosgp -r" + pathExport);
                p=runtime.exec("./Utilitarios para Backup/mysqldump.exe -uroot --add-drop-database -B bancosgp -r" + pathExport);

                int processComplete = p.waitFor();
                if(processComplete == 0){

                    labelMensagemExport.setText("Backup Criado com Sucesso!");
                    textFieldPathSalvar.setText("");

                }else{

                    labelMensagemExport.setText("Não foi possível criar o backup!");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            
        }
        
    }
    
    @FXML
    public void SelecionarArquivo(ActionEvent event) {
        
        Window meuWindows = new Stage();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("sql","*.sql"));
        try {
            File f = fc.showOpenDialog(meuWindows); 
            pathImport = f.getAbsolutePath();
            pathImport = pathImport.replace('\\', '/');
            textFieldPathCarregar.setText(pathImport);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
    }

    @FXML
    public void ImportarBackup(ActionEvent event) {

        String user = "root";
        String pass = "";
        String[] restoreCmd = new String[]{
          /*"C:/Program Files/MySQL/MySQL Workbench 6.3 CE/mysql.exe",
            "--user="+user,
            "--password="+pass,
            "-e","source "+pathImport  
            
            "./mysql/bin/mysql.exe",*/
            
            "./Utilitarios para Backup/mysql.exe",
            "--user="+user,
            "--password="+pass,
            "-e","source "+pathImport  
        };
        
        Process processo;
        
        if(textFieldPathCarregar.getText().isEmpty()){
                Alert mensagem = new Alert(Alert.AlertType.ERROR);
                mensagem.setTitle("Alerta");
                mensagem.setHeaderText("Erro ao Importar o Banco de Dados!");
                mensagem.setContentText("Não é possivel importar um banco sem selecionar arquivo!");
                mensagem.show();
                labelMensagemImport.setText("Mensagem");
            
        }else {
            
            try {
          
                processo = Runtime.getRuntime().exec(restoreCmd);
                int procCom = processo.waitFor();

                if(procCom == 0){

                    labelMensagemImport.setText("Importado com Sucesso!");
                    textFieldPathCarregar.setText("");

                }else {

                    labelMensagemImport.setText("Arquivo não Importado!");
                }
            
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
       
    }
    
}
