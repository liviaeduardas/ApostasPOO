package View;

import Controller.UsuarioController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaLogin extends JPanel {
    private MainFrame         mainFrame;
    private UsuarioController usuarioController;
    private JTextField        campoUsuario;
    private JPasswordField    campoSenha;

    public TelaLogin(MainFrame mainFrame, UsuarioController usuarioController) {
        this.mainFrame         = mainFrame;
        this.usuarioController = usuarioController;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 6, 20, 6);
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets    = new Insets(4, 6, 4, 6);

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
        cadastrar.addActionListener(e -> cadastrarNovoUsuario());

        campoSenha.addActionListener(e -> login());
    }

    private void login() {
        String usuario = campoUsuario.getText().trim();
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
            boolean entrou = escolherGrupo(participante);
            if (entrou) mainFrame.trocarTela("telaApostas");
        }
    }

    private void cadastrarNovoUsuario() {
        JTextField     fNome    = new JTextField();
        JTextField     fUsuario = new JTextField();
        JPasswordField fSenha   = new JPasswordField();

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

    /**
     * Verifica se o participante JÁ está em algum grupo comparando pelo ID
     * (evita falso negativo por comparação de referência de objeto).
     * Só oferece escolha de grupo se ainda não pertencer a nenhum.
     */
    private boolean escolherGrupo(Participante participante) {
        List<Grupo> grupos = mainFrame.getGrupoController().getGrupos();
        if (grupos == null || grupos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum grupo disponível. Aguarde o administrador criar um grupo.");
            mainFrame.setParticipanteLogado(null);
            return false;
        }

        // Busca cada grupo fresco do banco para verificar participantes atualizados
        for (Grupo g : grupos) {
            Grupo grupoAtualizado = mainFrame.getGrupoController().buscarNome(g.getNome());
            if (grupoAtualizado == null) continue;
            for (Participante p : grupoAtualizado.getParticipantes())
                if (p.getId() == participante.getId()) return true; // já está em um grupo
        }

        // Não está em nenhum grupo — filtra grupos com vaga
        List<Grupo> gruposComVaga = new java.util.ArrayList<>();
        for (Grupo g : grupos)
            if (g.getParticipantes().size() < 5) gruposComVaga.add(g);

        if (gruposComVaga.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os grupos estão cheios!");
            mainFrame.setParticipanteLogado(null);
            return false;
        }

        // Pede para escolher um grupo
        while (true) {
            String[] nomes = new String[gruposComVaga.size()];
            for (int i = 0; i < gruposComVaga.size(); i++)
                nomes[i] = gruposComVaga.get(i).getNome()
                        + " (" + gruposComVaga.get(i).getParticipantes().size() + "/5)";

            String escolhido = (String) JOptionPane.showInputDialog(
                    this, "Escolha um grupo para continuar:",
                    "Escolha seu grupo",
                    JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);

            if (escolhido == null) {
                JOptionPane.showMessageDialog(this, "Você precisa entrar em um grupo!");
                mainFrame.setParticipanteLogado(null);
                return false;
            }

            String nomeGrupo = escolhido.substring(0, escolhido.indexOf(" (")).trim();
            Grupo grupo = mainFrame.getGrupoController().buscarNome(nomeGrupo);
            boolean ok = mainFrame.getGrupoController().addParticipante(grupo, participante);

            if (ok) return true;

            // Grupo ficou cheio — atualiza lista
            JOptionPane.showMessageDialog(this, "Grupo cheio! Escolha outro.");
            gruposComVaga.clear();
            for (Grupo g : mainFrame.getGrupoController().getGrupos())
                if (g.getParticipantes().size() < 5) gruposComVaga.add(g);

            if (gruposComVaga.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os grupos estão cheios!");
                mainFrame.setParticipanteLogado(null);
                return false;
            }
        }
    }
    }