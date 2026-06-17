package Repository;

import Model.Aposta;
import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ApostaRepository {


    public boolean salvarAposta(Aposta aposta) {

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(aposta);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
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
            System.out.println("Erro ao atualizar aposta: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }


    public List<Aposta> buscarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT a FROM Aposta a WHERE a.participante = :p", Aposta.class)
                    .setParameter("p", participante)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }


    public List<Aposta> buscarPartida(int partidaId) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT a FROM Aposta a " +
                                    "JOIN FETCH a.participante " +
                                    "JOIN FETCH a.partida " +
                                    "WHERE a.partida.id = :id", Aposta.class)
                    .setParameter("id", partidaId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas por partida: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }


    public List<Aposta> buscarTodasApostas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Aposta a", Aposta.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }
}