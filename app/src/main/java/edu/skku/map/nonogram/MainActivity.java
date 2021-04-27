package edu.skku.map.nonogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button searchButton = (Button) findViewById(R.id.buttonSearch);
        final Button galleryButton = (Button) findViewById(R.id.buttonGallery);
        final EditText searchEditText = (EditText) findViewById(R.id.editTextSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textForSearch = searchEditText.getText().toString();

                NaverSearchImageApi.request(textForSearch, new NaverSearchImageApiCallback() {
                    @Override
                    public void onSuccess(final NaverImageResponse response) {
                        NaverImage[] naverImages = response.getItems();

                        if (naverImages.length > 0) {
                            String imageUrl = naverImages[0].getLink();

                            try {
                                Bitmap srcImageBitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());

                                initializeGrid(srcImageBitmap);
                            } catch (Exception e) {
                                    e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                initializeGrid(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeGrid(Bitmap srcImageBitmap) {
        final BoardInfo boardInfo = new BoardInfo(srcImageBitmap);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawGrid(boardInfo);
            }
        });
    }

    private void drawGrid(final BoardInfo boardInfo) {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(boardInfo, getOnClickGridItem(boardInfo));
        gridView.setAdapter(adapter);
    }

    private OnClickGridItem getOnClickGridItem(final BoardInfo boardInfo) {
        return new OnClickGridItem() {
            @Override
            public void OnClick(View view, int rowIndex, int colIndex) {
                ArrayList<Point> selectedPoints = boardInfo.getSelectedPoints();

                if (selectedPoints.size() < boardInfo.getBlackCount()) {
                    if (boardInfo.getIsBlack(rowIndex, colIndex)) {

                        boolean isSelected = false;

                        for (int i = 0; i < selectedPoints.size(); i++) {
                            Point point = selectedPoints.get(i);
                            if (point.x == rowIndex && point.y == colIndex) {
                                isSelected = true;
                                break;
                            }
                        }

                        if (!isSelected) {
                            view.setBackgroundColor(Color.BLACK);
                            boardInfo.select(rowIndex, colIndex);

                            if (selectedPoints.size() >= boardInfo.getBlackCount()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(), "FINISH!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "Not Black", Toast.LENGTH_SHORT).show();
                        boardInfo.clearSelectedPoints();
                        drawGrid(boardInfo);
                    }
                } else {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "FINISH!!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}