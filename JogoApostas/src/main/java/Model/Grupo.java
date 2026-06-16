package Model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupos")

public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "grupo_participantes", joinColumns = @JoinColumn(name = "grupo_id"), inverseJoinColumns = @JoinColumn(name = "participante_id"))
    private List<Participante> participantes;

    public Grupo(){
        this.participantes = new ArrayList<>();
    }

    public Grupo(String nome) {
        this.nome = nome;
        this.participantes = new ArrayList<>();
    }

    public boolean addParticipante(Participante participante){
        if(participantes.size() >= 5){
            System.out.println("Grupo está cheio");
            return false;
        }
        else{
            participantes.add(participante);
            return true;
        }
    }

    public List<Participante> getRanking() {
        return new ArrayList<>(participantes);
    }

    public String toString(){
        return "Grupo " + this.nome + " - Participantes: " + participantes.size() + "/5";
    }

    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public List<Participante> getParticipantes(){
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes){
        this.participantes = participantes;
    }
}
