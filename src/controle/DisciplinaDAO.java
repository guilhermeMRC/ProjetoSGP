/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.List;
import modelo.Disciplina;

/**
 *
 * @author GuiGuizinho
 */
public class DisciplinaDAO extends GenericDAO<Disciplina>{
    
    public List<Disciplina> listar() throws Exception {
        
        List<Disciplina> lista = null;
        conectar();
        
        try {
            
            lista = getManager().createQuery("from Disciplina d").getResultList();
            
        } finally {
            
            getManager().close();
            
        }
        return lista;
    }
    
    public List<Disciplina> listarPorDisciplina(String descricao) throws Exception {
        
        List<Disciplina> lista = null;
        conectar();
        
        try {
            
            lista = getManager().createQuery("from Disciplina d where d.descricao like '%" + descricao + "%'").getResultList();
            
        } finally {
            
            getManager().close();
            
        }
        return lista;
    }
            
}