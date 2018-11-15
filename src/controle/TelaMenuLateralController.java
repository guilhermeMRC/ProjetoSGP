
package controle;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author gnunes
 */
public class TelaMenuLateralController implements Initializable{
    
    
    @FXML
    private JFXButton botaoInicio, botaoPergunta, botaoDisciplina, botaoSalas, botaoBackupBanco, botaosobre;
    private CallBack callBack;
    
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
   @FXML
   public void telas(ActionEvent event){
       JFXButton botao = (JFXButton) event.getSource();
       switch (botao.getText()) {
            case "Inicio":
                callBack.changeScenes("/visao/TelaLogoPrincipal.fxml");
                break;
            case "Perguntas":
                callBack.changeScenes("/visao/TelaListagemPerguntas.fxml");
                break;
            case "Disciplinas":
                callBack.changeScenes("/visao/TelaCadastroDisciplinas.fxml");
                break;
            case "Salas":
                callBack.changeScenes("/visao/TelaListagemSalas.fxml");
                break;
            case "Backup":
                callBack.changeScenes("/visao/TelaBackupERestauracao.fxml");
                break;
            case "Sobre":
                callBack.changeScenes("/visao/TelaSobre.fxml");
                break;
        }
   }
    
}
