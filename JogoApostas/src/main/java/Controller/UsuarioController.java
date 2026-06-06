package Controller;
import Model.Administrador;
import Model.Participante;
import Model.Usuario;
import java.util.ArrayList;

public class UsuarioController {
    private Administrador administrador;
    private ArrayList<Participante> participantes;

    public UsuarioController() {
        administrador = new Administrador(1, "admin", "admin", "Administrador");
        participantes = new ArrayList<>();
    }

    public Usuario autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) return null;

        if (usuario.equals("admin") && senha.equals("admin")) {
            administrador.autenticar();
            return administrador;
        }

        if (usuario.equals("participante") && senha.equals("participante")) {
            Participante participante = new Participante();
            participante.autenticar();
            return participante;
        }
        return null;
    }

    public boolean cadastrar(Participante participante, String nome) {
        if (participante == null) return false;
        if (nome == null || nome.trim().isEmpty()) return false;
        participante.setNome(nome.trim());
        participantes.add(participante);
        return true;
    }

    public boolean temCadastro(String nome) {
        if (nome == null) return false;
        for (Participante participante : participantes) {
            if (participante.getNome().equalsIgnoreCase(nome.trim())) return true;
        }
        return false;
    }

    public Participante buscarNome(String nome) {
        if (nome == null) return null;
        for (Participante participante : participantes) {
            if (participante.getNome().equalsIgnoreCase(nome.trim())) return participante;
        }
        return null;
    }

    public Administrador getAdministrador(){ return administrador; }
    public ArrayList<Participante> getParticipantes() { return participantes; }
}