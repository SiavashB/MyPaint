package paint.thirdeyeds.com.mypaint;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private DrawView drawView;
    private View colorSwatch;
    private View btnColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView)findViewById(R.id.drawViewMain);
        colorSwatch = findViewById(R.id.colorSwatch);
        btnColorPicker = findViewById(R.id.btnColorPicker);

    }



    public void buttonHandler(View v){
        switch (v.getId()){
            case R.id.btnColorPicker:
                if(colorSwatch.getVisibility() == View.GONE){
                    colorSwatch.setTranslationY(colorSwatch.getHeight());
                    colorSwatch.setVisibility(View.VISIBLE);
                }
                colorSwatch.animate().translationY(0);
                break;
            case R.id.btnUndo:
                drawView.undo();
                break;
            case R.id.btnRedo:
                drawView.redo();
                break;

        }
    }
    public void onColorSelected(View v){
        int newColor = ((ColorDrawable)v.getBackground()).getColor();
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
