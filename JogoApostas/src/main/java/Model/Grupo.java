package Model;

import java.util.ArrayList;

public class Grupo {
    private int id;
    private String nome;
    private String criador;
    private ArrayList<Participante> participantes;

    public Grupo(){
        this.participantes = new ArrayList<>();
    }

    public Grupo(int id, String nome, String criador) {
        this.id = id;
        this.nome = nome;
        this.criador = criador;
        this.participantes = new ArrayList<>();
    }

    public boolean addParticipante(Participante participante){
        if(participantes.size() >= 5){
            System.out.println("Grupo está CHEIO!");
            return false;
        }
        else{
            participantes.add(participante);
            return true;
        }
    }

    public ArrayList<Participante> getRancking(){
        ArrayList<Participante> ranking = new ArrayList<>(participantes);
        ranking.sort((p1, p2) -> p2.getPontosTotal() - p1.getPontosTotal());
        return ranking;
    }

    public String toString(){
        return "Grupo " + this.nome + " | Participantes: " + participantes.size() + "/5";
    }


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

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public ArrayList<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Participante> participantes) {
        this.participantes = participantes;
    }
}
