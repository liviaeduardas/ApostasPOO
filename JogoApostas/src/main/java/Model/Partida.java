package Model;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "partidas")

public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    private Clube ClubeCasa;

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

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public boolean isPartidaFinalizada(){
        return PartidaFinalizada;
    }
    public void setPartidaFinalizada(boolean PartidaFinalizada){
        this.PartidaFinalizada = PartidaFinalizada;
    }

    public void resultadoFinal(int GolsCasa, int GolsVisitante){
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

    public int getId() {
        return id;
    }
}
