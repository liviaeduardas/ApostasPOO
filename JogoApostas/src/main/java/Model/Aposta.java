package Model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "apostas")
public class Aposta implements ICalcularPontos{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //banco gera autoático
    private int idAposta;

    // Muitas apostas pertencem a um participante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    // Muitas apostas pertencem a uma partida
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(name = "gols_mandante_palpite", nullable = false)
    private int PalpiteGolsCasa;

    @Column(name = "gols_visitante_palpite", nullable = false)
    private int PalpiteGolsVsitante;

    @Column(name = "data_hora_aposta")
    private LocalDateTime TempoApostar;

    @Column(name = "pontuacao_obtida")
    private int PontosObtidos;

    public Aposta(){
    }

    public Aposta(int idAposta, Participante participante, Partida partida, int PalpiteGolsCasa, int PalpiteGolsVsitante) {
        this.idAposta = idAposta;
        this.participante = participante;
        this.partida = partida;
        this.PalpiteGolsCasa = PalpiteGolsCasa;
        this.PalpiteGolsVsitante = PalpiteGolsVsitante;
        this.TempoApostar = LocalDateTime.now();
        this.PontosObtidos = 0;
    }

    public boolean PossivelApostar(){
        LocalDateTime horarioPartida = LocalDateTime.of(partida.getDataPartida(), partida.getHoraPartida());
        LocalDateTime limiteAposta = horarioPartida.minusMinutes(20);

        if (LocalDateTime.now().isBefore(limiteAposta)){
            return true;
        }
        return false;
    }

    private int getResultadoPalpitePartida() {
        if (PalpiteGolsCasa > PalpiteGolsVsitante){
            return 1; //casa
        }
        if (PalpiteGolsCasa < PalpiteGolsVsitante){
            return 2; //visitante
        }
        return 0; //empate
    }


    @Override
    public int CalcularResultadoAposta() {
        if (!partida.isPartidaFinalizada()) {
            return 0;
        }
        int ResultadoCerto = partida.getResultado();
        int ResultadoPalpite = getResultadoPalpitePartida();

        if (PalpiteGolsCasa == partida.getGolsCasa() && PalpiteGolsVsitante == partida.getGolsVisitante()){
            this.PontosObtidos = 10;
            return 10;
        }

        if (ResultadoPalpite == ResultadoCerto) {
            this.PontosObtidos = 5;
            return 5;
        }

        this.PontosObtidos = 0;
        return 0;
    }

    public int getIdAposta(){
        return idAposta;
    }

    public void setIdAposta(int idAposta){
        this.idAposta = idAposta;
    }

    public Participante getParticipante(){
        return participante;
    }

    public void setParticipante(Participante participante){
        this.participante = participante;
    }

    public Partida getPartida(){
        return partida;
    }

    public void setPartida(Partida partida){
        this.partida = partida;
    }

    public int getPalpiteGolsCasa(){
        return PalpiteGolsCasa;
    }

    public void setPalpiteGolsCasa(int palpiteGolsCasa){
        this.PalpiteGolsCasa = palpiteGolsCasa;
    }

    public int getPalpiteGolsVsitante(){
        return PalpiteGolsVsitante;
    }

    public void setPalpiteGolsVsitante(int palpiteGolsVsitante){
        this.PalpiteGolsVsitante = palpiteGolsVsitante;
    }

    public LocalDateTime getTempoApostar(){
        return TempoApostar;
    }

    public int getPontosObtidos(){
        return PontosObtidos;
    }

    public void setPontosObtidos(int pontosObtidos){
        this.PontosObtidos = pontosObtidos;
    }

}
