package Repository;

import Model.Aposta;
import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ApostaRepository {

    // Salva uma aposta nova no banco (INSERT)
    public boolean salvarAposta(Aposta aposta) {
        // abre sessão com o banco
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin(); // começa a operação
            entityManager.persist(aposta);          // salva no banco
            entityManager.getTransaction().commit();// confirma
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // desfaz se deu erro
            System.out.println("Erro ao salvar aposta: " + e.getMessage());
            return false;
        } finally {
            entityManager.close(); // sempre fecha a sessão
        }
    }

    // Atualiza uma aposta existente no banco (UPDATE)
    public boolean atualizarAposta(Aposta aposta) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(aposta); // merge = atualiza no banco
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

    // Busca todas as apostas de um participante específico (SELECT com filtro)
    public List<Aposta> buscarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT a FROM Aposta a WHERE a.participante = :p",
                            Aposta.class) //Define que a consulta retornará objetos do tipo Aposta.
                    .setParameter("p", participante) //Substitui o parâmetro :p pelo objeto armazenado na variável participante.
                    .getResultList(); // executa consulta e retorna uma lista
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas: " + e.getMessage());
            return List.of(); // retorna lista vazia se der erro
        } finally {
            entityManager.close();
        }
    }

    // Busca todas as apostas de uma partida pelo id da partida
    public List<Aposta> buscarPartida(int partidaId) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT a FROM Aposta a " +
                                    "JOIN FETCH a.participante " + // tras aposta e participante junto
                                    "JOIN FETCH a.partida " + // tras partida também
                                    "WHERE a.partida.id = :id", Aposta.class) //Retorna apenas as apostas da partida informada.
                    .setParameter("id", partidaId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar apostas por partida: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    // Busca todas as apostas do banco sem filtro (SELECT *)
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