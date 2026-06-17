package View;

import Controller.CampeonatoController;
import Model.Campeonato;
import Model.Partida;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaResultados extends JPanel {

    private MainFrame main;
    private CampeonatoController campCtrl;
    private JComboBox<String> comboCamp;
    private DefaultTableModel modelo;


    private static final String VAZIO = "";

    public TelaResultados(MainFrame main, CampeonatoController campCtrl) {
        this.main = main;
        this.campCtrl = campCtrl;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topo = new JPanel();
        topo.add(new JLabel("Campeonato:"));

        comboCamp = new JComboBox<>();
        comboCamp.addActionListener(e -> atualizarTabela());
        topo.add(comboCamp);

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(e -> main.trocarTela("telaApostas"));
        topo.add(voltar);

        add(topo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"Casa", "Visitante", "Placar", "Resultado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);

        String nomeCamp = (String) comboCamp.getSelectedItem();
        if (nomeCamp == null || nomeCamp.isEmpty()) return;

        Campeonato campeonato = campCtrl.procurarCampeonato(nomeCamp);
        if (campeonato == null) return;

        boolean algumResultado = false;
        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) continue;

            String resultado;
            if (p.getResultado() == 1)
                resultado = p.getClubeCasa().getNome() + " venceu";
            else if (p.getResultado() == 2)
                resultado = p.getClubeVisitante().getNome() + " venceu";
            else
                resultado = "Empate";

            modelo.addRow(new Object[]{
                    p.getClubeCasa().getNome(),
                    p.getClubeVisitante().getNome(),
                    p.getGolsCasa() + " x " + p.getGolsVisitante(),
                    resultado
            });
            algumResultado = true;
        }

        if (!algumResultado) {
            modelo.addRow(new Object[]{"Nenhum resultado disponível.", "", "", ""});
        }
    }

    public void atualizar() {
        comboCamp.removeAllItems();
        comboCamp.addItem(VAZIO);
        for (Campeonato c : campCtrl.getCampeonatos())
            comboCamp.addItem(c.getNome());

        modelo.setRowCount(0);
    }
}