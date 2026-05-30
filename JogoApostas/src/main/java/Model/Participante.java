package Model;
import jakarta.persistence.*;
import java.util.ArrayList;

/**
 * @Entity — vira tabela no banco
 * @Inheritance — como o Hibernate vai lidar com a herança de Usuario
 */

@Entity
@Table(name = "participante")
@DiscriminatorValue("PARTICIPANTE") //no Participante as linhas dessa classe terão tipo_usuario = "PARTICIPANTE"
public class Participante extends Usuario{

    @Column(name = "total_pontos")
    private int totalPontos;

    @OneToMany( //define relação entre a tabela apostas -> participante
            mappedBy = "participante", // na tabela aposta terá um atributo chamado participante, Sem isso o Hibernate criaria uma tabela intermediária desnecessária.
            cascade = CascadeType.ALL, // defini que deve fazer  tudo com a aposta quando ocorre algo com participante
            fetch = FetchType.LAZY) // apostas são carregadas do banco so quando chamar
    private ArrayList<Aposta> apostas;

    public Participante() {
        super();
        this.totalPontos = 0;
        this.apostas = new ArrayList<>();
    }

    public Participante(int id, String senha, String usuario, String nome) {
        super(id, senha, usuario, nome);
        this.totalPontos = 0;
        this.apostas = new ArrayList<>();
    }

    public void autenticar(){
        System.out.println("Participante autenticado: " + getNome());
    }

    public void registrarAposta(Aposta aposta){
        apostas.add(aposta);
    }

    public int getTotalPontos() {
        return totalPontos;
    }
    public void setTotalPontos(int totalPontos) {
        this.totalPontos = totalPontos;
    }

    public ArrayList<Aposta> getApostas() {
        return apostas;
    }
    public void setApostas(ArrayList<Aposta> apostas) {
        this.apostas = apostas;
    }
}
