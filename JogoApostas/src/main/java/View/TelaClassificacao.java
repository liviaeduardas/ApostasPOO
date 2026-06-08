package View;
import Controller.GrupoController;
import Model.Grupo;
import Model.Participante;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaClassificacao extends JPanel {
    private MainFrame main;
    private GrupoController grupoController;
    private JComboBox<String> comboGrupo;
    private JTextArea areaRanking;

    public TelaClassificacao(MainFrame main, GrupoController grupoController) {
        this.main = main;
        this.grupoController = grupoController;

        setLayout(new BorderLayout(5, 5));

        JPanel topo = new JPanel();
        topo.add(new JLabel("Grupo:"));

        comboGrupo = new JComboBox<>();
        topo.add(comboGrupo);

        JButton atualizar = new JButton("Atualizar");
        atualizar.addActionListener(e -> mostrarRanking());
        topo.add(atualizar);

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(e -> main.trocarTela("telaApostas"));
        topo.add(voltar);

        add(topo, BorderLayout.NORTH);

        areaRanking = new JTextArea();
        areaRanking.setEditable(false);
        add(new JScrollPane(areaRanking), BorderLayout.CENTER);
    }

    private void mostrarRanking() {
        areaRanking.setText("");

        String nomeGrupo = (String) comboGrupo.getSelectedItem();
        if (nomeGrupo == null) return;

        Grupo grupo = grupoController.buscarNome(nomeGrupo);
        if (grupo == null) return;

        ArrayList<Participante> ranking = grupoController.getRanking(grupo);

        for (int i = 0; i < ranking.size(); i++) {
            Participante p = ranking.get(i);
            areaRanking.append((i + 1) + "º - " + p.getNome() + " - " + p.getTotalPontos() + " pontos\n");
        }
    }

    public void atualizar() {
        comboGrupo.removeAllItems();
        for (Grupo g : grupoController.getGrupos())
            comboGrupo.addItem(g.getNome());
        mostrarRanking();
    }
}