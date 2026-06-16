package Model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campeonatos")

public class Campeonato {
    private static final int MAX_CLUBES = 8;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "ano", nullable = false)
    private int ano;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "campeonato_clubes", joinColumns = @JoinColumn(name = "campeonato_id"), inverseJoinColumns = @JoinColumn(name = "clube_id"))
    private List<Clube> clubes;

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Partida> partidas;

    public Campeonato(){
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public Campeonato(String nome, int ano) {
        this.nome = nome;
        this.ano = ano;
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public boolean addCLube(Clube clube) {
        if (clubes.size() >= MAX_CLUBES) {
            System.out.println("Limite máximo de 8 clubes atingido");
            return false;
        }

        for (Clube c : clubes) {
            if (c.getId() == clube.getId()) {
                System.out.println("Clube já cadastrado no campeonato.");
                return false;
            }
        }
        clubes.add(clube);
        return true;
    }

    public boolean addPartida(Partida partida) {
        boolean CasaExiste = false;
        boolean VisitanteExiste = false;

        for (Clube clube : clubes) {
            if (clube.getId() == partida.getClubeCasa().getId()){
                CasaExiste = true;
            }

            if (clube.getId() == partida.getClubeVisitante().getId()) {
                VisitanteExiste = true;
            }
        }

        if (!CasaExiste || !VisitanteExiste) {
            System.out.println("Clube não pertence ao campeonato");
            return false;
        }
        partidas.add(partida);
        return true;
    }

    public boolean partidasNaoFinalizadas() {
        for (Partida partida : partidas) {
            if (!partida.isPartidaFinalizada()) {
                return true;
            }
        }
        return false;
    }

    public boolean partidasFinalizadas() {
        for (Partida partida : partidas) {
            if (partida.isPartidaFinalizada()) {
                return true;
            }
        }
        return false;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public int getAno(){
        return ano;
    }
    public void setAno(int ano){
        this.ano = ano;
    }

    public int getId() {
        return id;
    }

    public List<Clube> getClubes(){
        return clubes;
    }

    public List<Partida> getPartidas(){
        return partidas;
    }

    public int getMaxClubes() {
        return MAX_CLUBES;
    }

}
