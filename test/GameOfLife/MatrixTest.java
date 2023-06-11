package GameOfLife;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MatrixTest {


    /**
    * Konstruktor testek:
    * File ból beolvasott Matrix konstruktor teszt.
     * @throws IOException ha probléma van a file-al
     */
    @Test
    public void MatrixConstructorFromFileTest() throws IOException, BadFileException {
        Matrix mx = new Matrix("smallGrid.txt");
        assertEquals(3, mx.width());
        assertEquals(3, mx.height());
        assertEquals(1, mx.getValueAt(2, 1));
        assertEquals(0, mx.getValueAt(0, 0));
    }

    /**
     * Matrix konstruktora hibás vagy nem talált file esetenén exceptiopn dob-e?
     * @throws Exception
     */
    @Test
    public void MatrixConstructorFromFileExceptionTest(){
        assertThrows(IOException.class, () -> {
            Matrix mx = new Matrix("alma");
        });
    }

    /**
     * Exception-t dob, mert rossz a file formátuma.
     */
    @Test
    public void MatrixConstructorFromFileBadFileExceptionTest(){
        assertThrows(BadFileException.class, () -> {
            Matrix mx = new Matrix("badGrid.txt");
        });
    }

    @Test
    public void MatrixConstructorFromDimensionTest(){
        Matrix mx = new Matrix(4, 3);
        assertEquals(4, mx.width());
        assertEquals(3, mx.height());
    }

}