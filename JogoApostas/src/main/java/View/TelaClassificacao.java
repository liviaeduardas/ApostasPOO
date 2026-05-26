package View;
import Controller.GrupoController;
import Model.Grupo;
import Model.Participante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TelaClassificacao extends JPanel {

    private MainFrame mainFrame;
    private GrupoController grupoController;

    private JComboBox<String> comboGrupos;
    private JTable tabelaRanking;
    private javax.swing.table.DefaultTableModel modeloTabela;
    private JLabel labelInfo;

    private static final Color VERMELHO = new Color(0x95, 0x0E, 0x17);
    private static final Color VERMELHO_ESC = new Color(0x70, 0x0A, 0x11);
    private static final Color FUNDO = new Color(0xFF, 0xF5, 0xF5);

    public TelaClassificacao(MainFrame mainFrame, GrupoController grupoController) {
        this.mainFrame = mainFrame;
        this.grupoController = grupoController;
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

        JLabel sub = new JLabel("Classificação", SwingConstants.CENTER);
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

        JButton btnAtualizar = criarBotaoMenu("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        lateral.add(btnAtualizar);
        lateral.add(Box.createVerticalStrut(4));

        lateral.add(Box.createVerticalGlue());

        JButton botaoVoltar = criarBotaoMenu("Voltar");
        botaoVoltar.setForeground(new Color(0xFF, 0xCC, 0xCC));
        botaoVoltar.addActionListener(e -> mainFrame.trocarTela("telaApostas"));
        lateral.add(botaoVoltar);
        lateral.add(Box.createVerticalStrut(10));

        add(lateral, BorderLayout.WEST);

        JPanel direita = new JPanel(new BorderLayout(0, 16));
        direita.setBackground(FUNDO);
        direita.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("Classificação do Grupo");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(VERMELHO);
        direita.add(titulo, BorderLayout.NORTH);

        JPanel painelGrupo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelGrupo.setBackground(FUNDO);
        painelGrupo.add(new JLabel("Grupo: "));
        comboGrupos = new JComboBox<>();
        comboGrupos.setFont(new Font("Arial", Font.PLAIN, 13));
        comboGrupos.setPreferredSize(new Dimension(200, 32));
        comboGrupos.addActionListener(e -> atualizarRanking());
        painelGrupo.add(comboGrupos);

        String[] colunas = {"Posição", "Participante", "Pontos"};
        modeloTabela = new javax.swing.table.DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelaRanking = new JTable(modeloTabela);
        tabelaRanking.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaRanking.setRowHeight(32);
        tabelaRanking.setForeground(Color.BLACK);
        tabelaRanking.setBackground(FUNDO);
        tabelaRanking.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabelaRanking.getTableHeader().setBackground(VERMELHO);
        tabelaRanking.getTableHeader().setForeground(VERMELHO_ESC);
        tabelaRanking.setSelectionBackground(new Color(0xFF, 0xDD, 0xDD));
        tabelaRanking.setGridColor(new Color(0xEE, 0xEE, 0xEE));

        javax.swing.table.DefaultTableCellRenderer centro = new javax.swing.table.DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        centro.setForeground(Color.BLACK);
        centro.setBackground(FUNDO);
        for (int i = 0; i < colunas.length; i++) {
            tabelaRanking.getColumnModel().getColumn(i).setCellRenderer(centro);
        }

        JScrollPane scroll = new JScrollPane(tabelaRanking);
        scroll.getViewport().setBackground(FUNDO);

        JPanel painelCentro = new JPanel(new BorderLayout(0, 10));
        painelCentro.setBackground(FUNDO);
        painelCentro.add(painelGrupo, BorderLayout.NORTH);
        painelCentro.add(scroll, BorderLayout.CENTER);

        labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        labelInfo.setForeground(VERMELHO);
        painelCentro.add(labelInfo, BorderLayout.SOUTH);

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

    private void atualizarRanking() {
        modeloTabela.setRowCount(0);
        String nomeGrupo = (String) comboGrupos.getSelectedItem();
        if (nomeGrupo == null) return;

        Grupo grupo = grupoController.buscarNome(nomeGrupo);
        if (grupo == null) return;

        ArrayList<Participante> ranking = grupoController.getRanking(grupo);

        for (int i = 0; i < ranking.size(); i++) {
            Participante p = ranking.get(i);
            modeloTabela.addRow(new Object[]{(i + 1) + "º", p.getNome(), p.getPontosTotal() + " pts"});
        }
        labelInfo.setText("Total: " + ranking.size() + " participante(s)");
    }

    public void atualizar() {
        comboGrupos.removeAllItems();
        for (Grupo g : grupoController.getGrupos()) {
            comboGrupos.addItem(g.getNome());
        }
        atualizarRanking();
    }
}