package View;

import Controller.CampeonatoController;
import Controller.PartidaController;

import javax.swing.*;

public class TelaCadastro extends JPanel {

    private MainFrame main;
    private CampeonatoController campeonatoController;
    private PartidaController partidaController;

    public TelaCadastro(MainFrame main,
                        CampeonatoController campeonatoController,
                        PartidaController partidaController) {

        this.main = main;
        this.campeonatoController = campeonatoController;
        this.partidaController = partidaController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JTabbedPane abas = new JTabbedPane();

        abas.add("Campeonato", painelCampeonato());
        abas.add("Clube", painelClube());
        abas.add("Partida", painelPartida());
        abas.add("Resultado", painelResultado());

        add(abas);

        JButton sair = new JButton("Sair");

        sair.addActionListener(e ->
                main.trocarTela("telaLogin"));

        add(sair);
    }

    private JPanel painelCampeonato() {

        JPanel p = new JPanel();

        p.add(new JLabel("Nome"));
        JTextField nome = new JTextField(15);
        p.add(nome);

        p.add(new JLabel("Ano"));
        JTextField ano = new JTextField(5);
        p.add(ano);

        JButton criar = new JButton("Criar");

        criar.addActionListener(e -> {

            try {

                campeonatoController.NovoCampeonato(
                        nome.getText(),
                        Integer.parseInt(ano.getText())
                );

                JOptionPane.showMessageDialog(this,
                        "Campeonato criado");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Erro");

            }
        });

        p.add(criar);

        return p;
    }

    private JPanel painelClube() {
        JPanel p = new JPanel();

        p.add(new JLabel("Nome"));
        p.add(new JTextField(15));

        p.add(new JLabel("Sigla"));
        p.add(new JTextField(5));

        p.add(new JButton("Cadastrar"));

        return p;
    }

    private JPanel painelPartida() {
        JPanel p = new JPanel();

        p.add(new JLabel("Mandante"));
        p.add(new JComboBox<>());

        p.add(new JLabel("Visitante"));
        p.add(new JComboBox<>());

        p.add(new JButton("Cadastrar"));

        return p;
    }

    private JPanel painelResultado() {
        JPanel p = new JPanel();

        p.add(new JLabel("Gols Casa"));
        p.add(new JTextField(5));

        p.add(new JLabel("Gols Visitante"));
        p.add(new JTextField(5));

        p.add(new JButton("Salvar"));

        return p;
    }
}