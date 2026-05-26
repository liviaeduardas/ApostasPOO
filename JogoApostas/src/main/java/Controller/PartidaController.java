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

    public boolean cadastrarPartida(Campeonato campeonato, Clube clubeMandante, Clube clubeVisitante, LocalDate data, LocalTime hora) {
        if (campeonato == null || clubeMandante == null || clubeVisitante == null) {
            return false;
        }
        if (clubeMandante.equals(clubeVisitante)) {
            return false;
        }
        if (data == null || hora == null) {
            return false;
        }

        PartidaRegular nova = new PartidaRegular(clubeMandante, clubeVisitante, data, hora);
        return campeonato.adicionarPartida(nova);
    }

    public boolean registrarResultado(Partida partida, int golMandante, int golVisitante) {
        if (partida == null) {
            return false;
        }
        if (partida.isEncerrada()) {
            return false;
        }
        if (golMandante < 0 || golVisitante < 0) {
            return false;
        }

        partida.registrarResultado(golMandante, golVisitante);
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

    public Partida buscarPartida(Campeonato campeonato, Clube clubeMandante, Clube clubeVisitante) {
        if (campeonato == null || clubeMandante == null || clubeVisitante == null) {
            return null;
        }
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeMandante().equals(clubeMandante) && p.getClubeVisitante().equals(clubeVisitante)) {
                return p;
            }
        }
        return null;
    }
}
