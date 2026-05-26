package Model;

public class Administrador extends Usuario{
    private String nivelAesso;

    public Administrador(){
        super();
        this.nivelAesso = "TOTAL";
    }

    public Administrador(int id, String senha, String usuario, String nome) {
        super(id, senha, usuario, nome);
        this.nivelAesso = "TOTAL";
    }

    public void autenticar(){
        System.out.println("Administrador autenticado: " + getNome());
    }

    public String getNivelAesso() {
        return nivelAesso;
    }

    public void setNivelAesso(String nivelAesso) {
        this.nivelAesso = nivelAesso;
    }
}
