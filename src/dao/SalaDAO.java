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
public class SalaDAO extends GenericDAO<Sala>{
    public List<Sala> listar(){
        
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
}
