package Model;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "partidas")

//como o Hibernate lida com a herança de Partida,  uma tabela só para todos os tipos de partida
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

//coluna discriminadora dizendo qual tipo é cada linha.
@DiscriminatorColumn(name = "tipo_partida", discriminatorType = DiscriminatorType.STRING)

public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Muitas partidas pertencem a um campeonato
    @ManyToOne(fetch = FetchType.LAZY)
    //O Hibernate cria a coluna campeonato_id na tabela partidas automaticamente
    //é a chave estrangeira que liga as duas tabelas.
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    // Clube que joga em casa
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn — define o nome da coluna da chave estrangeira no banco.
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    private Clube ClubeCasa;

    // Clube que joga fora
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    private Clube ClubeVisitante;

    @Column(name = "data_partida", nullable = false)
    private LocalDate DataPartida;

    @Column(name = "hora_partida", nullable = false)
    private LocalTime HoraPartida;

    @Column(name = "gol_mandante")
    private int GolsCasa;

    @Column(name = "gol_visitante")
    private int GolsVisitante;

    @Column(name = "encerrada")
    private boolean PartidaFinalizada;

    public Partida() {
        this.GolsCasa = 0;
        this.GolsVisitante = 0;
        this.PartidaFinalizada = false;
    }

    public Partida(Clube ClubeCasa, Clube ClubeVisitante, LocalDate DataPartida, LocalTime HoraPartida) {
        this.ClubeCasa = ClubeCasa;
        this.ClubeVisitante = ClubeVisitante;
        this.DataPartida = DataPartida;
        this.HoraPartida = HoraPartida;
        this.GolsCasa = 0;
        this.GolsVisitante = 0;
        this.PartidaFinalizada = false;
    }

    public Clube getClubeCasa(){
        return ClubeCasa;
    }
    public void setClubeCasa(Clube ClubeCasa){
        this.ClubeCasa = ClubeCasa;
    }

    public Clube getClubeVisitante(){
        return ClubeVisitante;
    }
    public void setClubeVisitante(Clube ClubeVisitante){
        this.ClubeVisitante = ClubeVisitante;
    }

    public LocalDate getDataPartida(){
        return DataPartida;
    }
    public void setDataPartida(LocalDate DataPartida){
        this.DataPartida = DataPartida;
    }

    public LocalTime getHoraPartida(){
        return HoraPartida;
    }
    public void setHoraPartida(LocalTime HoraPartida){
        this.HoraPartida = HoraPartida;
    }

    public int getGolsCasa(){
        return GolsCasa;
    }
    public void setGolsCasa(int GolsCasa){
        this.GolsCasa = GolsCasa;
    }

    public int getGolsVisitante(){
        return GolsVisitante;
    }
    public void setGolsVisitante(int GolsVisitante){
        this.GolsVisitante = GolsVisitante;
    }

    public boolean isPartidaFinalizada(){
        return PartidaFinalizada;
    }
    public void setPartidaFinalizada(boolean PartidaFinalizada){
        this.PartidaFinalizada = PartidaFinalizada;
    }

    public void ResultadoFinal(int GolsCasa, int GolsVisitante){
        this.GolsVisitante = GolsVisitante;
        this.GolsCasa = GolsCasa;
        this.PartidaFinalizada = true;
    }

    public int getResultado(){
        if (GolsCasa > GolsVisitante) {
            return 1; //casa venceu
        }

        if (GolsCasa < GolsVisitante) {
            return 2; //visitante venceu
        }
        return 0; //empate
    }
}
