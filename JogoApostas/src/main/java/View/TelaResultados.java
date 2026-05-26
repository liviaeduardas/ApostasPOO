package View;
import Controller.ApostaController;
import Controller.CampeonatoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaResultados extends JPanel {

    private MainFrame mainFrame;
    private PartidaController partidaController;
    private CampeonatoController campeonatoController;
    private ApostaController apostaController;

    private JComboBox<String> comboCampeonato;
    private JPanel painelConteudo;
    private CardLayout cardLayout;

    private javax.swing.table.DefaultTableModel modeloPendentes;
    private javax.swing.table.DefaultTableModel modeloFinalizadas;
    private JTable tabelaPendentes;
    private JTable tabelaFinalizadas;

    private static final Color VERMELHO = new Color(0x95, 0x0E, 0x17);
    private static final Color VERMELHO_ESC = new Color(0x70, 0x0A, 0x11);
    private static final Color FUNDO = new Color(0xFF, 0xF5, 0xF5);

    public TelaResultados(MainFrame mainFrame, PartidaController partidaController, CampeonatoController campeonatoController, ApostaController apostaController) {
        this.mainFrame = mainFrame;
        this.partidaController = partidaController;
        this.campeonatoController = campeonatoController;
        this.apostaController = apostaController;
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

        JLabel sub = new JLabel("Resultados", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(0xFF, 0xCC, 0xCC));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sub);

        lateral.add(Box.createVerticalStrut(30));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(140, 1));
        sep.setForeground(new Color(0xAA, 0x33, 0x33));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sep);

        lateral.add(Box.createVerticalStrut(20));

        JButton btnPendentes = criarBotaoMenu("Aguardando");
        btnPendentes.addActionListener(e -> cardLayout.show(painelConteudo, "pendentes"));
        lateral.add(btnPendentes);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnFinalizadas = criarBotaoMenu("Finalizadas");
        btnFinalizadas.addActionListener(e -> cardLayout.show(painelConteudo, "finalizadas"));
        lateral.add(btnFinalizadas);
        lateral.add(Box.createVerticalStrut(4));

        JButton btnAtualizar = criarBotaoMenu("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        lateral.add(btnAtualizar);

        lateral.add(Box.createVerticalGlue());

        JButton botaoVoltar = criarBotaoMenu("Voltar");
        botaoVoltar.setForeground(new Color(0xFF, 0xCC, 0xCC));
        botaoVoltar.addActionListener(e -> mainFrame.trocarTela("telaApostas"));
        lateral.add(botaoVoltar);
        lateral.add(Box.createVerticalStrut(10));

        add(lateral, BorderLayout.WEST);

        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(FUNDO);
        direita.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("Resultados das Partidas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(VERMELHO);
        direita.add(titulo, BorderLayout.NORTH);

        JPanel painelGrupo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        painelGrupo.setBackground(FUNDO);
        painelGrupo.add(new JLabel("Campeonato: "));
        comboCampeonato = new JComboBox<>();
        comboCampeonato.setFont(new Font("Arial", Font.PLAIN, 13));
        comboCampeonato.setPreferredSize(new Dimension(200, 32));
        comboCampeonato.addActionListener(e -> atualizarTabelas());
        painelGrupo.add(comboCampeonato);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(FUNDO);

        String[] colunasPendentes = {"Mandante", "Visitante", "Data", "Hora"};
        modeloPendentes = new javax.swing.table.DefaultTableModel(colunasPendentes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaPendentes = new JTable(modeloPendentes);
        tabelaPendentes.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelaPendentes.setRowHeight(28);
        tabelaPendentes.setForeground(Color.BLACK);
        tabelaPendentes.setBackground(FUNDO);
        tabelaPendentes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabelaPendentes.getTableHeader().setBackground(VERMELHO);
        tabelaPendentes.getTableHeader().setForeground(VERMELHO_ESC);
        tabelaPendentes.setGridColor(new Color(0xEE, 0xEE, 0xEE));

        javax.swing.table.DefaultTableCellRenderer centroPendentes = new javax.swing.table.DefaultTableCellRenderer();
        centroPendentes.setHorizontalAlignment(SwingConstants.CENTER);
        centroPendentes.setForeground(Color.BLACK);
        centroPendentes.setBackground(FUNDO);
        for (int i = 0; i < colunasPendentes.length; i++) {
            tabelaPendentes.getColumnModel().getColumn(i).setCellRenderer(centroPendentes);
        }
        JScrollPane scrollPendentes = new JScrollPane(tabelaPendentes);
        scrollPendentes.getViewport().setBackground(FUNDO);
        painelConteudo.add(scrollPendentes, "pendentes");

        String[] colunasFinalizadas = {"Mandante", "Visitante", "Placar", "Resultado"};
        modeloFinalizadas = new javax.swing.table.DefaultTableModel(colunasFinalizadas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaFinalizadas = new JTable(modeloFinalizadas);
        tabelaFinalizadas.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelaFinalizadas.setRowHeight(28);
        tabelaFinalizadas.setForeground(Color.BLACK);
        tabelaFinalizadas.setBackground(FUNDO);
        tabelaFinalizadas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabelaFinalizadas.getTableHeader().setBackground(VERMELHO);
        tabelaFinalizadas.getTableHeader().setForeground(VERMELHO_ESC);
        tabelaFinalizadas.setGridColor(new Color(0xEE, 0xEE, 0xEE));

        javax.swing.table.DefaultTableCellRenderer centroFinalizadas = new javax.swing.table.DefaultTableCellRenderer();
        centroFinalizadas.setHorizontalAlignment(SwingConstants.CENTER);
        centroFinalizadas.setForeground(Color.BLACK);
        centroFinalizadas.setBackground(FUNDO);
        for (int i = 0; i < colunasFinalizadas.length; i++) {
            tabelaFinalizadas.getColumnModel().getColumn(i).setCellRenderer(centroFinalizadas);
        }
        JScrollPane scrollFinalizadas = new JScrollPane(tabelaFinalizadas);
        scrollFinalizadas.getViewport().setBackground(FUNDO);
        painelConteudo.add(scrollFinalizadas, "finalizadas");

        JPanel painelCentro = new JPanel(new BorderLayout(0, 10));
        painelCentro.setBackground(FUNDO);
        painelCentro.add(painelGrupo, BorderLayout.NORTH);
        painelCentro.add(painelConteudo, BorderLayout.CENTER);

        direita.add(painelCentro, BorderLayout.CENTER);
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

    private void atualizarTabelas() {
        modeloPendentes.setRowCount(0);
        modeloFinalizadas.setRowCount(0);

        String nomeCampeonato = (String) comboCampeonato.getSelectedItem();
        if (nomeCampeonato == null) return;

        Campeonato campeonato = campeonatoController.buscarNome(nomeCampeonato);
        if (campeonato == null) return;

        List<Partida> pendentes = partidaController.getPartidasPendentes(campeonato);
        for (Partida p : pendentes) {
            modeloPendentes.addRow(new Object[]{
                    p.getClubeMandante().getNome(),
                    p.getClubeVisitante().getNome(),
                    p.getData().toString(),
                    p.getHora().toString()
            });
        }

        List<Partida> finalizadas = partidaController.getPartidasFinalizadas(campeonato);
        for (Partida p : finalizadas) {
            String placar = p.getGolMandante() + " x " + p.getGolVisitante();
            modeloFinalizadas.addRow(new Object[]{
                    p.getClubeMandante().getNome(),
                    p.getClubeVisitante().getNome(),
                    placar,
                    p.getResultado()
            });
        }
    }

    public void atualizar() {
        comboCampeonato.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos()) {
            comboCampeonato.addItem(c.getNome());
        }
        atualizarTabelas();
    }
}