package View;

import Controller.UsuarioController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 6, 20, 6);
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets  = new Insets(4, 6, 4, 6);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Usuário:"), gbc);
        campoUsuario = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 1; add(campoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Senha:"), gbc);
        campoSenha = new JPasswordField(18);
        gbc.gridx = 1; gbc.gridy = 2; add(campoSenha, gbc);

        JButton entrar = new JButton("Entrar");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 40, 4, 40);
        add(entrar, gbc);
        entrar.addActionListener(e -> login());

        JButton cadastrar = new JButton("Cadastrar");
        gbc.gridy = 4;
        gbc.insets = new Insets(4, 40, 4, 40);
        add(cadastrar, gbc);
        cadastrar.addActionListener(e -> cadastrarUsuario());

        campoSenha.addActionListener(e -> login());
    }

    private void login() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());
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

            boolean entrouNoGrupo = participanteEntrouEmGrupo(participante);
            if (entrouNoGrupo) {
                mainFrame.trocarTela("telaApostas");
            }
        }
    }

    private void cadastrarUsuario() {
        JTextField fNome = new JTextField();
        JTextField fUsuario = new JTextField();
        JPasswordField fSenha = new JPasswordField();

        Object[] campos = { "Nome:", fNome, "Usuário:", fUsuario, "Senha:", fSenha };

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


    private boolean participanteEntrouEmGrupo(Participante participante) {
        if (participanteEstaEmGrupo(participante)) {
            return true;
        }
        return escolheGrupo(participante);

    }


    private boolean participanteEstaEmGrupo(Participante participante) {
        for (Grupo grupo : mainFrame.getGrupoController().getGrupos()) {
            for (Participante p : grupo.getParticipantes()) {
                if (p.getId() == participante.getId()) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<Grupo> buscarGruposDisponiveis() {
        List<Grupo> comVaga = new ArrayList<>();
        for (Grupo g : mainFrame.getGrupoController().getGrupos()) {
            if (g.getParticipantes().size() < 5) {
                comVaga.add(g);
            }
        }
        return comVaga;
    }


    private boolean escolheGrupo(Participante participante) {
        List<Grupo> gruposComVaga = buscarGruposDisponiveis();

        if (gruposComVaga.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os grupos estão cheios!");
            mainFrame.setParticipanteLogado(null);
            return false;
        }

        String[] nomesGrupos = new String[gruposComVaga.size()];
        for (int i = 0; i < gruposComVaga.size(); i++) {
            nomesGrupos[i] = gruposComVaga.get(i).getNome();
        }

        String nomeEscolhido = (String) JOptionPane.showInputDialog(
                this, "Escolha um grupo para continuar:",
                "Escolha seu grupo",
                JOptionPane.PLAIN_MESSAGE, null, nomesGrupos, nomesGrupos[0]);

        if (nomeEscolhido == null) {
            JOptionPane.showMessageDialog(this, "Você precisa entrar em um grupo!");
            mainFrame.setParticipanteLogado(null);
            return false;
        }

        Grupo grupo = mainFrame.getGrupoController().buscarNome(nomeEscolhido);
        boolean ok = mainFrame.getGrupoController().addParticipante(grupo, participante);

        if (!ok) {
            JOptionPane.showMessageDialog(this, "Não foi possível entrar nesse grupo. Tente novamente.");
            mainFrame.setParticipanteLogado(null);
            return false;
        }

        return true;
    }
}