package Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária que gerencia a conexão com o banco de dados.
 * Usa o padrão Singleton — só existe uma instância do EntityManagerFactory
 * durante toda a execução do programa.
 * JPAUtil.getEntityManager()
 *          ↓
 *    abre uma sessão com o banco
 *          ↓
 *    você salva, busca, deleta dados
 *          ↓
 *    fecha o EntityManager
 */
public class JPAUtil {

    // Nome deve ser igual ao que está no persistence.xml
    private static final String PERSISTENCE_UNIT = "JogoApostasPU";

    // EntityManagerFactory é pesado — criamos só uma vez
    private static EntityManagerFactory factory;

    /**
     * Retorna o EntityManagerFactory — cria se ainda não existir.
     */
    private static EntityManagerFactory getFactory() {
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return factory;
    }

    /**
     * Abre e retorna um EntityManager — é como uma "sessão" com o banco.
     * Cada operação (salvar, buscar, deletar) usa um EntityManager.
     */
    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    /**
     * Fecha a conexão com o banco — deve ser chamado ao encerrar o programa.
     */
    public static void fechar() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}