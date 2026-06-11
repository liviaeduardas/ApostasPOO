package View;

import Controller.CampeonatoController;
import Controller.GrupoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;
import Model.Partida;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TelaCadastro extends JPanel {

    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private GrupoController grupoController;
    private JPanel painelForm;
    private CardLayout cardForm;

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.main                 = main;
        this.campeonatoController = campeonatoController;
        this.partidaController    = partidaController;
        this.grupoController      = main.getGrupoController();

        setLayout(new BorderLayout());

        // Menu lateral
        JPanel painelMenu = new JPanel(new GridLayout(6, 1, 5, 5));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelMenu.setPreferredSize(new Dimension(160, 0));

        JButton btnCampeonato = new JButton("Campeonato");
        JButton btnClube      = new JButton("Clube");
        JButton btnPartida    = new JButton("Partida");
        JButton btnResultado  = new JButton("Resultado");
        JButton btnGrupo      = new JButton("Grupo");
        JButton btnSair       = new JButton("Sair");

        btnCampeonato.addActionListener(e -> mostrar("campeonato"));
        btnClube.addActionListener(e ->      mostrar("clube"));
        btnPartida.addActionListener(e ->    mostrar("partida"));
        btnResultado.addActionListener(e ->  mostrar("resultado"));
        btnGrupo.addActionListener(e ->      mostrar("grupo"));
        btnSair.addActionListener(e ->       main.trocarTela("telaLogin"));

        painelMenu.add(btnCampeonato);
        painelMenu.add(btnClube);
        painelMenu.add(btnPartida);
        painelMenu.add(btnResultado);
        painelMenu.add(btnGrupo);
        painelMenu.add(btnSair);

        cardForm   = new CardLayout();
        painelForm = new JPanel(cardForm);
        painelForm.add(painelCampeonato(), "campeonato");
        painelForm.add(painelClube(),      "clube");
        painelForm.add(painelPartida(),    "partida");
        painelForm.add(painelResultado(),  "resultado");
        painelForm.add(painelGrupo(),      "grupo");

        add(painelMenu,  BorderLayout.WEST);
        add(painelForm,  BorderLayout.CENTER);
    }

    private void mostrar(String nome) {
        cardForm.show(painelForm, nome);
    }

    // ── Campeonato ────────────────────────────────────────────────────────────

    private JPanel painelCampeonato() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome = new JTextField();
        JTextField ano  = new JTextField();

        p.add(new JLabel("Nome:")); p.add(nome);
        p.add(new JLabel("Ano:"));  p.add(ano);

        JButton criar = new JButton("Criar");
        criar.addActionListener(e -> {
            try {
                boolean ok = campeonatoController.novoCampeonato(nome.getText(), Integer.parseInt(ano.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Campeonato criado!" : "Dados inválidos.");
                if (ok) { nome.setText(""); ano.setText(""); }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido.");
            }
        });
        p.add(criar);
        return p;
    }

    // ── Clube ─────────────────────────────────────────────────────────────────

    private JPanel painelClube() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome  = new JTextField();
        JTextField sigla = new JTextField();

        // Combo de campeonatos — carrega automaticamente ao abrir
        JComboBox<String> comboCamp = new JComboBox<>();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        p.add(new JLabel("Nome:"));       p.add(nome);
        p.add(new JLabel("Sigla:"));      p.add(sigla);
        p.add(new JLabel("Campeonato:")); p.add(comboCamp);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            // 1. Cadastra o clube no banco
            boolean ok = campeonatoController.cadastrarClube(nome.getText(), sigla.getText());
            if (!ok) { JOptionPane.showMessageDialog(this, "Dados inválidos."); return; }

            // 2. Busca o campeonato selecionado
            Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
            if (camp == null) { JOptionPane.showMessageDialog(this, "Selecione um campeonato!"); return; }

            // 3. Busca o clube recém cadastrado e adiciona ao campeonato
            Clube clube = campeonatoController.procurarClube(sigla.getText().trim());
            boolean adicionou = campeonatoController.addClube(camp, clube);

            JOptionPane.showMessageDialog(this, adicionou
                    ? "Clube cadastrado e adicionado ao campeonato!"
                    : "Limite de 8 clubes atingido!");
            if (adicionou) { nome.setText(""); sigla.setText(""); }
        });
        p.add(cadastrar);
        return p;
    }

    // ── Partida ───────────────────────────────────────────────────────────────

    private JPanel painelPartida() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Combos carregados automaticamente
        JComboBox<String> comboCamp   = new JComboBox<>();
        JComboBox<String> mandante    = new JComboBox<>();
        JComboBox<String> visitante   = new JComboBox<>();

        // Carrega campeonatos automaticamente
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        // Carrega clubes automaticamente
        for (Clube c : campeonatoController.getClubes()) {
            mandante.addItem(c.getNome());
            visitante.addItem(c.getNome());
        }

        // Ao trocar campeonato, recarrega os clubes do campeonato selecionado
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

        // Seletor de data — spinner com formato de data
        SpinnerDateModel modeloData = new SpinnerDateModel();
        JSpinner spinnerData = new JSpinner(modeloData);
        spinnerData.setEditor(new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy"));

        // Seletor de hora — spinner com formato de hora
        SpinnerDateModel modeloHora = new SpinnerDateModel();
        JSpinner spinnerHora = new JSpinner(modeloHora);
        spinnerHora.setEditor(new JSpinner.DateEditor(spinnerHora, "HH:mm"));

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Casa:"));       p.add(mandante);
        p.add(new JLabel("Visitante:"));  p.add(visitante);
        p.add(new JLabel("Data:"));       p.add(spinnerData);
        p.add(new JLabel("Hora:"));       p.add(spinnerHora);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
                Clube casa = buscarClubePorNome((String) mandante.getSelectedItem());
                Clube vis  = buscarClubePorNome((String) visitante.getSelectedItem());

                if (casa == null || vis == null) {
                    JOptionPane.showMessageDialog(this, "Selecione os times!");
                    return;
                }
                if (casa.equals(vis)) {
                    JOptionPane.showMessageDialog(this, "Selecione times diferentes!");
                    return;
                }

                // Converte a data do spinner para LocalDate
                java.util.Date dateData = (java.util.Date) spinnerData.getValue();
                java.util.Calendar calData = java.util.Calendar.getInstance();
                calData.setTime(dateData);
                LocalDate data = LocalDate.of(
                        calData.get(java.util.Calendar.YEAR),
                        calData.get(java.util.Calendar.MONTH) + 1,
                        calData.get(java.util.Calendar.DAY_OF_MONTH)
                );

                // Converte a hora do spinner para LocalTime
                java.util.Date dateHora = (java.util.Date) spinnerHora.getValue();
                java.util.Calendar calHora = java.util.Calendar.getInstance();
                calHora.setTime(dateHora);
                LocalTime hora = LocalTime.of(
                        calHora.get(java.util.Calendar.HOUR_OF_DAY),
                        calHora.get(java.util.Calendar.MINUTE)
                );

                boolean ok = partidaController.cadastrarPartida(camp, casa, vis, data, hora);
                JOptionPane.showMessageDialog(this, ok ? "Partida cadastrada!" : "Erro ao cadastrar.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });
        p.add(cadastrar);
        return p;
    }

    // ── Resultado ─────────────────────────────────────────────────────────────

    private JPanel painelResultado() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboCamp    = new JComboBox<>();
        JComboBox<String> comboPartida = new JComboBox<>();

        // Carrega campeonatos automaticamente
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        // Ao trocar campeonato, carrega partidas pendentes
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

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
                if (camp == null) return;

                List<Partida> pendentes = new java.util.ArrayList<>();
                for (Partida pt : camp.getPartidas())
                    if (!pt.isPartidaFinalizada()) pendentes.add(pt);

                int idx = comboPartida.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) return;

                boolean ok = partidaController.addResultado(
                        pendentes.get(idx),
                        Integer.parseInt(golsCasa.getText()),
                        Integer.parseInt(golsVis.getText())
                );

                JOptionPane.showMessageDialog(this, ok ? "Resultado salvo!" : "Erro.");
                if (ok) {
                    // Atualiza a lista de partidas pendentes
                    comboPartida.removeAllItems();
                    for (Partida pt : camp.getPartidas())
                        if (!pt.isPartidaFinalizada())
                            comboPartida.addItem(pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
                    golsCasa.setText(""); golsVis.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite números válidos para os gols.");
            }
        });
        p.add(salvar);
        return p;
    }

    // ── Grupo ─────────────────────────────────────────────────────────────────

    private JPanel painelGrupo() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nomeGrupo = new JTextField();
        p.add(new JLabel("Nome do grupo:")); p.add(nomeGrupo);

        JButton criar = new JButton("Criar grupo");
        criar.addActionListener(e -> {
            boolean ok = grupoController.criarGrupo(nomeGrupo.getText(), main.getAdminLogado());
            JOptionPane.showMessageDialog(this, ok ? "Grupo criado!" : "Não foi possível criar o grupo.");
            if (ok) nomeGrupo.setText("");
        });
        p.add(criar);
        return p;
    }

    // ── Auxiliar ──────────────────────────────────────────────────────────────

    private Clube buscarClubePorNome(String nome) {
        if (nome == null) return null;
        for (Clube c : campeonatoController.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}