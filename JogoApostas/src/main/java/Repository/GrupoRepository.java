package Repository;

import Model.Grupo;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Responsável por salvar e buscar Grupos no banco de dados.
 */
public class GrupoRepository {

    public boolean salvarGrupo(Grupo grupo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(grupo);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
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
            entityManager.getTransaction().rollback();
            System.out.println("Erro ao atualizar grupo: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public Grupo buscarPorGrupo(String nome) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Grupo> resultado = entityManager.createQuery(
                "SELECT g FROM Grupo g WHERE LOWER(g.nome) = LOWER(:nome)",
                Grupo.class
            ).setParameter("nome", nome.trim()).getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar grupo: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Grupo> buscarTodosGrupos() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT g FROM Grupo g", Grupo.class).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar grupos: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }
}
