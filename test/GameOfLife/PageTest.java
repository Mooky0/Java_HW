package GameOfLife;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PageTest {

    @Test
    public void readBSTest() {
        Page page = new Page("Test");
        Page.readBS();
        HashMap<String, ArrayList<Integer>> rules = new HashMap<>();
        ArrayList<Integer> born = new ArrayList<>();
        born.add(3);
        rules.put("born", born);
        ArrayList<Integer> survive = new ArrayList<>();
        survive.add(2);
        survive.add(3);
        rules.put("survive", survive);
        assertEquals(rules,Table.getRules());
    }
}