package Repository;

import Model.Participante;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;


public class ParticipanteRepository {

    public boolean salvarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(participante);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar participante: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }


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

    public boolean atualizarParticipante(Participante participante) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(participante);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar participante: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }


    public Participante buscarPorUsuario(String usuario) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Participante> resultado = entityManager.createQuery(
                    "SELECT p FROM Participante p WHERE LOWER(p.usuario) = LOWER(:usuario)",
                    Participante.class
            ).setParameter("usuario", usuario.trim()).getResultList();

            if (resultado.isEmpty()) {
                return null;
            }
            return resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar participante: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }


    public Participante buscarPorUsuarioESenha(String usuario, String senha) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Participante> resultado = entityManager.createQuery(
                            "SELECT p FROM Participante p WHERE LOWER(p.usuario) = LOWER(:usuario) AND p.senha = :senha",
                            Participante.class
                    ).setParameter("usuario", usuario.trim())
                    .setParameter("senha", senha.trim())
                    .getResultList();

            if (resultado.isEmpty()) {
                return null;
            }
            return resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar participante: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }
}