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

public abstract class Partida {

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
    private Clube clubeMandante;

    // Clube que joga fora
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    private Clube clubeVisitante;

    @Column(name = "data_partida", nullable = false)
    private LocalDate data;

    @Column(name = "hora_partida", nullable = false)
    private LocalTime hora;

    @Column(name = "gol_mandante")
    private int golMandante;

    @Column(name = "gol_visitante")
    private int golVisitante;

    @Column(name = "encerrada")
    private boolean encerrada;

    public Partida() {
        this.golMandante  = 0;
        this.golVisitante = 0;
        this.encerrada    = false;
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
