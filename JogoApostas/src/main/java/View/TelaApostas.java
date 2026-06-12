package View;

import Controller.ApostaController;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaApostas extends JPanel {

    private MainFrame             main;
    private ApostaController      apostaController;
    private CampeonatoController  campeonatoController;

    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JTextField        txtGolsCasa;
    private JTextField        txtGolsVisitante;

    // Lista paralela ao comboPartida — guarda os objetos Partida na mesma ordem
    private final List<Partida> partidasExibidas = new ArrayList<>();

    public TelaApostas(MainFrame main, ApostaController apostaController,
                       CampeonatoController campeonatoController,
                       GrupoController grupoController) {
        this.main                 = main;
        this.apostaController     = apostaController;
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

    // -------------------------------------------------------------------------

    private void fazerAposta() {
        try {
            Participante participante = main.getParticipanteLogado();
            if (participante == null) {
                JOptionPane.showMessageDialog(this, "Nenhum participante logado.");
                return;
            }

            Partida partida = getPartidaSelecionada();
            if (partida == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma partida.");
                return;
            }

            int golsCasa      = Integer.parseInt(txtGolsCasa.getText().trim());
            int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());

            boolean ok = apostaController.FazerAposta(participante, partida, golsCasa, golsVisitante);
            JOptionPane.showMessageDialog(this, ok
                    ? "Aposta realizada!"
                    : "Não foi possível apostar (prazo encerrado, partida finalizada ou aposta duplicada).");

            if (ok) { txtGolsCasa.setText(""); txtGolsVisitante.setText(""); }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos para os gols.");
        }
    }

    /**
     * Recarrega o combo de partidas com as partidas PENDENTES do campeonato selecionado.
     * Mantém a lista paralela "partidasExibidas" sincronizada para recuperar o
     * objeto Partida pelo índice — sem depender de toString().
     */
    private void carregarPartidas() {
        comboPartida.removeAllItems();
        partidasExibidas.clear();

        Campeonato campeonato = campeonatoController.procurarCampeonato(
                (String) comboCampeonato.getSelectedItem());
        if (campeonato == null) return;

        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) {
                comboPartida.addItem(
                        p.getClubeCasa().getNome() + " x " + p.getClubeVisitante().getNome());
                partidasExibidas.add(p);
            }
        }
    }

    /**
     * Retorna o objeto Partida correspondente ao item selecionado no combo,
     * usando a lista paralela (índice garantido consistente).
     */
    private Partida getPartidaSelecionada() {
        int idx = comboPartida.getSelectedIndex();
        if (idx < 0 || idx >= partidasExibidas.size()) return null;
        return partidasExibidas.get(idx);
    }

    /**
     * Chamado pelo MainFrame toda vez que a tela de apostas é exibida.
     * Recarrega campeonatos e partidas com dados frescos do banco.
     */
    public void atualizar() {
        comboCampeonato.removeAllItems();
        partidasExibidas.clear();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampeonato.addItem(c.getNome());

    }
}