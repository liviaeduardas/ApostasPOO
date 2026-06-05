package Model;
import jakarta.persistence.*;

//diz ao Hibernate que essa classe não vira tabela, mas seus atributos são herdados pelas subclasses que são @Entity
@Entity

//define como a herança é mapeada no banco. SINGLE_TABLE = uma só tabela para todas as subclasses
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

//coluna discriminadora dizendo qual tipo é cada linha.
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)


public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // faz o banco gerar id automático
    private int id;

    // nullable não pode ser nulo | length é o tamanho máximo do texto
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;

    @Column(name = "senha", nullable = false, length = 50)
    private String senha;

    public Usuario(){}
    public Usuario(int id, String senha, String usuario, String nome) {
        this.id = id;
        this.senha = senha;
        this.usuario = usuario;
        this.nome = nome;
    }

    public abstract void autenticar();

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public String getUsuario(){
        return usuario;
    }
    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getSenha(){
        return senha;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
}
