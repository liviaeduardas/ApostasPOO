package Controller;
import Model.Aposta;
import Model.Participante;
import Model.Partida;
import java.util.ArrayList;

public class ApostaController {
    private ArrayList<Aposta> apostas;

    public ApostaController() {
        this.apostas = new ArrayList<>();
    }

    public boolean FazerAposta(Participante participante, Partida partida, int GolsCasa, int GolsVisitante) {
        if (participante == null || partida == null) {
            return false;
        }

        if (GolsCasa < 0 || GolsVisitante < 0) {
            return false;
        }

        if (partida.isPartidaFinalizada()) {
            return false;
        }

        Aposta aposta = new Aposta(int idAposta, Participante, Partida, int, int);

        if (!aposta.PossivelApostar()) {
            return false;
        }

        for (Aposta a : apostas) {
            if (a.getParticipante() == participante && a.getPartida() == partida) {
                return false;
            }
        }
        apostas.add(aposta);
        participante.FazerAposta(aposta);
        return true;
    }

    public void CalcularPontos() {
        for (Aposta aposta : apostas) {
            if (aposta.getPartida().isPartidaFinalizada()) {
                aposta.CalcularResultadoAposta();
            }
        }
    }

    public ArrayList<Aposta> getApostasPorParticipante(Participante participante) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta aposta : apostas) {
            if (aposta.getParticipante() == participante) {
                resultado.add(aposta);
            }
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostasPorPartida(Partida partida) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta aposta : apostas) {
            if (aposta.getPartida() == partida) {
                resultado.add(aposta);
            }
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostas() {
        return apostas;
    }
}