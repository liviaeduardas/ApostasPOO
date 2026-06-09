package Controller;

import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import java.util.List;

import Repository.CampeonatoRepository;
import Repository.ClubeRepository;

/**
 * Gerencia campeonatos e clubes.
 * Agora usa os Repositories para salvar e buscar no banco.
 */
public class CampeonatoController {

    private CampeonatoRepository campeonatoRepository;
    private ClubeRepository clubeRepository;

    public CampeonatoController() {
        campeonatoRepository = new CampeonatoRepository();
        clubeRepository = new ClubeRepository();
    }

    // Cria e salva um novo campeonato no banco
    public boolean novoCampeonato(String nome, int ano) {
        if (nome == null || nome.trim().isEmpty()) return false;
        Campeonato novo = new Campeonato(nome.trim(), ano);
        return campeonatoRepository.salvarCampeonato(novo);
    }

    // Cria e salva um novo clube no banco
    public boolean cadastrarClube(String nome, String sigla) {
        if (nome == null || nome.trim().isEmpty()) return false;
        if (sigla == null || sigla.trim().isEmpty()) return false;
        Clube novo = new Clube(nome.trim(), sigla.trim());
        return clubeRepository.salvarClube(novo);
    }

    // Adiciona clube ao campeonato e atualiza no banco
    public boolean addClube(Campeonato campeonato, Clube clube) {
        if (campeonato == null || clube == null) return false;
        boolean adicionou = campeonato.addCLube(clube); // regra no Model
        if (adicionou) campeonatoRepository.atualizarCampeonato(campeonato); // salva no banco
        return adicionou;
    }

    // Adiciona partida ao campeonato e atualiza no banco
    public boolean addPartida(Campeonato campeonato, Partida partida) {
        if (campeonato == null || partida == null)
            return false;
        boolean adicionou = campeonato.addPartida(partida); // regra no Model
        if (adicionou)
            campeonatoRepository.atualizarCampeonato(campeonato); // salva no banco
        return adicionou;
    }

    // Retorna partidas ainda não finalizadas
    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null)
            return List.of();
        List<Partida> pendentes = new java.util.ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) pendentes.add(p);
        }
        return pendentes;
    }

    // Retorna partidas já finalizadas
    public List<Partida> getPartidasFinalizadas(Campeonato campeonato) {
        if (campeonato == null) return List.of();
        List<Partida> finalizadas = new java.util.ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (p.isPartidaFinalizada()) finalizadas.add(p);
        }
        return finalizadas;
    }

    // Busca campeonato pelo nome no banco
    public Campeonato procurarCampeonato(String nome) {
        if (nome == null) return null;
        return campeonatoRepository.buscarPorCampeonato(nome);
    }

    // Busca clube pela sigla no banco
    public Clube procurarClube(String sigla) {
        if (sigla == null) return null;
        return clubeRepository.buscarPorSigla(sigla);
    }

    // Retorna todos os campeonatos do banco
    public List<Campeonato> getCampeonatos() {
        return campeonatoRepository.buscarTodosCampeonatos();
    }

    // Retorna todos os clubes do banco
    public List<Clube> getClubes() {
        return clubeRepository.buscarTodosClubes();
    }
}