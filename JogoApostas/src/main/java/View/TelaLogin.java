package View;
import Controller.LoginController;
import Model.Administrador;
import Model.Participante;
import Model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JPanel {
    private MainFrame mainFrame;
    private LoginController loginController;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JLabel labelErro;

    private static final Color VERMELHO = new Color(0x95, 0x0E, 0x17);
    private static final Color FUNDO = new Color(0xFF, 0xF5, 0xF5);

    public TelaLogin(MainFrame mainFrame, LoginController loginController) {
        this.mainFrame = mainFrame;
        this.loginController = loginController;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setBackground(VERMELHO);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(FUNDO);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        card.add(new JLabel("Usuário:"), gbc);

        campoUsuario = new JTextField(16);
        gbc.gridx = 1;
        card.add(campoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(16);
        gbc.gridx = 1;
        card.add(campoSenha, gbc);

        labelErro = new JLabel("", SwingConstants.CENTER);
        labelErro.setForeground(VERMELHO);
        labelErro.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        card.add(labelErro, gbc);

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBackground(VERMELHO);
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoEntrar.setOpaque(true);
        gbc.gridy = 4;
        card.add(botaoEntrar, gbc);

        botaoEntrar.addActionListener(e -> realizarLogin());
        campoSenha.addActionListener(e -> realizarLogin());

        add(card);
    }

    private void realizarLogin() {
        String login = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();
        Usuario usuario = loginController.autenticar(login, senha);

        if (usuario == null) {
            labelErro.setText("Usuário ou senha incorretos!");
            campoSenha.setText("");
            return;
        }

        labelErro.setText("");
        campoUsuario.setText("");
        campoSenha.setText("");

        if (usuario instanceof Administrador) {
            mainFrame.setAdminLogado((Administrador) usuario);
            mainFrame.trocarTela("telaCadastro");

        } else if (usuario instanceof Participante) {
            Participante participante = (Participante) usuario;

            if (participante.getNome() == null || participante.getNome().isEmpty()) {
                pedirNome(participante);
            } else {
                mainFrame.setParticipanteLogado(participante);
                mainFrame.trocarTela("telaApostas");
            }
        }
    }

    private void pedirNome(Participante participante) {
        String nome = JOptionPane.showInputDialog(
                mainFrame,
                "Digite seu nome:",
                "Cadastro",
                JOptionPane.PLAIN_MESSAGE
        );

        if (nome == null || nome.trim().isEmpty()) {
            labelErro.setText("Nome obrigatório para continuar!");
            return;
        }

        Participante existente = loginController.buscarNome(nome);
        if (existente != null) {
            mainFrame.setParticipanteLogado(existente);
            mainFrame.trocarTela("telaApostas");
            return;
        }

        boolean cadastrou = loginController.cadastrar(participante, nome);
        if (!cadastrou) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Erro ao cadastrar participante!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        escolherGrupo(participante);
    }

    private void escolherGrupo(Participante participante) {
        java.util.ArrayList<Model.Grupo> grupos = mainFrame.getGrupoController().getGrupos();

        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Nenhum grupo disponível no momento.\nVocê pode entrar em um grupo depois.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            mainFrame.setParticipanteLogado(participante);
            mainFrame.trocarTela("telaApostas");
            return;
        }

        String[] nomesGrupos = grupos.stream()
                .map(Model.Grupo::getNome)
                .toArray(String[]::new);

        String grupoEscolhido = (String) JOptionPane.showInputDialog(
                mainFrame,
                "Escolha seu grupo:",
                "Entrar em um grupo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomesGrupos,
                nomesGrupos[0]
        );

        if (grupoEscolhido != null) {
            Model.Grupo grupo = mainFrame.getGrupoController().buscarNome(grupoEscolhido);
            boolean entrou = mainFrame.getGrupoController().adicionarParticipante(grupo, participante);
            if (entrou) {
                JOptionPane.showMessageDialog(mainFrame, "Você entrou no grupo " + grupoEscolhido + "!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Grupo cheio!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        mainFrame.setParticipanteLogado(participante);
        mainFrame.trocarTela("telaApostas");
    }
}
