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
            valorVenda = 100; 
        } else if (torre.poder.equals("Bola de fogo")) {
            valorVenda = 40; 
        }
        this.dinheiro += valorVenda;
        System.out.println("Torre vendida! Você recebeu " + valorVenda + " moedas.");
    }
}