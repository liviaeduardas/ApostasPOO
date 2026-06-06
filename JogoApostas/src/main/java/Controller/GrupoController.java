package Controller;
import Model.Administrador;
import Model.Participante;
import Model.Grupo;
import java.util.ArrayList;

public class GrupoController {
    private ArrayList<Grupo> grupos;

    public GrupoController() {
        grupos = new ArrayList<>();
    }

    public boolean criarGrupo(String nome) {
        if (grupos.size() >= 5) {
            System.out.println("Limite de grupos atingido.");
            return false;
        }
        Grupo grupo = new Grupo(0, nome);
        grupos.add(grupo);
        return true;
    }

    public boolean addParticipante(Grupo grupo, Participante participante) {
        return grupo.addParticipante(participante);
    }

    public ArrayList<Participante> getRanking(Grupo grupo) {
        return grupo.getRanking();
    }

    public Grupo BuscarNome(String nome) {
        for (Grupo grupo : grupos) {
            if (grupo.getNome().equalsIgnoreCase(nome)) {
                return grupo;
            }
        }
        return null;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }
}