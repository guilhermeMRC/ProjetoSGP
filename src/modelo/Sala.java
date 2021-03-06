/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jfoenix.controls.JFXToggleButton;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.ToggleButton;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

/**
 *
 * @author Turma de Sistemas 6° periodo
 */

@Entity
public class Sala implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String descricao;
    private boolean habilitar = true;
    
    @Transient
    private ToggleButton togglebutton = new JFXToggleButton();
    
    @ManyToMany
    private List<Pergunta> perguntas;
    
    public Sala(){
        
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

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public boolean isHabilitar() {
        return habilitar;
    }

    public void setHabilitar(boolean habilitar) {
        this.habilitar = habilitar;
    }

    public ToggleButton getTogglebutton() {
        if(isHabilitar() == true){
            
            this.togglebutton.setSelected(true);
            
        }else{
            
            this.togglebutton.setSelected(false);
            
        }
        return togglebutton;
    }
    
    public void setTogglebutton(ToggleButton toggleButton) {
        this.togglebutton = toggleButton;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.perguntas);
        return hash;
    }

}
