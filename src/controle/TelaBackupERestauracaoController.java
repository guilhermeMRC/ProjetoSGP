/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    
    String pathImport = null;
    
    @FXML
    private JFXButton botaoExportar;
    
    @FXML
    private JFXButton botaoImportar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void exportarBackup(ActionEvent event) {
        
        try {

            Process p = null;

            String data = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String arquivoPathPadrao = "./Backup/backup_"+ data + ".sql";
            

            Runtime runtime = Runtime.getRuntime();
            //p=runtime.exec("C:/Program Files/MySQL/MySQL Workbench 6.3 CE/mysqldump.exe -uroot --add-drop-database -B bancosgp -r" + pathExport);
            p=runtime.exec("./Utilitarios para Backup/mysqldump.exe -u root --add-drop-database -B bancosgp -r " + arquivoPathPadrao);

            int processComplete = p.waitFor();
            
            if(processComplete == 0){

                Alert mensagem = new Alert(Alert.AlertType.NONE);

                FontAwesomeIconView icone = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE_ALT);
                icone.setGlyphSize(50);

                Paint paint = new Color(0.0, 0.7, 0.0, 1.0);
                icone.setFill(paint);

                mensagem.setGraphic(icone);
                mensagem.setTitle("Mensagem do sistema");
                mensagem.setHeaderText("Exportar Banco de Dados");
                mensagem.setContentText("Banco de dados exportado com sucesso! "
                                        + "Você pode conferir o arquivo na pasta ./Backup.");
                mensagem.getOnCloseRequest();
                mensagem.getButtonTypes().add(ButtonType.OK);
                mensagem.showAndWait();

            }else{

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Mensagem do sistema");
                alert.setHeaderText("Exportar Banco de Dados");
                alert.setContentText("Erro! Não foi possível exportar o banco.");
                alert.show();
            }

        } catch (Exception e) {

        }
            
    }
        
    /*Esse botão ele importa o arquivo de backup formato .sql
    eu usei um método da classe FileChooser para que ja abra na pasta 
    de backup do projeto, mas pode ficar a cargo do usuário trafegar 
    por outras pastas*/
    
    @FXML
    public void ImportarBackup(ActionEvent event) {

        Window meuWindows = new Stage();
        File file = new  File("./Backup");
        
        FileChooser fc = new FileChooser();
        fc.setTitle("Importar banco de dados");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("sql","*.sql"));
        fc.setInitialDirectory(file);
        
        File f = fc.showOpenDialog(meuWindows); 
        pathImport = f.getAbsolutePath();
        pathImport = pathImport.replace('\\', '/');
        
        try {
            
            String user = "root";
            String pass = "";
            String[] restoreCmd = new String[]{
                "./Utilitarios para Backup/mysql.exe",
                "--user="+user,
                "--password="+pass,
                "-e","source "+pathImport  
            };
            
            Process processo;
            
            processo = Runtime.getRuntime().exec(restoreCmd);
            int procCom = processo.waitFor();

            if(procCom == 0){
                
                menssagem(Alert.AlertType.NONE, 
                          "Importar", 
                          "Importar banco de dados", 
                          "Banco de dados importado com sucesso!");
                
            }else {

                menssagem(Alert.AlertType.ERROR,
                          "Importar" ,
                          "Importar banco de dados", 
                          "Erro! Não foi possível importar o banco.");
                
            }
            
        } catch (Exception e) {
            
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
