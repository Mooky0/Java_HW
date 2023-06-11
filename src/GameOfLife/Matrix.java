package GameOfLife;

import java.io.*;
import java.util.Arrays;

/**
 * Ez az osztály tárolja a pályát, és tölti be, txt file-ból.
 */
public class Matrix implements Serializable {
    //A pálya szélessége
    private int M = 50;
    //A pálya magassága
    private int N = 50;
    //A nátrix ami 1/0 értéket tárol attól függően, hogy a cella él-e vagy sem.
    private int[][] matrix = new int[M][N];

    /**
     * Az osztáy konstruktora. Egy megadott és megfelelően formázott szövegese file-ból betölti a pályát.
     * A formázás: Az értékeket <Code>tab</Code> választja el, és a sorok új sorba.
     * Ahol nincs érték, ott nulla lesz, ez a java inicializációs tulajdonságaiból jön.
     * @param file Az a file neve, amit be szeretnénk olvasni, a projekt legfelső szintjén kell lennie.
     * @throws IOException Csak akkor dobja, ha valami belső függvény dobja, tehát mondjuk a file nem található,
     *      vagy írásvédett stb.
     * @throws BadFileException Akkor tobbja, ha a formázás nem helyes, és tudja beolvasni.
     */
    public Matrix(String file) throws IOException, BadFileException {
        int[][] safemx = matrix.clone();
        File grid = new File(file);
        FileReader fr = new FileReader(grid);
        BufferedReader br = new BufferedReader(fr);
        String[] read;
        int sor = 0;
        br.mark(200);
        int size = (br.readLine().length()+1)/2;
        N = M = size;
        matrix = new int[size][size];
        br.reset();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            read = line.split("\t");
            for (int i=0; i<read.length; i++){
                try {
                    matrix[sor][i] = Integer.parseInt(read[i]);
                } catch (NumberFormatException e){
                    matrix = safemx.clone();
                    N = safemx.length;
                    M = safemx[0].length;
                    throw new BadFileException("Hiba a szám olvasásánál @ " + sor + ":" + i);
                }
            }
            sor++;
        }
        br.close();
    }

    /**
     * Konstruktor. Egy üres mátrixot hoz létre, csak inicialiációs szempontból létezik, hogy utána ez értékeit módosíthassuk.
     * @param m szélesség
     * @param n magasság
     */
    public Matrix(int m, int n) {
        M = m;
        N = n;
        matrix = new int[m][n];
    }

    /**
     * A pálya szélességét (azaz az M-t) adja vissza.
     * @return a pálya szélessége.
     */
    public int width(){
        return M;
    }

    /**
     * A pálya magasságát (azaz az N-t) adja vissza.
     * @return a pálya magassága.
     */
    public int height(){
        return N;
    }

    /**
     * egy bizonyos sor és oszlop által meghatározott cella értékét adja vissza
     * @param rowNumber a sor száma
     * @param columnNumber az oszlop száma.
     * @return a sor és oszlop álltal meghatározott érték
     */
    public int getValueAt(int rowNumber, int columnNumber){
        return matrix[rowNumber][columnNumber];
    }

    /**
     * Sor ész oszlop szám álltat meghatározott cella értékét állytja be.
     * @param rowNumber a sor száma
     * @param columnNumber az oszlop száma
     * @param value a beállítandó érték.
     */
    public void setValueAt(int rowNumber, int columnNumber, int value){
        matrix[rowNumber][columnNumber] = value;
    }

    /**
     * Feltölti a matrix minden celláját random értékkel. a mérete ugyan akkora mint ami előzőleg volt.
     * Megoldható, hogy nagyobb/kisebb legyen, de a programban minden plya 50x50-es, ezért ez is.
     */
    public void randomFill(){
        matrix = new int[N][M];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                matrix[i][j] = Math.round((float)Math.random());
            }
        }
    }

    /**
     * Egyenlőség vizsgáló. Magát a 2D-s tömböt deepEquals-al vizsgálja, így a mátrix minden értékének egyezése esetén
     *  ad vissza igaz értéket.
     * @param o A jobb oldali vizsgálandó Matrix
     * @return Igaz, ha egyenlőek a mátrixok értékei, különben hamis
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix1 = (Matrix) o;

        if (M != matrix1.M) return false;
        if (N != matrix1.N) return false;
        return Arrays.deepEquals(matrix, matrix1.matrix);
    }
}
