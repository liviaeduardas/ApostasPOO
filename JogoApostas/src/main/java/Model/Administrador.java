package Model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")

public class Administrador extends Usuario {
    @Column(name = "nivel_acesso")
    private String nivelAcesso = "TOTAL";

    public Administrador(){
        super();
    }

    public Administrador(String senha, String usuario, String nome) {
        super(senha, usuario, nome);
    }

    @Override
    public void autenticar() {
        System.out.println("Administrador autenticado: " + getNome());
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }
}
