/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import modelo.Disciplina;

/**
 *
 * @author GuiGuizinho
 */
public class DisciplinaDAO extends GenericDAO<Disciplina>{
    
    public List<Disciplina> listar(){
        
        List<Disciplina> lista = null;
        conectar();
        
        try {
            lista = getManager().createQuery("from Disciplina d").getResultList();
            encerrar();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return lista;
        }
    }   
    
    /*public List<Disciplina> listarAtivos(){
        
        List<Disciplina> lista = null;
        List<Disciplina> listaAtiva = new ArrayList<Disciplina>();
        conectar();
        
        try {
            lista = getManager().createQuery("from Disciplina d").getResultList();
            encerrar();
            for (Disciplina disciplina : lista) {
                if(disciplina.isHabilitar()){
                    listaAtiva.add(disciplina);
                }
            }
            return listaAtiva;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return listaAtiva;
        }
    }*/
   
    public List<Disciplina> listarDisciplinasAtivasOuDesativadas(boolean status){
        
        List<Disciplina> lista = null;
        String query = "from Disciplina d where d.habilitar ="+ status;
        conectar();
        
        try {
            lista = getManager().createQuery(query, Disciplina.class).getResultList();
            encerrar();
            
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return lista;
        }
    }
    
    /*Método que pega um palavra ou uma letra 
    e passa como parametro para busca no banco 
    e retorna um lista com a pesquisa correspondente*/
    public List<Disciplina> listarDisciplinasPorDescricao(String filtro){
        
        List<Disciplina> lista = null;
        String query = "from Disciplina d where d.descricao like '%"+ filtro +"%'" 
                        + "Order By d.descricao";
        conectar();
        
        try {
            lista = getManager().createQuery(query, Disciplina.class).getResultList();
            encerrar();
            
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return lista;
        }
    }
}
