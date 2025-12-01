package leandro;

/**
 *
 * @author DANIEL
 */
public class Jogador {
    int dinheiro = 20;
    int verdade = 1;

    void creditardinheiro(){
        this.dinheiro += 60;
    }

    
    void venderTorre(Torre torre){
        int valorVenda = 0;
        if (torre.poder.equals("laser")) {
            valorVenda = 100; // metade do preço original (200)
        } else if (torre.poder.equals("Bola de fogo")) {
            valorVenda = 40; // metade do preço original (80)
        }
        this.dinheiro += valorVenda;
        System.out.println("Torre vendida! Você recebeu " + valorVenda + " moedas.");
    }
}