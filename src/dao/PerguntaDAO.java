/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import modelo.Alternativa;
import modelo.Disciplina;
import modelo.Pergunta;

/**
 *
 * @author gnune
 */
public class PerguntaDAO extends GenericDAO<Pergunta>{
    
    public List<Pergunta> listar(){
        
        List<Pergunta> lista = null;
        conectar();
        
        try {
            
            lista = getManager().createQuery("from Pergunta p where p.habilitar = 1").getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }
    
    public void incluirComAlternativas(Pergunta p) {
        try {
           
            for(Alternativa a: p.getAlternativas()){
                    getManager().getTransaction().begin();
                    getManager().persist(a);
                    getManager().getTransaction().commit();  
            }
            getManager().getTransaction().begin();
            getManager().persist(p);
            encerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarComAlternativas(Pergunta p) {
       
        try {
            
            for(Alternativa a: p.getAlternativas()){
                    getManager().getTransaction().begin();
                    getManager().merge(a);
                    getManager().getTransaction().commit();  
            }
            getManager().getTransaction().begin();
            getManager().merge(p);
            encerrar();
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
    
     public void atualizarPerguntas(List<Pergunta> p) {
       
        try {
            
            for(Pergunta pergunta: p){
                    getManager().getTransaction().begin();
                    getManager().merge(pergunta);
                    getManager().getTransaction().commit();  
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
     
}
