package View;
import Controller.*;
import Model.Administrador;
import Model.Participante;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    private LoginController loginController;
    private GrupoController grupoController;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private ApostaController apostaController;

    private Participante participanteLogado;
    private Administrador adminLogado;

    private TelaLogin telaLogin;
    private TelaClassificacao telaClassificacao;
    private TelaApostas telaApostas;
    private TelaResultados telaResultados;
    private TelaCadastro telaCadastro;

    public MainFrame() {
        loginController = new LoginController();
        grupoController = new GrupoController();
        campeonatoController = new CampeonatoController();
        partidaController = new PartidaController();
        apostaController = new ApostaController();

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        telaLogin = new TelaLogin(this, loginController);
        telaClassificacao = new TelaClassificacao(this, grupoController);
        telaCadastro = new TelaCadastro(this, campeonatoController, partidaController);
        telaApostas = new TelaApostas(this, apostaController, campeonatoController);
        telaResultados = new TelaResultados(this, partidaController, campeonatoController, apostaController);

        painelPrincipal.add(telaResultados, "telaResultados");
        painelPrincipal.add(telaApostas, "telaApostas");
        painelPrincipal.add(telaCadastro, "telaCadastro");
        painelPrincipal.add(telaLogin, "telaLogin");
        painelPrincipal.add(telaClassificacao, "telaClassificacao");

        setTitle("Sistema de Apostas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        add(painelPrincipal);

        trocarTela("telaLogin");
    }

    public void trocarTela(String nomeTela) {
        cardLayout.show(painelPrincipal, nomeTela);

        if (nomeTela.equals("telaClassificacao")) {
            telaClassificacao.atualizar();
        }
        if (nomeTela.equals("telaApostas")) {
            telaApostas.atualizar();
        }
        if (nomeTela.equals("telaResultados")) {
            telaResultados.atualizar();
        }
    }

    public void setParticipanteLogado(Participante participante) {
        this.participanteLogado = participante;
    }

    public Participante getParticipanteLogado() {
        return participanteLogado;
    }

    public void setAdminLogado(Administrador admin) {
        this.adminLogado = admin;
    }

    public Administrador getAdminLogado() {
        return adminLogado;
    }

    public LoginController getLoginController() {
        return loginController;
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