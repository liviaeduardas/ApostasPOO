package Model;

public abstract class Usuario {
    private int id;
    private String nome;
    private String usuario;
    private String senha;

    public Usuario(){}
    public Usuario(int id, String senha, String usuario, String nome) {
        this.id = id;
        this.senha = senha;
        this.usuario = usuario;
        this.nome = nome;
    }

    public abstract void autenticar();

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

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }


}
