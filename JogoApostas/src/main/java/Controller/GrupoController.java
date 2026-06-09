package Controller;

import Model.Administrador;
import Model.Grupo;
import Model.Participante;

import java.util.ArrayList;
import java.util.List;

import Repository.GrupoRepository;
import Repository.ParticipanteRepository;

/**
 * Gerencia grupos.
 * Salva e atualiza grupos no banco.
 * Só Administrador pode criar grupos.
 */
public class GrupoController {

    private static final int MAX_GRUPOS = 5;

    private GrupoRepository grupoRepository;
    private ParticipanteRepository participanteRepository;

    public GrupoController() {
        grupoRepository = new GrupoRepository();
        participanteRepository = new ParticipanteRepository();
    }

    // Só Administrador pode criar grupos
    public boolean criarGrupo(String nome, Administrador admin) {
        if (admin == null)
            return false;
        if (nome == null || nome.trim().isEmpty())
            return false;
        if (getGrupos().size() >= MAX_GRUPOS)
            return false;

        Grupo grupo = new Grupo(0, nome.trim());
        return grupoRepository.salvarGrupo(grupo);
    }

    // Adiciona participante ao grupo e atualiza no banco
    public boolean addParticipante(Grupo grupo, Participante participante) {
        if (grupo == null || participante == null)
            return false;
        boolean adicionou = grupo.addParticipante(participante); // regra no Model
        if (adicionou)
            grupoRepository.atualizarGrupo(grupo); // salva no banco
        return adicionou;
    }

    // Ranking — regra de ordenação vive no Model
    public ArrayList<Participante> getRanking(Grupo grupo) {
        if (grupo == null)
            return new ArrayList<>();
        return grupo.getRanking();
    }

    // Busca grupo pelo nome no banco
    public Grupo BuscarNome(String nome) {
        if (nome == null)
            return null;
        return grupoRepository.buscarPorGrupo(nome);
    }

    // Retorna todos os grupos do banco
    public List<Grupo> getGrupos() {
        return grupoRepository.buscarTodosGrupos();
    }
}