/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import modelo.Alternativa;
import modelo.Pergunta;

/**
 *
 * @author gnune
 */
public class PerguntaDAO extends GenericDAO<Pergunta>{
    
    /*Lista todas as perguntas cadastradas*/
    public List<Pergunta> listar(){
        
        List<Pergunta> lista = null;
        conectar();
        
        try {
            
            lista = getManager().createQuery("from Pergunta p").getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }
    
    /*Listar perguntas ativas ou desativadas. O para isso precisa passar um 
    boolean como parametro para que se traga do banco as desativadas ou as ativadas
    se for true retorna a lista com as ativadas e se for false retorna a lista desativada*/
    public List<Pergunta> listarPerguntasAtivasOuDesativadas(boolean status){
        
        List<Pergunta> lista = null;
        String query = "from Pergunta p where p.habilitar ="+ status;
        conectar();
        
        try {
            
            lista = getManager().createQuery(query, Pergunta.class).getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }

    /*Método que recebe uma String como filtro e através disso
    vai ao banco e filtra por descrição ou  disciplina ou dificuldade*/
    public List<Pergunta> listarPerguntasPorDescricaoOuDificuldadeOuDisciplina(String filtro){
        
        List<Pergunta> lista = null;
        String query = "from Pergunta p where p.descricao like '%"+ filtro +"%' or "
                + "p.dificuldade like '%"+filtro+"%' or "
                + "p.disciplina.descricao like '%"+filtro+"%'"
                + "Order By p.descricao";
        conectar();
        
        try {
            
            lista = getManager().createQuery(query, Pergunta.class).getResultList();
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
