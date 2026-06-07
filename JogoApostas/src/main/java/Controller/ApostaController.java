package Controller;
import Model.Aposta;
import Model.Campeonato;
import Model.Participante;
import Model.Partida;
import java.util.ArrayList;

public class ApostaController {
    private ArrayList<Aposta> apostas;

    public ApostaController() {
        this.apostas = new ArrayList<>();
    }

    public boolean fazerAposta(Participante participante, Partida partida, int GolsCasa, int GolsVisitante) {
        if (participante == null || partida == null) return false;
        if (GolsCasa < 0 || GolsVisitante < 0) return false;
        if (partida.isPartidaFinalizada()) return false;

        // Cria a aposta corretamente — id 0 pois o banco vai gerar automaticamente
        Aposta aposta = new Aposta(0, participante, partida, GolsCasa, GolsVisitante);
        if (!aposta.possivelApostar()) return false;

        // Verifica duplicata — mesmo participante na mesma partida
        for (Aposta a : apostas) {
            if (a.getParticipante() == participante && a.getPartida() == partida) return false;
        }
        apostas.add(aposta);
        participante.fazerAposta(aposta);
        return true;
    }

    public void calcularPontos(Campeonato campeonato) {
        for (Aposta aposta : apostas) {
            if (campeonato.getPartidas().contains(aposta.getPartida()) && aposta.getPartida().isPartidaFinalizada()) {
                aposta.calcularResultadoAposta();
            }
        }
    }

    public ArrayList<Aposta> getApostasPorParticipante(Participante participante) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta aposta : apostas) {
            if (aposta.getParticipante() == participante) resultado.add(aposta);
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostasPorPartida(Partida partida) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta aposta : apostas) {
            if (aposta.getPartida() == partida) resultado.add(aposta);
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostas() {
        return apostas;
    }
}