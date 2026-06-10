package Model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PARTICIPANTE")
public class Participante extends Usuario {

    @OneToMany(mappedBy = "participante",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Aposta> apostas;

    public Participante() {
        super();
        this.apostas = new ArrayList<>();
    }

    public Participante(int id, String senha, String usuario, String nome) {
        super(id, senha, usuario, nome);
        this.apostas = new ArrayList<>();
    }

    @Override
    public void autenticar() {
        System.out.println("Participante autenticado: " + getNome());
    }

    public void fazerAposta(Aposta aposta){
        apostas.add(aposta);
    }

    public int getTotalPontos() {
        int total = 0;
        for (Aposta aposta : apostas){
            total += aposta.getPontosObtidos();
        }
        return total;

    }

    public List<Aposta> getApostas(){
        return apostas;
    }

    public void setApostas(List<Aposta> apostas){
        this.apostas = apostas;
    }
}