package View;

import Controller.ApostaController;
import Controller.GrupoController;
import Model.Grupo;
import Model.Participante;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TelaClassificacao extends JPanel {

    private MainFrame main;
    private GrupoController grupoController;
    private ApostaController apostaController;
    private JComboBox<String> comboGrupo;
    private JTextArea areaRanking;

    public TelaClassificacao(MainFrame main, GrupoController grupoController, ApostaController apostaController) {
        this.main = main;
        this.grupoController = grupoController;
        this.apostaController = apostaController;

        setLayout(new BorderLayout(5, 5));

        JPanel topo = new JPanel();
        topo.add(new JLabel("Grupo:"));

        comboGrupo = new JComboBox<>();
        comboGrupo.addActionListener(e -> mostrarRanking());
        topo.add(comboGrupo);

        JButton atualizar = new JButton("Atualizar");
        atualizar.addActionListener(e -> atualizar());
        topo.add(atualizar);

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(e -> main.trocarTela("telaApostas"));
        topo.add(voltar);

        add(topo, BorderLayout.NORTH);

        areaRanking = new JTextArea();
        areaRanking.setEditable(false);
        areaRanking.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(areaRanking), BorderLayout.CENTER);
    }


    private void mostrarRanking() {
        areaRanking.setText("");

        String nomeGrupo = (String) comboGrupo.getSelectedItem();
        if (nomeGrupo == null) return;

        Grupo grupo = grupoController.buscarNome(nomeGrupo);
        if (grupo == null) return;

        List<Participante> participantes = grupo.getParticipantes();
        if (participantes.isEmpty()) {
            areaRanking.append("Nenhum participante neste grupo ainda.\n");
            return;
        }

        List<Participante> ordenados = new ArrayList<>(participantes);
        ordenados.sort(Comparator.comparingInt(this::getPontos).reversed());

        for (int i = 0; i < ordenados.size(); i++) {
            Participante p = ordenados.get(i);
            int pontos = getPontos(p);
            areaRanking.append((i + 1) + "º  " + p.getNome() + "  —  " + pontos + " pontos\n");
        }
    }

    private int getPontos(Participante p) {
        return apostaController.getTotalPontosPorParticipante(p);
    }

    public void atualizar() {
        comboGrupo.removeAllItems();

        Participante participanteLogado = main.getParticipanteLocalizado();
        if (participanteLogado == null) return;

        for (Grupo g : grupoController.getGrupos()) {
            if (participanteEstaNoGrupo(participanteLogado, g)) {
                comboGrupo.addItem(g.getNome());
            }
        }

        mostrarRanking();
    }

    private boolean participanteEstaNoGrupo(Participante participante, Grupo grupo) {
        for (Participante p : grupo.getParticipantes()) {
            if (p.getId() == participante.getId()) {
                return true;
            }
        }
        return false;
    }
}