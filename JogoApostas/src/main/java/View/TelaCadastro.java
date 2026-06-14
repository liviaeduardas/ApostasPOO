package View;

import Controller.CampeonatoController;
import Controller.GrupoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TelaCadastro extends JPanel {

    private MainFrame            main;
    private CampeonatoController campeonatoController;
    private PartidaController    partidaController;
    private GrupoController      grupoController;
    private JPanel               painelForm;
    private CardLayout           cardForm;

    // Combos que precisam ser recarregados ao abrir cada painel
    private JComboBox<String> comboCampAssociar;
    private JComboBox<String> comboClubeAssociar;
    private JComboBox<String> comboCampPartida;
    private JComboBox<String> comboMandante;
    private JComboBox<String> comboVisitante;
    private JComboBox<String> comboCampResultado;
    private JComboBox<String> comboPartidaResultado;

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController,
                        PartidaController partidaController) {
        this.main                 = main;
        this.campeonatoController = campeonatoController;
        this.partidaController    = partidaController;
        this.grupoController      = main.getGrupoController();

        setLayout(new BorderLayout());

        JPanel menu = new JPanel(new GridLayout(7, 1, 5, 5));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menu.setPreferredSize(new Dimension(160, 0));

        JButton btnCampeonato = new JButton("Campeonato");
        JButton btnClube      = new JButton("Clube");
        JButton btnAssociar   = new JButton("Associar Clube");
        JButton btnPartida    = new JButton("Partida");
        JButton btnResultado  = new JButton("Resultado");
        JButton btnGrupo      = new JButton("Grupo");
        JButton btnSair       = new JButton("Sair");

        btnCampeonato.addActionListener(e -> mostrar("campeonato"));
        btnClube     .addActionListener(e -> mostrar("clube"));
        btnAssociar  .addActionListener(e -> { recarregarAssociar();   mostrar("associar");   });
        btnPartida   .addActionListener(e -> { recarregarPartida();    mostrar("partida");    });
        btnResultado .addActionListener(e -> { recarregarResultado();  mostrar("resultado");  });
        btnGrupo     .addActionListener(e -> mostrar("grupo"));
        btnSair      .addActionListener(e -> main.trocarTela("telaLogin"));

        menu.add(btnCampeonato);
        menu.add(btnClube);
        menu.add(btnAssociar);
        menu.add(btnPartida);
        menu.add(btnResultado);
        menu.add(btnGrupo);
        menu.add(btnSair);

        cardForm   = new CardLayout();
        painelForm = new JPanel(cardForm);
        painelForm.add(painelCampeonato(), "campeonato");
        painelForm.add(painelClube(),      "clube");
        painelForm.add(painelAssociar(),   "associar");
        painelForm.add(painelPartida(),    "partida");
        painelForm.add(painelResultado(),  "resultado");
        painelForm.add(painelGrupo(),      "grupo");

        add(menu,       BorderLayout.WEST);
        add(painelForm, BorderLayout.CENTER);
    }

    private void mostrar(String nome) { cardForm.show(painelForm, nome); }

    // ── Campeonato ────────────────────────────────────────────────────────────

    private JPanel painelCampeonato() {
        JPanel p = form();
        JTextField nome = new JTextField();
        JTextField ano  = new JTextField();
        p.add(new JLabel("Nome:")); p.add(nome);
        p.add(new JLabel("Ano:"));  p.add(ano);
        JButton criar = new JButton("Criar Campeonato");
        criar.addActionListener(e -> {
            try {
                boolean ok = campeonatoController.novoCampeonato(
                        nome.getText(), Integer.parseInt(ano.getText()));
                msg(ok ? "Campeonato criado!" : "Dados inválidos.");
                if (ok) { nome.setText(""); ano.setText(""); }
            } catch (NumberFormatException ex) { msg("Ano inválido."); }
        });
        p.add(criar);
        return p;
    }

    // ── Clube ─────────────────────────────────────────────────────────────────

    private JPanel painelClube() {
        JPanel p = form();
        JTextField nome  = new JTextField();
        JTextField sigla = new JTextField();
        p.add(new JLabel("Nome:"));  p.add(nome);
        p.add(new JLabel("Sigla:")); p.add(sigla);
        JButton cadastrar = new JButton("Cadastrar Clube");
        cadastrar.addActionListener(e -> {
            boolean ok = campeonatoController.cadastrarClube(nome.getText(), sigla.getText());
            msg(ok ? "Clube cadastrado! Use 'Associar Clube' para adicioná-lo a um campeonato."
                    : "Dados inválidos.");
            if (ok) { nome.setText(""); sigla.setText(""); }
        });
        p.add(cadastrar);
        return p;
    }

    // ── Associar Clube ao Campeonato ──────────────────────────────────────────

    private JPanel painelAssociar() {
        JPanel p = form();

        comboCampAssociar  = new JComboBox<>();
        comboClubeAssociar = new JComboBox<>();

        JButton associar = new JButton("Associar ao Campeonato");
        associar.addActionListener(e -> {
            Campeonato camp = campeonatoController.procurarCampeonato(
                    (String) comboCampAssociar.getSelectedItem());
            if (camp == null) { msg("Selecione um campeonato!"); return; }

            String itemClube = (String) comboClubeAssociar.getSelectedItem();
            if (itemClube == null) { msg("Selecione um clube!"); return; }

            String sigla = itemClube.substring(itemClube.indexOf("(") + 1, itemClube.indexOf(")"));
            Clube clube = campeonatoController.procurarClube(sigla.trim());

            boolean ok = campeonatoController.addClube(camp, clube);
            msg(ok ? "Clube associado ao campeonato!"
                    : "Clube já está no campeonato ou limite de 8 atingido!");
        });

        p.add(new JLabel("Campeonato:")); p.add(comboCampAssociar);
        p.add(new JLabel("Clube:"));      p.add(comboClubeAssociar);
        p.add(associar);
        return p;
    }

    // ── Partida ───────────────────────────────────────────────────────────────

    private JPanel painelPartida() {
        JPanel p = form();

        comboCampPartida = new JComboBox<>();
        comboMandante    = new JComboBox<>();
        comboVisitante   = new JComboBox<>();

        comboCampPartida.addActionListener(e -> carregarClubesDoCampeonato());

        SpinnerDateModel modeloData = new SpinnerDateModel();
        JSpinner spinnerData = new JSpinner(modeloData);
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));

        SpinnerDateModel modeloHora = new SpinnerDateModel();
        JSpinner spinnerHora = new JSpinner(modeloHora);
        spinnerHora.setEditor(new JSpinner.DateEditor(spinnerHora, "HH:mm"));

        p.add(new JLabel("Campeonato:")); p.add(comboCampPartida);
        p.add(new JLabel("Casa:"));       p.add(comboMandante);
        p.add(new JLabel("Visitante:"));  p.add(comboVisitante);
        p.add(new JLabel("Data:"));       p.add(spinnerData);
        p.add(new JLabel("Hora:"));       p.add(spinnerHora);

        JButton cadastrar = new JButton("Cadastrar Partida");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCampPartida.getSelectedItem());
                Clube casa = buscarClubeNoCampeonato(camp, (String) comboMandante.getSelectedItem());
                Clube vis  = buscarClubeNoCampeonato(camp, (String) comboVisitante.getSelectedItem());

                if (camp == null) { msg("Selecione um campeonato!"); return; }
                if (casa == null || vis == null) { msg("Selecione os times!"); return; }
                if (casa.equals(vis)) { msg("Selecione times diferentes!"); return; }

                java.util.Date dateData = (java.util.Date) spinnerData.getValue();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dateData);
                LocalDate data = LocalDate.of(
                        cal.get(java.util.Calendar.YEAR),
                        cal.get(java.util.Calendar.MONTH) + 1,
                        cal.get(java.util.Calendar.DAY_OF_MONTH));

                java.util.Date dateHora = (java.util.Date) spinnerHora.getValue();
                cal.setTime(dateHora);
                LocalTime hora = LocalTime.of(
                        cal.get(java.util.Calendar.HOUR_OF_DAY),
                        cal.get(java.util.Calendar.MINUTE));

                boolean ok = partidaController.cadastrarPartida(camp, casa, vis, data, hora);
                msg(ok ? "Partida cadastrada!"
                        : "Erro: verifique se os clubes pertencem ao campeonato.");
            } catch (Exception ex) {
                msg("Erro: " + ex.getMessage());
            }
        });
        p.add(cadastrar);
        return p;
    }

    // ── Resultado ─────────────────────────────────────────────────────────────

    private JPanel painelResultado() {
        JPanel p = form();

        comboCampResultado    = new JComboBox<>();
        comboPartidaResultado = new JComboBox<>();

        comboCampResultado.addActionListener(e -> carregarPartidasPendentes());

        JTextField golsCasa = new JTextField();
        JTextField golsVis  = new JTextField();

        p.add(new JLabel("Campeonato:"));     p.add(comboCampResultado);
        p.add(new JLabel("Partida:"));         p.add(comboPartidaResultado);
        p.add(new JLabel("Gols casa:"));       p.add(golsCasa);
        p.add(new JLabel("Gols visitante:"));  p.add(golsVis);

        JButton salvar = new JButton("Salvar Resultado");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCampResultado.getSelectedItem());
                if (camp == null) { msg("Selecione um campeonato!"); return; }

                List<Partida> pendentes = new java.util.ArrayList<>();
                for (Partida pt : camp.getPartidas())
                    if (!pt.isPartidaFinalizada()) pendentes.add(pt);

                int idx = comboPartidaResultado.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) { msg("Selecione uma partida!"); return; }

                boolean ok = partidaController.addResultado(
                        pendentes.get(idx),
                        Integer.parseInt(golsCasa.getText().trim()),
                        Integer.parseInt(golsVis.getText().trim()));

                msg(ok ? "Resultado salvo!" : "Erro ao salvar.");
                if (ok) {
                    carregarPartidasPendentes();
                    golsCasa.setText(""); golsVis.setText("");
                }
            } catch (NumberFormatException ex) {
                msg("Digite números válidos para os gols.");
            }
        });
        p.add(salvar);
        return p;
    }

    // ── Grupo ─────────────────────────────────────────────────────────────────

    private JPanel painelGrupo() {
        JPanel p = form();
        JTextField nomeGrupo = new JTextField();
        p.add(new JLabel("Nome do grupo:")); p.add(nomeGrupo);
        JButton criar = new JButton("Criar Grupo");
        criar.addActionListener(e -> {
            boolean ok = grupoController.criarGrupo(nomeGrupo.getText(), main.getAdminLogado());
            msg(ok ? "Grupo criado!" : "Não foi possível criar o grupo.");
            if (ok) nomeGrupo.setText("");
        });
        p.add(criar);
        return p;
    }

    // ── Métodos de recarga ────────────────────────────────────────────────────

    private void recarregarAssociar() {
        comboCampAssociar.removeAllItems();
        comboClubeAssociar.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampAssociar.addItem(c.getNome());
        for (Clube c : campeonatoController.getClubes())
            comboClubeAssociar.addItem(c.getNome() + " (" + c.getSigla() + ")");
    }

    private void recarregarPartida() {
        comboCampPartida.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampPartida.addItem(c.getNome());
        // listener do combo dispara carregarClubesDoCampeonato automaticamente
    }

    private void recarregarResultado() {
        comboCampResultado.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampResultado.addItem(c.getNome());
        // listener do combo dispara carregarPartidasPendentes automaticamente
    }

    private void carregarClubesDoCampeonato() {
        comboMandante.removeAllItems();
        comboVisitante.removeAllItems();
        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampPartida.getSelectedItem());
        if (camp == null) return;
        for (Clube c : camp.getClubes()) {
            comboMandante.addItem(c.getNome());
            comboVisitante.addItem(c.getNome());
        }
    }

    private void carregarPartidasPendentes() {
        comboPartidaResultado.removeAllItems();
        Campeonato camp = campeonatoController.procurarCampeonato(
                (String) comboCampResultado.getSelectedItem());
        if (camp == null) return;
        for (Partida pt : camp.getPartidas())
            if (!pt.isPartidaFinalizada())
                comboPartidaResultado.addItem(
                        pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel form() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    private void msg(String texto) { JOptionPane.showMessageDialog(this, texto); }

    private Clube buscarClubeNoCampeonato(Campeonato camp, String nome) {
        if (camp == null || nome == null) return null;
        for (Clube c : camp.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }

    private Clube buscarClubePorNome(String nome) {
        if (nome == null) return null;
        for (Clube c : campeonatoController.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}