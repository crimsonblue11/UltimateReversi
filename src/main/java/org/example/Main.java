/**
 * Main class.
 *
 * @Project Ultimate reversi
 * @Author Medusa Dempsey
 * @Version 1.0
 * @since 1.0
 */

package org.example;

public class Main {
    /**
     * Main method.
     * Instantiates two views, one black and one white.
     *
     * @since 1.0
     */
    public static void main(String[] args) {
        new View(false).createGUI();
        new View(true).createGUI();
    }
}
