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

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController,
                        PartidaController partidaController) {
        this.main                 = main;
        this.campeonatoController = campeonatoController;
        this.partidaController    = partidaController;
        this.grupoController      = main.getGrupoController();

        setLayout(new BorderLayout());

        // Menu lateral
        JPanel menu = new JPanel(new GridLayout(7, 1, 5, 5));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menu.setPreferredSize(new Dimension(160, 0));

        String[] botoes = {"Campeonato", "Clube", "Associar Clube", "Partida", "Resultado", "Grupo", "Sair"};
        String[] paineis = {"campeonato", "clube", "associar", "partida", "resultado", "grupo", null};

        for (int i = 0; i < botoes.length; i++) {
            JButton b = new JButton(botoes[i]);
            final String painel = paineis[i];
            if (painel == null) {
                b.addActionListener(e -> main.trocarTela("telaLogin"));
            } else {
                b.addActionListener(e -> mostrar(painel));
            }
            menu.add(b);
        }

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
                boolean ok = campeonatoController.novoCampeonato(nome.getText(), Integer.parseInt(ano.getText()));
                msg(ok ? "Campeonato criado!" : "Dados inválidos.");
                if (ok) { nome.setText(""); ano.setText(""); }
            } catch (NumberFormatException ex) { msg("Ano inválido."); }
        });
        p.add(criar);
        return p;
    }

    // ── Clube — só cadastra, sem associar ─────────────────────────────────────

    private JPanel painelClube() {
        JPanel p = form();
        JTextField nome  = new JTextField();
        JTextField sigla = new JTextField();
        p.add(new JLabel("Nome:"));  p.add(nome);
        p.add(new JLabel("Sigla:")); p.add(sigla);
        JButton cadastrar = new JButton("Cadastrar Clube");
        cadastrar.addActionListener(e -> {
            boolean ok = campeonatoController.cadastrarClube(nome.getText(), sigla.getText());
            msg(ok ? "Clube cadastrado! Use 'Associar Clube' para adicioná-lo a um campeonato." : "Dados inválidos.");
            if (ok) { nome.setText(""); sigla.setText(""); }
        });
        p.add(cadastrar);
        return p;
    }

    // ── Associar Clube ao Campeonato ──────────────────────────────────────────

    private JPanel painelAssociar() {
        JPanel p = form();

        JComboBox<String> comboCamp  = new JComboBox<>();
        JComboBox<String> comboClube = new JComboBox<>();

        // Botão para recarregar listas do banco
        JButton recarregar = new JButton("Recarregar listas");
        recarregar.addActionListener(e -> {
            comboCamp.removeAllItems();
            comboClube.removeAllItems();
            for (Campeonato c : campeonatoController.getCampeonatos())
                comboCamp.addItem(c.getNome());
            for (Clube c : campeonatoController.getClubes())
                comboClube.addItem(c.getNome() + " (" + c.getSigla() + ")");
        });

        // Carrega automaticamente ao montar o painel
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());
        for (Clube c : campeonatoController.getClubes())
            comboClube.addItem(c.getNome() + " (" + c.getSigla() + ")");

        JButton associar = new JButton("Associar ao Campeonato");
        associar.addActionListener(e -> {
            Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
            if (camp == null) { msg("Selecione um campeonato!"); return; }

            String itemClube = (String) comboClube.getSelectedItem();
            if (itemClube == null) { msg("Selecione um clube!"); return; }

            // Extrai a sigla do item "Nome (SIGLA)"
            String sigla = itemClube.substring(itemClube.indexOf("(") + 1, itemClube.indexOf(")"));
            Clube clube = campeonatoController.procurarClube(sigla);

            boolean ok = campeonatoController.addClube(camp, clube);
            msg(ok ? "Clube associado ao campeonato!" : "Clube já está no campeonato ou limite de 8 atingido!");
        });

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Clube:"));      p.add(comboClube);
        p.add(recarregar);
        p.add(associar);
        return p;
    }

    // ── Partida ───────────────────────────────────────────────────────────────

    private JPanel painelPartida() {
        JPanel p = form();

        JComboBox<String> comboCamp   = new JComboBox<>();
        JComboBox<String> mandante    = new JComboBox<>();
        JComboBox<String> visitante   = new JComboBox<>();

        // Carrega campeonatos
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        // Ao trocar campeonato, carrega só os clubes desse campeonato
        comboCamp.addActionListener(e -> {
            mandante.removeAllItems();
            visitante.removeAllItems();
            Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
            if (camp == null) return;
            for (Clube c : camp.getClubes()) {
                mandante.addItem(c.getNome());
                visitante.addItem(c.getNome());
            }
        });

        // Dispara o evento para carregar os clubes do primeiro campeonato
        if (comboCamp.getItemCount() > 0) comboCamp.setSelectedIndex(0);

        // Seletores de data e hora
        SpinnerDateModel modeloData = new SpinnerDateModel();
        JSpinner spinnerData = new JSpinner(modeloData);
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));

        SpinnerDateModel modeloHora = new SpinnerDateModel();
        JSpinner spinnerHora = new JSpinner(modeloHora);
        spinnerHora.setEditor(new JSpinner.DateEditor(spinnerHora, "HH:mm"));

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Casa:"));       p.add(mandante);
        p.add(new JLabel("Visitante:"));  p.add(visitante);
        p.add(new JLabel("Data:"));       p.add(spinnerData);
        p.add(new JLabel("Hora:"));       p.add(spinnerHora);

        JButton cadastrar = new JButton("Cadastrar Partida");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
                Clube casa = buscarClubePorNome((String) mandante.getSelectedItem());
                Clube vis  = buscarClubePorNome((String) visitante.getSelectedItem());

                if (camp == null) { msg("Selecione um campeonato!"); return; }
                if (casa == null || vis == null) { msg("Selecione os times!"); return; }
                if (casa.equals(vis)) { msg("Selecione times diferentes!"); return; }

                // Converte data do spinner
                java.util.Date dateData = (java.util.Date) spinnerData.getValue();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dateData);
                LocalDate data = LocalDate.of(
                        cal.get(java.util.Calendar.YEAR),
                        cal.get(java.util.Calendar.MONTH) + 1,
                        cal.get(java.util.Calendar.DAY_OF_MONTH));

                // Converte hora do spinner
                java.util.Date dateHora = (java.util.Date) spinnerHora.getValue();
                cal.setTime(dateHora);
                LocalTime hora = LocalTime.of(
                        cal.get(java.util.Calendar.HOUR_OF_DAY),
                        cal.get(java.util.Calendar.MINUTE));

                boolean ok = partidaController.cadastrarPartida(camp, casa, vis, data, hora);
                msg(ok ? "Partida cadastrada!" : "Erro ao cadastrar. Verifique se os clubes pertencem ao campeonato.");
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

        JComboBox<String> comboCamp    = new JComboBox<>();
        JComboBox<String> comboPartida = new JComboBox<>();

        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        comboCamp.addActionListener(e -> {
            comboPartida.removeAllItems();
            Campeonato c = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
            if (c == null) return;
            for (Partida pt : c.getPartidas())
                if (!pt.isPartidaFinalizada())
                    comboPartida.addItem(pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
        });

        JTextField golsCasa = new JTextField();
        JTextField golsVis  = new JTextField();

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Partida:"));    p.add(comboPartida);
        p.add(new JLabel("Gols casa:"));  p.add(golsCasa);
        p.add(new JLabel("Gols visitante:")); p.add(golsVis);

        JButton salvar = new JButton("Salvar Resultado");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
                if (camp == null) return;

                // Busca a partida pelo índice selecionado
                List<Partida> pendentes = new java.util.ArrayList<>();
                for (Partida pt : camp.getPartidas())
                    if (!pt.isPartidaFinalizada()) pendentes.add(pt);

                int idx = comboPartida.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) { msg("Selecione uma partida!"); return; }

                boolean ok = partidaController.addResultado(
                        pendentes.get(idx),
                        Integer.parseInt(golsCasa.getText().trim()),
                        Integer.parseInt(golsVis.getText().trim()));

                msg(ok ? "Resultado salvo!" : "Erro ao salvar.");
                if (ok) {
                    // Calcula pontuações das apostas dessa partida
                    main.getApostaController().CalcularPontos(camp);
                    // Recarrega partidas pendentes
                    comboPartida.removeAllItems();
                    for (Partida pt : camp.getPartidas())
                        if (!pt.isPartidaFinalizada())
                            comboPartida.addItem(pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
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

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel form() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    private void msg(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private Clube buscarClubePorNome(String nome) {
        if (nome == null) return null;
        for (Clube c : campeonatoController.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}