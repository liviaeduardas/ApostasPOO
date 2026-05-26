package Model;
import java.util.ArrayList;
import java.util.List;

public class Campeonato {
    private static final int MAX_CLUBES = 8;
    private String nome;
    private int ano;
    private List<Clube> clubes;
    private List<Partida> partidas;

    public Campeonato(){
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public Campeonato(String nome, int ano) {
        this.nome = nome;
        this.ano = ano;
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public boolean adicionarClube(Clube clube) {
        if (clubes.size() >= MAX_CLUBES) {
            System.out.println("Limite máximo de 8 clubes atingido");
            return false;
        }
        if (clubes.contains(clube)) {
            System.out.println("Clube já cadastrado no campeonato.");
            return false;
        }
        clubes.add(clube);
        return true;
    }

    public boolean adicionarPartida(Partida partida) {
        if (!clubes.contains(partida.getClubeMandante()) || !clubes.contains(partida.getClubeVisitante())) {
            System.out.println("Clube não pertence ao campeonato");
            return false;
        }
        partidas.add(partida);
        return true;
    }

    public List<Partida> getPartidasPendentes() {
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : partidas) {
            if (!p.isEncerrada()) pendentes.add(p);
        }
        return pendentes;
    }

    public List<Partida> getPartidasFinalizadas() {
        List<Partida> finalizadas = new ArrayList<>();
        for (Partida p : partidas) {
            if (p.isEncerrada()) finalizadas.add(p);
        }
        return finalizadas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public List<Clube> getClubes() {
        return clubes;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public int getMaxClubes() {
        return MAX_CLUBES;
    }

}
