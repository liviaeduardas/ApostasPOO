package Controller;

import Model.Administrador;
import Model.Participante;
import Model.Usuario;

import Repository.ParticipanteRepository;

import java.util.List;

/**
 * Gerencia login e cadastro de usuários.
 * Admin é predefinido no código — não vai pro banco.
 * Participantes se cadastram com nome, usuário e senha próprios.
 */
public class UsuarioController {

    private Administrador administrador;
    private ParticipanteRepository participanteRepository;

    public UsuarioController() {
        administrador = new Administrador(1, "admin", "admin", "Administrador");
        participanteRepository = new ParticipanteRepository();
    }

    /**
     * Verifica credenciais e retorna o usuário autenticado.
     * Retorna null se usuário ou senha estiverem errados.
     */
    public Usuario autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) return null;

        // Verifica se é o admin
        if (usuario.equals("admin") && senha.equals("admin")) {
            administrador.autenticar();
            return administrador;
        }

        // Busca participante no banco pelo usuário e senha
        Participante participante = participanteRepository.buscarPorUsuarioESenha(usuario, senha);
        if (participante != null) {
            participante.autenticar();
            return participante;
        }

        return null;
    }

    /**
     * Cadastra um novo participante com nome, usuário e senha próprios.
     * Retorna false se usuário já existir ou dados inválidos.
     */
    public boolean cadastrar(String nome, String usuario, String senha) {
        if (nome == null || nome.trim().isEmpty())
            return false;
        if (usuario == null || usuario.trim().isEmpty())
            return false;
        if (senha == null || senha.trim().isEmpty())
            return false;

        // Verifica se o usuário já existe no banco
        if (usuarioJaExiste(usuario)) return false;

        Participante novo = new Participante();
        novo.setNome(nome.trim());
        novo.setUsuario(usuario.trim());
        novo.setSenha(senha.trim());

        return participanteRepository.salvarParticipante(novo);
    }

    /**
     * Verifica se já existe um participante com esse usuário no banco.
     */
    public boolean usuarioJaExiste(String usuario) {
        return participanteRepository.buscarPorUsuario(usuario) != null;
    }

    /**
     * Busca participante pelo nome no banco.
     */
    public Participante buscarNome(String nome) {
        if (nome == null) return null;
        return participanteRepository.buscarPorUsuario(nome);
    }

    /**
     * Retorna todos os participantes do banco.
     */
    public List<Participante> getParticipantes() {
        return participanteRepository.buscarTodosParticipante();
    }

    public Administrador getAdministrador() { return administrador; }
}