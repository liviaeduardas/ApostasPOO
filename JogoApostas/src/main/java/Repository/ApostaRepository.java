package Repository;

import Model.Aposta;
import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Responsável por salvar e buscar Apostas no banco de dados.
 */
public class ApostaRepository {

    public boolean salvarAposta(Aposta aposta) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(aposta);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erro ao salvar aposta: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean atualizarAposta(Aposta aposta) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(aposta);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erro ao atualizar aposta: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public List<Aposta> buscarPorParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                "SELECT a FROM Aposta a WHERE a.participante = :participante",
                Aposta.class
            ).setParameter("participante", participante).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    public List<Aposta> buscarTodasApostas() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT a FROM Aposta a", Aposta.class).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }
}
