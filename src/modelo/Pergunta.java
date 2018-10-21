/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author Turma de Sistemas 6° periodo
 */

@Entity
public class Pergunta implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String descricao;
    private Integer tempo;
    private List<String> tags;
   
    /*coloquei essa variavel como inicializada como true 
    para poder vir por padrão*/
    private boolean habilitar = true;
    
    @ManyToOne
    private Disciplina disciplina;
    
    @OneToMany()
    private List<Alternativa> alternativas;
    
    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;
    
    //atributo que é usado mas não vai ser salvo no banco
    @Transient
    private JFXCheckBox checkbox;
    
    @Transient
    private ToggleButton togglebutton = new ToggleButton();
    
    public Pergunta (){
        this.tags = new ArrayList();
        this.alternativas = new ArrayList<Alternativa>();
        this.checkbox = new JFXCheckBox();
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

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public boolean isHabilitar() {
        return habilitar;
    }

    public void setHabilitar(boolean habilitar) {
        this.habilitar = habilitar;
    }
    
    public JFXCheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(JFXCheckBox checkbox) {
        this.checkbox = checkbox;
    }
    
    /*Aqui é onde faz as mudanças na aparencia do tooglebutto
    quando ele habilita ou desabilita e quando a pergunta 
    esta desabilitada o checkbox agora desabilita*/
    public ToggleButton getTogglebutton() {
        if(isHabilitar() == true){
            //this.togglebutton.setText("Desabilitar");
            this.togglebutton.setSelected(true);
            this.checkbox.setDisable(false);
        }else{
            //this.togglebutton.setText("Habilitar");
            this.togglebutton.setSelected(false);
            this.checkbox.setDisable(true);
        }
        return togglebutton;
    }

    public void setTogglebutton(ToggleButton togglebutton) {
        this.togglebutton = togglebutton;
    }
    
    public void addAlternativa(Alternativa alt){
        getAlternativas().add(alt);
    }
    
}
