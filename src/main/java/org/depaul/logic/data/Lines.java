package org.depaul.logic.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Lines {

    private final IntegerProperty lines = new SimpleIntegerProperty(0);

    public IntegerProperty linesProperty() {
        return lines;
    }

    public void add(int i){
        lines.setValue(lines.getValue() + i);
    }

    public void reset() {
        lines.setValue(0);
    }
}
