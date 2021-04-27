package edu.skku.map.nonogram;

import android.view.View;

@FunctionalInterface
public interface OnClickGridItem {
    public void OnClick(View view, int rowIndex, int colIndex);
}
