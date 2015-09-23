package paint.thirdeyeds.com.mypaint;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private static final int MAX_BRUSH_THICKNESS = 50;
    private DrawView drawView;
    private View colorSwatch;
    private View btnColorPicker;
    private View eraseBtn;
    private SeekBar sbBrushThickness;
    private boolean eraseEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.drawViewMain);
        colorSwatch = findViewById(R.id.colorSwatch);
        btnColorPicker = findViewById(R.id.btnColorPicker);
        eraseBtn = findViewById(R.id.ivErase);

        sbBrushThickness = (SeekBar) findViewById(R.id.sbBrushThickness);

        sbBrushThickness.setProgress((int) ((drawView.getBrushThickness() / MAX_BRUSH_THICKNESS) * 100));
        sbBrushThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawView.setBrushThickness(Math.max(MAX_BRUSH_THICKNESS * progress / 100f, 1f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void buttonHandler(View v) {
        switch (v.getId()) {
            case R.id.btnColorPicker:
                if (colorSwatch.getVisibility() == View.GONE) {
                    colorSwatch.setTranslationY(colorSwatch.getHeight());
                    colorSwatch.setVisibility(View.VISIBLE);
                }
                colorSwatch.animate().translationY(0);
                break;
            case R.id.ivUndo:
                drawView.undo();
                break;
            case R.id.ivRedo:
                drawView.redo();
                break;
            case R.id.ivColorSwatchCloseBtn:
                colorSwatch.animate().translationY(colorSwatch.getHeight());
                break;
            case R.id.ivErase:
                if (eraseEnabled) {
                    eraseEnabled = false;
                    drawView.setDrawingPaint(((ColorDrawable) btnColorPicker.getBackground()).getColor());
                    eraseBtn.setBackgroundColor(Color.TRANSPARENT);

                } else {
                    drawView.setDrawingPaint(DrawView.CANVAS_COLOR);
                    eraseEnabled = true;
                    eraseBtn.setBackgroundColor(Color.GRAY);
                }
                break;
            case R.id.ivSave:

                drawView.setDrawingCacheEnabled(true);
                String result = MediaStore.Images.Media.insertImage(
                        getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "drawing");
                if (result != null) {
                    Toast.makeText(this,
                            "Image Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"siavashCJ@gmail.com"});
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
//                    intent.putExtra(Intent.EXTRA_TEXT, "body text");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(result));
                    startActivity(Intent.createChooser(intent, "Send email..."));

                } else {
                    Toast.makeText(this,
                            "Save failed", Toast.LENGTH_SHORT).show();
                }

                drawView.setDrawingCacheEnabled(false);


//                new AlertDialog.Builder(this).setNeutralButton(R.string.SaveToGallery, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).setNeutralButton(R.string.Email, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(), Bitmap.Config.ARGB_8888);
//                        Canvas canvas = new Canvas(bitmap);
//                        drawView.draw(canvas);
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//
//
//
//                    }
//                });


                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //TODO: save paths to bundle
        super.onSaveInstanceState(outState);
    }

    public void onColorSelected(View v) {
        int newColor = ((ColorDrawable) v.getBackground()).getColor();
        drawView.setDrawingPaint(newColor);
        colorSwatch.animate().translationY(colorSwatch.getHeight());
        btnColorPicker.setBackgroundColor(newColor);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
