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
    public boolean cadastrarPartida(Campeonato campeonato, Clube ClubeCasa, Clube ClubeVisitante, LocalDate DataPartida, LocalTime HoraPartida){
        if (campeonato == null || ClubeCasa == null || ClubeVisitante == null) {
            return false;
        }
        if (ClubeCasa.equals(ClubeVisitante)) {
            return false;
        }
        if (DataPartida == null || HoraPartida == null) {
            return false;
        }
        PartidaRegular nova = new PartidaRegular(ClubeCasa, ClubeVisitante, DataPartida, HoraPartida);
        return campeonato.AddPartida(nova);
    }

    public boolean AddResultado(Partida partida, int GolsCasa, int GolsVisitante) {
        if (partida == null) {
            return false;
        }
        if (partida.isPartidaFinalizada()) {
            return false;
        }
        if (GolsCasa < 0 || GolsVisitante < 0) {
            return false;
        }
        partida.ResultadoFinal(GolsCasa, GolsVisitante);
        return true;
    }

    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null) {
            return new ArrayList<>();
        }
        return campeonato.getPartidasPendentes();
    }

    public List<Partida> getPartidasFinalizadas(Campeonato campeonato) {
        if (campeonato == null) {
            return new ArrayList<>();
        }
        return campeonato.getPartidasFinalizadas();
    }

    public Partida ProcurarPartida(Campeonato campeonato, Clube ClubeCasa, Clube ClubeVisitante) {
        if (campeonato == null || ClubeCasa == null || ClubeVisitante == null) {
            return null;
        }
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeCasa() == ClubeCasa && p.getClubeVisitante() == ClubeVisitante){
                return p;
            }
        }
        return null;
    }
}
