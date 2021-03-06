/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import modelo.Sala;

/**
 *
 * @author gnune
 */
public class SalaDAO extends GenericDAO<Sala> {

    public List<Sala> listar() {

        List<Sala> lista = null;
        conectar();

        try {
            lista = getManager().createQuery("from Sala s").getResultList();
            encerrar();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return lista;
        }
    }

    public Sala listarPorId(Long id) {

        Sala sala = null;
        conectar();

        try {
            sala = (Sala) getManager().createQuery("from Sala s where s.id = " + id).getSingleResult();
            encerrar();
            return sala;
        } catch (Exception e) {
            e.printStackTrace();
            encerrar();
            return sala;
        }
    }
    
    public List<Sala> listarSalasAtivasOuDesativadas(boolean status){
        
        List<Sala> lista = null;
        String query = "from Sala s where s.habilitar ="+ status;
        conectar();
        
        try {
            
            lista = getManager().createQuery(query, Sala.class).getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }
    
    public List<Sala> listarSalasPorDescricao(String filtro){
        
        List<Sala> lista = null;
        String query = "from Sala s where s.descricao like '%"+ filtro +"%'" 
                        + "Order By s.descricao";
        conectar();
        
        try {
            
            lista = getManager().createQuery(query, Sala.class).getResultList();
            encerrar();
            return lista;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            encerrar();
            return lista;
            
        }
    }
}
