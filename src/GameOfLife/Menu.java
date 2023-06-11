package GameOfLife;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;


/**
 * Menürendszer. Az ablak tetején megjelenő menurendszer.
 * Egyelten nagy metódusból áll, ami létrehoz mindent. Egyébként még pár ActionListener-ből áll, a menük kezelésésre.
 * Nagyrészt innen vettem át a kódot: <a href="https://www.tutorialspoint.com/swing/swing_jmenu_control.htm">tutorialspoint</a>
 */
public class Menu extends JMenuBar{

    Menu() {
        showMenu();
    }

    private void showMenu(){
        //create menus
        JMenu fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Load");
        openMenuItem.setActionCommand("Load");

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setActionCommand("Save");

        JMenuItem randomMenuItem = new JMenuItem("Random");
        saveMenuItem.setActionCommand("Random");

        openMenuItem.addActionListener(new LoadSaveFileListener());
        saveMenuItem.addActionListener(new SaveMatrixActionListener());
        randomMenuItem.addActionListener(new RandomActionListener());

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(randomMenuItem);
        fileMenu.addSeparator();

        //add menu to menubar
        add(fileMenu);
    }

    class LoadSaveFileListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Table.loadMatrix("map.mx");
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            Page.getEast().repaint();
        }
    }
    class SaveMatrixActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Table.saveMatrix("map.mx");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    class RandomActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Table.randomMatrix();
            Page.getEast().repaint();
        }
    }

}
