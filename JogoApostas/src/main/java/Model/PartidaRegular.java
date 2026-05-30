package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;


@Entity
@DiscriminatorValue("REGULAR") //identifica essa classe na coluna tipo_partida
public class PartidaRegular extends Partida{

    public PartidaRegular() {
    }


    public PartidaRegular(Clube clubeMandante, Clube clubeVisitante, LocalDate data, LocalTime hora) {
        super(clubeMandante, clubeVisitante, data, hora, 0, 0, false);
    }

    public String getResultado() {
        if (!isEncerrada()) {
            return "Não encerrada";
        }
        if (getGolMandante() > getGolVisitante()) {
            return "Mandante";
        } else if (getGolVisitante() > getGolMandante()) {
            return "Visitante";
        } else {
            return "Empate";
        }
    }

    @Override
    public String toString() {
        return getClubeMandante().getNome() + " x " + getClubeVisitante().getNome()
                + " | " + getData() + " " + getHora();
    }
}
