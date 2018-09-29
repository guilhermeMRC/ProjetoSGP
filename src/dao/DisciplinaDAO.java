/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.GenericDAO;
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
    
    public List<Disciplina> listarPorDisciplina(String descricao){
        
        List<Disciplina> lista = null;
        conectar();
        
        try {
            lista = getManager().createQuery("from Disciplina d where d.descricao like '%" + descricao + "%'").getResultList();
            encerrar();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return lista;
        }
    }
    //VERIFICAR
    public Disciplina ListarDisciplinaPorID(long id){
        Disciplina disciplina = new Disciplina();
        conectar();
        
        try {
            disciplina =  (Disciplina) getManager().createQuery("from Disciplina d where d.id = " + id).getResultList().get(0);
            encerrar();
            return disciplina;
        } catch (Exception e) {
            System.out.println("\n\n\n ERRO NO BUSCA DISCIPLINA\n"+e);
            encerrar();
            return disciplina;
        }
    }
            
}
