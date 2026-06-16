package Controller;

import Model.Campeonato;
import Model.Clube;
import Model.Partida;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

import Model.Aposta;
import Model.PartidaRegular;
import Repository.ApostaRepository;
import Repository.CampeonatoRepository;
import Repository.ParticipanteRepository;

public class PartidaController {

    private CampeonatoRepository   campeonatoRepository;
    private ApostaRepository       apostaRepository;
    private ParticipanteRepository participanteRepository;

    public PartidaController() {
        campeonatoRepository   = new CampeonatoRepository();
        apostaRepository       = new ApostaRepository();
        participanteRepository = new ParticipanteRepository();
    }

    public boolean cadastrarPartida(Campeonato campeonato, Clube clubeCasa,
                                    Clube clubeVisitante, LocalDate dataPartida,
                                    LocalTime horaPartida) {
        if (campeonato == null || clubeCasa == null || clubeVisitante == null) return false;
        if (dataPartida == null || horaPartida == null) return false;
        if (clubeCasa.equals(clubeVisitante)) return false;

        PartidaRegular nova = new PartidaRegular(clubeCasa, clubeVisitante, dataPartida, horaPartida);
        nova.setCampeonato(campeonato);

        boolean adicionou = campeonato.addPartida(nova);
        if (adicionou)
            campeonatoRepository.atualizarCampeonato(campeonato);
        return adicionou;
    }

    /**
     * Registra o resultado e calcula pontos das apostas desta partida.
     * Busca apostas por partida diretamente — evita acessar lazy fora de sessão.
     */
    public boolean addResultado(Partida partida, int golsCasa, int golsVisitante) {
        if (partida == null)                   return false;
        if (partida.isPartidaFinalizada())     return false;
        if (golsCasa < 0 || golsVisitante < 0) return false;

        // 1. Registra o resultado e salva no banco
        partida.resultadoFinal(golsCasa, golsVisitante);
        campeonatoRepository.atualizarCampeonato(partida.getCampeonato());

        // 2. Busca apostas desta partida pelo ID — sem acessar lazy
        try {
            List<Aposta> apostas = apostaRepository.buscarPartida(partida.getId());
            for (Aposta a : apostas) {
                // Injeta os gols reais no palpite para calcular corretamente
                a.getPartida().resultadoFinal(golsCasa, golsVisitante);
                a.calcularResultadoAposta();
                apostaRepository.atualizarAposta(a);
                participanteRepository.atualizarParticipante(a.getParticipante());
            }
        } catch (Exception e) {
            System.out.println("Erro ao calcular pontos: " + e.getMessage());
        }

        return true;
    }

    public Partida procurarPartida(Campeonato campeonato, Clube clubeCasa, Clube clubeVisitante) {
        if (campeonato == null || clubeCasa == null || clubeVisitante == null) return null;
        for (Partida p : campeonato.getPartidas()) {
            if (p.getClubeCasa() == clubeCasa && p.getClubeVisitante() == clubeVisitante)
                return p;
        }
        return null;
    }

    public List<Partida> getPartidasPendentes(Campeonato campeonato) {
        if (campeonato == null) return List.of();
        List<Partida> pendentes = new ArrayList<>();
        for (Partida p : campeonato.getPartidas())
            if (!p.isPartidaFinalizada()) pendentes.add(p);
        return pendentes;
    }
}