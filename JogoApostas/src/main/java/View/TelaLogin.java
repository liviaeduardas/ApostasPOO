package View;

import Controller.UsuarioController;
import Model.Administrador;
import Model.Grupo;
import Model.Participante;
import Model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaLogin extends TelaBase {

    private MainFrame mainFrame;
    private UsuarioController usuarioController;

    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JLabel labelErro;

    public TelaLogin(MainFrame mainFrame, UsuarioController usuarioController) {
        this.mainFrame         = mainFrame;
        this.usuarioController = usuarioController;
        montar();
    }

    private void montar() {
        setBackground(VERMELHO);
        setLayout(new GridBagLayout());

        // Card branco centralizado
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(FUNDO);
        card.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Título
        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(titulo, gbc);

        // Campos de login
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

        // Mensagem de erro
        labelErro = new JLabel(" ", SwingConstants.CENTER);
        labelErro.setForeground(VERMELHO);
        labelErro.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        card.add(labelErro, gbc);

        // Botão entrar
        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBackground(VERMELHO);
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoEntrar.setOpaque(true);
        gbc.gridy = 4;
        card.add(botaoEntrar, gbc);

        botaoEntrar.addActionListener(e -> login());
        campoSenha.addActionListener(e -> login());

        add(card);
    }

    private void login() {
        String usuario = campoUsuario.getText().trim();
        String senha   = new String(campoSenha.getPassword()).trim();
        Usuario u      = usuarioController.autenticar(usuario, senha);

        if (u == null) {
            labelErro.setText("Usuário ou senha incorretos!");
            campoSenha.setText("");
            return;
        }

        campoUsuario.setText("");
        campoSenha.setText("");
        labelErro.setText(" ");

        if (u instanceof Administrador admin) {
            mainFrame.setAdminLogado(admin);
            mainFrame.trocarTela("telaCadastro");

        } else if (u instanceof Participante participante) {
            // Participante novo — ainda não tem nome
            if (participante.getNome() == null || participante.getNome().isEmpty()) {
                pedirNome(participante);
            } else {
                mainFrame.setParticipanteLogado(participante);
                mainFrame.trocarTela("telaApostas");
            }
        }
    }

    private void pedirNome(Participante participante) {
        String nome = JOptionPane.showInputDialog(mainFrame, "Digite seu nome:", "Cadastro", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) {
            labelErro.setText("Nome obrigatório!");
            return;
        }

        // Se já existe esse nome, só loga
        Participante existente = usuarioController.buscarNome(nome);
        if (existente != null) {
            mainFrame.setParticipanteLogado(existente);
            mainFrame.trocarTela("telaApostas");
            return;
        }

        usuarioController.cadastrar(participante, nome);
        escolherGrupo(participante);
    }

    private void escolherGrupo(Participante participante) {
        ArrayList<Grupo> grupos = mainFrame.getGrupoController().getGrupos();

        if (grupos.isEmpty()) {
            aviso("Nenhum grupo disponível. Você pode entrar em um depois.");
        } else {
            String[] nomes = grupos.stream().map(Grupo::getNome).toArray(String[]::new);
            String escolhido = (String) JOptionPane.showInputDialog(mainFrame,
                    "Escolha seu grupo:", "Entrar em um grupo",
                    JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);

            if (escolhido != null) {
                Grupo grupo   = mainFrame.getGrupoController().BuscarNome(escolhido);
                boolean entrou = mainFrame.getGrupoController().addParticipante(grupo, participante);
                if (!entrou) erro("Grupo cheio!");
            }
        }

        mainFrame.setParticipanteLogado(participante);
        mainFrame.trocarTela("telaApostas");
    }
}