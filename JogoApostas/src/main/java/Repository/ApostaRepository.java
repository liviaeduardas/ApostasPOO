package Repository;

import Model.Aposta;
import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ApostaRepository {

    public boolean salvarAposta(Aposta aposta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aposta);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao salvar aposta: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public boolean atualizarAposta(Aposta aposta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(aposta);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao atualizar aposta: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public List<Aposta> buscarPorParticipante(Participante participante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Aposta a WHERE a.participante = :p", Aposta.class)
                    .setParameter("p", participante)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }

    /**
     * Busca apostas pelo ID da partida — usado para calcular pontos após resultado.
     * Carrega participante e partida com JOIN FETCH para evitar LazyInitializationException.
     */
    public List<Aposta> buscarPorPartida(int partidaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
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
            em.close();
        }
    }

    public List<Aposta> buscarTodasApostas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Aposta a", Aposta.class).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }
}