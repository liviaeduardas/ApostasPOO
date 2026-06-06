package View;

import Controller.CampeonatoController;
import Model.Campeonato;
import Model.Partida;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaResultados extends JPanel {

    private MainFrame main;
    private CampeonatoController campCtrl;

    private JComboBox<String> comboCamp;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaResultados(MainFrame main, CampeonatoController campCtrl) {
        this.main = main;
        this.campCtrl = campCtrl;

        setLayout(new BorderLayout());

        JPanel topo = new JPanel();

        JLabel titulo = new JLabel("Resultados");
        JButton voltar = new JButton("Voltar");

        voltar.addActionListener(e ->
                main.trocarTela("telaApostas"));

        topo.add(titulo);
        topo.add(voltar);

        add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel();

        centro.add(new JLabel("Campeonato:"));

        comboCamp = new JComboBox<>();
        comboCamp.addActionListener(e -> atualizarTabela());

        centro.add(comboCamp);

        add(centro, BorderLayout.SOUTH);

        modelo = new DefaultTableModel();
        modelo.addColumn("Mandante");
        modelo.addColumn("Visitante");
        modelo.addColumn("Placar");
        modelo.addColumn("Resultado");

        tabela = new JTable(modelo);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void atualizarTabela() {

        modelo.setRowCount(0);

        String nome = (String) comboCamp.getSelectedItem();

        if(nome == null){
            return;
        }

        Campeonato campeonato =
                campCtrl.ProcurarCampeonato(nome);

        if(campeonato == null){
            return;
        }

        for(Partida p : campeonato.getPartidas()){

            if(!p.isPartidaFinalizada()){
                continue;
            }

            String resultado;

            if(p.getResultado() == 1){
                resultado =
                        p.getClubeCasa().getNome() + " venceu";
            }
            else if(p.getResultado() == 2){
                resultado =
                        p.getClubeVisitante().getNome() + " venceu";
            }
            else{
                resultado = "Empate";
            }

            modelo.addRow(new Object[]{
                    p.getClubeCasa().getNome(),
                    p.getClubeVisitante().getNome(),
                    p.getGolsCasa() + " x " + p.getGolsVisitante(),
                    resultado
            });
        }
    }

    public void atualizar(){

        comboCamp.removeAllItems();

        for(Campeonato c : campCtrl.getCampeonatos()){
            comboCamp.addItem(c.getNome());
        }

        atualizarTabela();
    }
}