package Repository;

import Model.Campeonato;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;


public class CampeonatoRepository {

    public boolean salvarCampeonato(Campeonato campeonato) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(campeonato);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar campeonato: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean atualizarCampeonato(Campeonato campeonato) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(campeonato);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar campeonato: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }


    public Campeonato buscarPorCampeonato(String nome) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            List<Campeonato> lista = entityManager.createQuery(
                            "SELECT DISTINCT c FROM Campeonato c " +
                                    "LEFT JOIN FETCH c.clubes " +
                                    "LEFT JOIN FETCH c.partidas " +
                                    "WHERE LOWER(c.nome) = LOWER(:n)", Campeonato.class)
                    .setParameter("n", nome.trim())
                    .getResultList();

            if (lista.isEmpty()) {
                return null;
            } else {
                return lista.get(0);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar campeonato: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Campeonato> buscarTodosCampeonatos() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT c FROM Campeonato c", Campeonato.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar campeonatos: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }
}