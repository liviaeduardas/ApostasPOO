package View;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TelaCadastro extends JPanel implements ActionListener {
    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private GrupoController grupoController;
    private JPanel painelCentral;
    private JPanel painelCampeonato;
    private JPanel painelClube;
    private JPanel painelAssociar;
    private JPanel painelPartida;
    private JPanel painelResultado;
    private JPanel painelGrupo;
    private JButton btnCampeonato;
    private JButton btnClube;
    private JButton btnAssociar;
    private JButton btnPartida;
    private JButton btnResultado;
    private JButton btnGrupo;
    private JButton btnSair;
    private JTextField txtNomeCampeonato;
    private JTextField txtAnoCampeonato;
    private JButton btnCriarCampeonato;
    private JTextField txtNomeClube;
    private JTextField txtSiglaClube;
    private JButton btnCadastrarClube;
    private JComboBox<String> comboCampAssociar;
    private JComboBox<String> comboClubeAssociar;
    private JButton btnAssociarClube;
    private JComboBox<String> comboCampPartida;
    private JComboBox<String> comboCasa;
    private JComboBox<String> comboVisitante;
    private JSpinner spinnerData;
    private JSpinner spinnerHora;
    private JButton btnCadastrarPartida;
    private JComboBox<String> comboCampResultado;
    private JComboBox<String> comboPartidaResultado;
    private JTextField txtGolsCasaResultado;
    private JTextField txtGolsVisResultado;
    private JButton btnSalvarResultado;
    private final List<Partida> partidasPendentesExibidas = new ArrayList<>();
    private JTextField txtNomeGrupo;
    private JButton btnCriarGrupo;

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.main = main;
        this.campeonatoController = campeonatoController;
        this.partidaController = partidaController;
        this.grupoController = main.getGrupoController();

        setLayout(new BorderLayout());
        montarMenuLateral();
        montarPainelCentral();
        mostrarPainel(painelCampeonato);
    }

    private void montarMenuLateral() {
        JPanel menu = new JPanel(new GridLayout(7, 1, 5, 5));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menu.setPreferredSize(new Dimension(160, 0));

        btnCampeonato = new JButton("Campeonato");
        btnClube = new JButton("Clube");
        btnAssociar = new JButton("Associar Clube");
        btnPartida = new JButton("Partida");
        btnResultado = new JButton("Resultado");
        btnGrupo = new JButton("Grupo");
        btnSair = new JButton("Sair");

        btnCampeonato.addActionListener(this);
        btnClube.addActionListener(this);
        btnAssociar.addActionListener(this);
        btnPartida.addActionListener(this);
        btnResultado.addActionListener(this);
        btnGrupo.addActionListener(this);
        btnSair.addActionListener(this);

        menu.add(btnCampeonato);
        menu.add(btnClube);
        menu.add(btnAssociar);
        menu.add(btnPartida);
        menu.add(btnResultado);
        menu.add(btnGrupo);
        menu.add(btnSair);
        add(menu, BorderLayout.WEST);
    }


    private void montarPainelCentral() {
        painelCentral = new JPanel();
        painelCentral.setLayout(new OverlayLayout(painelCentral));
        painelCampeonato = criarPainelCampeonato();
        painelClube = criarPainelClube();
        painelAssociar = criarPainelAssociar();
        painelPartida = criarPainelPartida();
        painelResultado = criarPainelResultado();
        painelGrupo = criarPainelGrupo();
        painelCentral.add(painelCampeonato);
        painelCentral.add(painelClube);
        painelCentral.add(painelAssociar);
        painelCentral.add(painelPartida);
        painelCentral.add(painelResultado);
        painelCentral.add(painelGrupo);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void mostrarPainel(JPanel painelParaMostrar) {
        painelCampeonato.setVisible(painelParaMostrar == painelCampeonato);
        painelClube.setVisible(painelParaMostrar == painelClube);
        painelAssociar.setVisible(painelParaMostrar == painelAssociar);
        painelPartida.setVisible(painelParaMostrar == painelPartida);
        painelResultado.setVisible(painelParaMostrar == painelResultado);
        painelGrupo.setVisible(painelParaMostrar == painelGrupo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCampeonato) {
            mostrarPainel(painelCampeonato);

        } else if (e.getSource() == btnClube) {
            mostrarPainel(painelClube);

        } else if (e.getSource() == btnAssociar) {
            recarregarAssociar();
            mostrarPainel(painelAssociar);

        } else if (e.getSource() == btnPartida) {
            recarregarPartida();
            mostrarPainel(painelPartida);

        } else if (e.getSource() == btnResultado) {
            recarregarResultado();
            mostrarPainel(painelResultado);

        } else if (e.getSource() == btnGrupo) {
            mostrarPainel(painelGrupo);

        } else if (e.getSource() == btnSair) {
            main.trocarTela("telaLogin");

        } else if (e.getSource() == comboCampPartida) {
            carregarClubesDoCampeonato();

        } else if (e.getSource() == comboCampResultado) {
            carregarPartidasPendentes();

        } else if (e.getSource() == btnCriarCampeonato) {
            criarCampeonato();

        } else if (e.getSource() == btnCadastrarClube) {
            cadastrarClube();

        } else if (e.getSource() == btnAssociarClube) {
            associarClubeAoCampeonato();

        } else if (e.getSource() == btnCadastrarPartida) {
            cadastrarPartida();

        } else if (e.getSource() == btnSalvarResultado) {
            salvarResultado();

        } else if (e.getSource() == btnCriarGrupo) {
            criarGrupo();
        }
    }

    private JPanel criarPainelCampeonato() {
        JPanel p = form();

        txtNomeCampeonato = new JTextField();
        txtAnoCampeonato = new JTextField();

        p.add(new JLabel("Nome:"));
        p.add(txtNomeCampeonato);
        p.add(new JLabel("Ano:"));
        p.add(txtAnoCampeonato);

        btnCriarCampeonato = new JButton("Criar Campeonato");
        btnCriarCampeonato.addActionListener(this);
        p.add(btnCriarCampeonato);

        return p;
    }

    private void criarCampeonato() {
        try {
            boolean ok = campeonatoController.novoCampeonato(
                    txtNomeCampeonato.getText(),
                    Integer.parseInt(txtAnoCampeonato.getText()));

            msg(ok ? "Campeonato criado!" : "Dados inválidos.");

            if (ok) {
                txtNomeCampeonato.setText("");
                txtAnoCampeonato.setText("");
            }
        } catch (NumberFormatException ex) {
            msg("Ano inválido.");
        }
    }

    private JPanel criarPainelClube() {
        JPanel p = form();

        txtNomeClube = new JTextField();
        txtSiglaClube = new JTextField();

        p.add(new JLabel("Nome:"));
        p.add(txtNomeClube);
        p.add(new JLabel("Sigla:"));
        p.add(txtSiglaClube);

        btnCadastrarClube = new JButton("Cadastrar Clube");
        btnCadastrarClube.addActionListener(this);
        p.add(btnCadastrarClube);

        return p;
    }

    private void cadastrarClube() {
        boolean ok = campeonatoController.cadastrarClube(txtNomeClube.getText(), txtSiglaClube.getText());

        msg(ok ? "Clube cadastrado! Use 'Associar Clube' para adicioná-lo a um campeonato."
                : "Dados inválidos.");

        if (ok) {
            txtNomeClube.setText("");
            txtSiglaClube.setText("");
        }
    }


    private JPanel criarPainelAssociar() {
        JPanel p = form();

        comboCampAssociar = new JComboBox<>();
        comboClubeAssociar = new JComboBox<>();

        p.add(new JLabel("Campeonato:"));
        p.add(comboCampAssociar);
        p.add(new JLabel("Clube:"));
        p.add(comboClubeAssociar);

        btnAssociarClube = new JButton("Associar ao Campeonato");
        btnAssociarClube.addActionListener(this);
        p.add(btnAssociarClube);

        return p;
    }

    private final List<Clube> clubesExibidosAssociar = new ArrayList<>();
    private void recarregarAssociar() {
        comboCampAssociar.removeAllItems();
        comboClubeAssociar.removeAllItems();
        clubesExibidosAssociar.clear();

        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampAssociar.addItem(c.getNome());

        for (Clube c : campeonatoController.getClubes()) {
            comboClubeAssociar.addItem(c.getNome() + " (" + c.getSigla() + ")");
            clubesExibidosAssociar.add(c);
        }
    }

    private void associarClubeAoCampeonato() {
        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampAssociar.getSelectedItem());
        if (camp == null) {
            msg("Selecione um campeonato!");
            return;
        }

        int indiceClube = comboClubeAssociar.getSelectedIndex();
        if (indiceClube < 0 || indiceClube >= clubesExibidosAssociar.size()) {
            msg("Selecione um clube!");
            return;
        }
        Clube clube = clubesExibidosAssociar.get(indiceClube);

        boolean ok = campeonatoController.addClube(camp, clube);
        msg(ok ? "Clube associado ao campeonato!"
                : "Clube já está no campeonato ou limite de 8 atingido!");
    }

    private JPanel criarPainelPartida() {
        JPanel p = form();

        comboCampPartida = new JComboBox<>();
        comboCasa = new JComboBox<>();
        comboVisitante = new JComboBox<>();
        comboCampPartida.addActionListener(this);
        SpinnerDateModel modeloData = new SpinnerDateModel();
        spinnerData = new JSpinner(modeloData);
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));

        SpinnerDateModel modeloHora = new SpinnerDateModel();
        spinnerHora = new JSpinner(modeloHora);
        spinnerHora.setEditor(new JSpinner.DateEditor(spinnerHora, "HH:mm"));

        p.add(new JLabel("Campeonato:"));
        p.add(comboCampPartida);
        p.add(new JLabel("Casa:"));
        p.add(comboCasa);
        p.add(new JLabel("Visitante:"));
        p.add(comboVisitante);
        p.add(new JLabel("Data:"));
        p.add(spinnerData);
        p.add(new JLabel("Hora:"));
        p.add(spinnerHora);

        btnCadastrarPartida = new JButton("Cadastrar Partida");
        btnCadastrarPartida.addActionListener(this);
        p.add(btnCadastrarPartida);

        return p;
    }

    private void recarregarPartida() {
        comboCampPartida.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampPartida.addItem(c.getNome());
    }

    private void carregarClubesDoCampeonato() {
        comboCasa.removeAllItems();
        comboVisitante.removeAllItems();

        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampPartida.getSelectedItem());
        if (camp == null) return;

        for (Clube c : camp.getClubes()) {
            comboCasa.addItem(c.getNome());
            comboVisitante.addItem(c.getNome());
        }
    }

    private void cadastrarPartida() {
        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampPartida.getSelectedItem());
        if (camp == null) {
            msg("Selecione um campeonato!");
            return;
        }

        Clube casa = buscarClubeNoCampeonato(camp, (String) comboCasa.getSelectedItem());
        Clube visitante = buscarClubeNoCampeonato(camp, (String) comboVisitante.getSelectedItem());

        if (casa == null || visitante == null) {
            msg("Selecione os times!");
            return;
        }
        if (casa.equals(visitante)) {
            msg("Selecione times diferentes!");
            return;
        }

        java.util.Date dataEscolhida = (java.util.Date) spinnerData.getValue();
        java.util.Calendar calendario = java.util.Calendar.getInstance();
        calendario.setTime(dataEscolhida);

        LocalDate data = LocalDate.of(
                calendario.get(java.util.Calendar.YEAR),
                calendario.get(java.util.Calendar.MONTH) + 1,
                calendario.get(java.util.Calendar.DAY_OF_MONTH));

        java.util.Date horaEscolhida = (java.util.Date) spinnerHora.getValue();
        calendario.setTime(horaEscolhida);

        LocalTime hora = LocalTime.of(
                calendario.get(java.util.Calendar.HOUR_OF_DAY),
                calendario.get(java.util.Calendar.MINUTE));

        boolean ok = partidaController.cadastrarPartida(camp, casa, visitante, data, hora);
        msg(ok ? "Partida cadastrada!" : "Erro: verifique se os clubes pertencem ao campeonato.");
    }

    private JPanel criarPainelResultado() {
        JPanel p = form();

        comboCampResultado = new JComboBox<>();
        comboPartidaResultado = new JComboBox<>();
        comboCampResultado.addActionListener(this);

        txtGolsCasaResultado = new JTextField();
        txtGolsVisResultado = new JTextField();

        p.add(new JLabel("Campeonato:"));
        p.add(comboCampResultado);
        p.add(new JLabel("Partida:"));
        p.add(comboPartidaResultado);
        p.add(new JLabel("Gols casa:"));
        p.add(txtGolsCasaResultado);
        p.add(new JLabel("Gols visitante:"));
        p.add(txtGolsVisResultado);

        btnSalvarResultado = new JButton("Salvar Resultado");
        btnSalvarResultado.addActionListener(this);
        p.add(btnSalvarResultado);

        return p;
    }

    private void recarregarResultado() {
        comboCampResultado.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampResultado.addItem(c.getNome());
    }

    private void carregarPartidasPendentes() {
        comboPartidaResultado.removeAllItems();
        partidasPendentesExibidas.clear();

        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampResultado.getSelectedItem());
        if (camp == null) return;

        for (Partida pt : camp.getPartidas()) {
            if (!pt.isPartidaFinalizada()) {
                comboPartidaResultado.addItem(
                        pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
                partidasPendentesExibidas.add(pt);
            }
        }
    }

    private void salvarResultado() {
        int indice = comboPartidaResultado.getSelectedIndex();
        if (indice < 0 || indice >= partidasPendentesExibidas.size()) {
            msg("Selecione uma partida!");
            return;
        }
        Partida partida = partidasPendentesExibidas.get(indice);

        if (txtGolsCasaResultado.getText().trim().isEmpty()
                || txtGolsVisResultado.getText().trim().isEmpty()) {
            msg("Preencha os gols!");
            return;
        }

        try {
            int GolsCasa = Integer.parseInt(txtGolsCasaResultado.getText().trim());
            int GolsVisitante = Integer.parseInt(txtGolsVisResultado.getText().trim());

            boolean ok = partidaController.addResultado(partida, GolsCasa, GolsVisitante);
            msg(ok ? "Resultado salvo!" : "Erro ao salvar.");

            if (ok) {
                carregarPartidasPendentes();
                txtGolsCasaResultado.setText("");
                txtGolsVisResultado.setText("");
            }
        } catch (NumberFormatException ex) {
            msg("Digite números válidos para os gols.");
        }
    }

    private JPanel criarPainelGrupo() {
        JPanel p = form();

        txtNomeGrupo = new JTextField();
        p.add(new JLabel("Nome do grupo:"));
        p.add(txtNomeGrupo);

        btnCriarGrupo = new JButton("Criar Grupo");
        btnCriarGrupo.addActionListener(this);
        p.add(btnCriarGrupo);

        return p;
    }

    private void criarGrupo() {
        boolean ok = grupoController.criarGrupo(txtNomeGrupo.getText(), main.getAdminLogado());
        msg(ok ? "Grupo criado!" : "Não foi possível criar o grupo.");
        if (ok) txtNomeGrupo.setText("");
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    private void msg(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private Clube buscarClubeNoCampeonato(Campeonato camp, String nome) {
        if (camp == null || nome == null) return null;
        for (Clube c : camp.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}