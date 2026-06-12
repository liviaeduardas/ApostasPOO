package View;

import Controller.ApostaController;
import Controller.GrupoController;
import Model.Grupo;
import Model.Participante;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaClassificacao extends JPanel {

    private MainFrame        main;
    private GrupoController  grupoController;
    private ApostaController apostaController;
    private JComboBox<String> comboGrupo;
    private JTextArea         areaRanking;

    public TelaClassificacao(MainFrame main, GrupoController grupoController,
                             ApostaController apostaController) {
        this.main             = main;
        this.grupoController  = grupoController;
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

        // Busca participantes diretamente do grupo — sem acessar lista lazy
        List<Participante> participantes = grupo.getParticipantes();
        if (participantes.isEmpty()) {
            areaRanking.append("Nenhum participante neste grupo ainda.\n");
            return;
        }

        // Remove duplicatas por id antes de ordenar
        List<Participante> semDuplicatas = new ArrayList<>();
        List<Integer> idsVistos = new ArrayList<>();
        for (Participante p : participantes) {
            if (!idsVistos.contains(p.getId())) {
                idsVistos.add(p.getId());
                semDuplicatas.add(p);
            }
        }

        // Ordena por pontos (maior primeiro) — pontos calculados via apostas no banco
        semDuplicatas.sort((a, b) ->
                apostaController.getTotalPontosPorParticipante(b)
                        - apostaController.getTotalPontosPorParticipante(a));

        for (int i = 0; i < semDuplicatas.size(); i++) {
            Participante p = semDuplicatas.get(i);
            int pontos = apostaController.getTotalPontosPorParticipante(p);
            areaRanking.append((i + 1) + "º  " + p.getNome() + "  —  " + pontos + " pontos\n");
        }
    }

    public void atualizar() {
        comboGrupo.removeAllItems();
        for (Grupo g : grupoController.getGrupos())
            comboGrupo.addItem(g.getNome());
        mostrarRanking();
    }
}