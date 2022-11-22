/**
 * Class for the many variables relating to the board.
 * Also, this class is a singleton.
 *
 * @Project Ultimate Reversi
 * @Author Medusa Dempsey
 * @Version 1.0
 * @since 1.0
 */
package org.example;

import java.util.ArrayList;

public class Model {
    /**
     * ArrayList of all views to update.
     */
    private final ArrayList<View> listOfViews = new ArrayList<View>();
    /**
     * Static instance.
     */
    private static final Model model = new Model();
    /**
     * BoardState object.
     */
    BoardState bState = new BoardState();

    // private constructor

    /**
     * Private constructor. Prevents external methods instantiating this class.
     *
     * @since 1.0
     */
    private Model() {
    }

    /**
     * Accessor method for static instance.
     *
     * @return Static instance.
     * @since 1.0
     */
    public static Model get() {
        return model;
    }

    /**
     * Method for adding a view to the list of views to update.
     *
     * @param view View to add to the list of views.
     * @since 1.0
     */
    public void storeView(View view) {
        listOfViews.add(view);
    }

    /**
     * Method to update all views in the list of views.
     *
     * @since 1.0
     */
    public void updateAllViews() {
        for (View listOfView : listOfViews) {
            listOfView.update();
        }
    }
}
