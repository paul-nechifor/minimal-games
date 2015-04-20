package ro.minimul.mg.tetravex;

import java.util.ArrayList;
import java.util.Collections;

public class PieceSetBuilder {
    public static Piece[][] build(int lin, int col) {
        int[][] vertValues = makeRandomIntArray(lin + 1, col, Piece.N_VALUES);
        int[][] horizValues = makeRandomIntArray(lin, col + 1, Piece.N_VALUES);        
        Piece[][] matrix = new Piece[lin][col];
        
        // Creating solved matrix.
        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = new Piece(i, j,
                        vertValues[i][j],
                        horizValues[i][j+1],
                        vertValues[i+1][j],
                        horizValues[i][j]);
            }
        }
        
        shuffleMatrix(matrix, lin, col);
        
        return matrix;
    }
    
    private static int[][] makeRandomIntArray(int lin, int col, int max) {
        int[][] ret = new int[lin][col];
        
        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                ret[i][j] = (int)(Math.random() * max);
            }
        }
        
        return ret;
    }
    
    private static void shuffleMatrix(Piece[][] matrix, int lin, int col) {
        ArrayList<Piece> list = new ArrayList<Piece>();
        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                list.add(matrix[i][j]);
            }
        }
        
        Collections.shuffle(list);

        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = list.get(i * col + j);
            }
        }
    }
}
