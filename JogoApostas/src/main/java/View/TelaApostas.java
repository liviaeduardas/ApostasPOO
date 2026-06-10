package View;
import Controller.ApostaController;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Model.*;
import javax.swing.*;
import java.awt.*;

public class TelaApostas extends JPanel {
    private MainFrame main;
    private ApostaController apostaController;
    private CampeonatoController campeonatoController;
    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JTextField txtGolsCasa;
    private JTextField txtGolsVisitante;

    public TelaApostas(MainFrame main, ApostaController apostaController, CampeonatoController campeonatoController, GrupoController grupoController) {
        this.main = main;
        this.apostaController = apostaController;
        this.campeonatoController = campeonatoController;

        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Campeonato:"));
        comboCampeonato = new JComboBox<>();
        comboCampeonato.addActionListener(e -> carregarPartidas());
        add(comboCampeonato);

        add(new JLabel("Partida:"));
        comboPartida = new JComboBox<>();
        add(comboPartida);

        add(new JLabel("Gols casa:"));
        txtGolsCasa = new JTextField();
        add(txtGolsCasa);

        add(new JLabel("Gols visitante:"));
        txtGolsVisitante = new JTextField();
        add(txtGolsVisitante);

        JButton apostar = new JButton("Apostar");
        apostar.addActionListener(e -> fazerAposta());
        add(apostar);

        JButton verResultados = new JButton("Ver Resultados");
        verResultados.addActionListener(e -> main.trocarTela("telaResultados"));
        add(verResultados);

        JButton verClassificacao = new JButton("Ver Classificação");
        verClassificacao.addActionListener(e -> main.trocarTela("telaClassificacao"));
        add(verClassificacao);

        JButton sair = new JButton("Sair");
        sair.addActionListener(e -> main.trocarTela("telaLogin"));
        add(sair);
    }

    private void fazerAposta() {
        try {
            Participante participante = main.getParticipanteLogado();
            Partida partida = getPartidaSelecionada();
            int golsCasa = Integer.parseInt(txtGolsCasa.getText());
            int golsVisitante = Integer.parseInt(txtGolsVisitante.getText());
            boolean ok = apostaController.FazerAposta(participante, partida, golsCasa, golsVisitante);
            JOptionPane.showMessageDialog(this, ok ? "Aposta realizada!" : "Não foi possível apostar.");
            if (ok) { txtGolsCasa.setText(""); txtGolsVisitante.setText(""); }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos para os gols.");
        }
    }

    private void carregarPartidas() {
        comboPartida.removeAllItems();
        Campeonato campeonato = campeonatoController.procurarCampeonato((String) comboCampeonato.getSelectedItem());
        if (campeonato == null) return;
        for (Partida p : campeonato.getPartidas())
            if (!p.isPartidaFinalizada())
                comboPartida.addItem(p.toString());
    }

    private Partida getPartidaSelecionada() {
        String texto = (String) comboPartida.getSelectedItem();
        if (texto == null) return null;
        for (Campeonato c : campeonatoController.getCampeonatos())
            for (Partida p : c.getPartidas())
                if (p.toString().equals(texto)) return p;
        return null;
    }

    public void atualizar() {
        comboCampeonato.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampeonato.addItem(c.getNome());
        carregarPartidas();
    }
}
