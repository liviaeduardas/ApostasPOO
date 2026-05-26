package Controller;
import Model.Aposta;
import Model.Campeonato;
import Model.Participante;
import Model.Partida;

import java.util.ArrayList;

public class ApostaController {
    private ArrayList<Aposta> apostas;
    private int proximoId;

    public ApostaController() {
        this.apostas = new ArrayList<>();
        this.proximoId = 1;
    }

    public boolean registrarAposta(Participante participante, Partida partida, int golsMandante, int golsVisitante) {
        if (participante == null || partida == null) {
            return false;
        }
        if (golsMandante < 0 || golsVisitante < 0) {
            return false;
        }
        if (partida.isEncerrada()) {
            return false;
        }

        Aposta aposta = new Aposta(proximoId++, participante, partida, golsMandante, golsVisitante);

        if (!aposta.podeApostar()) {
            return false;
        }

        for (Aposta a : apostas) {
            if (a.getParticipante().equals(participante) && a.getPartida().equals(partida)) {
                return false;
            }
        }

        apostas.add(aposta);
        participante.registrarAposta(aposta);
        return true;
    }

    public void calcularPontuacoes(Campeonato campeonato) {
        for (Aposta a : apostas) {
            if (campeonato.getPartidas().contains(a.getPartida()) && a.getPartida().isEncerrada()) {
                int pontos = a.calcularPontuacao();
                a.getParticipante().setPontosTotal(a.getParticipante().getPontosTotal() + pontos);
            }
        }
    }

    public ArrayList<Aposta> getApostasPorParticipante(Participante participante) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getParticipante().equals(participante)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostasPorPartida(Partida partida) {
        ArrayList<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getPartida().equals(partida)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public ArrayList<Aposta> getApostas() {
        return apostas;
    }
}
