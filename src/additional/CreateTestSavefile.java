package additional;

import GameOfLife.Table;

import java.io.IOException;

public class CreateTestSavefile {
    public static void main(String[] args) {
        Table table = new Table("Test.txt");
        try {
            Table.saveMatrix("mapForTest.mx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
