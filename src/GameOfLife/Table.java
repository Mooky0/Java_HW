package GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Tábla osztály. Feladat az ablak jobb oldalának kijelzése, és kezelése.
 * A kijelzést a Graphics osztéllyan valósítja meg.
 * Továbbá itt kapott helyet a szimuláció futtatása is.
 */
public class Table extends JPanel {
    private int generation = 0;
    //A kockák és négyzetek mögötti szín.
    private static Color backgroundColor = Color.white;
    //A kockák és négyzetek színe, valamint a generáció szöveg színe.
    private static Color foregroundColor = Color.black;
    //Éppen milyen formákat akarunk kijelezni. Ez egy enum, amely jelenleg 2 értéket tud felvenni.
    Shapes shape = Shapes.SQUARE;
    //A pálya, a 20,20 az csak beállított érték, jelentése nincs, de kell, hogy legyen valami értéke a szélességnek/magasságnak.
    static Matrix matrix = new Matrix(20, 20);
    //A játékszabályt tároló. A HashMap-ben 2 kulcs van, a "born" és a "survive", mindegyikhez tartozik egy ArrayList
    // mint value amiben azok at értékek vannak, ahány szomszédja ha a cellának van, a cella megszóletik (bron) vagy túlél (survive).
    private static HashMap<String, ArrayList<Integer>> rules = new HashMap<>();
    static {
        ArrayList<Integer> born = new ArrayList<>();
        born.add(3);
        rules.put("born", born);
        ArrayList<Integer> survive = new ArrayList<>();
        survive.add(2);
        survive.add(3);
        rules.put("survive", survive);
    }
    // A timer, ami 50 ms-ént lépteti a szimulációt, fur folyamatosan.
    private Timer timer = new Timer(50, e1 -> {
        step();
        this.repaint();
    });

    /**
     * Konstruktor. Betölti pályát.
     * @param file A file neve amit be kell töltenie. A projekt legfelső szintjén kell, hogy legyen.
     */
    public Table(String file) {
        try {
            loadGrid(file);
        } catch (IOException e) {
            System.out.println("Nem megfelelő file");
        } catch (BadFileException e){
            System.out.println("Nem sikerült olvasni a file-t a mx nem változott.");
        }
        backgroundColor = Color.CYAN;
    }

    /**
     * A JPanel metósuda felüldefiniálva. Megnézni, hogy négy- vagy háromszöget kell rajtolnia, és meghívja a szükséges
     * metódust.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(foregroundColor);
        super.paintComponent(g);
        g.drawString("Generation: " + generation, 10, 15);

        if(shape == Shapes.TRIANGLE){
            drawTriangles(matrix, g);
        } else {
            drawSquares(matrix, g);
        }
    }

    /**
     * A négyszögeket rajzoló metódus. Végigmegy a mátrixon, és kirajzol minden négyszöget. A színeket válogatva,
     * ami attribútumként be van állítva.
     * @param matrix A mátrix amit ki kell rajzolnia.
     * @param g the <code>Graphics</code> object to protect
     */
    private void drawSquares(Matrix matrix, Graphics g){
        for (int i =0; i<matrix.width(); i++){
            for (int j = 0; j < matrix.height(); j++) {
                int y = i*10+20;
                if (matrix.getValueAt(i, j) == 0) {
                    g.clearRect(j * 10 +10, y, 10, 10);
                    g.setColor(backgroundColor);
                    g.fillRect(j * 10+10, y, 10, 10);
                    g.setColor(foregroundColor);
                    g.drawRect(j * 10+10, y, 10, 10);
                } else {
                    g.setColor(foregroundColor);
                    g.fillRect(j * 10+10, y, 10, 10);
                }
            }
        }
    }

    /**
     * A háromszögeket kirajzoló metódus. minden alkalommal a cella sor és oszéopából kiszámolja a háromszög 3 pontját,
     * majd ezt a Polynomot kirajzolja. Fontos, hogy mivel minden második háromszöf fejjel lefele áll, így a sor 2-vel
     * való oszthatóságát vizsgáljuk, és a pontok számítása ettől függ.
     * @param matrix
     * @param g the <code>Graphics</code> object to protect
     */
    private  void drawTriangles(Matrix matrix, Graphics g) {
        int x = 10;
        for (int i = 0; i < matrix.width(); i++) {
            for (int j = 0; j < matrix.height(); j++) {
                int y = i * 10 + 20;
                int[] xpoints;
                int[] ypoints;
                if (j % 2 == 1) {
                    xpoints = new int[]{x + 5, x, x + 10};
                    ypoints = new int[]{y, y + 10, y + 10};
                } else {
                    xpoints = new int[]{x, x + 10, x + 5};
                    ypoints = new int[]{y, y, y + 10};
                }
                Polygon triangle = new Polygon(xpoints, ypoints, 3);
                if (matrix.getValueAt(i, j) == 0) {
                    g.setColor(foregroundColor);
                    g.drawPolygon(triangle);
                } else {
                    g.setColor(foregroundColor);
                    g.fillPolygon(triangle);
                }
                x = x + 5;
            }
            x = 10;
        }
    }

    /**
     * Egy lépés a szumilációban. Lemásolja a mátrixot, majd a régin végiglépegetve,minden cella szomszédait összeszmolva
     * meghatározza az új mátrixot, majd a végén a régit felülírja az újjal.
     * Ez a metódus lépteti a generációszámolót.
     */
    void step(){
        int M = matrix.width();
        int N = matrix.height();
        Matrix future = new Matrix(M, N);

        for (int l = 0; l < M; l++)
        {
            for (int m = 0; m < N; m++)
            {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if ((l+i>=0 && l+i<M) && (m+j>=0 && m+j<N))
                            aliveNeighbours += matrix.getValueAt(l+i, m+j);

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= matrix.getValueAt(l, m);

                //A cell dies, bc to lonely or overpopulated
                if ((matrix.getValueAt(l, m) == 1) && !(rules.get("survive").contains(aliveNeighbours)))
                    future.setValueAt(l, m, 0);

                    // A new cell is born
                else if ((matrix.getValueAt(l, m) == 0) && (rules.get("born").contains(aliveNeighbours)))
                    future.setValueAt(l, m, 1);

                    // Remains the same
                else
                    future.setValueAt(l, m, matrix.getValueAt(l, m));
            }
        }
        matrix = future;
        generation++;
    }

    /**
     * csak egy adapter metódus, hogy a Page-ből lehessem betölteni, meghatározott nevő file-t. Így ez a metódus, csak
     * a Matrix mgfelelő konstruktorát hívja meg.
     * @param file A beolvasandó file neve. A projekt legfelső mappájában kell lennie.
     * @throws IOException Ha a filet, nem tudja, megnyitni
     * @throws BadFileException Ha a file formázása nem megfelelő, és nem tudja beolvasni
     * @see Matrix
     */
    public static void loadGrid(String file) throws IOException, BadFileException {
        matrix = new Matrix(file);
    }

    /**
     * Kívülről meghívható metósud a szimuláció indítására.
     */
    public void simulationStop(){
        timer.stop();
    }

    /**
     * Kivülről meghívható metódus a szimuláció megállítására.
     */
    public void simulationStart(){
        timer.start();
    }

    /**
     * Enumerátor, hogy milyen alakot kell kijelezni.
     */
    enum Shapes{
        SQUARE, TRIANGLE
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * A Matrix sorosítás. A Matrix, és annak minden attribútumának kiírása, java standard módon.
     * @param file A célfile, amibe írni szeretnénk.
     * @throws IOException Ha a file valamiért nem írható.
     */
    static public void saveMatrix(String file) throws IOException{
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(matrix);
        out.close();

    }

    /**
     * A Matrix desorosítása. A MAtrix minden attribútumának beolvasása, a megadott file-ból.
     * @param file A file amiből olvasni szeretnénk.
     * @throws IOException Ha a file valamiért nem olvasható.
     * @throws ClassNotFoundException Ha az osztály nem található. Nem szabad, hogy keletkezzen.
     */
    static public void loadMatrix(String file) throws IOException, ClassNotFoundException{
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(f);
        matrix = (Matrix) in.readObject();
        in.close();
    }

    /**
     * Kívülről elérhető metódus új random matrix generálására.
     */
    public static void randomMatrix(){
        matrix.randomFill();
    }

    /**
     * A játék szabályait adja, vissza, eredeti(HashMap) formában, fent meghatározott módon.
     * @return A szabályok.
     */
    public static HashMap<String, ArrayList<Integer>> getRules() {
        return rules;
    }

    /**
     * A játékszabályok beállítása, fent meghatározz formátumban HashMap-ben.
     * @param rules
     */
    public static void setRules(HashMap<String, ArrayList<Integer>> rules) {
        Table.rules = rules;
    }
}
