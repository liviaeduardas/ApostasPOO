package org.example;

import View.MainFrame;

public class Main {

    public static void main(String[] args) {

        try {

            MainFrame tela = new MainFrame();
            tela.setVisible(true);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}