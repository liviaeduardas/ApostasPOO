package Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {


    private static final String PERSISTENCE_UNIT = "JogoApostasPU";
    private static EntityManagerFactory factory;

    private static EntityManagerFactory getFactory() {
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static void fechar() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}