package Controller;
import Model.Administrador;
import Model.Grupo;
import Model.Participante;
import java.util.ArrayList;

public class GrupoController {
    private static final int MAX_GRUPOS = 5;
    private ArrayList<Grupo> grupos;

    public GrupoController() {
        grupos = new ArrayList<>();
    }

    // Só Administrador pode criar grupos
    public boolean criarGrupo(String nome, Administrador admin) {
        if (admin == null) return false;
        if (nome == null || nome.trim().isEmpty()) return false;
        if (grupos.size() >= MAX_GRUPOS) return false;
        grupos.add(new Grupo(0, nome.trim()));
        return true;
    }

    public boolean addParticipante(Grupo grupo, Participante participante) {
        if (grupo == null || participante == null) return false;
        return grupo.addParticipante(participante);
    }

    public ArrayList<Participante> getRanking(Grupo grupo) {
        if (grupo == null) return new ArrayList<>();
        return grupo.getRanking();
    }

    public Grupo BuscarNome(String nome) {
        if (nome == null) return null;
        for (Grupo grupo : grupos) {
            if (grupo.getNome().equalsIgnoreCase(nome.trim())) return grupo;
        }
        return null;
    }

    public ArrayList<Grupo> getGrupos() { return grupos; }
}