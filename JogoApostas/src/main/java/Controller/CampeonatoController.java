package Controller;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import java.util.List;
import java.util.ArrayList;
import Repository.CampeonatoRepository;
import Repository.ClubeRepository;

public class CampeonatoController {
    private CampeonatoRepository campeonatoRepository;
    private ClubeRepository clubeRepository;

    public CampeonatoController() {
        campeonatoRepository = new CampeonatoRepository();
        clubeRepository = new ClubeRepository();
    }

    public boolean novoCampeonato(String nome, int ano) {
        if (nome == null || nome.trim().isEmpty()){
            return false;
        }
        Campeonato novo = new Campeonato(nome.trim(), ano);
        return campeonatoRepository.salvarCampeonato(novo);
    }

    public boolean cadastrarClube(String nome, String sigla) {
        if (nome == null || nome.trim().isEmpty()){
            return false;
        }
        if (sigla == null || sigla.trim().isEmpty()){
            return false;
        }
        Clube novo = new Clube(nome.trim(), sigla.trim());
        return clubeRepository.salvarClube(novo);
    }

    public boolean addClube(Campeonato campeonato, Clube clube) {
        if (campeonato == null || clube == null){
            return false;
        }
        boolean adicionou = campeonato.addCLube(clube);
        if (adicionou){
            campeonatoRepository.atualizarCampeonato(campeonato);
        }
        return adicionou;
    }

    public boolean addPartida(Campeonato campeonato, Partida partida){
        if (campeonato == null || partida == null) {
            return false;
        }
        boolean adicionou = campeonato.addPartida(partida);
        if (adicionou) {
            campeonatoRepository.atualizarCampeonato(campeonato);
        }
        return adicionou;
    }

    public List<Partida> getPartidasPendentes(Campeonato campeonato){
        if (campeonato == null) {
            return new ArrayList<>();
        }
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) pendentes.add(p);
        }
        return pendentes;
    }

    public List<Partida> getPartidasFinalizadas(Campeonato campeonato) {
        if (campeonato == null) {
            return new ArrayList<>();
        }
        List<Partida> finalizadas = new java.util.ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (p.isPartidaFinalizada()) finalizadas.add(p);
        }
        return finalizadas;
    }

    public Campeonato procurarCampeonato(String nome) {
        if (nome == null) {
            return null;
        }
        return campeonatoRepository.buscarPorCampeonato(nome);
    }

    public Clube procurarClube(String sigla) {
        if (sigla == null){
            return null;
        }
        return clubeRepository.buscarPorSigla(sigla);
    }

    public List<Campeonato> getCampeonatos(){
        return campeonatoRepository.buscarTodosCampeonatos();
    }

    public List<Clube> getClubes() {
        return clubeRepository.buscarTodosClubes();
    }
}