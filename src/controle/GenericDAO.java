/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author GuiGuizinho
 */
public abstract class GenericDAO<T> {
    
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("ProjetoSGPPU");
    private final EntityManager manager = factory.createEntityManager();
    
    public void incluir(T c) throws Exception {
        
        try {
            
            conectar();
            manager.persist(c);
            encerrar();
            
        } catch (Exception e) {
            
            e.printStackTrace();
           
        }
        
    }
    
    public EntityManager getManager(){
        
        return manager;
        
    }
    
    protected void conectar(){
        
        manager.getTransaction().begin();
        
    }
    
    protected void encerrar(){
        
        manager.getTransaction().commit();
        manager.close();
        
    }
    
    public void editar(T c){
        try {
            conectar();
            manager.merge(c);
            encerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
