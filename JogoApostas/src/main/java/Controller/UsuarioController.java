package Controller;
import Model.Administrador;
import Model.Participante;
import Model.Usuario;
import Repository.ParticipanteRepository;
import java.util.List;

public class UsuarioController {
    private Administrador administrador;
    private ParticipanteRepository participanteRepository;

    public UsuarioController() {
        administrador = new Administrador("admin", "admin", "Administrador");
        participanteRepository = new ParticipanteRepository();
    }

    public Usuario autenticar(String usuario, String senha) {
        if (usuario == null || senha == null){
            return null;
        }

        if (usuario.equals("admin") && senha.equals("admin")) {
            administrador.autenticar();
            return administrador;
        }

        Participante participante = participanteRepository.buscarPorUsuarioESenha(usuario, senha);
        if (participante != null) {
            participante.autenticar();
            return participante;
        }
        return null;
    }

    public boolean cadastrar(String nome, String usuario, String senha) {
        if (nome == null || nome.trim().isEmpty()){
            return false;
        }
        if (usuario == null || usuario.trim().isEmpty()) {
            return false;
        }
        if (senha == null || senha.trim().isEmpty()) {
            return false;
        }

        if (participanteRepository.buscarPorUsuario(usuario) != null) {
            return false;
        }

        Participante novo = new Participante(senha.trim(), usuario.trim(), nome.trim());

        return participanteRepository.salvarParticipante(novo);
    }

    public Participante buscarNome(String nome) {
        if (nome == null){
            return null;
        }
        return participanteRepository.buscarPorUsuario(nome);
    }

    public List<Participante> getParticipantes() {
        return participanteRepository.buscarTodosParticipante();
    }

    public Administrador getAdministrador() {
        return administrador;
    }
}