package Controller;
import Model.Administrador;
import Model.Grupo;
import Model.Participante;
import java.util.ArrayList;
import java.util.List;
import Repository.GrupoRepository;
import Repository.ParticipanteRepository;

public class GrupoController {
    private static final int MAX_GRUPOS = 5;
    private GrupoRepository grupoRepository;
    private ParticipanteRepository participanteRepository;

    public GrupoController() {
        grupoRepository = new GrupoRepository();
        participanteRepository = new ParticipanteRepository();
    }

    public boolean criarGrupo(String nome, Administrador admin) {
        if (admin == null) {
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (getGrupos().size() >= MAX_GRUPOS) {
            return false;
        }
        Grupo grupo = new Grupo(nome.trim());
        return grupoRepository.salvarGrupo(grupo);
    }

    public boolean addParticipante(Grupo grupo, Participante participante) {
        if (grupo == null || participante == null) {
            return false;
        }
        boolean adicionou = grupo.addParticipante(participante);
        if (adicionou) {
            grupoRepository.atualizarGrupo(grupo);
        }
        return adicionou;
    }

    public List<Participante> getRanking(Grupo grupo) {
        if (grupo == null) {
            return new ArrayList<>();
        }
        return grupo.getRanking();
    }

    public Grupo buscarNome(String nome){
        if (nome == null){
            return null;
        }
        return grupoRepository.buscarPorGrupo(nome);
    }

    public List<Grupo> getGrupos(){
        return grupoRepository.buscarTodosGrupos();
    }
}