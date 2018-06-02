package com.n2q.racing.view;

import javax.swing.*;

public abstract class BasePanel extends JPanel implements ViewInitializer {
    public BasePanel(){
        initContainer();
        initComponents();
        registerListeners();
    }
}