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

        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        add(campoUsuario);

        add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        add(campoSenha);

        JButton entrar = new JButton("Entrar");
        entrar.addActionListener(e -> login());

        add(new JLabel());
        add(entrar);
    }

    private void login() {
        String usuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());

        Usuario u = usuarioController.autenticar(usuario, senha);

        if (u == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha incorretos!");
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

        if (nome == null || nome.isBlank()) {
            return;
        }

        Participante existente =
                usuarioController.buscarNome(nome);

        if (existente != null) {
            mainFrame.setParticipanteLogado(existente);
            mainFrame.trocarTela("telaApostas");
            return;
        }

        usuarioController.cadastrar(participante, nome);

        ArrayList<Grupo> grupos =
                mainFrame.getGrupoController().getGrupos();

        if (!grupos.isEmpty()) {

            String[] nomes = new String[grupos.size()];

            for (int i = 0; i < grupos.size(); i++) {
                nomes[i] = grupos.get(i).getNome();
            }

            String escolhido = (String) JOptionPane.showInputDialog(
                    this,
                    "Escolha um grupo:",
                    "Grupo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    nomes,
                    nomes[0]
            );

            if (escolhido != null) {
                Grupo grupo =
                        mainFrame.getGrupoController().buscarNome(escolhido);

                mainFrame.getGrupoController()
                        .adicionarParticipante(grupo, participante);
            }
        }

        mainFrame.setParticipanteLogado(participante);
        mainFrame.trocarTela("telaApostas");
    }
}