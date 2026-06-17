package Controller;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import Model.Aposta;
import Repository.ApostaRepository;
import Repository.CampeonatoRepository;
import Repository.ParticipanteRepository;

public class PartidaController {
    private CampeonatoRepository campeonatoRepository;
    private ApostaRepository apostaRepository;
    private ParticipanteRepository participanteRepository;

    public PartidaController() {
        campeonatoRepository = new CampeonatoRepository();
        apostaRepository = new ApostaRepository();
        participanteRepository = new ParticipanteRepository();
    }

    public boolean cadastrarPartida(Campeonato campeonato, Clube clubeCasa, Clube clubeVisitante, LocalDate dataPartida, LocalTime horaPartida) {
        if (campeonato == null || clubeCasa == null || clubeVisitante == null) {
            return false;
        }
        if (dataPartida == null || horaPartida == null){
            return false;
        }
        if (clubeCasa.equals(clubeVisitante)){
            return false;
        }

        Partida nova = new Partida(clubeCasa, clubeVisitante, dataPartida, horaPartida);
        nova.setCampeonato(campeonato);

        boolean adicionou = campeonato.addPartida(nova);
        if (adicionou) {
            campeonatoRepository.atualizarCampeonato(campeonato);
        }
        return adicionou;
    }

    public boolean addResultado(Partida partida, int GolsCasa, int GolsVisitante) {
        if (partida == null){
            return false;
        }
        if (partida.isPartidaFinalizada()){
            return false;
        }
        if (GolsCasa < 0 || GolsVisitante < 0){
            return false;
        }

        partida.resultadoFinal(GolsCasa, GolsVisitante);
        campeonatoRepository.atualizarCampeonato(partida.getCampeonato());

        try {
            List<Aposta> apostas = apostaRepository.buscarPartida(partida.getId());
            for (Aposta a : apostas) {
                a.calcularResultadoAposta();
                apostaRepository.atualizarAposta(a);
                participanteRepository.atualizarParticipante(a.getParticipante());
            }
        } catch (Exception e) {
            System.out.println("Erro ao calcular pontos das apostas: " + e.getMessage());
            return false;
        }
        return true;
    }

    public Partida procurarPartida(Campeonato campeonato, Clube clubeCasa, Clube clubeVisitante) {
        if (campeonato == null || clubeCasa == null || clubeVisitante == null){
            return null;
        }
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeCasa() == clubeCasa && p.getClubeVisitante() == clubeVisitante)
                return p;
        }
        return null;
    }

    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null){
            return new ArrayList<>();
        }
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : campeonato.getPartidas())
            if (!p.isPartidaFinalizada()){
                pendentes.add(p);
            }
        return pendentes;
    }
}