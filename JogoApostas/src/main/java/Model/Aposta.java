package Model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "apostas")
public class Aposta implements ICalcularPontos {
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
    private int golsMandantePalpite;

    @Column(name = "gols_visitante_palpite", nullable = false)
    private int golsVisitantePalpite;

    @Column(name = "data_hora_aposta")
    private LocalDateTime dataHoraAposta;

    @Column(name = "pontuacao_obtida")
    private int pontuacaoObtida;

    public Aposta(){}
    public Aposta(int idAposta, Participante participante, Partida partida, int golsMandantePalpite, int golsVisitantePalpite) {
        this.idAposta = idAposta;
        this.participante = participante;
        this.partida = partida;
        this.golsMandantePalpite = golsMandantePalpite;
        this.golsVisitantePalpite = golsVisitantePalpite;
        this.dataHoraAposta = LocalDateTime.now();
        this.pontuacaoObtida = 0;
    }

    public boolean podeApostar() {
        LocalDateTime dataHoraPartida = LocalDateTime.of(partida.getData(), partida.getHora());
        LocalDateTime limiteAposta = dataHoraPartida.minusMinutes(20);
        return LocalDateTime.now().isBefore(limiteAposta);
    }

    private String getResultadoPalpite() {
        if (golsMandantePalpite > golsVisitantePalpite){
            return "Mandante";
        }
        if (golsMandantePalpite < golsVisitantePalpite){
            return "Visitante";
        }
        return "Empate";
    }


    @Override
    public int calcularPontuacao() {
        if (!partida.isEncerrada()) {
            return 0;
        }

        String resultadoReal = partida.getResultado();
        String resultadoPalpite = getResultadoPalpite();

        if (golsMandantePalpite == partida.getGolMandante() && golsVisitantePalpite == partida.getGolVisitante()) {
            this.pontuacaoObtida = 10;
            return 10;
        }

        if (resultadoPalpite.equals(resultadoReal)) {
            this.pontuacaoObtida = 5;
            return 5;
        }

        this.pontuacaoObtida = 0;
        return 0;
    }

    public int getIdAposta() {
        return idAposta;
    }

    public void setIdAposta(int idAposta) {
        this.idAposta = idAposta;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public int getGolsMandantePalpite() {
        return golsMandantePalpite;
    }

    public void setGolsMandantePalpite(int golsMandantePalpite) {
        this.golsMandantePalpite = golsMandantePalpite;
    }

    public int getGolsVisitantePalpite() {
        return golsVisitantePalpite;
    }

    public void setGolsVisitantePalpite(int golsVisitantePalpite) {
        this.golsVisitantePalpite = golsVisitantePalpite;
    }

    public LocalDateTime getData() {
        return dataHoraAposta;
    }

    public int getPontuacaoObtida() {
        return pontuacaoObtida;
    }

}
