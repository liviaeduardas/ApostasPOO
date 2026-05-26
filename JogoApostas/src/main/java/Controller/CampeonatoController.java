package Controller;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import java.util.ArrayList;

public class CampeonatoController {
    private ArrayList<Campeonato> campeonatos;
    private ArrayList<Clube> clubes;

    public CampeonatoController() {
        this.campeonatos = new ArrayList<>();
        this.clubes = new ArrayList<>();
    }

    public boolean criarCampeonato(String nome, int ano) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        Campeonato novo = new Campeonato(nome.trim(), ano);
        campeonatos.add(novo);
        return true;
    }

    public boolean cadastrarClube(String nome, String sigla) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (sigla == null || sigla.trim().isEmpty()) {
            return false;
        }
        Clube novo = new Clube(nome.trim(), sigla.trim());
        clubes.add(novo);
        return true;
    }

    public boolean adicionarClubeCampeonato(Campeonato campeonato, Clube clube) {
        if (campeonato == null || clube == null) {
            return false;
        }
        return campeonato.adicionarClube(clube);
    }

    public boolean adicionarPartidaCampeonato(Campeonato campeonato, Partida partida) {
        if (campeonato == null || partida == null) {
            return false;
        }
        return campeonato.adicionarPartida(partida);
    }

    public Campeonato buscarNome(String nome) {
        for (Campeonato c : campeonatos) {
            if (c.getNome().equalsIgnoreCase(nome.trim())) {
                return c;
            }
        }
        return null;
    }

    public Clube buscarClubeSigla(String sigla) {
        for (Clube c : clubes) {
            if (c.getSigla().equalsIgnoreCase(sigla.trim())) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Campeonato> getCampeonatos() {
        return campeonatos;
    }

    public ArrayList<Clube> getClubes() {
        return clubes;
    }
}
