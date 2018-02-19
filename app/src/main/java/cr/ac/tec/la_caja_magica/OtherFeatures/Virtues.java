package cr.ac.tec.la_caja_magica.OtherFeatures;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import charlie.la_caja_magica.R;

public class Virtues extends AppCompatActivity {

  private Typeface font;
  private Typeface fontLarge;
  private TextView title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_valores);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout(width, height);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));
    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    title = (TextView) this.findViewById(R.id.textViewTituloValores);
    title.setTypeface(fontLarge);
  }

  public void seeLove(View view) {
    virtueDialog("seeLove");
  }

  public void seePeace(View view) {
    virtueDialog("seePeace");
  }

  public void seeRespect(View view) {
    virtueDialog("seeRespect");
  }

  public void seeSolidarity(View view) {
    virtueDialog("seeSolidarity");
  }

  public void seeDiversity(View view) {
    virtueDialog("seeDiversity");
  }

  public void seeEquality(View view) {
    virtueDialog("seeEquality");
  }

  public void seeEmpathy(View view) {
    virtueDialog("seeEmpathy");
  }

  public void seeLiberty(View view) {
    virtueDialog("seeLiberty");
  }

  public void seeResponsibility(View view) {
    virtueDialog("seeResponsibility");
  }

  public void seeHonesty(View view) {
    virtueDialog("seeHonesty");
  }

  public void virtueDialog(String valor) {

    Dialog mDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    mDialog.getWindow()
        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    mDialog.setContentView(R.layout.dialog_valor);

    TextView descripcion = (TextView) mDialog.findViewById(R.id.textViewValor);
    ImageView icono = (ImageView) mDialog.findViewById(R.id.imageViewValor);

    descripcion.setTypeface(font);

    switch (valor) {
      case "seeLove":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.amor)));
        icono.setBackground(getResources().getDrawable(R.drawable.amor_logo));
        break;
      case "seePeace":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.paz)));
        icono.setBackground(getResources().getDrawable(R.drawable.paz_logo));
        break;
      case "seeRespect":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.respeto)));
        icono.setBackground(getResources().getDrawable(R.drawable.respeto_logo));
        break;
      case "seeSolidarity":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.solidaridad)));
        icono.setBackground(getResources().getDrawable(R.drawable.solidaridad_logo));
        break;
      case "seeDiversity":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.diversidad)));
        icono.setBackground(getResources().getDrawable(R.drawable.diversidad_logo));
        break;
      case "seeEquality":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.equidad)));
        icono.setBackground(getResources().getDrawable(R.drawable.equidad_logo));
        break;
      case "seeEmpathy":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.empatia)));
        icono.setBackground(getResources().getDrawable(R.drawable.empatia_logo));
        break;
      case "seeLiberty":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.libertad)));
        icono.setBackground(getResources().getDrawable(R.drawable.libertad_logo));
        break;
      case "seeResponsibility":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.responsabilidad)));
        icono.setBackground(getResources().getDrawable(R.drawable.responsabilidad_logo));
        break;
      case "seeHonesty":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.honestidad)));
        icono.setBackground(getResources().getDrawable(R.drawable.honestidad_logo));
        break;
    }

    mDialog.show();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!MainMenu.isMusicPlaying()) {
      MainMenu.playMusic();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    MainMenu.pauseMusic();
  }
}
