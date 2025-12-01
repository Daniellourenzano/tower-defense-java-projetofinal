package leandro;


/**
 *
 * @author DANIEL
 */
public class Torre {
    public float altura;
    public float comprimento;
    public String poder;
    public float dano_por_segundo;
    int posicaox;
    int posicaoy;
    
    public Torre(float a, float c, float d, String p, int x, int y){
        this.altura = a;
        this.comprimento = c;
        this.dano_por_segundo = d;
        this.poder = p;
        this.posicaox = x;
        this.posicaoy = y;
    }
}
