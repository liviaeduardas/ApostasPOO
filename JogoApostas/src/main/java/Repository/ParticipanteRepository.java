package Repository;

import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Responsável por salvar e buscar Participantes no banco de dados.
 * Toda operação com banco usa um EntityManager — é como uma sessão com o banco.
 */
public class ParticipanteRepository {

    /**
     * Salva um novo participante no banco.
     */
    public boolean salvarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(participante); // persist = INSERT no banco
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // se der erro, desfaz tudo
            System.out.println("Erro ao salvar participante: " + e.getMessage());
            return false;
        } finally {
            entityManager.close(); // sempre fecha o EntityManager no final
        }
    }

    /**
     * Retorna todos os participantes cadastrados.
     */
    public List<Participante> buscarTodosParticipante() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Participante p", Participante.class).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar participantes: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Atualiza os dados de um participante já existente no banco.
     */
    public boolean atualizarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.merge(participante); // merge = UPDATE no banco
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erro ao atualizar participante: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    // Busca participante pelo nome de usuário
    public Participante buscarPorUsuario(String usuario) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Participante> resultado = entityManager.createQuery(
                    "SELECT p FROM Participante p WHERE LOWER(p.usuario) = LOWER(:usuario)",
                    Participante.class
            ).setParameter("usuario", usuario.trim()).getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar participante: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    // Busca participante pelo usuário E senha — usado no login
    public Participante buscarPorUsuarioESenha(String usuario, String senha) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Participante> resultado = entityManager.createQuery(
                            "SELECT p FROM Participante p WHERE LOWER(p.usuario) = LOWER(:usuario) AND p.senha = :senha",
                            Participante.class
                    ).setParameter("usuario", usuario.trim())
                    .setParameter("senha", senha.trim())
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar participante: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }
}