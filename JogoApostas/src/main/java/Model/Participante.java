package Model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PARTICIPANTE")

public class Participante extends Usuario{
    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Aposta> apostas;

    public Participante() {
        super();
        this.apostas = new ArrayList<>();
    }

    public Participante(String senha, String usuario, String nome) {
        super(senha, usuario, nome);
        this.apostas = new ArrayList<>();
    }

    @Override
    public void autenticar(){
        System.out.println("Participante autenticado: " + getNome());
    }

    public void fazerAposta(Aposta aposta){
        apostas.add(aposta);
    }

    public int getTotalPontos(){
        return 0;
    }

    public List<Aposta> getApostas(){
        return apostas;
    }

    public void setApostas(List<Aposta> apostas){
        this.apostas = apostas;
    }
}