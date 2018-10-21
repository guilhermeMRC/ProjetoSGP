/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
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
}
