package Model;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("REGULAR")
public class PartidaRegular extends Partida {

    public PartidaRegular() {
        super();
    }

    public PartidaRegular(Clube ClubeCasa, Clube ClubeVisitante, LocalDate DataPartida, LocalTime HoraPartida) {
        super(ClubeCasa, ClubeVisitante, DataPartida, HoraPartida);
    }
}