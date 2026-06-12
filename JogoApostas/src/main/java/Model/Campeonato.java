package Model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

//tabela no banco
@Entity
@Table(name = "campeonatos")
public class Campeonato {
    private static final int MAX_CLUBES = 8;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // banco gera automático
    private int id;

    // nullable não pode ser nulo | length é o tamanho máximo do texto
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "ano", nullable = false)
    private int ano;

    // Um campeonato tem vários clubes e um clube pode estar em vários campeonatos
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "campeonato_clubes", //hibernate cria tabela intermediária
            joinColumns = @JoinColumn(name = "campeonato_id"),  // chave do campeonato
            inverseJoinColumns = @JoinColumn(name = "clube_id") // chave do clube
    )
    private List<Clube> clubes;

    // Um campeonato tem várias partidas
    @OneToMany( //define relação entre a tabela
            mappedBy = "campeonato", // na campeonato partidas terá um atributo campeonato
            cascade = CascadeType.ALL, // defini que deve fazer  tudo com uma tabela o que ocorrer com a outra
            fetch = FetchType.EAGER)// apostas são carregadas do banco so quando chamar
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
            if (c == clube) {
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
            if (clube == partida.getClubeCasa()) {
                CasaExiste = true;
            }

            if (clube == partida.getClubeVisitante()) {
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
