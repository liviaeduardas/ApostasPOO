package Model;
import jakarta.persistence.*;

/**
 * @Entity — diz ao Hibernate que essa classe vira uma tabela no banco
 * @Table  — define o nome da tabela no banco
 */

@Entity
@Table(name = "clubes")
public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // faz o banco gerar id automático
    private int id;

    // nullable não pode ser nulo | length é o tamanho máximo do texto
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "sigla", nullable = false, length = 100)
    private String sigla;

    public Clube(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Clube() {
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
