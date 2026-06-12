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

    private MainFrame            main;
    private ApostaController     apostaController;
    private CampeonatoController campeonatoController;

    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JLabel            labelMandante;   // mostra nome do time da casa
    private JLabel            labelVisitante;  // mostra nome do time visitante
    private JTextField        txtGolsCasa;
    private JTextField        txtGolsVisitante;

    // Lista paralela ao comboPartida para recuperar o objeto Partida pelo índice
    private final List<Partida> partidasExibidas = new ArrayList<>();

    public TelaApostas(MainFrame main, ApostaController apostaController,
                       CampeonatoController campeonatoController,
                       GrupoController grupoController) {
        this.main                 = main;
        this.apostaController     = apostaController;
        this.campeonatoController = campeonatoController;

        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campeonato
        add(new JLabel("Campeonato:"));
        comboCampeonato = new JComboBox<>();
        comboCampeonato.addActionListener(e -> carregarPartidas());
        add(comboCampeonato);

        // Partida
        add(new JLabel("Partida:"));
        comboPartida = new JComboBox<>();
        comboPartida.addActionListener(e -> atualizarNomeTimes()); // atualiza labels ao trocar partida
        add(comboPartida);

        // Labels com nome dos times — atualizadas quando o usuário troca a partida
        labelMandante  = new JLabel("Time da casa: -");
        labelVisitante = new JLabel("Time visitante: -");
        add(labelMandante);
        add(labelVisitante);

        // Campos de gols
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

    // Atualiza os labels com o nome dos times da partida selecionada
    private void atualizarNomeTimes() {
        Partida p = getPartidaSelecionada();
        if (p == null) {
            labelMandante.setText("Time da casa: -");
            labelVisitante.setText("Time visitante: -");
            return;
        }
        labelMandante.setText("Time da casa: " + p.getClubeCasa().getNome());
        labelVisitante.setText("Time visitante: " + p.getClubeVisitante().getNome());
    }

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

            if (txtGolsCasa.getText().trim().isEmpty() || txtGolsVisitante.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha os gols!");
                return;
            }

            int golsCasa      = Integer.parseInt(txtGolsCasa.getText().trim());
            int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());

            boolean ok = apostaController.FazerAposta(participante, partida, golsCasa, golsVisitante);
            JOptionPane.showMessageDialog(this, ok
                    ? "Aposta realizada!"
                    : "Não foi possível apostar.\nVerifique se já apostou nessa partida ou se o prazo encerrou.");

            if (ok) { txtGolsCasa.setText(""); txtGolsVisitante.setText(""); }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos para os gols.");
        }
    }

    private void carregarPartidas() {
        comboPartida.removeAllItems();
        partidasExibidas.clear();
        labelMandante.setText("Time da casa: -");
        labelVisitante.setText("Time visitante: -");

        Campeonato campeonato = campeonatoController.procurarCampeonato(
                (String) comboCampeonato.getSelectedItem());
        if (campeonato == null) return;

        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) {
                comboPartida.addItem(p.getClubeCasa().getNome() + " x " + p.getClubeVisitante().getNome());
                partidasExibidas.add(p);
            }
        }

        atualizarNomeTimes(); // atualiza labels com o primeiro item do combo
    }

    private Partida getPartidaSelecionada() {
        int idx = comboPartida.getSelectedIndex();
        if (idx < 0 || idx >= partidasExibidas.size()) return null;
        return partidasExibidas.get(idx);
    }

    public void atualizar() {
        comboCampeonato.removeAllItems();
        partidasExibidas.clear();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampeonato.addItem(c.getNome());
        carregarPartidas();
    }
}