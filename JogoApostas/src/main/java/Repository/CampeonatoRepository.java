package Repository;

import Model.Campeonato;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Responsável por salvar e buscar Campeonatos no banco de dados.
 */
public class CampeonatoRepository {

    public boolean salvarCampeonato(Campeonato campeonato) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(campeonato);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
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
            entityManager.getTransaction().rollback();
            System.out.println("Erro ao atualizar campeonato: " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Busca campeonato pelo nome carregando clubes e partidas em queries separadas
     * para evitar MultipleBagFetchException (Hibernate não permite JOIN FETCH
     * em duas listas simultaneamente na mesma query).
     */
    public Campeonato buscarPorCampeonato(String nome) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            // 1ª query — carrega o campeonato com os clubes
            List<Campeonato> lista = entityManager.createQuery(
                            "SELECT DISTINCT c FROM Campeonato c " +
                                    "LEFT JOIN FETCH c.clubes " +
                                    "WHERE LOWER(c.nome) = LOWER(:n)", Campeonato.class)
                    .setParameter("n", nome.trim())
                    .getResultList();

            if (lista.isEmpty()) return null;

            Campeonato campeonato = lista.get(0);

            // 2ª query — carrega as partidas do mesmo campeonato separadamente
            entityManager.createQuery(
                            "SELECT DISTINCT c FROM Campeonato c " +
                                    "LEFT JOIN FETCH c.partidas " +
                                    "WHERE c.id = :id", Campeonato.class)
                    .setParameter("id", campeonato.getId())
                    .getResultList();

            return campeonato;
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