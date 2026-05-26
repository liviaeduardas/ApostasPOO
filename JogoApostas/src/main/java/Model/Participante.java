package Model;
import java.util.ArrayList;

public class Participante extends Usuario{
    private int pontosTotal;
    private ArrayList<Aposta> apostas;

    public Participante() {
        super();
        this.pontosTotal = 0;
        this.apostas = new ArrayList<>();
    }

    public Participante(int id, String senha, String usuario, String nome) {
        super(id, senha, usuario, nome);
        this.pontosTotal = 0;
    }

    public void autenticar(){
        System.out.println("Participante autenticado: " + getNome());
    }

    public void registrarAposta(Aposta aposta){
        apostas.add(aposta);
    }

    public int getPontosTotal() {
        return pontosTotal;
    }
    public void setPontosTotal(int pontosTotal) {
        this.pontosTotal = pontosTotal;
    }

    public ArrayList<Aposta> getApostas() {
        return apostas;
    }
    public void setApostas(ArrayList<Aposta> apostas) {
        this.apostas = apostas;
    }
}
