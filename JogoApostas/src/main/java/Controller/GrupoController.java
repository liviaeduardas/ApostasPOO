package Controller;
import Model.Administrador;
import Model.Participante;
import Model.Grupo;
import java.util.ArrayList;

public class GrupoController {
    private ArrayList<Grupo> grupos;
    private static final int maximo = 5;
    private int proximo;

    public GrupoController(){
        this.grupos = new ArrayList<>();
        this.proximo = 1;
    }

    public boolean criarGrupo(String nome, Administrador criador) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (grupos.size() >= 5) {
            return false;
        }

        Grupo novoGrupo = new Grupo(proximo++, nome.trim(), null);
        grupos.add(novoGrupo);
        return true;
    }



    public boolean adicionarParticipante(Grupo grupo, Participante participante){
        if(grupo == null || participante == null){
            return false;
        }
        return grupo.addParticipante(participante);
    }

    public ArrayList<Participante> getRanking(Grupo grupo){
        if(grupo == null){
            return new ArrayList<>();
        }
        return grupo.getRancking();
    }

    public Grupo buscarNome(String nome){
        for(Grupo g: grupos){
            if(g.getNome().equalsIgnoreCase(nome.trim())){
                return g;
            }
        }

        return null;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }
}

