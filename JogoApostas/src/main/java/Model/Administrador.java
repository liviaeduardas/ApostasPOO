package Model;
import jakarta.persistence.*;

//vira tabela no banco
@Entity

//identifica essa classe na coluna tipo_usuario
@DiscriminatorValue("ADMIN")

public class Administrador extends Usuario{
    @Column(name = "nivel_acesso")
    private String nivelAcesso;

    public Administrador(){
        super();
        this.nivelAcesso = "TOTAL";
    }

    public Administrador(int id, String senha, String usuario, String nome) {
        super(id, senha, usuario, nome);
        this.nivelAcesso = "TOTAL";
    }

    public void autenticar(){
        System.out.println("Administrador autenticado: " + getNome());
    }

    public String getNivelAcesso(){
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso){
        this.nivelAcesso = nivelAcesso;
    }
}
