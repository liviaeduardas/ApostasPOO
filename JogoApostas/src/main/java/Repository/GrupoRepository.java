package Repository;

import Model.Grupo;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class GrupoRepository {

    public boolean salvarGrupo(Grupo grupo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(grupo);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar grupo: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean atualizarGrupo(Grupo grupo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(grupo);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar grupo: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public Grupo buscarPorGrupo(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Grupo> lista = em.createQuery(
                            "SELECT DISTINCT g FROM Grupo g " +
                                    "LEFT JOIN FETCH g.participantes " +
                                    "WHERE LOWER(g.nome) = LOWER(:nome)", Grupo.class)
                    .setParameter("nome", nome.trim())
                    .getResultList();

            if (lista.isEmpty()) {
                return null;
            }
            return lista.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar grupo: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public List<Grupo> buscarTodosGrupos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT g FROM Grupo g LEFT JOIN FETCH g.participantes", Grupo.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar grupos: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }
}
