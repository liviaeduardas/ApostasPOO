package View;

import Controller.CampeonatoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TelaCadastro extends TelaBase {

    private MainFrame mainFrame;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    // Campos — campeonato
    private JTextField campoCampeonatoNome, campoCampeonatoAno;

    // Campos — clube
    private JTextField campoClubeNome, campoClubeSigla;
    private JComboBox<String> comboCampeonatoClube;

    // Campos — partida
    private JComboBox<String> comboCampeonatoPartida, comboCasa, comboVisitante;
    private JTextField campoData, campoHora;

    // Campos — resultado
    private JComboBox<String> comboCampeonatoResultado, comboPartidaResultado;
    private JTextField campoGolsCasa, campoGolsVisitante;

    public TelaCadastro(MainFrame mainFrame, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.mainFrame            = mainFrame;
        this.campeonatoController = campeonatoController;
        this.partidaController    = partidaController;
        montar();
    }

    private void montar() {
        setLayout(new BorderLayout());
        setBackground(FUNDO);

        // Lateral com botões de navegação
        JPanel lateral = painelLateral("Sistema", "Administrador");

        lateral.add(botaoMenu("Campeonato", e -> cardLayout.show(painelConteudo, "campeonato")));
        lateral.add(Box.createVerticalStrut(4));
        lateral.add(botaoMenu("Clube", e -> {
            atualizarCombos(comboCampeonatoClube);
            cardLayout.show(painelConteudo, "clube");
        }));
        lateral.add(Box.createVerticalStrut(4));
        lateral.add(botaoMenu("Partida", e -> {
            atualizarCombos(comboCampeonatoPartida);
            cardLayout.show(painelConteudo, "partida");
        }));
        lateral.add(Box.createVerticalStrut(4));
        lateral.add(botaoMenu("Resultado", e -> {
            atualizarCombos(comboCampeonatoResultado);
            cardLayout.show(painelConteudo, "resultado");
        }));
        lateral.add(Box.createVerticalGlue());

        JButton sair = botaoMenu("Sair", e -> mainFrame.trocarTela("telaLogin"));
        sair.setForeground(new Color(0xFF, 0xCC, 0xCC));
        lateral.add(sair);
        lateral.add(Box.createVerticalStrut(10));

        add(lateral, BorderLayout.WEST);

        // Área de conteúdo com CardLayout — troca o formulário sem abrir nova janela
        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(FUNDO);

        JLabel titulo = new JLabel("Painel do Administrador");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(VERMELHO);
        titulo.setBorder(new EmptyBorder(24, 30, 16, 20));
        direita.add(titulo, BorderLayout.NORTH);

        cardLayout     = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(FUNDO);
        painelConteudo.setBorder(new EmptyBorder(0, 20, 20, 20));

        painelConteudo.add(painelCampeonato(), "campeonato");
        painelConteudo.add(painelClube(),      "clube");
        painelConteudo.add(painelPartida(),    "partida");
        painelConteudo.add(painelResultado(),  "resultado");

        direita.add(painelConteudo, BorderLayout.CENTER);
        add(direita, BorderLayout.CENTER);
    }

    // ── Formulários ───────────────────────────────────────────────────────────

    private JPanel painelCampeonato() {
        JPanel p = formulario();
        GridBagConstraints gbc = gbc();
        campoCampeonatoNome = addCampo(p, gbc, "Nome:", 0);
        campoCampeonatoAno  = addCampo(p, gbc, "Ano:",  1);
        adicionarBotao(p, gbc, "Criar Campeonato", 2, e -> criarCampeonato());
        return p;
    }

    private JPanel painelClube() {
        JPanel p = formulario();
        GridBagConstraints gbc = gbc();
        campoClubeNome       = addCampo(p, gbc, "Nome:",        0);
        campoClubeSigla      = addCampo(p, gbc, "Sigla:",       1);
        comboCampeonatoClube = addCombo(p, gbc, "Campeonato:",  2);
        adicionarBotao(p, gbc, "Cadastrar Clube", 3, e -> cadastrarClube());
        return p;
    }

    private JPanel painelPartida() {
        JPanel p = formulario();
        GridBagConstraints gbc = gbc();
        comboCampeonatoPartida = addCombo(p, gbc, "Campeonato:", 0);
        comboCampeonatoPartida.addActionListener(e -> atualizarClubesPartida());
        comboCasa      = addCombo(p, gbc, "Mandante:",         1);
        comboVisitante = addCombo(p, gbc, "Visitante:",        2);
        campoData      = addCampo(p, gbc, "Data (dd/MM/yyyy):", 3);
        campoHora      = addCampo(p, gbc, "Hora (HH:mm):",     4);
        adicionarBotao(p, gbc, "Cadastrar Partida", 5, e -> cadastrarPartida());
        return p;
    }

    private JPanel painelResultado() {
        JPanel p = formulario();
        GridBagConstraints gbc = gbc();
        comboCampeonatoResultado = addCombo(p, gbc, "Campeonato:", 0);
        comboCampeonatoResultado.addActionListener(e -> atualizarPartidasResultado());
        comboPartidaResultado = addCombo(p, gbc, "Partida:",        1);
        campoGolsCasa         = addCampo(p, gbc, "Gols mandante:",  2);
        campoGolsVisitante    = addCampo(p, gbc, "Gols visitante:", 3);
        adicionarBotao(p, gbc, "Registrar Resultado", 4, e -> registrarResultado());
        return p;
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    private void criarCampeonato() {
        String nome   = campoCampeonatoNome.getText().trim();
        String anoStr = campoCampeonatoAno.getText().trim();
        try {
            boolean ok = campeonatoController.NovoCampeonato(nome, Integer.parseInt(anoStr));
            if (ok) { msg("Campeonato criado!"); campoCampeonatoNome.setText(""); campoCampeonatoAno.setText(""); }
            else erro("Erro ao criar campeonato!");
        } catch (NumberFormatException ex) { erro("Ano inválido!"); }
    }

    private void cadastrarClube() {
        String nome  = campoClubeNome.getText().trim();
        String sigla = campoClubeSigla.getText().trim();
        Campeonato c = campeonatoController.ProcurarCampeonato((String) comboCampeonatoClube.getSelectedItem());
        if (c == null) { erro("Selecione um campeonato!"); return; }
        if (!campeonatoController.CadastrarClube(nome, sigla)) { erro("Erro ao cadastrar clube!"); return; }
        Clube clube = campeonatoController.ProcurarClube(sigla);
        if (campeonatoController.AddClube(c, clube)) {
            msg("Clube cadastrado!"); campoClubeNome.setText(""); campoClubeSigla.setText("");
        } else erro("Limite de 8 clubes atingido!");
    }

    private void cadastrarPartida() {
        String nomeMandante  = (String) comboCasa.getSelectedItem();
        String nomeVisitante = (String) comboVisitante.getSelectedItem();
        if (nomeMandante == null || nomeMandante.equals(nomeVisitante)) { erro("Selecione times diferentes!"); return; }
        try {
            String[] pd    = campoData.getText().trim().split("/");
            LocalDate data = LocalDate.of(Integer.parseInt(pd[2]), Integer.parseInt(pd[1]), Integer.parseInt(pd[0]));
            String[] ph    = campoHora.getText().trim().split(":");
            LocalTime hora = LocalTime.of(Integer.parseInt(ph[0]), Integer.parseInt(ph[1]));
            Campeonato c   = campeonatoController.ProcurarCampeonato((String) comboCampeonatoPartida.getSelectedItem());
            Clube mandante  = campeonatoController.ProcurarClube(nomeMandante);
            Clube visitante = campeonatoController.ProcurarClube(nomeVisitante);
            if (partidaController.cadastrarPartida(c, mandante, visitante, data, hora)) {
                msg("Partida cadastrada!"); campoData.setText(""); campoHora.setText("");
            } else erro("Erro ao cadastrar partida!");
        } catch (Exception ex) { erro("Data ou hora inválida!\nUse dd/MM/yyyy e HH:mm"); }
    }

    private void registrarResultado() {
        String nomePartida = (String) comboPartidaResultado.getSelectedItem();
        Campeonato c = campeonatoController.ProcurarCampeonato((String) comboCampeonatoResultado.getSelectedItem());
        if (c == null) return;
        Partida partida = null;
        for (Partida p : c.getPartidas()) {
            if (p.toString().equals(nomePartida)) { partida = p; break; }
        }
        if (partida == null) return;
        try {
            int golsM = Integer.parseInt(campoGolsCasa.getText().trim());
            int golsV = Integer.parseInt(campoGolsVisitante.getText().trim());
            if (partidaController.AddResultado(partida, golsM, golsV)) {
                mainFrame.getApostaController().CalcularPontos(c);
                msg("Resultado registrado! Pontuações calculadas.");
                campoGolsCasa.setText(""); campoGolsVisitante.setText("");
                atualizarPartidasResultado();
            } else erro("Erro ao registrar resultado!");
        } catch (NumberFormatException ex) { erro("Gols inválidos!"); }
    }

    // ── Atualizações de combos ─────────────────────────────────────────────────

    private void atualizarCombos(JComboBox<String> combo) {
        combo.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos()) combo.addItem(c.getNome());
    }

    private void atualizarClubesPartida() {
        comboCasa.removeAllItems(); comboVisitante.removeAllItems();
        Campeonato c = campeonatoController.ProcurarCampeonato((String) comboCampeonatoPartida.getSelectedItem());
        if (c == null) return;
        for (Clube clube : c.getClubes()) { comboCasa.addItem(clube.getSigla()); comboVisitante.addItem(clube.getSigla()); }
    }

    private void atualizarPartidasResultado() {
        comboPartidaResultado.removeAllItems();
        Campeonato c = campeonatoController.ProcurarCampeonato((String) comboCampeonatoResultado.getSelectedItem());
        if (c == null) return;
        List<Partida> pendentes = campeonatoController.getPartidasPendentes(c);
        for (Partida p : pendentes) comboPartidaResultado.addItem(p.toString());
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────

    private JPanel formulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(FUNDO);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        return p;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void adicionarBotao(JPanel p, GridBagConstraints gbc, String texto, int linha, java.awt.event.ActionListener acao) {
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        p.add(botaoAcao(texto, acao), gbc);
    }
}