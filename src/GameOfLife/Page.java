package GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Az egész ablakot megvalósító, tároló és kezelő osztály. A main csak meghívja ennek az egyetlen Konstruktorát.
 */
public class Page extends JFrame {
    // A szumláció éppen fut-e
    private static boolean running = false;
    //A pálya. Ami egy Table
    private static Table east = new Table("grid.txt");
    //A kezelő felület
    private static JPanel west = new JPanel();
    private static JComboBox comboBox;
    private JMenuBar menu;
    private static JTextField bsTextField;

    /**
     * Konstruktor. Létrehozza az ablakot, és hozzáad mindent, ami kell.
     * Alapvetően 3 egységre osztik.
     *  1. Menu: menusáv. a Menu osztály kezeli.
     *  2. East: Table osztály példánya, a pálya megjelenítését csinálja.
     *  3. West: az irányítófelület, JPanel osztálya.
     * Továbbá van benn egy windowActionListener, ami azt a célt szolgálja, hogy ha kilépünk a programból, akkor
     * elmenti, a szimuláció aktuálsi szabályát.
     * @param title Az ablak címe.
     */
    public Page(String title){
        super(title);
        Dimension d1 = new  Dimension(120, 1);

        JButton startStopButton = new JButton("Start!/Stop");
        startStopButton.addActionListener(new startStopButtonActionListener());
        startStopButton.setSize(d1);
        startStopButton.setMinimumSize(d1);
        startStopButton.setMaximumSize(d1);

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new stepButtonActionListener());
        stepButton.setSize(d1);
        stepButton.setMinimumSize(d1);
        stepButton.setMaximumSize(d1);

        Object[] shapes = {"square", "triangle"};
        comboBox = new JComboBox<>(shapes);
        comboBox.setSize(d1);
        comboBox.setMinimumSize(d1);
        comboBox.setMaximumSize(d1);

        JLabel ruleLabel = new JLabel("Game rules:");
        //b/s mező
        JPanel bs = new JPanel();
        JLabel bsLabel = new JLabel("B/S: ");
        bsTextField = new JTextField(10);
        bsTextField.setText("3/23");
        bs.add(bsLabel);
        bs.add(bsTextField);

        east.setBackground(Table.getBackgroundColor());

        west.setBounds(61, 11, 81, 140);
        west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));

        west.add(Box.createRigidArea(new Dimension(5, 5)));
        west.add(startStopButton);
        startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        west.add(Box.createRigidArea(new Dimension(5, 5)));
        west.add(stepButton);
        stepButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        west.add(Box.createRigidArea(new Dimension(5, 5)));
        west.add(comboBox);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        west.add(Box.createRigidArea(new Dimension(5, 5)));
        west.add(ruleLabel);
        ruleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        west.add(bs);
        bs.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu = new Menu();
        setJMenuBar(menu);

        add(east, BorderLayout.CENTER);
        add(west, BorderLayout.WEST);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Table.saveMatrix("map.mx");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        Dimension d = new Dimension(689, 590);
        setMinimumSize(d);
        setSize(d);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        pack();
    }

    /**
     * Belső osztály, a szimuláció indítását és megállítását kezeli.
     */
    static class startStopButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            readBS();
            String shape = (String) comboBox.getSelectedItem();
            if (shape.equals("square")){
                east.shape = Table.Shapes.SQUARE;
            } else if(shape.equals("triangle")){
                east.shape = Table.Shapes.TRIANGLE;
            } else{
                System.out.println("Panic!");
            }
            if (running) {
                running = false;
                east.simulationStop();
            } else{
                running = true;
                east.simulationStart();
            }
        }
    }

    /**
     * Belső osztály, a szimuláció léptetését végzi.
     */
    static class stepButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            String shape = (String) comboBox.getSelectedItem();
            if (shape.equals("square")){
                east.shape = Table.Shapes.SQUARE;
            } else if(shape.equals("triangle")){
                east.shape = Table.Shapes.TRIANGLE;
            } else{
                System.out.println("Panic!");
            }
            readBS();
            if(running){
                running = false;
                east.simulationStop();
                east.step();
                east.repaint();
            } else{
                east.step();
                east.repaint();
            }
        }
    }

    /**
     * A játékszabályokat olvassa be és értelmezi, majd tárolja el a megfeleő helyen (Table.rules), hogy szimuláció használni tudja
     */
    public static void readBS(){
        String[] bsString = bsTextField.getText().split("/");
        HashMap<String, ArrayList<Integer>> bs = new HashMap<>();
        bs.put("born", new ArrayList<>());
        String[] s = bsString[0].split("");
        for (int i = 0; i < s.length; i++) {
            bs.get("born").add(Integer.parseInt(s[i]));
        }
        bs.put("survive", new ArrayList<>());
        s = bsString[1].split("");
        for (int i = 0; i < s.length; i++) {
            bs.get("survive").add(Integer.parseInt(s[i]));
        }
        Table.setRules(bs);
    }

    public static Table getEast() {
        return east;
    }
}
