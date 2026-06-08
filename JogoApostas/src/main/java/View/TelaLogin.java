package View;
import Controller.UsuarioController;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaLogin extends JPanel {
    private MainFrame mainFrame;
    private UsuarioController usuarioController;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;

    public TelaLogin(MainFrame mainFrame, UsuarioController usuarioController) {
        this.mainFrame = mainFrame;
        this.usuarioController = usuarioController;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 6, 20, 6);
        add(titulo, gbc);

        // Label Usuário
        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Usuário:"), gbc);

        // Campo Usuário
        campoUsuario = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 1;
        add(campoUsuario, gbc);

        // Label Senha
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);

        // Campo Senha
        campoSenha = new JPasswordField(18);
        gbc.gridx = 1; gbc.gridy = 2;
        add(campoSenha, gbc);

        // Botão Entrar (centralizado, ocupa as 2 colunas)
        JButton entrar = new JButton("Entrar");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 40, 4, 40);
        add(entrar, gbc);
        entrar.addActionListener(e -> login());

        // Permite login com Enter
        campoSenha.addActionListener(e -> login());
    }

    private void login() {
        String usuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());
        Usuario u = usuarioController.autenticar(usuario, senha);

        if (u == null) {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!");
            return;
        }

        if (u instanceof Administrador) {
            mainFrame.setAdminLogado((Administrador) u);
            mainFrame.trocarTela("telaCadastro");
        }

        if (u instanceof Participante) {
            Participante participante = (Participante) u;
            if (participante.getNome() == null) {
                cadastrarParticipante(participante);
            } else {
                mainFrame.setParticipanteLogado(participante);
                mainFrame.trocarTela("telaApostas");
            }
        }
    }

    private void cadastrarParticipante(Participante participante) {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        if (nome == null || nome.isBlank()) return;
        Participante existente = usuarioController.buscarNome(nome);
        if (existente != null) {
            mainFrame.setParticipanteLogado(existente);
            mainFrame.trocarTela("telaApostas");
            return;
        }

        usuarioController.cadastrar(participante, nome);

        ArrayList<Grupo> grupos = mainFrame.getGrupoController().getGrupos();
        if (!grupos.isEmpty()) {
            String[] nomes = new String[grupos.size()];
            for (int i = 0; i < grupos.size(); i++) {
                nomes[i] = grupos.get(i).getNome();
            }

            String escolhido = (String) JOptionPane.showInputDialog(
                    this, "Escolha um grupo:", "Grupo",
                    JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]
            );

            if (escolhido != null) {
                Grupo grupo = mainFrame.getGrupoController().buscarNome(escolhido);
                mainFrame.getGrupoController().addParticipante(grupo, participante);
            }
        }

        mainFrame.setParticipanteLogado(participante);
        mainFrame.trocarTela("telaApostas");
    }
}