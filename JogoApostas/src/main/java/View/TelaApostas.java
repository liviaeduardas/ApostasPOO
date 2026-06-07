package View;
import Controller.ApostaController;
import Controller.CampeonatoController;
import Controller.GrupoController;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaApostas extends JPanel {

    private MainFrame main;
    private ApostaController apostaController;
    private CampeonatoController campeonatoController;
    private GrupoController grupoController;

    private JComboBox<String> comboCampeonato;
    private JComboBox<String> comboPartida;
    private JTextField txtGolsCasa;
    private JTextField txtGolsVisitante;

    public TelaApostas(MainFrame main,
                       ApostaController apostaController,
                       CampeonatoController campeonatoController,
                       GrupoController grupoController) {

        this.main = main;
        this.apostaController = apostaController;
        this.campeonatoController = campeonatoController;
        this.grupoController = grupoController;

        setLayout(new GridLayout(7,2));

        add(new JLabel("Campeonato"));
        comboCampeonato = new JComboBox<>();
        add(comboCampeonato);

        add(new JLabel("Partida"));
        comboPartida = new JComboBox<>();
        add(comboPartida);

        add(new JLabel("Gols Casa"));
        txtGolsCasa = new JTextField();
        add(txtGolsCasa);

        add(new JLabel("Gols Visitante"));
        txtGolsVisitante = new JTextField();
        add(txtGolsVisitante);

        JButton apostar = new JButton("Apostar");
        add(apostar);

        JButton sair = new JButton("Sair");
        add(sair);

        apostar.addActionListener(e -> fazerAposta());

        sair.addActionListener(e ->
                main.trocarTela("telaLogin"));

        comboCampeonato.addActionListener(e ->
                carregarPartidas());
    }

    private void fazerAposta() {

        try {

            Participante participante = main.getParticipanteLogado();

            Partida partida = getPartidaSelecionada();

            int golsCasa =
                    Integer.parseInt(txtGolsCasa.getText());

            int golsVisitante =
                    Integer.parseInt(txtGolsVisitante.getText());

            boolean ok =
                    apostaController.fazerAposta(
                            participante,
                            partida,
                            golsCasa,
                            golsVisitante);

            if(ok){
                JOptionPane.showMessageDialog(this,
                        "Aposta realizada!");
            }else{
                JOptionPane.showMessageDialog(this,
                        "Não foi possível apostar.");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Digite valores válidos.");

        }
    }

    private void carregarPartidas() {

        comboPartida.removeAllItems();

        String nome =
                (String) comboCampeonato.getSelectedItem();

        Campeonato campeonato =
                campeonatoController.procurarCampeonato(nome);

        if(campeonato == null){
            return;
        }

        for(Partida partida : campeonato.getPartidas()){
            if(!partida.isPartidaFinalizada()){
                comboPartida.addItem(partida.toString());
            }
        }
    }

    private Partida getPartidaSelecionada() {

        String texto =
                (String) comboPartida.getSelectedItem();

        if(texto == null){
            return null;
        }

        for(Campeonato c :
                campeonatoController.getCampeonatos()) {

            for(Partida p : c.getPartidas()) {

                if(p.toString().equals(texto)){
                    return p;
                }
            }
        }

        return null;
    }

    public void atualizar() {

        comboCampeonato.removeAllItems();

        for(Campeonato campeonato :
                campeonatoController.getCampeonatos()) {

            comboCampeonato.addItem(
                    campeonato.getNome());
        }

        carregarPartidas();
    }
}