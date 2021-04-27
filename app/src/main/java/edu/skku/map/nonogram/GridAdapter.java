package edu.skku.map.nonogram;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private BoardInfo boardInfo;
    private OnClickGridItem onClickGridItem;

    GridAdapter(BoardInfo boardInfo, OnClickGridItem onClickGridItem) {
        this.boardInfo = boardInfo;
        this.onClickGridItem = onClickGridItem;
    }

    @Override
    public int getCount() {
        return (BoardInfo.ROW_COUNT + BoardInfo.ROW_HINT_COUNT) * (BoardInfo.COL_COUNT + BoardInfo.COL_HINT_COUNT);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        final int rowIndex = position / (BoardInfo.ROW_COUNT + BoardInfo.COL_HINT_COUNT);
        final int colIndex = position % (BoardInfo.ROW_COUNT + BoardInfo.COL_HINT_COUNT);

        if (rowIndex < BoardInfo.COL_HINT_COUNT && colIndex >= BoardInfo.ROW_HINT_COUNT) {
            int hintCount = boardInfo.getColHintCount(colIndex - BoardInfo.COL_HINT_COUNT);

            int hintIndex = hintCount - (BoardInfo.COL_HINT_COUNT - rowIndex);

            if (hintIndex >= 0) {
                int hint = boardInfo.getColHint(colIndex - BoardInfo.COL_HINT_COUNT, hintIndex);

                if (hint > 0) {
                    TextView gridItemTextView = convertView.findViewById(R.id.gridItemTextView);

                    gridItemTextView.setText("" + hint);
                }
            }
        } else if (colIndex < BoardInfo.ROW_HINT_COUNT && rowIndex >= BoardInfo.COL_HINT_COUNT) {
            int hintCount = boardInfo.getRowHintCount(rowIndex - BoardInfo.ROW_HINT_COUNT);

            int hintIndex = hintCount - (BoardInfo.ROW_HINT_COUNT - colIndex);

            if (hintIndex >= 0) {
                int hint = boardInfo.getRowHint(rowIndex - BoardInfo.ROW_HINT_COUNT, hintIndex);

                if (hint > 0) {
                    TextView gridItemTextView = convertView.findViewById(R.id.gridItemTextView);

                    gridItemTextView.setText("" + hint);
                }
            }
        }

        if (rowIndex < BoardInfo.COL_HINT_COUNT || colIndex < BoardInfo.ROW_HINT_COUNT) {
            convertView.setEnabled(false);
        } else {
            ArrayList<Point> selectedPoints = boardInfo.getSelectedPoints();

            convertView.setBackgroundColor(Color.WHITE);

            for (int i = 0; i < selectedPoints.size(); i++) {
                Point point = selectedPoints.get(i);

                if (point.x == rowIndex - BoardInfo.COL_HINT_COUNT && point.y == colIndex - BoardInfo.ROW_HINT_COUNT) {
                    convertView.setBackgroundColor(Color.BLACK);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGridItem.OnClick(v, rowIndex - BoardInfo.COL_HINT_COUNT, colIndex - BoardInfo.ROW_HINT_COUNT);
                }
            });
        }

        return convertView;
    }
}
