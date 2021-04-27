package edu.skku.map.nonogram;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

public class BoardInfo {
    public static int ROW_COUNT = 20;
    public static int COL_COUNT = 20;
    public static int ROW_HINT_COUNT = (COL_COUNT + 1) / 2;
    public static int COL_HINT_COUNT = (ROW_COUNT + 1) / 2;

    private boolean[][] isBlack;
    private int[][] colHints;
    private int[][] rowHints;

    private ArrayList<Point> selectedPoints;

    BoardInfo(Bitmap srcImageBitmap) {
        isBlack = makeIsBlack(srcImageBitmap);
        colHints = makeColHints();
        rowHints = makeRowHints();
        this.selectedPoints = new ArrayList<Point>();
    }

    private boolean[][] makeIsBlack(Bitmap srcImageBitmap) {
        int width = srcImageBitmap.getWidth();
        int height = srcImageBitmap.getHeight();
        int[][] blackCount = new int[ROW_COUNT][COL_COUNT];

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                blackCount[i][j] = 0;
            }
        }

        int itemWidthPixelCount = width / COL_COUNT;
        int itemHeightPixelCount = height / ROW_COUNT;

        for (int row = 0; row < itemHeightPixelCount * ROW_COUNT; row++) {
            for (int col = 0; col < itemWidthPixelCount * COL_COUNT; col++) {
                int pixel = srcImageBitmap.getPixel(col, row);

                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);

                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                if (gray < 129) {
                    blackCount[row / itemHeightPixelCount][col / itemWidthPixelCount] += 1;
                }
            }
        }



        boolean result[][] = new boolean[ROW_COUNT][COL_COUNT];

        int itemPixelCount = itemWidthPixelCount * itemHeightPixelCount;
        int blackMinimumCount = itemPixelCount / 2;

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                if (blackCount[i][j] > blackMinimumCount) {
                    result[i][j] = true;
                } else {
                    result[i][j] = false;
                }
            }
        }

        return result;
    }

    private int[][] makeColHints() {
        int[][] hint = new int[COL_COUNT][COL_HINT_COUNT];

        for (int i = 0; i < COL_COUNT; i++) {
            for (int j = 0; j < COL_HINT_COUNT; j++) {
                hint[i][j] = 0;
            }
        }

        for (int i = 0; i < COL_COUNT; i++) {
            int topIndex = 0;

            for (int j = 0; j < ROW_COUNT; j++) {
                if (isBlack[j][i]) {
                    hint[i][topIndex] += 1;
                } else {
                    if (hint[i][topIndex] != 0) {
                        topIndex += 1;
                    }
                }
            }
        }

        return hint;
    }

    private int[][] makeRowHints() {
        int[][] hint = new int[ROW_COUNT][ROW_HINT_COUNT];

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < ROW_HINT_COUNT; j++) {
                hint[i][j] = 0;
            }
        }

        for (int i = 0; i < ROW_COUNT; i++) {
            int topIndex = 0;

            for (int j = 0; j < COL_COUNT; j++) {
                if (isBlack[i][j]) {
                    hint[i][topIndex] += 1;
                } else {
                    if (hint[i][topIndex] != 0) {
                        topIndex += 1;
                    }
                }
            }
        }

        return hint;
    }

    public int getColHint(int colIndex, int rowIndex) {
        return colHints[colIndex][rowIndex];
    }

    public int getColHintCount(int colIndex) {
        int hintCount = 0;

        for (int i = 0; i < BoardInfo.COL_HINT_COUNT; i++) {
            if (colHints[colIndex][i] > 0) {
                hintCount += 1;
            }
        }

        return hintCount;
    }

    public int getRowHint(int rowIndex, int colIndex) {
        return rowHints[rowIndex][colIndex];
    }

    public int getRowHintCount(int rowIndex) {
        int hintCount = 0;

        for (int i = 0; i < BoardInfo.ROW_HINT_COUNT; i++) {
            if (rowHints[rowIndex][i] > 0) {
                hintCount += 1;
            }
        }

        return hintCount;
    }

    public ArrayList<Point> getSelectedPoints() {
        return selectedPoints;
    }

    public void select(int rowIndex, int colIndex) {
        selectedPoints.add(new Point(rowIndex, colIndex));
    }

    public boolean getIsBlack(int rowIndex, int colIndex) {
        return isBlack[rowIndex][colIndex];
    }

    public int getBlackCount() {
        int count = 0;

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                if (isBlack[i][j]) {
                    count += 1;
                }
            }
        }

        return count;
    }

    public void clearSelectedPoints() {
        selectedPoints.clear();
    }
}
