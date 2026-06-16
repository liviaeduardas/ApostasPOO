package Controller;
import Model.Aposta;
import Model.Campeonato;
import Model.Participante;
import Model.Partida;

import java.util.List;

import Repository.ApostaRepository;
import Repository.ParticipanteRepository;

/**
 * Gerencia apostas.
 * Salva apostas e atualiza pontuações no banco.
 */
public class ApostaController {

    private ApostaRepository apostaRepository;
    private ParticipanteRepository participanteRepository;

    public ApostaController() {
        apostaRepository = new ApostaRepository();
        participanteRepository = new ParticipanteRepository();
    }

    // Registra uma nova aposta após validações
    public boolean FazerAposta(Participante participante, Partida partida,
                               int GolsCasa, int GolsVisitante) {
        if (participante == null || partida == null)
            return false;
        if (GolsCasa < 0 || GolsVisitante < 0)
            return false;
        if (partida.isPartidaFinalizada())
            return false;

        Aposta aposta = new Aposta(0, participante, partida, GolsCasa, GolsVisitante);

        // Regra de prazo — vive no Model
        if (!aposta.possivelApostar())
            return false;

        // Verifica duplicata — mesmo participante na mesma partida
        for (Aposta a : apostaRepository.buscarParticipante(participante)) {
            if (a.getPartida() == partida)
                return false;
        }

        participante.fazerAposta(aposta);

        // Salva a aposta no banco
        return apostaRepository.salvarAposta(aposta);
    }

    // Calcula pontos e atualiza participantes no banco
    public void CalcularPontos(Campeonato campeonato) {
        for (Aposta aposta : apostaRepository.buscarTodasApostas()) {
            if (campeonato.getPartidas().contains(aposta.getPartida())
                    && aposta.getPartida().isPartidaFinalizada()) {
                aposta.calcularResultadoAposta(); // regra no Model
                apostaRepository.atualizarAposta(aposta);     // atualiza pontos da aposta no banco
                participanteRepository.atualizarParticipante(aposta.getParticipante()); // atualiza participante no banco
            }
        }
    }

    public int getTotalPontosPorParticipante(Participante participante) {
        int total = 0;
        for (Aposta a : apostaRepository.buscarParticipante(participante)) {
            total += a.getPontosObtidos();
        }
        return total;
    }

    // Retorna todas as apostas de um participante
    public List<Aposta> getApostasPorParticipante(Participante participante) {
        return apostaRepository.buscarParticipante(participante);
    }

    // Retorna todas as apostas
    public List<Aposta> getApostas() {
        return apostaRepository.buscarTodasApostas();
    }
}