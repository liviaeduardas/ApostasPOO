package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Classe base que todas as telas herdam.
 * Centraliza os métodos repetidos: criar botões, labels, separadores e mensagens.
 * Isso evita duplicar o mesmo código em cada tela.
 */
public abstract class TelaBase extends JPanel {

    protected static final Color VERMELHO     = new Color(0x95, 0x0E, 0x17);
    protected static final Color VERMELHO_ESC = new Color(0x70, 0x0A, 0x11);
    protected static final Color FUNDO        = new Color(0xFF, 0xF5, 0xF5);

    // Cria um botão para o menu lateral
    protected JButton botaoMenu(String texto, ActionListener acao) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.PLAIN, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(VERMELHO);
        b.setMaximumSize(new Dimension(180, 36));
        b.setPreferredSize(new Dimension(180, 36));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.addActionListener(acao);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(VERMELHO_ESC); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(VERMELHO); }
        });
        return b;
    }

    // Cria um botão de ação (ex: "Salvar", "Registrar")
    protected JButton botaoAcao(String texto, ActionListener acao) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(VERMELHO);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(200, 36));
        b.addActionListener(acao);
        return b;
    }

    // Cria o painel lateral vermelho com título e subtítulo
    protected JPanel painelLateral(String titulo, String subtitulo) {
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(VERMELHO);
        lateral.setPreferredSize(new Dimension(180, 0));
        lateral.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(lblTitulo);

        JLabel lblSub = new JLabel(subtitulo, SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(0xFF, 0xCC, 0xCC));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(lblSub);

        lateral.add(Box.createVerticalStrut(20));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(140, 1));
        sep.setForeground(new Color(0xAA, 0x33, 0x33));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(sep);
        lateral.add(Box.createVerticalStrut(20));

        return lateral;
    }

    // Cria um campo de texto com label — retorna o JTextField
    protected JTextField addCampo(JPanel painel, GridBagConstraints gbc, String label, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.fill = GridBagConstraints.NONE;
        painel.add(new JLabel(label), gbc);

        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 2, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gbc);
        return campo;
    }

    // Cria um combo com label — retorna o JComboBox
    protected JComboBox<String> addCombo(JPanel painel, GridBagConstraints gbc, String label, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.insets = new Insets(10, 0, 2, 16);
        gbc.fill = GridBagConstraints.NONE;
        painel.add(new JLabel(label), gbc);

        JComboBox<String> combo = new JComboBox<>();
        combo.setPreferredSize(new Dimension(260, 34));
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 2, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(combo, gbc);
        return combo;
    }

    // Exibe mensagem de sucesso
    protected void msg(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    // Exibe mensagem de erro
    protected void erro(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // Exibe aviso
    protected void aviso(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}