package org.example;

import java.util.ArrayList;

public class Model {
    private final ArrayList<View> listOfViews = new ArrayList<View>();
    private static final Model model = new Model();

    // package level access
    BoardState bState = new BoardState();

    // private constructor
    private Model() {
    }

    // get method for singleton
    public static Model get() {
        return model;
    }

    public void storeView(View view) {
        listOfViews.add(view);
    }

    public void updateAllViews() {
        for (View listOfView : listOfViews) {
            listOfView.update();
        }
    }
}
