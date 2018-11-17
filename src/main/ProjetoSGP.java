/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author GuiGuizinho
 */
public class ProjetoSGP extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/visao/TelaInicial.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Sistema de Gerenciamento de Perguntas");
        stage.initStyle(StageStyle.UNDECORATED);
        gerarBD();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void gerarBD() {
        try{
            String user = "root"; //Nome de usuário
            String password = ""; //Senha do mysql
            String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull"; //URL de comunicação com o MYSQL

            Connection connection = DriverManager.getConnection(url, user, password); //Criando a comunicação com o banco
            String sql = "CREATE DATABASE IF NOT EXISTS bancosgp"; //Criando a query de criação do bancosgp caso ele não exista na máquina
            
            Statement statement = (Statement) connection.createStatement();
            statement.executeUpdate(sql); //Executando a query
            statement.close();
            connection.close();
            
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
