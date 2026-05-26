package View;
import Controller.ApostaController;
import Controller.CampeonatoController;
import Model.Campeonato;
import Model.Partida;
import Model.Participante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaApostas extends JPanel {

    private MainFrame mainFrame;
    private ApostaController apostaController;
    private CampeonatoController campeonatoController;

    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JTextField campoGolsMandante;
    private JTextField campoGolsVisitante;
    private JLabel labelBemVindo;
    private JLabel labelInfo;

    private static final Color VERMELHO = new Color(0x95, 0x0E, 0x17);
    private static final Color VERMELHO_ESC = new Color(0x70, 0x0A, 0x11);
    private static final Color FUNDO = new Color(0xFF, 0xF5, 0xF5);

    public TelaApostas(MainFrame mainFrame, ApostaController apostaController, CampeonatoController campeonatoController) {
        this.mainFrame = mainFrame;
        this.apostaController = apostaController;
        this.campeonatoController = campeonatoController;
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

        labelBemVindo = new JLabel("Participante", SwingConstants.CENTER);
        labelBemVindo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelBemVindo.setForeground(new Color(0xFF, 0xCC, 0xCC));
        labelBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(labelBemVindo);

        lateral.add(Box.createVerticalStrut(30));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(140, 1));
        sep.setForeground(new Color(0xAA, 0x33, 0x33));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sep);

        lateral.add(Box.createVerticalStrut(20));

        JButton btnApostas = criarBotaoMenu("Minhas Apostas");
        btnApostas.addActionListener(e -> verMinhasApostas());
        lateral.add(btnApostas);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnGrupos = criarBotaoMenu("Grupos");
        btnGrupos.addActionListener(e -> abrirDialogGrupos());
        lateral.add(btnGrupos);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnClassificacao = criarBotaoMenu("Classificação");
        btnClassificacao.addActionListener(e -> mainFrame.trocarTela("telaClassificacao"));
        lateral.add(btnClassificacao);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnResultados = criarBotaoMenu("Resultados");
        btnResultados.addActionListener(e -> mainFrame.trocarTela("telaResultados"));
        lateral.add(btnResultados);

        lateral.add(Box.createVerticalGlue());

        JButton botaoSair = criarBotaoMenu("Sair");
        botaoSair.setForeground(new Color(0xFF, 0xCC, 0xCC));
        botaoSair.addActionListener(e -> mainFrame.trocarTela("telaLogin"));
        lateral.add(botaoSair);
        lateral.add(Box.createVerticalStrut(10));

        add(lateral, BorderLayout.WEST);

        JPanel direita = new JPanel(new GridBagLayout());
        direita.setBackground(FUNDO);
        direita.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Registrar Aposta");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(VERMELHO);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 24, 0);
        direita.add(titulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 16); gbc.fill = GridBagConstraints.NONE;
        direita.add(new JLabel("Campeonato:"), gbc);
        comboCampeonato = new JComboBox<>();
        comboCampeonato.setPreferredSize(new Dimension(260, 34));
        comboCampeonato.addActionListener(e -> atualizarPartidas());
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0); gbc.fill = GridBagConstraints.HORIZONTAL;
        direita.add(comboCampeonato, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(10, 0, 2, 16); gbc.fill = GridBagConstraints.NONE;
        direita.add(new JLabel("Partida:"), gbc);
        comboPartida = new JComboBox<>();
        comboPartida.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0); gbc.fill = GridBagConstraints.HORIZONTAL;
        direita.add(comboPartida, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(10, 0, 2, 16); gbc.fill = GridBagConstraints.NONE;
        direita.add(new JLabel("Gols mandante:"), gbc);
        campoGolsMandante = new JTextField();
        campoGolsMandante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0); gbc.fill = GridBagConstraints.HORIZONTAL;
        direita.add(campoGolsMandante, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(10, 0, 2, 16); gbc.fill = GridBagConstraints.NONE;
        direita.add(new JLabel("Gols visitante:"), gbc);
        campoGolsVisitante = new JTextField();
        campoGolsVisitante.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1; gbc.insets = new Insets(10, 0, 2, 0); gbc.fill = GridBagConstraints.HORIZONTAL;
        direita.add(campoGolsVisitante, gbc);

        labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        labelInfo.setForeground(new Color(0x99, 0x00, 0x00));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 8, 0);
        direita.add(labelInfo, gbc);

        JButton botaoApostar = criarBotaoAcao("Registrar Aposta");
        botaoApostar.addActionListener(e -> registrarAposta());
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        direita.add(botaoApostar, gbc);

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

    private void registrarAposta() {
        Participante participante = mainFrame.getParticipanteLogado();
        if (participante == null) return;

        String nomePartida = (String) comboPartida.getSelectedItem();
        String golsMStr = campoGolsMandante.getText().trim();
        String golsVStr = campoGolsVisitante.getText().trim();

        if (nomePartida == null || golsMStr.isEmpty() || golsVStr.isEmpty()) {
            labelInfo.setText("Preencha todos os campos!");
            return;
        }

        try {
            int golsM = Integer.parseInt(golsMStr);
            int golsV = Integer.parseInt(golsVStr);

            Partida partida = buscarPartidaPorString(nomePartida);
            if (partida == null) return;

            boolean apostou = apostaController.registrarAposta(participante, partida, golsM, golsV);

            if (apostou) {
                JOptionPane.showMessageDialog(mainFrame, "Aposta registrada! Palpite: " + golsM + " x " + golsV);
                campoGolsMandante.setText("");
                campoGolsVisitante.setText("");
                labelInfo.setText(" ");
            } else {
                labelInfo.setText("Não foi possível registrar a aposta!");
            }

        } catch (NumberFormatException ex) {
            labelInfo.setText("Digite apenas números nos gols!");
        }
    }

    private void verMinhasApostas() {
        Participante participante = mainFrame.getParticipanteLogado();
        if (participante == null) return;

        List<Model.Aposta> apostas = apostaController.getApostasPorParticipante(participante);

        if (apostas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Você ainda não fez nenhuma aposta!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Model.Aposta a : apostas) {
            sb.append("Partida: ").append(a.getPartida().toString()).append("\n");
            sb.append("Palpite: ").append(a.getGolsMandantePalpite()).append(" x ").append(a.getGolsVisitantePalpite()).append("\n");
            sb.append("Pontos: ").append(a.getPontuacaoObtida()).append("\n");
            sb.append("---------------------\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(380, 260));
        JOptionPane.showMessageDialog(mainFrame, scroll, "Minhas Apostas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirDialogGrupos() {
        String[] opcoes = {"Criar novo grupo", "Entrar em grupo existente"};
        int escolha = JOptionPane.showOptionDialog(mainFrame,
                "O que deseja fazer?", "Grupos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);

        if (escolha == 0) criarGrupo();
        else if (escolha == 1) entrarEmGrupo();
    }

    private void criarGrupo() {
        Participante participante = mainFrame.getParticipanteLogado();
        if (participante == null) return;

        String nome = JOptionPane.showInputDialog(mainFrame, "Nome do novo grupo:");
        if (nome == null || nome.trim().isEmpty()) return;

        boolean criou = mainFrame.getGrupoController().criarGrupoPorParticipante(nome.trim(), participante);
        if (criou) {
            Model.Grupo grupo = mainFrame.getGrupoController().buscarNome(nome.trim());
            mainFrame.getGrupoController().adicionarParticipante(grupo, participante);
            JOptionPane.showMessageDialog(mainFrame, "Grupo criado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Limite de 5 grupos atingido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void entrarEmGrupo() {
        Participante participante = mainFrame.getParticipanteLogado();
        if (participante == null) return;

        ArrayList<Model.Grupo> grupos = mainFrame.getGrupoController().getGrupos();
        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhum grupo disponível!");
            return;
        }

        String[] nomes = new String[grupos.size()];
        for (int i = 0; i < grupos.size(); i++) {
            nomes[i] = grupos.get(i).getNome();
        }

        String escolhido = (String) JOptionPane.showInputDialog(mainFrame,
                "Escolha o grupo:", "Entrar em Grupo",
                JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);

        if (escolhido == null) return;

        Model.Grupo grupo = mainFrame.getGrupoController().buscarNome(escolhido);
        boolean entrou = mainFrame.getGrupoController().adicionarParticipante(grupo, participante);

        if (entrou) {
            JOptionPane.showMessageDialog(mainFrame, "Você entrou no grupo!");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Não foi possível entrar no grupo!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarPartidas() {
        comboPartida.removeAllItems();
        String nomeCampeonato = (String) comboCampeonato.getSelectedItem();
        if (nomeCampeonato == null) return;
        Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
        if (campeonato == null) return;
        for (Partida p : campeonato.getPartidasPendentes()) {
            comboPartida.addItem(p.toString());
        }
    }

    private Partida buscarPartidaPorString(String texto) {
        for (Campeonato c : campeonatoController.getCampeonatos()) {
            for (Partida p : c.getPartidas()) {
                if (p.toString().equals(texto)) return p;
            }
        }
        return null;
    }

    public void atualizar() {
        Participante participante = mainFrame.getParticipanteLogado();
        if (participante != null) {
            labelBemVindo.setText(participante.getNome());
        }
        comboCampeonato.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos()) {
            comboCampeonato.addItem(c.getNome());
        }
        atualizarPartidas();
        labelInfo.setText(" ");

        campoGolsMandante.setText("");
        campoGolsVisitante.setText("");
    }
}