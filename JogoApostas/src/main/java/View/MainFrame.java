package View;

import Controller.*;
import Model.Administrador;
import Model.Participante;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Controllers — gerenciam as regras do sistema
    private UsuarioController usuarioController;
    private GrupoController grupoController;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private ApostaController apostaController;

    // Usuário logado no momento
    private Participante participanteLogado;
    private Administrador adminLogado;

    // Telas do sistema
    private CardLayout cardLayout;
    private JPanel painelPrincipal;
    private TelaLogin telaLogin;
    private TelaCadastro telaCadastro;
    private TelaApostas telaApostas;
    private TelaResultados telaResultados;
    private TelaClassificacao telaClassificacao;

    public MainFrame() {
        // Inicializa todos os controllers
        usuarioController = new UsuarioController();
        grupoController = new GrupoController();
        campeonatoController = new CampeonatoController();
        partidaController = new PartidaController();
        apostaController = new ApostaController();

        // Inicializa todas as telas
        telaLogin = new TelaLogin(this, usuarioController);
        telaCadastro = new TelaCadastro(this, campeonatoController, partidaController);
        telaApostas = new TelaApostas(this, apostaController, campeonatoController);
        telaResultados = new TelaResultados(this, campeonatoController, apostaController);
        telaClassificacao = new TelaClassificacao(this, grupoController);

        // CardLayout — troca as telas sem abrir novas janelas
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.add(telaLogin, "telaLogin");
        painelPrincipal.add(telaCadastro, "telaCadastro");
        painelPrincipal.add(telaApostas, "telaApostas");
        painelPrincipal.add(telaResultados,"telaResultados");
        painelPrincipal.add(telaClassificacao, "telaClassificacao");

        setTitle("Sistema de Apostas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        add(painelPrincipal);

        trocarTela("telaLogin");
    }

    // Troca a tela visível e avisa a tela para atualizar seus dados
    public void trocarTela(String nome) {
        cardLayout.show(painelPrincipal, nome);
        if (nome.equals("telaApostas")) telaApostas.atualizar();
        if (nome.equals("telaResultados"))telaResultados.atualizar();
        if (nome.equals("telaClassificacao")) telaClassificacao.atualizar();
    }

    // Getters e setters do usuário logado
    public Participante getParticipanteLogado(){ return participanteLogado; }
    public void setParticipanteLogado(Participante p){ this.participanteLogado = p; }
    public Administrador getAdminLogado(){ return adminLogado; }
    public void setAdminLogado(Administrador a) { this.adminLogado = a; }

    // Getters dos controllers — usados pelas telas
    public UsuarioController getUsuarioController(){ return usuarioController; }
    public GrupoController getGrupoController(){ return grupoController; }
    public CampeonatoController getCampeonatoController() { return campeonatoController; }
    public PartidaController getPartidaController(){ return partidaController; }
    public ApostaController getApostaController(){ return apostaController; }
}