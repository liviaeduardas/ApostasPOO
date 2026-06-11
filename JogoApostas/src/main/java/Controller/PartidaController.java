package Controller;

import Model.Campeonato;
import Model.Clube;
import Model.Partida;

import java.time.LocalDate;
import java.time.LocalTime;

import Model.PartidaRegular;
import Repository.CampeonatoRepository;

/**
 * Gerencia partidas.
 * Atualiza o campeonato no banco após cada operação.
 */
public class PartidaController {

    private CampeonatoRepository campeonatoRepository;

    public PartidaController() {
        campeonatoRepository = new CampeonatoRepository();
    }

    // Cria e cadastra uma nova partida no campeonato
    public boolean cadastrarPartida(Campeonato campeonato, Clube clubeCasa,
                                    Clube clubeVisitante, LocalDate DataPartida, LocalTime HoraPartida) {
        if (campeonato == null || clubeCasa == null || clubeVisitante == null)
            return false;
        if (DataPartida == null || HoraPartida == null)
            return false;
        if (clubeCasa.equals(clubeVisitante))
            return false;



        PartidaRegular nova = new PartidaRegular(clubeCasa, clubeVisitante, DataPartida, HoraPartida);
        nova.setCampeonato(campeonato); // liga a partida ao campeonato

        boolean adicionou = campeonato.addPartida(nova); // regra no Model
        if (adicionou)
            campeonatoRepository.atualizarCampeonato(campeonato); // salva no banco
        return adicionou;
    }

    // Registra o resultado de uma partida e atualiza no banco
    public boolean addResultado(Partida partida, int GolsCasa, int GolsVisitante) {
        if (partida == null)
            return false;
        if (partida.isPartidaFinalizada())
            return false;
        if (GolsCasa < 0 || GolsVisitante < 0)
            return false;

        partida.resultadoFinal(GolsCasa, GolsVisitante); // regra no Model
        campeonatoRepository.atualizarCampeonato(partida.getCampeonato()); // salva no banco
        return true;
    }

    // Busca uma partida pelos dois clubes
    public Partida procurarPartida(Campeonato campeonato, Clube ClubeCasa, Clube ClubeVisitante) {
        if (campeonato == null || ClubeCasa == null || ClubeVisitante == null)
            return null;
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeCasa() == ClubeCasa && p.getClubeVisitante() == ClubeVisitante)
                return p;
        }
        return null;
    }
}