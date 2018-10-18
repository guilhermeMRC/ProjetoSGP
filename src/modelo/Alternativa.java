/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 *
 * @author Turma de Sistemas 6° periodo
 */

@Entity
public class Alternativa implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String descricao;
    private boolean correto;
    
    /*Criei esse enum para marcar a ordem certa
    porque está gravando fora de ordem no banco*/
    @Enumerated(EnumType.STRING)
    private LetraAlternativa letraAlternativa;
    
    public Alternativa (){
        
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

    public boolean isCorreto() {
        return correto;
    }

    public void setCorreto(boolean correto) {
        this.correto = correto;
    }

    public LetraAlternativa getLetraAlternativa() {
        return letraAlternativa;
    }

    public void setLetraAlternativa(LetraAlternativa letraAlternativa) {
        this.letraAlternativa = letraAlternativa;
    }

    @Override
    public String toString() {
        
        return getDescricao();
    }

    
    
}
