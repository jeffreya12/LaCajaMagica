package cr.ac.tec.la_caja_magica.OtherFeatures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import charlie.la_caja_magica.R;

/**
 * Created by JEFFREY on 17/09/2016.
 */
public class Splash extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);

    Thread timerThread = new Thread() {
      public void run() {
        try {
          sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          Intent intent = new Intent(Splash.this, MainMenu.class);
          startActivity(intent);
        }
      }
    };
    timerThread.start();
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    finish();
  }

}