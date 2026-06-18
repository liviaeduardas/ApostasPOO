package Model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity

public class Aposta implements ICalcularPontos{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(name = "gols_casa_aposta", nullable = false)
    private int palpiteGolsCasa;

    @Column(name = "gols_visitante_aposta", nullable = false)
    private int palpiteGolsVsitante;

    @Column(name = "data_hora_aposta")
    private LocalDateTime tempoApostar;

    @Column(name = "pontuacao_obtida")
    private int pontosObtidos;

    public Aposta(){
    }

    public Aposta(Participante participante, Partida partida, int palpiteGolsCasa, int palpiteGolsVsitante) {
        this.participante = participante;
        this.partida = partida;
        this.palpiteGolsCasa = palpiteGolsCasa;
        this.palpiteGolsVsitante = palpiteGolsVsitante;
        this.tempoApostar = LocalDateTime.now();
        this.pontosObtidos = 0;
    }

    public boolean possivelApostar(){
        LocalDateTime horarioPartida = LocalDateTime.of(partida.getDataPartida(), partida.getHoraPartida());
        LocalDateTime limiteAposta = horarioPartida.minusMinutes(20);

        if (LocalDateTime.now().isBefore(limiteAposta)){
            return true;
        }
        return false;
    }

    private int getResultadoPalpitePartida() {
        if (palpiteGolsCasa > palpiteGolsVsitante){
            return 1;
        }
        if (palpiteGolsCasa < palpiteGolsVsitante){
            return 2;
        }
        return 0;
    }

    @Override
    public int calcularResultadoAposta() {
        if (!partida.isPartidaFinalizada()) {
            return 0;
        }
        int ResultadoCerto = partida.getResultado();
        int ResultadoPalpite = getResultadoPalpitePartida();

        if (palpiteGolsCasa == partida.getGolsCasa() && palpiteGolsVsitante == partida.getGolsVisitante()){
            this.pontosObtidos = 10;
            return 10;
        }

        if (ResultadoPalpite == ResultadoCerto) {
            this.pontosObtidos = 5;
            return 5;
        }

        this.pontosObtidos = 0;
        return 0;
    }

    public int getIdAposta(){
        return idAposta;
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
        return palpiteGolsCasa;
    }

    public void setPalpiteGolsCasa(int palpiteGolsCasa){
        this.palpiteGolsCasa = palpiteGolsCasa;
    }

    public int getPalpiteGolsVsitante(){
        return palpiteGolsVsitante;
    }

    public void setPalpiteGolsVsitante(int palpiteGolsVsitante){
        this.palpiteGolsVsitante = palpiteGolsVsitante;
    }

    public LocalDateTime getTempoApostar(){
        return tempoApostar;
    }

    public int getPontosObtidos(){
        return pontosObtidos;
    }

    public void setPontosObtidos(int pontosObtidos){
        this.pontosObtidos = pontosObtidos;
    }

}
