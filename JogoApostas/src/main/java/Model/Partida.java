package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Partida {
    private Clube clubeMandante;
    private  Clube clubeVisitante;
    private LocalDate data;
    private LocalTime hora;
    private int golMandante;
    private int golVisitante;
    private boolean encerrada;

    public Partida(){
        this.golMandante = 0;
        this.golVisitante = 0;
        this.encerrada = false;
    }

    public Partida(Clube clubeMandante, Clube clubeVisitante, LocalDate data, LocalTime hora, int golMandante, int golVisitante, boolean encerrada) {
        this.clubeMandante = clubeMandante;
        this.clubeVisitante = clubeVisitante;
        this.data = data;
        this.hora = hora;
        this.golMandante = 0;
        this.golVisitante = 0;
        this.encerrada = false;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }
    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }
    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getGolMandante() {
        return golMandante;
    }
    public void setGolMandante(int golMandante) {
        this.golMandante = golMandante;
    }

    public int getGolVisitante() {
        return golVisitante;
    }
    public void setGolVisitante(int golVisitante) {
        this.golVisitante = golVisitante;
    }

    public boolean isEncerrada() {
        return encerrada;
    }
    public void setEncerrada(boolean encerrada) {
        this.encerrada = encerrada;
    }

    public void registrarResultado(int golMandante, int golVisitante){
        this.golVisitante = golVisitante;
        this.golMandante = golMandante;
        this.encerrada = true;
    }

    public abstract String getResultado();
}
