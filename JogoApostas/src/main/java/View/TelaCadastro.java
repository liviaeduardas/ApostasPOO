package View;
import Controller.CampeonatoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TelaCadastro extends JPanel {
    private MainFrame mainFrame;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private JPanel painelConteudo;
    private CardLayout cardLayout;

    private JTextField campoCampeonatoNome;
    private JTextField campoCampeonatoAno;

    private JTextField campoClubeNome;
    private JTextField campoClubeSigla;
    private JComboBox<String> comboCampeonatoClube;

    private JComboBox<String> comboCampeonatoPartida;
    private JComboBox<String> comboMandante;
    private JComboBox<String> comboVisitante;
    private JTextField campoData;
    private JTextField campoHora;

    private JComboBox<String> comboCampeonatoResultado;
    private JComboBox<String> comboPartidaResultado;
    private JTextField campoGolsMandante;
    private JTextField campoGolsVisitante;

    private static final Color VERMELHO = new Color(0x95, 0x0E, 0x17);
    private static final Color VERMELHO_ESC = new Color(0x70, 0x0A, 0x11);
    private static final Color FUNDO = new Color(0xFF, 0xF5, 0xF5);

    public TelaCadastro(MainFrame mainFrame, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.mainFrame = mainFrame;
        this.campeonatoController = campeonatoController;
        this.partidaController = partidaController;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(FUNDO);

        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(VERMELHO);
        lateral.setPreferredSize(new Dimension(180, 0));
        lateral.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel sistema = new JLabel("Sistema", SwingConstants.CENTER);
        sistema.setFont(new Font("Arial", Font.BOLD, 18));
        sistema.setForeground(Color.WHITE);
        sistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sistema);

        JLabel admin = new JLabel("Administrador", SwingConstants.CENTER);
        admin.setFont(new Font("Arial", Font.PLAIN, 12));
        admin.setForeground(new Color(0xFF, 0xCC, 0xCC));
        admin.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(admin);

        lateral.add(Box.createVerticalStrut(30));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(140, 1));
        sep.setForeground(new Color(0xAA, 0x33, 0x33));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sep);

        lateral.add(Box.createVerticalStrut(20));

        JButton btnCampeonato = criarBotaoMenu("Campeonato");
        btnCampeonato.addActionListener(e -> cardLayout.show(painelConteudo, "campeonato"));
        lateral.add(btnCampeonato);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnClube = criarBotaoMenu("Clube");
        btnClube.addActionListener(e -> {
            atualizarComboCampeonatos(comboCampeonatoClube);
            cardLayout.show(painelConteudo, "clube");
        });
        lateral.add(btnClube);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnPartida = criarBotaoMenu("Partida");
        btnPartida.addActionListener(e -> {
            atualizarComboCampeonatos(comboCampeonatoPartida);
            cardLayout.show(painelConteudo, "partida");
        });
        lateral.add(btnPartida);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnResultado = criarBotaoMenu("Resultado");
        btnResultado.addActionListener(e -> {
            atualizarComboCampeonatos(comboCampeonatoResultado);
            cardLayout.show(painelConteudo, "resultado");
        });
        lateral.add(btnResultado);
        lateral.add(Box.createVerticalStrut(4));

        lateral.add(Box.createVerticalGlue());

        JButton botaoSair = criarBotaoMenu("Sair");
        botaoSair.setForeground(new Color(0xFF, 0xCC, 0xCC));
        botaoSair.addActionListener(e -> mainFrame.trocarTela("telaLogin"));
        lateral.add(botaoSair);
        lateral.add(Box.createVerticalStrut(10));

        add(lateral, BorderLayout.WEST);

        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(FUNDO);

        JLabel titulo = new JLabel("Painel do Administrador", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(VERMELHO);
        titulo.setBorder(new EmptyBorder(24, 30, 16, 20));
        direita.add(titulo, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(FUNDO);
        painelConteudo.setBorder(new EmptyBorder(0, 20, 20, 20));

        painelConteudo.add(criarPainelCampeonato(), "campeonato");
        painelConteudo.add(criarPainelClube(),      "clube");
        painelConteudo.add(criarPainelPartida(),    "partida");
        painelConteudo.add(criarPainelResultado(),  "resultado");

        direita.add(painelConteudo, BorderLayout.CENTER);
        add(direita, BorderLayout.CENTER);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 13));
        botao.setForeground(Color.WHITE);
        botao.setBackground(VERMELHO);
        botao.setMaximumSize(new Dimension(180, 36));
        botao.setPreferredSize(new Dimension(180, 36));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { botao.setBackground(VERMELHO_ESC); }
            public void mouseExited(java.awt.event.MouseEvent e)  { botao.setBackground(VERMELHO); }
        });
        return botao;
    }

    private JButton criarBotaoAcao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setBackground(VERMELHO);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(200, 36));
        return botao;
    }

    private JPanel criarPainelCampeonato() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome do campeonato:"), gbc);
        campoCampeonatoNome = new JTextField();
        campoCampeonatoNome.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoCampeonatoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Ano:"), gbc);
        campoCampeonatoAno = new JTextField();
        campoCampeonatoAno.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoCampeonatoAno, gbc);

        JButton botao = criarBotaoAcao("Criar Campeonato");
        botao.addActionListener(e -> criarCampeonato());
        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(20, 0, 0, 0);
        painel.add(botao, gbc);

        return painel;
    }

    private JPanel criarPainelClube() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome do clube:"), gbc);
        campoClubeNome = new JTextField();
        campoClubeNome.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoClubeNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Sigla:"), gbc);
        campoClubeSigla = new JTextField();
        campoClubeSigla.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoClubeSigla, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Campeonato:"), gbc);
        comboCampeonatoClube = new JComboBox<>();
        comboCampeonatoClube.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboCampeonatoClube, gbc);

        JButton botao = criarBotaoAcao("Cadastrar Clube");
        botao.addActionListener(e -> cadastrarClube());
        gbc.gridx = 1; gbc.gridy = 3; gbc.insets = new Insets(20, 0, 0, 0);
        painel.add(botao, gbc);

        return painel;
    }

    private JPanel criarPainelPartida() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Campeonato:"), gbc);
        comboCampeonatoPartida = new JComboBox<>();
        comboCampeonatoPartida.setPreferredSize(new Dimension(260, 34));
        comboCampeonatoPartida.addActionListener(e -> atualizarClubesPartida());
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboCampeonatoPartida, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Mandante:"), gbc);
        comboMandante = new JComboBox<>();
        comboMandante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboMandante, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Visitante:"), gbc);
        comboVisitante = new JComboBox<>();
        comboVisitante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboVisitante, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        campoData = new JTextField();
        campoData.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoData, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Hora (HH:mm):"), gbc);
        campoHora = new JTextField();
        campoHora.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoHora, gbc);

        JButton botao = criarBotaoAcao("Cadastrar Partida");
        botao.addActionListener(e -> cadastrarPartida());
        gbc.gridx = 1; gbc.gridy = 5; gbc.insets = new Insets(20, 0, 0, 0);
        painel.add(botao, gbc);

        return painel;
    }

    private JPanel criarPainelResultado() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Campeonato:"), gbc);
        comboCampeonatoResultado = new JComboBox<>();
        comboCampeonatoResultado.setPreferredSize(new Dimension(260, 34));
        comboCampeonatoResultado.addActionListener(e -> atualizarPartidasResultado());
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboCampeonatoResultado, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Partida:"), gbc);
        comboPartidaResultado = new JComboBox<>();
        comboPartidaResultado.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(comboPartidaResultado, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Gols mandante:"), gbc);
        campoGolsMandante = new JTextField();
        campoGolsMandante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoGolsMandante, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(10, 0, 2, 16);
        painel.add(new JLabel("Gols visitante:"), gbc);
        campoGolsVisitante = new JTextField();
        campoGolsVisitante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0);
        painel.add(campoGolsVisitante, gbc);

        JButton botao = criarBotaoAcao("Registrar Resultado");
        botao.addActionListener(e -> registrarResultado());
        gbc.gridx = 1; gbc.gridy = 4; gbc.insets = new Insets(20, 0, 0, 0);
        painel.add(botao, gbc);

        return painel;
    }

    private void criarCampeonato() {
        String nome = campoCampeonatoNome.getText().trim();
        String anoStr = campoCampeonatoAno.getText().trim();
        try {
            int ano = Integer.parseInt(anoStr);
            boolean criou = campeonatoController.criarCampeonato(nome, ano);
            if (criou) {
                JOptionPane.showMessageDialog(mainFrame, "Campeonato criado com sucesso!");
                campoCampeonatoNome.setText("");
                campoCampeonatoAno.setText("");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Erro ao criar campeonato!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Ano inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarClube() {
        String nome = campoClubeNome.getText().trim();
        String sigla = campoClubeSigla.getText().trim();
        String nomeCampeonato = (String) comboCampeonatoClube.getSelectedItem();
        Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
        if (campeonato == null) return;

        boolean cadastrou = campeonatoController.cadastrarClube(nome, sigla);
        if (!cadastrou) {
            JOptionPane.showMessageDialog(mainFrame, "Erro ao cadastrar clube!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Clube clube = campeonatoController.buscarClubeSigla(sigla);
        boolean adicionou = campeonatoController.adicionarClubeCampeonato(campeonato, clube);
        if (adicionou) {
            JOptionPane.showMessageDialog(mainFrame, "Clube cadastrado com sucesso!");
            campoClubeNome.setText("");
            campoClubeSigla.setText("");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Limite de 8 clubes atingido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cadastrarPartida() {
        String nomeCampeonato = (String) comboCampeonatoPartida.getSelectedItem();
        String nomeMandante = (String) comboMandante.getSelectedItem();
        String nomeVisitante = (String) comboVisitante.getSelectedItem();
        String dataStr = campoData.getText().trim();
        String horaStr = campoHora.getText().trim();

        if (nomeMandante.equals(nomeVisitante)) {
            JOptionPane.showMessageDialog(mainFrame, "Selecione times diferentes!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String[] pd = dataStr.split("/");
            LocalDate data = LocalDate.of(Integer.parseInt(pd[2]), Integer.parseInt(pd[1]), Integer.parseInt(pd[0]));
            String[] ph = horaStr.split(":");
            LocalTime hora = LocalTime.of(Integer.parseInt(ph[0]), Integer.parseInt(ph[1]));

            Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
            Clube mandante = campeonatoController.buscarClubeSigla(nomeMandante);
            Clube visitante = campeonatoController.buscarClubeSigla(nomeVisitante);

            boolean cadastrou = partidaController.cadastrarPartida(campeonato, mandante, visitante, data, hora);
            if (cadastrou) {
                JOptionPane.showMessageDialog(mainFrame, "Partida cadastrada com sucesso!");
                campoData.setText("");
                campoHora.setText("");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Erro ao cadastrar partida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Data ou hora inválida!\nUse dd/MM/yyyy e HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarResultado() {
        String nomePartida = (String) comboPartidaResultado.getSelectedItem();
        String nomeCampeonato = (String) comboCampeonatoResultado.getSelectedItem();
        String golsMStr = campoGolsMandante.getText().trim();
        String golsVStr = campoGolsVisitante.getText().trim();

        try {
            int golsM = Integer.parseInt(golsMStr);
            int golsV = Integer.parseInt(golsVStr);

            Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
            if (campeonato == null) return;

            Model.Partida partida = null;
            for (Model.Partida p : campeonato.getPartidas()) {
                if (p.toString().equals(nomePartida)) {
                    partida = p;
                    break;
                }
            }
            if (partida == null) return;

            boolean registrou = partidaController.registrarResultado(partida, golsM, golsV);
            if (registrou) {
                mainFrame.getApostaController().calcularPontuacoes(campeonato);
                JOptionPane.showMessageDialog(mainFrame, "Resultado registrado! Pontuações calculadas.");
                campoGolsMandante.setText("");
                campoGolsVisitante.setText("");
                atualizarPartidasResultado();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Erro ao registrar resultado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Gols inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarComboCampeonatos(JComboBox<String> combo) {
        combo.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos()) {
            combo.addItem(c.getNome());
        }
    }

    private void atualizarClubesPartida() {
        comboMandante.removeAllItems();
        comboVisitante.removeAllItems();
        String nomeCampeonato = (String) comboCampeonatoPartida.getSelectedItem();
        if (nomeCampeonato == null) return;
        Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
        if (campeonato == null) return;
        for (Clube c : campeonato.getClubes()) {
            comboMandante.addItem(c.getSigla());
            comboVisitante.addItem(c.getSigla());
        }
    }

    private void atualizarPartidasResultado() {
        comboPartidaResultado.removeAllItems();
        String nomeCampeonato = (String) comboCampeonatoResultado.getSelectedItem();
        if (nomeCampeonato == null) return;
        Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
        if (campeonato == null) return;
        List<Model.Partida> pendentes = partidaController.getPartidasPendentes(campeonato);
        for (Model.Partida p : pendentes) {
            comboPartidaResultado.addItem(p.toString());
        }
    }
}