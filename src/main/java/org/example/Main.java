/**
 * Main class.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example;

public class Main {
    /**
     * Main method.
     * Instantiates two views, one black and one white.
     */
    public static void main(String[] args) {
        new View(false).CreateGUI();
        new View(true).CreateGUI();
    }
}
