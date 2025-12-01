package leandro;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JogoGUI extends JPanel {
   //defino caracteristicas da janela
    private static final int COLS = 20;
    private static final int ROWS = 10;
    private static final int CELL = 30;
    private static final int LEFT_PANEL_W = 230; //com botoes
    private static final int TOP_MARGIN = 30; //margem de cima
    private static final int WIDTH = LEFT_PANEL_W + COLS * CELL; //largura e depois altura
    private static final int HEIGHT = TOP_MARGIN + ROWS * CELL + 40;

    private final List<Point> percurso = new ArrayList<>(); //guarda coordenadas do percurso

    private Inimigo inimigo;  //declarando a existencia de objetos derivados das classes
    private Jogador jogador;
    private final List<Torre> torres = new ArrayList<>();  //lista para as torres que eu inserir ficarem armazenadas

    private final JLabel lblDinheiro = new JLabel();  //aba de mostrar dinheiro + botões
    private final JButton btnIniciar = new JButton("Iniciar rodada");
    private final JButton btnComprar = new JButton("Comprar torre");
    private final JButton btnVender = new JButton("Vender torre");
    private final JButton btnSair = new JButton("Sair");

    private Timer rodadaTimer;//quando o inimigo começa a andar
    private int indicePercurso = 0;//em qual ponto da lista esta o percurso
    private boolean rodadaEmAndamento = false;

    public JogoGUI() {  //monta a interface do jogo
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        initPercurso();
        initGameObjects();
        initUI();
        initTimer();
    }

    private void initPercurso() {  //percurso sendo colocado na lista
        percurso.clear();  //???????
        for (int x = 0; x <= 8; x++) percurso.add(new Point(x, 0));
        for (int y = 1; y <= 6; y++) percurso.add(new Point(8, y));
        for (int x = 9; x <= 14; x++) percurso.add(new Point(x, 6));
    }

    private void initGameObjects() {
        try {
            inimigo = new Inimigo(600, 120, 5, 5, "toxico");
        } catch (Exception ex) {
            try {
                inimigo = (Inimigo) Class.forName("testeleandro.Inimigo").getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao instanciar Inimigo: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        try {
            jogador = new Jogador();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao instanciar Jogador: " + e.getMessage());
            throw new RuntimeException(e);
        }

        inimigo.posicaox = percurso.get(0).x;
        inimigo.posicaoy = percurso.get(0).y;
    }

    private void initUI() {//detalhes da interface grafica
        lblDinheiro.setBounds(12, 6, 200, 24); //tamanho da janela do dinheiro
        updateDinheiroLabel();
        add(lblDinheiro);

        btnIniciar.setBounds(12, 40, 200, 30);//tamanho e posições dos botões
        btnComprar.setBounds(12, 80, 200, 30);
        btnVender.setBounds(12, 120, 200, 30);
        btnSair.setBounds(12, 160, 200, 30);

        add(btnIniciar);//coloca os botões
        add(btnComprar);
        add(btnVender);
        add(btnSair);

        btnIniciar.addActionListener(e -> iniciarRodada());//define a função que cada botão executa
        btnComprar.addActionListener(e -> escolherTipoTorre());
        btnVender.addActionListener(e -> venderTorreUI());
        btnSair.addActionListener(e -> System.exit(0));
    }

    private void initTimer() {
        rodadaTimer = new Timer(300, e -> stepRodada());
    }

    private void iniciarRodada() {
        if (rodadaEmAndamento) return;

        indicePercurso = 0;
        Point start = percurso.get(0);
        inimigo.posicaox = start.x;
        inimigo.posicaoy = start.y;

        // Reinicia a vida do inimigo a cada rodada
        inimigo.vida = 600;

        rodadaEmAndamento = true;
        rodadaTimer.start();
    }

    private void stepRodada() {
        if (!rodadaEmAndamento) return;

        indicePercurso++;
        if (indicePercurso >= percurso.size()) {//quando o inimigo chega ao fim, tudo para e credita dinheiro
            rodadaTimer.stop();
            rodadaEmAndamento = false;
            jogador.creditardinheiro();
            updateDinheiroLabel();
            SwingUtilities.invokeLater(this::mostrarOpcoesAposRodada);
            return;
        }

        Point p = percurso.get(indicePercurso);
        inimigo.posicaox = p.x;
        inimigo.posicaoy = p.y;

        aplicarDanoDasTorres();

        if (inimigo.vida <= 0) {
            rodadaTimer.stop();
            rodadaEmAndamento = false;
            SwingUtilities.invokeLater(this::mostrarDialogVitoria);
            return;
        }

        repaint();
    }

    private void aplicarDanoDasTorres() {
        for (Torre t : torres) {
            int dx = Math.abs(t.posicaox - inimigo.posicaox);
            int dy = Math.abs(t.posicaoy - inimigo.posicaoy);
            if (dx <= 3 && dy <= 3) {
                inimigo.receber_dano(t.dano_por_segundo, t.poder); //determina quando receber dano
            }
            if (inimigo.vida <= 0) break;
        }
    }

    private void mostrarOpcoesAposRodada() {
        String[] opcoes = {"Comprar torre", "Vender torre", "Jogar novamente", "Sair"};
        int escolha = JOptionPane.showOptionDialog(this,
                "parabéns, você venceu. Agora o que deseja fazer?",
                "Fim de rodada",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        if (escolha == 0) {
            escolherTipoTorre();
        } else if (escolha == 1) {
            venderTorreUI();
        } else if (escolha == 2) {
            iniciarRodada();
        } else {
            System.exit(0);
        }
    }

    private void mostrarDialogVitoria() {
        String[] op = {"Jogar novamente", "Sair"};
        int res = JOptionPane.showOptionDialog(this,
                "Parabéns, o inimigo foi derrotado!",
                "Vitória",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                op,
                op[0]);
        if (res == JOptionPane.YES_OPTION) {
            torres.clear();
            inimigo.vida = 450;
            inimigo.posicaox = percurso.get(0).x;
            inimigo.posicaoy = percurso.get(0).y;
            updateDinheiroLabel();
            repaint();
        } else {
            System.exit(0);
        }
    }

    //jogador escolhe o tipo da torre
    private void escolherTipoTorre() {
        String[] tipos = {"Torre Alfa (80 moedas)", "Torre Beta (200 moedas)", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(this,
                "Escolha o tipo de torre:",
                "Tipo de Torre",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]);

        if (escolha == 0) {
            mostrarJanelaComprar("alfa", 80);
        } else if (escolha == 1) {
            mostrarJanelaComprar("beta", 200);
        }
    }

    // jogador escolhe onde colocar
    private void mostrarJanelaComprar(String tipo, int preco) {
        String[] locais = {"(10,0)", "(8,7)", "(15,6)", "Cancelar"};
        int sel = JOptionPane.showOptionDialog(this,
                "Escolha a posição para colocar sua torre " + tipo + ":",
                "Posição da Torre",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                locais,
                locais[0]);

        if (sel < 0 || sel >= locais.length - 1) return;

        int x = 0, y = 0;
        if (sel == 0) { x = 10; y = 0; }
        else if (sel == 1) { x = 8; y = 7; }
        else if (sel == 2) { x = 15; y = 6; }

        if (jogador.dinheiro < preco) {
            JOptionPane.showMessageDialog(this, "Dinheiro insuficiente!");
            return;
        }

        for (Torre t : torres) {
            if (t.posicaox == x && t.posicaoy == y) {
                JOptionPane.showMessageDialog(this, "Já existe torre nessa posição!");
                return;
            }
        }

        Torre nova;
        if (tipo.equals("alfa")) {
            nova = new Torre(20, 10, 30, "Bola de fogo", x, y);
        } else {
            nova = new Torre(30, 10, 50, "laser", x, y);
        }

        torres.add(nova);
        jogador.dinheiro -= preco;
        updateDinheiroLabel();
        repaint();
    }

    private void venderTorreUI() {
        if (torres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há torres para vender.");
            return;
        }
        String[] lista = new String[torres.size()];
        for (int i = 0; i < torres.size(); i++) {
            Torre t = torres.get(i);
            lista[i] = (i + 1) + ": " + t.poder + " (" + t.posicaox + "," + t.posicaoy + ")";
        }
        String sel = (String) JOptionPane.showInputDialog(this,
                "Escolha a torre para vender:",
                "Vender torre",
                JOptionPane.PLAIN_MESSAGE,
                null,
                lista,
                lista[0]);
        if (sel == null) return;
        int idx = Integer.parseInt(sel.split(":")[0].trim()) - 1;
        Torre vendida = torres.remove(idx);
        jogador.venderTorre(vendida);
        updateDinheiroLabel();
        repaint();
    }

    private void updateDinheiroLabel() {
        lblDinheiro.setText("Dinheiro: " + jogador.dinheiro + " moedas");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(220, 235, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, LEFT_PANEL_W, getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, LEFT_PANEL_W - 1, getHeight() - 1);

        int fieldX = LEFT_PANEL_W;
        int fieldY = TOP_MARGIN;

        g.setColor(new Color(240, 248, 255));
        g.fillRect(fieldX, fieldY, COLS * CELL, ROWS * CELL);

        g.setColor(new Color(200, 200, 200));
        for (Point p : percurso) {
            int px = fieldX + p.x * CELL;
            int py = fieldY + p.y * CELL;
            g.fillRect(px, py, CELL, CELL);
        }

        g.setColor(new Color(200, 220, 235));
        for (int r = 0; r <= ROWS; r++)
            g.drawLine(fieldX, fieldY + r * CELL, fieldX + COLS * CELL, fieldY + r * CELL);
        for (int c = 0; c <= COLS; c++)
            g.drawLine(fieldX + c * CELL, fieldY, fieldX + c * CELL, fieldY + ROWS * CELL);

        for (Torre t : torres) {
            int tx = fieldX + t.posicaox * CELL + CELL / 2;
            int ty = fieldY + t.posicaoy * CELL + CELL / 2;
            if (t.poder != null && t.poder.equalsIgnoreCase("laser"))
                g.setColor(Color.MAGENTA.darker());
            else
                g.setColor(Color.ORANGE.darker());
            g.fillOval(tx - 12, ty - 12, 24, 24);
            g.setColor(new Color(100, 100, 255, 40));
            int alcancePx = CELL * 3;
            g.fillOval(tx - alcancePx, ty - alcancePx, alcancePx * 2, alcancePx * 2);
        }

        int ix = fieldX + inimigo.posicaox * CELL;
        int iy = fieldY + inimigo.posicaoy * CELL;
        g.setColor(Color.RED.darker());
        g.fillRect(ix + 4, iy + 4, CELL - 8, CELL - 8);

        int barraW = CELL - 8;
        int barraX = ix + 4;
        int barraY = iy - 8;
        g.setColor(Color.GRAY);
        g.fillRect(barraX, barraY, barraW, 6);
        g.setColor(Color.GREEN.darker());
        int w = (int) Math.max(0, Math.min(barraW, barraW * (inimigo.vida / 450.0)));
        g.fillRect(barraX, barraY, w, 6);
        g.setColor(Color.BLACK);
        g.drawRect(barraX, barraY, barraW, 6);

        if (!rodadaEmAndamento) {
            g.setColor(Color.BLACK);
            g.drawString("Clique 'Iniciar rodada' para começar.", LEFT_PANEL_W + 10, 18);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tower Defense - Interface (Integrada)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JogoGUI());
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
