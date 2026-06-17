package Repository;

import Model.Clube;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClubeRepository {

    public boolean salvarClube(Clube clube) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(clube);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar clube: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public Clube buscarPorSigla(String sigla) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Clube> resultado = entityManager.createQuery(
                "SELECT c FROM Clube c WHERE LOWER(c.sigla) = LOWER(:sigla)",
                Clube.class
            ).setParameter("sigla", sigla.trim()).getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar clube: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Clube> buscarTodosClubes() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT c FROM Clube c", Clube.class).getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar clubes: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    public Clube buscarPorNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Clube> lista = em.createQuery(
                            "SELECT c FROM Clube c WHERE LOWER(c.nome) = LOWER(:n)", Clube.class)
                    .setParameter("n", nome.trim())
                    .getResultList();

            if (lista.isEmpty()) {
                return null;
            }
            return lista.get(0);
        } catch (Exception e) {
            System.out.println("Erro ao buscar clube: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }
}
