package Model;
import java.time.LocalDateTime;

public class Aposta implements Ipontuavel {
    private int idAposta;
    private Participante participante;
    private Partida partida;
    private int golsMandantePalpite;
    private int golsVisitantePalpite;
    private LocalDateTime dataHoraAposta;
    private int pontuacaoObtida;

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
