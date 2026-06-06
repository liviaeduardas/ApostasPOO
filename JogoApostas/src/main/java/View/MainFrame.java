package View;
import Controller.*;
import Model.Administrador;
import Model.Participante;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private UsuarioController usuarioController;
    private GrupoController grupoController;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private ApostaController apostaController;

    private Participante participanteLogado;
    private Administrador adminLogado;

    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    private TelaLogin telaLogin;
    private TelaCadastro telaCadastro;
    private TelaApostas telaApostas;
    private TelaResultados telaResultados;
    private TelaClassificacao telaClassificacao;

    public MainFrame() {

        usuarioController = new UsuarioController();
        grupoController = new GrupoController();
        campeonatoController = new CampeonatoController();
        partidaController = new PartidaController();
        apostaController = new ApostaController();

        telaLogin = new TelaLogin(this, usuarioController);
        telaCadastro = new TelaCadastro(this, campeonatoController, partidaController);
        telaApostas = new TelaApostas(this, apostaController, campeonatoController, grupoController);
        telaResultados = new TelaResultados(this, partidaController, campeonatoController, apostaController);
        telaClassificacao = new TelaClassificacao(this, grupoController);

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        painelPrincipal.add(telaLogin, "Login");
        painelPrincipal.add(telaCadastro, "Cadastro");
        painelPrincipal.add(telaApostas, "Apostas");
        painelPrincipal.add(telaResultados, "Resultados");
        painelPrincipal.add(telaClassificacao, "Classificacao");

        add(painelPrincipal);

        setTitle("Sistema de Apostas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        trocarTela("Login");
    }

    public void trocarTela(String tela) {

        cardLayout.show(painelPrincipal, tela);

        if (tela.equals("Apostas")) {
            telaApostas.atualizar();
        }

        if (tela.equals("Resultados")) {
            telaResultados.atualizar();
        }

        if (tela.equals("Classificacao")) {
            telaClassificacao.atualizar();
        }
    }

    public Participante getParticipanteLogado() {
        return participanteLogado;
    }

    public void setParticipanteLogado(Participante participanteLogado) {
        this.participanteLogado = participanteLogado;
    }

    public Administrador getAdminLogado() {
        return adminLogado;
    }

    public void setAdminLogado(Administrador adminLogado) {
        this.adminLogado = adminLogado;
    }

    public UsuarioController getUsuarioController() {
        return usuarioController;
    }

    public GrupoController getGrupoController() {
        return grupoController;
    }

    public CampeonatoController getCampeonatoController() {
        return campeonatoController;
    }

    public PartidaController getPartidaController() {
        return partidaController;
    }

    public ApostaController getApostaController() {
        return apostaController;
    }
}