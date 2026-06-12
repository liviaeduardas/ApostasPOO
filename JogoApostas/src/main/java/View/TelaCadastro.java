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
import java.util.ArrayList;
import java.util.List;

public class TelaCadastro extends JPanel {

    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController    partidaController;
    private GrupoController      grupoController;

    private JPanel    painelForm;
    private CardLayout cardForm;

    // ── referências aos combos que precisam ser recarregados ─────────────────
    // painel Clube
    private JComboBox<String> comboCampClube;

    // painel Partida
    private JComboBox<String> comboCampPartida;
    private JComboBox<String> comboMandante;
    private JComboBox<String> comboVisitante;

    // painel Resultado
    private JComboBox<String> comboCampResultado;
    private JComboBox<String> comboPartidaResultado;

    public TelaCadastro(MainFrame main,
                        CampeonatoController campeonatoController,
                        PartidaController partidaController) {
        this.main                 = main;
        this.campeonatoController = campeonatoController;
        this.partidaController    = partidaController;
        this.grupoController      = main.getGrupoController();

        setLayout(new BorderLayout());

        // ── Menu lateral ──────────────────────────────────────────────────────
        JPanel painelMenu = new JPanel(new GridLayout(6, 1, 5, 5));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelMenu.setPreferredSize(new Dimension(160, 0));

        JButton btnCampeonato = new JButton("Campeonato");
        JButton btnClube      = new JButton("Clube");
        JButton btnPartida    = new JButton("Partida");
        JButton btnResultado  = new JButton("Resultado");
        JButton btnGrupo      = new JButton("Grupo");
        JButton btnSair       = new JButton("Sair");

        // Ao abrir cada painel, recarrega os dados do banco antes de exibir
        btnCampeonato.addActionListener(e -> mostrar("campeonato"));
        btnClube     .addActionListener(e -> { recarregarClube();     mostrar("clube");     });
        btnPartida   .addActionListener(e -> { recarregarPartida();   mostrar("partida");   });
        btnResultado .addActionListener(e -> { recarregarResultado(); mostrar("resultado"); });
        btnGrupo     .addActionListener(e -> mostrar("grupo"));
        btnSair      .addActionListener(e -> main.trocarTela("telaLogin"));

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

    // =========================================================================
    // PAINEL CAMPEONATO
    // =========================================================================
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
                boolean ok = campeonatoController.novoCampeonato(
                        nome.getText(), Integer.parseInt(ano.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Campeonato criado!" : "Dados inválidos.");
                if (ok) { nome.setText(""); ano.setText(""); }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido.");
            }
        });
        p.add(criar);
        return p;
    }

    // =========================================================================
    // PAINEL CLUBE
    // Regra: cada campeonato comporta no máximo 8 clubes (validado no Model).
    // =========================================================================
    private JPanel painelClube() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome  = new JTextField();
        JTextField sigla = new JTextField();

        comboCampClube = new JComboBox<>();

        p.add(new JLabel("Nome:"));       p.add(nome);
        p.add(new JLabel("Sigla:"));      p.add(sigla);
        p.add(new JLabel("Campeonato:")); p.add(comboCampClube);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            String nomeText  = nome.getText().trim();
            String siglaText = sigla.getText().trim();

            if (nomeText.isEmpty() || siglaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome e sigla.");
                return;
            }

            // 1. Verifica campeonato selecionado
            Campeonato camp = campeonatoController.procurarCampeonato(
                    (String) comboCampClube.getSelectedItem());
            if (camp == null) {
                JOptionPane.showMessageDialog(this, "Selecione um campeonato válido.");
                return;
            }

            // 2. Salva o clube no banco
            boolean salvo = campeonatoController.cadastrarClube(nomeText, siglaText);
            if (!salvo) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar clube (sigla/nome duplicado?).");
                return;
            }

            // 3. Busca o clube recém-salvo e vincula ao campeonato
            Clube clube = campeonatoController.procurarClube(siglaText);
            if (clube == null) {
                JOptionPane.showMessageDialog(this, "Erro interno: clube não encontrado após salvar.");
                return;
            }

            boolean adicionou = campeonatoController.addClube(camp, clube);
            JOptionPane.showMessageDialog(this, adicionou
                    ? "Clube cadastrado e adicionado ao campeonato!"
                    : "Limite de 8 clubes atingido para este campeonato.");
            if (adicionou) { nome.setText(""); sigla.setText(""); }
        });
        p.add(cadastrar);
        return p;
    }

    // =========================================================================
    // PAINEL PARTIDA
    // Clubes exibidos são somente os vinculados ao campeonato selecionado.
    // =========================================================================
    private JPanel painelPartida() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboCampPartida = new JComboBox<>();
        comboMandante    = new JComboBox<>();
        comboVisitante   = new JComboBox<>();

        // Ao trocar campeonato, carrega apenas os clubes daquele campeonato
        comboCampPartida.addActionListener(e -> carregarClubesDoCampeonato(
                (String) comboCampPartida.getSelectedItem(),
                comboMandante, comboVisitante));

        // Spinners de data e hora (sem digitação livre — evita parse inválido)
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

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCampPartida.getSelectedItem());
                if (camp == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um campeonato.");
                    return;
                }

                String nomeCasa = (String) comboMandante.getSelectedItem();
                String nomeVis  = (String) comboVisitante.getSelectedItem();
                if (nomeCasa == null || nomeVis == null) {
                    JOptionPane.showMessageDialog(this, "Selecione os times.");
                    return;
                }
                if (nomeCasa.equals(nomeVis)) {
                    JOptionPane.showMessageDialog(this, "Selecione times diferentes.");
                    return;
                }

                // Busca os clubes pelo nome dentro dos clubes do campeonato
                Clube casa = buscarClubeNoCampeonato(camp, nomeCasa);
                Clube vis  = buscarClubeNoCampeonato(camp, nomeVis);
                if (casa == null || vis == null) {
                    JOptionPane.showMessageDialog(this, "Time não encontrado no campeonato.");
                    return;
                }

                LocalDate data = extrairLocalDate(spinnerData);
                LocalTime hora = extrairLocalTime(spinnerHora);

                boolean ok = partidaController.cadastrarPartida(camp, casa, vis, data, hora);
                JOptionPane.showMessageDialog(this, ok ? "Partida cadastrada!" : "Erro ao cadastrar partida.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });
        p.add(cadastrar);
        return p;
    }

    // =========================================================================
    // PAINEL RESULTADO
    // Mostra apenas partidas NÃO finalizadas do campeonato selecionado.
    // =========================================================================
    private JPanel painelResultado() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboCampResultado    = new JComboBox<>();
        comboPartidaResultado = new JComboBox<>();

        comboCampResultado.addActionListener(e ->
                carregarPartidasPendentes((String) comboCampResultado.getSelectedItem()));

        JTextField golsCasa = new JTextField();
        JTextField golsVis  = new JTextField();

        p.add(new JLabel("Campeonato:"));     p.add(comboCampResultado);
        p.add(new JLabel("Partida:"));         p.add(comboPartidaResultado);
        p.add(new JLabel("Gols casa:"));       p.add(golsCasa);
        p.add(new JLabel("Gols visitante:"));  p.add(golsVis);

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCampResultado.getSelectedItem());
                if (camp == null) return;

                // Reconstrói lista de pendentes na hora do clique (evita índice desatualizado)
                List<Partida> pendentes = new ArrayList<>();
                for (Partida pt : camp.getPartidas())
                    if (!pt.isPartidaFinalizada()) pendentes.add(pt);

                int idx = comboPartidaResultado.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) {
                    JOptionPane.showMessageDialog(this, "Selecione uma partida.");
                    return;
                }

                int gCasa = Integer.parseInt(golsCasa.getText().trim());
                int gVis  = Integer.parseInt(golsVis.getText().trim());

                boolean ok = partidaController.addResultado(pendentes.get(idx), gCasa, gVis);
                JOptionPane.showMessageDialog(this, ok ? "Resultado salvo!" : "Erro ao salvar resultado.");

                if (ok) {
                    // Atualiza o combo de partidas pendentes após salvar
                    carregarPartidasPendentes((String) comboCampResultado.getSelectedItem());
                    golsCasa.setText(""); golsVis.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite números válidos para os gols.");
            }
        });
        p.add(salvar);
        return p;
    }

    // =========================================================================
    // PAINEL GRUPO
    // Regra: máximo 5 grupos; só Administrador pode criar (validado no Controller).
    // =========================================================================
    private JPanel painelGrupo() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nomeGrupo = new JTextField();
        p.add(new JLabel("Nome do grupo:")); p.add(nomeGrupo);

        JButton criar = new JButton("Criar grupo");
        criar.addActionListener(e -> {
            boolean ok = grupoController.criarGrupo(nomeGrupo.getText(), main.getAdminLogado());
            JOptionPane.showMessageDialog(this, ok
                    ? "Grupo criado!"
                    : "Não foi possível criar o grupo (limite atingido ou nome inválido).");
            if (ok) nomeGrupo.setText("");
        });
        p.add(criar);
        return p;
    }

    // =========================================================================
    // MÉTODOS DE RECARGA — chamados ao abrir cada painel para garantir dados frescos
    // =========================================================================

    /** Recarrega o combo de campeonatos do painel Clube. */
    private void recarregarClube() {
        comboCampClube.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampClube.addItem(c.getNome());
    }

    /** Recarrega campeonatos e dispara o listener para carregar os clubes do primeiro. */
    private void recarregarPartida() {
        comboCampPartida.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampPartida.addItem(c.getNome());
        // O listener de comboCampPartida já chama carregarClubesDoCampeonato automaticamente
    }

    /** Recarrega campeonatos e as partidas pendentes do primeiro campeonato. */
    private void recarregarResultado() {
        comboCampResultado.removeAllItems();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCampResultado.addItem(c.getNome());
        // O listener de comboCampResultado já chama carregarPartidasPendentes automaticamente
    }

    // =========================================================================
    // AUXILIARES
    // =========================================================================

    /**
     * Carrega nos combos de mandante/visitante apenas os clubes do campeonato
     * selecionado (regra: cada campeonato tem seus próprios clubes).
     */
    private void carregarClubesDoCampeonato(String nomeCamp,
                                            JComboBox<String> mandante,
                                            JComboBox<String> visitante) {
        mandante.removeAllItems();
        visitante.removeAllItems();
        if (nomeCamp == null) return;

        Campeonato camp = campeonatoController.procurarCampeonato(nomeCamp);
        if (camp == null) return;

        for (Clube c : camp.getClubes()) {
            mandante.addItem(c.getNome());
            visitante.addItem(c.getNome());
        }
    }

    /**
     * Preenche o combo de partidas com as partidas PENDENTES do campeonato informado.
     */
    private void carregarPartidasPendentes(String nomeCamp) {
        comboPartidaResultado.removeAllItems();
        if (nomeCamp == null) return;

        Campeonato camp = campeonatoController.procurarCampeonato(nomeCamp);
        if (camp == null) return;

        for (Partida pt : camp.getPartidas())
            if (!pt.isPartidaFinalizada())
                comboPartidaResultado.addItem(
                        pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
    }

    /**
     * Busca um clube pelo nome dentro da lista de clubes do campeonato.
     * Evita buscar no banco um clube que não pertence ao campeonato.
     */
    private Clube buscarClubeNoCampeonato(Campeonato camp, String nome) {
        if (nome == null) return null;
        for (Clube c : camp.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }

    /** Extrai LocalDate de um JSpinner configurado com formato de data. */
    private LocalDate extrairLocalDate(JSpinner spinner) {
        java.util.Date d = (java.util.Date) spinner.getValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(d);
        return LocalDate.of(
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH));
    }

    /** Extrai LocalTime de um JSpinner configurado com formato de hora. */
    private LocalTime extrairLocalTime(JSpinner spinner) {
        java.util.Date d = (java.util.Date) spinner.getValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(d);
        return LocalTime.of(
                cal.get(java.util.Calendar.HOUR_OF_DAY),
                cal.get(java.util.Calendar.MINUTE));
    }
}