package Controller;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import java.util.ArrayList;
import java.util.List;

public class CampeonatoController {
    private ArrayList<Campeonato> campeonatos;
    private ArrayList<Clube> clubes;

    public CampeonatoController() {
        this.campeonatos = new ArrayList<>();
        this.clubes = new ArrayList<>();
    }

    public boolean novoCampeonato(String nome, int ano) {
        if (nome == null || nome.trim().isEmpty()) return false;
        Campeonato novo = new Campeonato(nome.trim(), ano);
        campeonatos.add(novo);
        return true;
    }

    public boolean cadastrarClube(String nome, String sigla) {
        if (nome == null || nome.trim().isEmpty()) return false;
        if (sigla == null || sigla.trim().isEmpty()) return false;
        Clube novo = new Clube(nome.trim(), sigla.trim());
        clubes.add(novo);
        return true;
    }

    // Delega a validação (limite, duplicata) para o próprio Campeonato
    public boolean addClube(Campeonato campeonato, Clube clube) {
        if (campeonato == null || clube == null) return false;
        return campeonato.addCLube(clube);
    }

    // Delega a validação (clube pertence ao campeonato) para o próprio Campeonato
    public boolean addPartida(Campeonato campeonato, Partida partida) {
        if (campeonato == null || partida == null) return false;
        return campeonato.addPartida(partida);
    }

    // Retorna lista de partidas ainda não finalizadas
    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null) return new ArrayList<>();
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) pendentes.add(p);
        }
        return pendentes;
    }

    // Retorna lista de partidas já finalizadas
    public List<Partida> getPartidasFinalizadas(Campeonato campeonato) {
        if (campeonato == null) return new ArrayList<>();
        List<Partida> finalizadas = new ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (p.isPartidaFinalizada()) finalizadas.add(p);
        }
        return finalizadas;
    }

    public Campeonato procurarCampeonato(String nome) {
        if (nome == null) return null;
        for (Campeonato c : campeonatos) {
            if (c.getNome().equalsIgnoreCase(nome.trim())) return c;
        }
        return null;
    }

    public Clube procurarClube(String sigla) {
        if (sigla == null) return null;
        for (Clube c : clubes) {
            if (c.getSigla().equalsIgnoreCase(sigla.trim())) return c;
        }
        return null;
    }

    public ArrayList<Campeonato> getCampeonatos() { return campeonatos; }
    public ArrayList<Clube> getClubes(){ return clubes; }
}