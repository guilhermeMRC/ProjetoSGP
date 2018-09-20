/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author lefoly
 */
public class JpaUtil {

    private static EntityManagerFactory factory;

    public static void conectar() {
        factory = Persistence.createEntityManagerFactory("ProjetoSGPPU");
    }

    public static EntityManager getEntityManager() {
        EntityManager entityManager = null;
        entityManager = factory.createEntityManager();
        return entityManager;
    }

    public static void desconectar() {
        factory.close();
    }

}
