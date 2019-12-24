package com.example.sudoku.Core;

import java.util.Random;

public enum Difficulty {
        easy(8, 2),
        medium(6, 3),
        hard(3,4),
        random( new Random().nextInt(6) + 3,  3); // entre 3 e 8

        private final int erros;
        private final int points;

        Difficulty(int erros, int points) {
                this.erros = erros;
                this.points = points;
        }

    public int getPoints() {
        return points;
    }
    public int getErros() {
                return erros;
        }
}
