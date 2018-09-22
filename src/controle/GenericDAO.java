/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

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
    //AINDA NÃO TESTEI
    public void atualizar(T c){
        try {
            conectar();
            manager.refresh(c);
            encerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        public void incluir(T c){
        try {
            conectar();
            manager.persist(c);
            encerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        public void excluir(T c){
        try {
            conectar();
            manager.remove(c);
            encerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
