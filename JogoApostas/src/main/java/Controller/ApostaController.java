package Controller;
import Model.Aposta;
import Model.Campeonato;
import Model.Participante;
import Model.Partida;
import java.util.List;
import Repository.ApostaRepository;
import Repository.ParticipanteRepository;

public class ApostaController {
    private ApostaRepository apostaRepository;
    private ParticipanteRepository participanteRepository;

    public ApostaController() {
        apostaRepository = new ApostaRepository();
        participanteRepository = new ParticipanteRepository();
    }

    public boolean FazerAposta(Participante participante, Partida partida, int GolsCasa, int GolsVisitante){
        if (participante == null || partida == null)
            return false;
        if (GolsCasa < 0 || GolsVisitante < 0)
            return false;
        if (partida.isPartidaFinalizada())
            return false;

        Aposta aposta = new Aposta(participante, partida, GolsCasa, GolsVisitante);

        if (!aposta.possivelApostar())
            return false;

        for (Aposta a : apostaRepository.buscarParticipante(participante)) {
            if (a.getPartida() == partida)
                return false;
        }
        participante.fazerAposta(aposta);
        return apostaRepository.salvarAposta(aposta);
    }

    public void CalcularPontos(Campeonato campeonato) {
        for (Aposta aposta : apostaRepository.buscarTodasApostas()) {
            if (campeonato.getPartidas().contains(aposta.getPartida()) && aposta.getPartida().isPartidaFinalizada()) {
                aposta.calcularResultadoAposta();
                apostaRepository.atualizarAposta(aposta);
                participanteRepository.atualizarParticipante(aposta.getParticipante());
            }
        }
    }

    public int getTotalPontosPorParticipante(Participante participante){
        int total = 0;
        for (Aposta a : apostaRepository.buscarParticipante(participante)) {
            total += a.getPontosObtidos();
        }
        return total;
    }

    public List<Aposta> getApostasPorParticipante(Participante participante) {
        return apostaRepository.buscarParticipante(participante);
    }

    public List<Aposta> getApostas(){
        return apostaRepository.buscarTodasApostas();
    }
}