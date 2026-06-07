package Controller;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import Model.PartidaRegular;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PartidaController {

    public boolean cadastrarPartida(Campeonato campeonato, Clube ClubeCasa, Clube ClubeVisitante,
                                    LocalDate DataPartida, LocalTime HoraPartida) {
        if (campeonato == null || ClubeCasa == null || ClubeVisitante == null) return false;
        if (DataPartida == null || HoraPartida == null) return false;
        if (ClubeCasa.equals(ClubeVisitante)) return false;

        PartidaRegular nova = new PartidaRegular(ClubeCasa, ClubeVisitante, DataPartida, HoraPartida);
        return campeonato.addPartida(nova);
    }

    public boolean addResultado(Partida partida, int GolsCasa, int GolsVisitante) {
        if (partida == null) return false;
        if (partida.isPartidaFinalizada()) return false;
        if (GolsCasa < 0 || GolsVisitante < 0) return false;
        partida.resultadoFinal(GolsCasa, GolsVisitante);
        return true;
    }

    // Filtra partidas ainda não finalizadas
    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null) return new ArrayList<>();
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) pendentes.add(p);
        }
        return pendentes;
    }

    // Filtra partidas já finalizadas
    public List<Partida> getPartidasFinalizadas(Campeonato campeonato) {
        if (campeonato == null) return new ArrayList<>();
        List<Partida> finalizadas = new ArrayList<>();
        for (Partida p : campeonato.getPartidas()) {
            if (p.isPartidaFinalizada()) finalizadas.add(p);
        }
        return finalizadas;
    }

    public Partida procurarPartida(Campeonato campeonato, Clube ClubeCasa, Clube ClubeVisitante) {
        if (campeonato == null || ClubeCasa == null || ClubeVisitante == null) return null;
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeCasa() == ClubeCasa && p.getClubeVisitante() == ClubeVisitante) return p;
        }
        return null;
    }
}