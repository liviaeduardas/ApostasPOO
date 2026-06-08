package View;
import Controller.CampeonatoController;
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
    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;
    private JPanel painelMenu;
    private JPanel painelForm;
    private CardLayout cardForm;

    public TelaCadastro(MainFrame main, CampeonatoController campeonatoController, PartidaController partidaController) {
        this.main = main;
        this.campeonatoController = campeonatoController;
        this.partidaController = partidaController;

        setLayout(new BorderLayout());

        painelMenu = new JPanel(new GridLayout(5, 1, 5, 5));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnCampeonato = new JButton("Campeonato");
        JButton btnClube = new JButton("Clube");
        JButton btnPartida = new JButton("Partida");
        JButton btnResultado = new JButton("Resultado");
        JButton btnSair = new JButton("Sair");

        btnCampeonato.addActionListener(e -> mostrar("campeonato"));
        btnClube.addActionListener(e -> mostrar("clube"));
        btnPartida.addActionListener(e -> mostrar("partida"));
        btnResultado.addActionListener(e -> mostrar("resultado"));
        btnSair.addActionListener(e -> main.trocarTela("telaLogin"));

        painelMenu.add(btnCampeonato);
        painelMenu.add(btnClube);
        painelMenu.add(btnPartida);
        painelMenu.add(btnResultado);
        painelMenu.add(btnSair);

        cardForm = new CardLayout();
        painelForm = new JPanel(cardForm);
        painelForm.add(painelCampeonato(), "campeonato");
        painelForm.add(painelClube(), "clube");
        painelForm.add(painelPartida(), "partida");
        painelForm.add(painelResultado(), "resultado");

        add(painelMenu, BorderLayout.WEST);
        add(painelForm, BorderLayout.CENTER);
    }

    private void mostrar(String nome) {
        cardForm.show(painelForm, nome);
    }

    private JPanel painelCampeonato() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome = new JTextField();
        JTextField ano = new JTextField();

        p.add(new JLabel("Nome:"));
        p.add(nome);
        p.add(new JLabel("Ano:"));
        p.add(ano);

        JButton criar = new JButton("Criar");
        criar.addActionListener(e -> {
            try {
                boolean ok = campeonatoController.novoCampeonato(nome.getText(), Integer.parseInt(ano.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Campeonato criado!" : "Dados inválidos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido.");
            }
        });
        p.add(criar);

        return p;
    }

    private JPanel painelClube() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nome = new JTextField();
        JTextField sigla = new JTextField();

        p.add(new JLabel("Nome:"));
        p.add(nome);
        p.add(new JLabel("Sigla:"));
        p.add(sigla);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            boolean ok = campeonatoController.cadastrarClube(nome.getText(), sigla.getText());
            JOptionPane.showMessageDialog(this, ok ? "Clube cadastrado!" : "Dados inválidos.");
        });
        p.add(cadastrar);

        return p;
    }

    private JPanel painelPartida() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboCamp = new JComboBox<>();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        JComboBox<String> mandante = new JComboBox<>();
        JComboBox<String> visitante = new JComboBox<>();
        for (Clube c : campeonatoController.getClubes()) {
            mandante.addItem(c.getNome());
            visitante.addItem(c.getNome());
        }

        JTextField data = new JTextField("AAAA-MM-DD");
        JTextField hora = new JTextField("HH:MM");

        p.add(new JLabel("Campeonato:"));
        p.add(comboCamp);
        p.add(new JLabel("Casa:"));
        p.add(mandante);
        p.add(new JLabel("Visitante:"));
        p.add(visitante);
        p.add(new JLabel("Data:"));
        p.add(data);
        p.add(new JLabel("Hora:"));
        p.add(hora);

        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCamp.getSelectedItem());
                Clube casa = buscarClubePorNome((String) mandante.getSelectedItem());
                Clube vis  = buscarClubePorNome((String) visitante.getSelectedItem());
                boolean ok = partidaController.cadastrarPartida(
                        camp, casa, vis,
                        LocalDate.parse(data.getText()),
                        LocalTime.parse(hora.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Partida cadastrada!" : "Erro.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos.");
            }
        });
        p.add(cadastrar);

        return p;
    }

    private JPanel painelResultado() {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboCamp = new JComboBox<>();
        for (Campeonato c : campeonatoController.getCampeonatos())
            comboCamp.addItem(c.getNome());

        JComboBox<String> comboPartida = new JComboBox<>();
        comboCamp.addActionListener(e -> {
            comboPartida.removeAllItems();
            Campeonato c = campeonatoController.procurarCampeonato(
                    (String) comboCamp.getSelectedItem());
            if (c != null)
                for (Partida pt : partidaController.getPartidasPendentes(c))
                    comboPartida.addItem(
                            pt.getClubeCasa().getNome() + " x " + pt.getClubeVisitante().getNome());
        });

        JTextField golsCasa = new JTextField();
        JTextField golsVis  = new JTextField();

        p.add(new JLabel("Campeonato:"));
        p.add(comboCamp);
        p.add(new JLabel("Partida:"));
        p.add(comboPartida);
        p.add(new JLabel("Gols mandante:"));
        p.add(golsCasa);
        p.add(new JLabel("Gols visitante:"));
        p.add(golsVis);

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> {
            try {
                Campeonato camp = campeonatoController.procurarCampeonato(
                        (String) comboCamp.getSelectedItem());
                List<Partida> pendentes = partidaController.getPartidasPendentes(camp);
                int idx = comboPartida.getSelectedIndex();
                if (idx < 0 || idx >= pendentes.size()) return;
                boolean ok = partidaController.addResultado(
                        pendentes.get(idx),
                        Integer.parseInt(golsCasa.getText()),
                        Integer.parseInt(golsVis.getText()));
                JOptionPane.showMessageDialog(this, ok ? "Resultado salvo!" : "Erro.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos.");
            }
        });
        p.add(salvar);

        return p;
    }

    private Clube buscarClubePorNome(String nome) {
        for (Clube c : campeonatoController.getClubes())
            if (c.getNome().equals(nome)) return c;
        return null;
    }
}