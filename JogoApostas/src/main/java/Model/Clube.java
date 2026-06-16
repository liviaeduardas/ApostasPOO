package Model;
import jakarta.persistence.*;

@Entity
@Table(name = "clubes")

public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    public int getId(){
        return id;
    }

    @Column(name = "sigla", nullable = false, length = 5)
    private String sigla;

    public Clube(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Clube(){
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getSigla(){
        return sigla;
    }

    public void setSigla(String sigla){
        this.sigla = sigla;
    }
}
