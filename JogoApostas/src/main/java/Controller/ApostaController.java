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
        if (participante == null || partida == null) {
            return false;
        }
        if (GolsCasa < 0 || GolsVisitante < 0) {
            return false;
        }
        if (partida.isPartidaFinalizada()) {
            return false;
        }

        Aposta aposta = new Aposta(participante, partida, GolsCasa, GolsVisitante);

        if (!aposta.possivelApostar()) {
            return false;
        }
        for (Aposta a : apostaRepository.buscarApostasParticipante(participante)) {
            if (a.getPartida().getId() == partida.getId()){
                return false;
            }
        }
        participante.fazerAposta(aposta);
        return apostaRepository.salvarAposta(aposta);
    }

    public void calcularpontos(Campeonato campeonato) {
        for (Partida partida : campeonato.getPartidas()) {
            if (!partida.isPartidaFinalizada()) {
                continue;
            }
            for (Aposta aposta : apostaRepository.buscarTodasApostas()) {
                if (aposta.getPartida() == partida) {
                    aposta.calcularResultadoAposta();
                    apostaRepository.atualizarAposta(aposta);
                    participanteRepository.atualizarParticipante(aposta.getParticipante());
                }
            }
        }
    }

    public int getTotalPontosPorParticipante(Participante participante){
        int total = 0;
        for (Aposta a : apostaRepository.buscarApostasParticipante(participante)) {
            total += a.getPontosObtidos();
        }
        return total;
    }

    public List<Aposta> getApostasPorParticipante(Participante participante) {
        return apostaRepository.buscarApostasParticipante(participante);
    }

    public List<Aposta> getApostas(){
        return apostaRepository.buscarTodasApostas();
    }
}