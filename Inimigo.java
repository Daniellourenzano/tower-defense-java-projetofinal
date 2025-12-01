package leandro;

/**
 *
 * 
 */
public class Inimigo {

 
    public float vida;
    public float tempo_de_chegada_segundos;
    public float altura;
    public float comprimento;
    public String poder;
    public int posicaox = 0;
    public int posicaoy = 0;

    
    private float s;

    public Inimigo(float v, float t, float a, float c, String p) {
        this.vida = v;
        this.tempo_de_chegada_segundos = t;
        this.altura = a;
        this.comprimento = c;
        this.poder = p;
    }

   
    void receber_dano(float D, String torre) {
        this.vida -= D;
        System.out.println("A torre " + torre + " deu " + D + " de dano no inimigo alfa. Vida restante: " + this.vida);
    }

 
    void percurso1vazio() throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaox += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));
        }
    }


    void percurso2vazio() throws InterruptedException {
        for (int i = 0; i <= 5; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaoy += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));
        }
    }


    void percurso1(float D, String torrenome, int torrex, int torrey) throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaox += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));

            if ((this.posicaoy + this.posicaox > 5) && (this.posicaoy + this.posicaox < 11)) {
                this.s = this.vida - D;
                this.vida = s;
                if (s > 0) {
                    System.out.println("A torre " + torrenome + " deu " + D + " de dano. Vida restante: " + s);
                } else {
                    System.out.println("O inimigo morreu!");
                    return;
                }
            }
        }
    }


    void percurso2(float D, String torrenome, int torrex, int torrey) throws InterruptedException {
        for (int i = 0; i <= 5; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaoy += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));

            if (torrey - this.posicaoy <= 3) {
                this.s = this.vida - D;
                this.vida = s;
                if (s > 0) {
                    System.out.println("A torre " + torrenome + " deu " + D + " de dano. Vida restante: " + s);
                } else {
                    System.out.println("O inimigo morreu!");
                    return;
                }
            }
        }
    }

    void percurso3(float D, String torrenome, int torrex, int torrey) throws InterruptedException {
        for (int i = 0; i < 7; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaox += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));

            
            if (torrex - this.posicaox <= 3) {
      
                this.s = this.vida - D;
            this.vida = s;
                if (s > 0) {
                    System.out.println("A torre " + torrenome + " deu " + D + " de dano. Vida restante: " + s);
                } else {
                    System.out.println("O inimigo morreu!");
                    return;
                }
            }
        }

        if (this.vida > 0) {
            System.out.println("O inimigo chegou na torre com " + this.vida + " pontos de vida!");
        }
    }

    void percurso3vazio() throws InterruptedException{
     for (int i = 0; i < 7; i++) {
            System.out.println("Posição inimigo: (" + this.posicaox + ", " + this.posicaoy + ")");
            this.posicaox += 1;
            Thread.sleep((long) (this.tempo_de_chegada_segundos * 10));
     }
       if (this.vida > 0) {
            System.out.println("O inimigo chegou na torre com " + this.vida + " pontos de vida!");
        }
    }
}