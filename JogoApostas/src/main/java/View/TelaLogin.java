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

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 6, 4, 6);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Usuário:"), gbc);

        campoUsuario = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 1;
        add(campoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(18);
        gbc.gridx = 1; gbc.gridy = 2;
        add(campoSenha, gbc);

        // Botão Entrar
        JButton entrar = new JButton("Entrar");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 40, 4, 40);
        add(entrar, gbc);
        entrar.addActionListener(e -> login());

        // Botão Cadastrar
        JButton cadastrar = new JButton("Cadastrar");
        gbc.gridy = 4;
        gbc.insets = new Insets(4, 40, 4, 40);
        add(cadastrar, gbc);
        cadastrar.addActionListener(e -> cadastrarNovoUsuario());

        campoSenha.addActionListener(e -> login());
    }

    private void login() {
        String usuario = campoUsuario.getText();
        String senha   = new String(campoSenha.getPassword());
        Usuario u = usuarioController.autenticar(usuario, senha);

        if (u == null) {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!");
            return;
        }

        if (u instanceof Administrador) {
            mainFrame.setAdminLogado((Administrador) u);
            mainFrame.trocarTela("telaCadastro");
            return;
        }

        if (u instanceof Participante) {
            Participante participante = (Participante) u;
            mainFrame.setParticipanteLogado(participante);
            escolherGrupoSeNecessario(participante);
            mainFrame.trocarTela("telaApostas");
        }
    }


    private void cadastrarNovoUsuario() {
        JTextField fNome    = new JTextField();
        JTextField fUsuario = new JTextField();
        JPasswordField fSenha = new JPasswordField();

        Object[] campos = {
                "Nome:",    fNome,
                "Usuário:", fUsuario,
                "Senha:",   fSenha
        };

        int r = JOptionPane.showConfirmDialog(this, campos, "Novo cadastro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) return;

        String nome    = fNome.getText().trim();
        String usuario = fUsuario.getText().trim();
        String senha   = new String(fSenha.getPassword()).trim();

        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        boolean ok = usuarioController.cadastrar(nome, usuario, senha);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Usuário já existe ou dados inválidos.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Cadastro realizado! Faça o login.");
    }

    private void escolherGrupoSeNecessario(Participante participante) {
        ArrayList<Grupo> grupos = (ArrayList<Grupo>) mainFrame.getGrupoController().getGrupos();
        if (grupos == null || grupos.isEmpty()) return;

        for (Grupo g : grupos) {
            if (g.getParticipantes().contains(participante)) return;
        }

        String[] nomes = new String[grupos.size()];
        for (int i = 0; i < grupos.size(); i++) nomes[i] = grupos.get(i).getNome();

        String escolhido = (String) JOptionPane.showInputDialog(
                this, "Escolha um grupo:", "Grupo",
                JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);

        if (escolhido != null) {
            Grupo grupo = mainFrame.getGrupoController().buscarNome(escolhido);
            mainFrame.getGrupoController().addParticipante(grupo, participante);
        }
    }
}
