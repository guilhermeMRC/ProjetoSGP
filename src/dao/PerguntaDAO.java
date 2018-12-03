/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import modelo.Alternativa;
import modelo.Pergunta;
import modelo.Sala;

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
     
     
     public List<Pergunta> retornarPerguntasForaDaSala(Long idSala){
         getManager().getTransaction().begin();
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<Pergunta> cq = cb.createQuery(Pergunta.class);
            Root<Pergunta> rootP = cq.from(Pergunta.class);
            
            Join<Pergunta, Sala> join = rootP.join("sala");
            Path<Long> salaIdAtual = join.get("id");
            
            cq.where(cb.isFalse(salaIdAtual.in(idSala)));
            cq.distinct(true);
            List<Pergunta> perguntas = getManager().createQuery(cq).getResultList();
            getManager().getTransaction().commit();
            return perguntas;
            
     }
}
