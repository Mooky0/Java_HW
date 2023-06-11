package GameOfLife;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.TabableView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TableTest {
    private static Table table;

    @Before
    public void setUp() throws Exception {
        table = new Table("grid.txt");
    }

    @Test
    public void loadMatrixTest() throws IOException, ClassNotFoundException {
        Table.loadMatrix("map.mx");
        table.simulationStart();
        table.simulationStop();
        HashMap<String, ArrayList<Integer>> rules = new HashMap<>();
        ArrayList<Integer> born = new ArrayList<>();
        born.add(3);
        rules.put("born", born);
        ArrayList<Integer> survive = new ArrayList<>();
        survive.add(2);
        survive.add(3);
        rules.put("survive", survive);
        assertEquals(rules, Table.getRules());
    }

    @Test
    public void loadAndSaveMatrixTest() throws IOException{
        Table.saveMatrix("map.mx");
        File f = new File("map.mx");
        assertTrue(f.exists());
        assertTrue(f.isFile());
    }

    @Test
    public void loadMatrixExceptionTest(){
        assertThrows(IOException.class, () -> {
            Table.loadMatrix("alma");
        });
    }

    /**
     * Teszteli egy lépését az algoritmusnak. hogy a szimuláció jól működik-e
     */
    @Test
    public void stepTest() throws IOException, ClassNotFoundException, BadFileException {
        Table.loadMatrix("mapForTest.mx");
        assertEquals(10, Table.matrix.width());
        assertEquals(10, Table.matrix.height());
        table.step();
        Matrix mx = new Matrix("TestResult.txt");
        assertEquals(Table.matrix, mx);

    }

    @AfterClass
    public static void tearDown() throws IOException, ClassNotFoundException {
        Table.loadMatrix("map.mx");
    }
}