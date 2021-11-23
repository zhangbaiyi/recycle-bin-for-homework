package view;

import javax.swing.*;

public class Checkbox {
    private int row;
    private int col;
    JCheckBox ckbox;

    Checkbox() {
        this.row = 0;
        this.col = 0;
        ckbox = new JCheckBox();
        ckbox.setSelected(false);
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }
}
