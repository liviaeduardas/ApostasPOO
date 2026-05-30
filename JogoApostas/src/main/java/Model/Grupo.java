package Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // banco gera id automático
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    // Um grupo tem vários participantes e um participante pode estar em vários grupos
    @ManyToMany(fetch = FetchType.LAZY) // grupos são carregadas do banco so quando chamar
    @JoinTable(
            name = "grupo_participantes",                        // nome da tabela intermediária
            joinColumns = @JoinColumn(name = "grupo_id"),        // chave do grupo
            inverseJoinColumns = @JoinColumn(name = "participante_id") // chave do participante
    )
    private ArrayList<Participante> participantes;

    public Grupo(){
        this.participantes = new ArrayList<>();
    }

    public Grupo(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.participantes = new ArrayList<>();
    }

    public boolean addParticipante(Participante participante){
        if(participantes.size() >= 5){
            System.out.println("Grupo está CHEIO!");
            return false;
        }
        else{
            participantes.add(participante);
            return true;
        }
    }

    public ArrayList<Participante> getRancking(){
        ArrayList<Participante> ranking = new ArrayList<>(participantes);
        ranking.sort((p1, p2) -> p2.getTotalPontos() - p1.getTotalPontos());
        return ranking;
    }

    public String toString(){
        return "Grupo " + this.nome + " | Participantes: " + participantes.size() + "/5";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Participante> participantes) {
        this.participantes = participantes;
    }
}
