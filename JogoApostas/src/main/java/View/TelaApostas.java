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

    private MainFrame main;
    private ApostaController apostaController;
    private CampeonatoController campeonatoController;

    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JLabel labelMandante;
    private JLabel labelVisitante;
    private JLabel labelData;
    private JLabel labelHora;
    private JTextField txtGolsCasa;
    private JTextField txtGolsVisitante;


    private final List<Partida> partidasExibidas = new ArrayList<>();

    public TelaApostas(MainFrame main, ApostaController apostaController,
                       CampeonatoController campeonatoController,
                       GrupoController grupoController) {
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
        comboPartida.addActionListener(e -> atualizarNomeTimes());
        add(comboPartida);


        labelMandante = new JLabel("Time da casa: -");
        labelVisitante = new JLabel("Time visitante: -");
        labelData = new JLabel("Data: -");
        labelHora = new JLabel("Hora: -");
        add(labelMandante);
        add(labelVisitante);
        add(labelData);
        add(labelHora);


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


    private void atualizarNomeTimes() {
        Partida partida = getPartidaSelecionada();

        if (partida == null) {
            labelMandante.setText("Time da casa: -");
            labelVisitante.setText("Time visitante: -");
            labelData.setText("Data: -");
            labelHora.setText("Hora: -");
            return;
        }

        labelMandante.setText("Time da casa: " + partida.getClubeCasa().getNome());
        labelVisitante.setText("Time visitante: " + partida.getClubeVisitante().getNome());
        labelData.setText("Data: " + partida.getDataPartida());
        labelHora.setText("Hora: " + partida.getHoraPartida());
    }

    private void fazerAposta() {
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

        try {
            int golsCasa = Integer.parseInt(txtGolsCasa.getText().trim());
            int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());

            boolean ok = apostaController.FazerAposta(participante, partida, golsCasa, golsVisitante);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Aposta realizada!");
                txtGolsCasa.setText("");
                txtGolsVisitante.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível apostar.\nVerifique se já apostou nessa partida ou se o prazo encerrou.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos para os gols.");
        }
    }

    private void carregarPartidas() {
        comboPartida.removeAllItems();
        partidasExibidas.clear();

        String nomeCampeonato = (String) comboCampeonato.getSelectedItem();
        if (nomeCampeonato == null) return;

        Campeonato campeonato = campeonatoController.procurarCampeonato(nomeCampeonato);
        if (campeonato == null) return;

        for (Partida p : campeonato.getPartidas()) {
            if (!p.isPartidaFinalizada()) {
                comboPartida.addItem(p.getClubeCasa().getNome() + " x " + p.getClubeVisitante().getNome());
                partidasExibidas.add(p);
            }
        }

        atualizarNomeTimes();
    }


    private Partida getPartidaSelecionada() {
        int indice = comboPartida.getSelectedIndex();
        if (indice < 0 || indice >= partidasExibidas.size()) {
            return null;
        }
        return partidasExibidas.get(indice);
    }


    public void atualizar() {
        comboCampeonato.removeAllItems();
        partidasExibidas.clear();

        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampeonato.addItem(c.getNome());

        carregarPartidas();
    }
}