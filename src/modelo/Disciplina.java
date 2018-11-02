/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import java.io.Serializable;
import javafx.scene.control.ToggleButton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.hibernate.annotations.Columns;

/**
 *
 * @author Turma de Sistemas 6° periodo
 */

@Entity
public class Disciplina implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String descricao;
    private boolean habilitar = true;
    
    @Transient 
    private ToggleButton togglebutton = new JFXToggleButton();

    public Disciplina(){
    
    }
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isHabilitar() {
        return habilitar;
    }

    public void setHabilitar(boolean habilitar) {
        this.habilitar = habilitar;
    }
    
    /*Aqui é onde faz as mudanças na aparencia do tooglebutto
    quando ele habilita ou desabilita */
    public ToggleButton getTogglebutton() {
        if(isHabilitar() == true){
            this.togglebutton.setSelected(true);
            
        }else{
            
            this.togglebutton.setSelected(false);  
        }
        return togglebutton;
    }

    public void setTogglebutton(ToggleButton togglebutton) {
        this.togglebutton = togglebutton;
    }
    
    @Override
    public String toString() {
        return getDescricao();
    }
}
