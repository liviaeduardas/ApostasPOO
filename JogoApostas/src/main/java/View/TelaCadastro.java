package View;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Controller.PartidaController;
import Model.Campeonato;
import Model.Clube;
import Model.Grupo;
import Model.Partida;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TelaCadastro extends JPanel {
    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private GrupoController grupoController;
    private JPanel painelMenu;
    private JPanel painelForm;
    private CardLayout cardForm;

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.main = main;
        this.campeonatoController = campeonatoController;
        this.partidaController = partidaController;
        this.grupoController = main.getGrupoController();

        setLayout(new BorderLayout());

        painelMenu = new JPanel(new GridLayout(6, 1, 5, 5));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnCampeonato = new JButton("Campeonato");
        JButton btnClube = new JButton("Clube");
        JButton btnPartida = new JButton("Partida");
        JButton btnResultado = new JButton("Resultado");
        JButton btnGrupo = new JButton("Grupo");
        JButton btnSair = new JButton("Sair");

        btnCampeonato.addActionListener(e -> mostrar("campeonato"));
        btnClube.addActionListener(e -> mostrar("clube"));
        btnPartida.addActionListener(e -> mostrar("partida"));
        btnResultado.addActionListener(e -> mostrar("resultado"));
        btnGrupo.addActionListener(e -> mostrar("grupo"));
        btnSair.addActionListener(e -> main.trocarTela("telaLogin"));

        painelMenu.add(btnCampeonato);
        painelMenu.add(btnClube);
        painelMenu.add(btnPartida);
        painelMenu.add(btnResultado);
        painelMenu.add(btnGrupo);
        painelMenu.add(btnSair);

        cardForm = new CardLayout();
        painelForm = new JPanel(cardForm);
        painelForm.add(painelCampeonato(), "campeonato");
        painelForm.add(painelClube(), "clube");
        painelForm.add(painelPartida(), "partida");
        painelForm.add(painelResultado(), "resultado");
        painelForm.add(painelGrupo(), "grupo");

        add(painelMenu, BorderLayout.WEST);
        add(painelForm, BorderLayout.CENTER);
    }

    private void mostrar(String nome) { cardForm.show(painelForm, nome); }

    //Campeonato
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

    //Clube
    private JPanel painelClube() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome  = new JTextField();
        JTextField sigla = new JTextField();

        p.add(new JLabel("Nome:"));  p.add(nome);
        p.add(new JLabel("Sigla:")); p.add(sigla);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            boolean ok = campeonatoController.cadastrarClube(nome.getText(), sigla.getText());
            JOptionPane.showMessageDialog(this, ok ? "Clube cadastrado!" : "Dados inválidos.");
            if (ok) { nome.setText(""); sigla.setText(""); }
        });
        p.add(cadastrar);
        return p;
    }

    //Partida
    private JPanel painelPartida() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboCamp = new JComboBox<>();
        JComboBox<String> mandante = new JComboBox<>();
        JComboBox<String> visitante = new JComboBox<>();
        JTextField data = new JTextField("AAAA-MM-DD");
        JTextField hora = new JTextField("HH:MM");

        JButton recarregar = new JButton("Recarregar listas");
        recarregar.addActionListener(e -> {
            comboCamp.removeAllItems();
            mandante .removeAllItems();
            visitante.removeAllItems();
            for (Campeonato c : campeonatoController.getCampeonatos()) comboCamp.addItem(c.getNome());
            for (Clube c : campeonatoController.getClubes()) {
                mandante .addItem(c.getNome());
                visitante.addItem(c.getNome());
            }
        });

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Casa:"));       p.add(mandante);
        p.add(new JLabel("Visitante:"));  p.add(visitante);
        p.add(new JLabel("Data:"));       p.add(data);
        p.add(new JLabel("Hora:"));       p.add(hora);
        p.add(recarregar);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
                Clube casa = buscarClubePorNome((String) mandante.getSelectedItem());
                Clube vis  = buscarClubePorNome((String) visitante.getSelectedItem());
                boolean ok = partidaController.cadastrarPartida(
                        camp, casa, vis,
                        LocalDate.parse(data.getText()),
                        LocalTime.parse(hora.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Partida cadastrada!" : "Erro ao cadastrar.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos: " + ex.getMessage());
            }
        });
        p.add(cadastrar);
        return p;
    }

    //Resultado
    private JPanel painelResultado() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboCamp    = new JComboBox<>();
        JComboBox<String> comboPartida = new JComboBox<>();

        for (Campeonato c : campeonatoController.getCampeonatos()) comboCamp.addItem(c.getNome());

        comboCamp.addActionListener(e -> {
            comboPartida.removeAllItems();
            Campeonato c = campeonatoController.procurarCampeonato((String) comboCamp.getSelectedItem());
            if (c == null) return;
            for (Partida pt : c.getPartidas())
                if (!pt.isPartidaFinalizada())
                    comboPartida.addItem(
                            pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
        });

        JTextField golsCasa = new JTextField();
        JTextField golsVis  = new JTextField();

        p.add(new JLabel("Campeonato:")); p.add(comboCamp);
        p.add(new JLabel("Partida:")); p.add(comboPartida);
        p.add(new JLabel("Gols casa:")); p.add(golsCasa);
        p.add(new JLabel("Gols visitante:")); p.add(golsVis);

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCamp.getSelectedItem());
                if (camp == null) return;

                List<Partida> pendentes = new java.util.ArrayList<>();
                for (Partida pt : camp.getPartidas())
                    if (!pt.isPartidaFinalizada()) pendentes.add(pt);

                int idx = comboPartida.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) return;

                boolean ok = partidaController.addResultado(
                        pendentes.get(idx),
                        Integer.parseInt(golsCasa.getText()),
                        Integer.parseInt(golsVis.getText()));

                JOptionPane.showMessageDialog(this, ok ? "Resultado salvo!" : "Erro.");
                if (ok) {

                    comboPartida.removeAllItems();
                    for (Partida pt : camp.getPartidas())
                        if (!pt.isPartidaFinalizada())
                            comboPartida.addItem(
                                    pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
                    golsCasa.setText(""); golsVis.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite números válidos para os gols.");
            }
        });
        p.add(salvar);
        return p;
    }

    //Grupo
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

    //Auxiliar
    private Clube buscarClubePorNome(String nome) {
        if (nome == null) return null;
        for (Clube c : campeonatoController.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}
