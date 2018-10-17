/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
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
            
            lista = getManager().createQuery("from Pergunta p").getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }

}
