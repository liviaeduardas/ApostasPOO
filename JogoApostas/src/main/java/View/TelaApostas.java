package View;

import Controller.ApostaController;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaApostas extends JPanel {

    private MainFrame main;
    private ApostaController apostaController;
    private CampeonatoController campeonatoController;
    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JTextField txtGolsCasa;
    private JTextField txtGolsVisitante;

    public TelaApostas(MainFrame main, ApostaController apostaController,
                       CampeonatoController campeonatoController, GrupoController grupoController) {
        this.main                 = main;
        this.apostaController     = apostaController;
        this.campeonatoController = campeonatoController;

        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Campeonato:"));
        comboCampeonato = new JComboBox<>();
        // Ao trocar campeonato, carrega automaticamente as partidas
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
            if (participante == null) {
                JOptionPane.showMessageDialog(this, "Nenhum participante logado!");
                return;
            }

            Partida partida = getPartidaSelecionada();
            if (partida == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma partida!");
                return;
            }

            if (txtGolsCasa.getText().trim().isEmpty() || txtGolsVisitante.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha os gols!");
                return;
            }

            int golsCasa      = Integer.parseInt(txtGolsCasa.getText().trim());
            int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());

            boolean ok = apostaController.FazerAposta(participante, partida, golsCasa, golsVisitante);
            JOptionPane.showMessageDialog(this, ok ? "Aposta realizada!" : "Não foi possível apostar.\nVerifique se já apostou nessa partida ou se o prazo encerrou.");

            if (ok) { txtGolsCasa.setText(""); txtGolsVisitante.setText(""); }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos para os gols.");
        }
    }

    private void carregarPartidas() {
        comboPartida.removeAllItems();
        String nomeCamp = (String) comboCampeonato.getSelectedItem();
        if (nomeCamp == null) return;

        // Busca fresco do banco para garantir dados atualizados
        Campeonato campeonato = campeonatoController.procurarCampeonato(nomeCamp);
        if (campeonato == null) return;

        // Mostra apenas partidas não finalizadas com nome dos times
        List<Partida> pendentes = campeonatoController.getPartidasPendentes(campeonato);
        for (Partida p : pendentes)
            // Exibe: "Flamengo x Corinthians" em vez do toString padrão
            comboPartida.addItem(p.getClubeCasa().getNome() + " x " + p.getClubeVisitante().getNome());
    }

    private Partida getPartidaSelecionada() {
        String texto   = (String) comboPartida.getSelectedItem();
        String nomeCamp = (String) comboCampeonato.getSelectedItem();
        if (texto == null || nomeCamp == null) return null;

        Campeonato campeonato = campeonatoController.procurarCampeonato(nomeCamp);
        if (campeonato == null) return null;

        // Busca a partida pelo nome dos times
        for (Partida p : campeonato.getPartidas()) {
            String nomePartida = p.getClubeCasa().getNome() + " x " + p.getClubeVisitante().getNome();
            if (nomePartida.equals(texto)) return p;
        }
        return null;
    }

    public void atualizar() {
        comboCampeonato.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampeonato.addItem(c.getNome());
        carregarPartidas();
    }
}