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

public class Park extends AppCompatActivity {

  private Typeface font;
  private Typeface fontLarge;
  private TextView title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parque);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));
    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    title = (TextView) this.findViewById(R.id.textViewTituloParque);
    title.setTypeface(fontLarge);
  }

  public void seeSkatePark(View view) {
    parkDialog("seeSkatePark");
  }

  public void seeParqueLaLibertad(View view) {
    parkDialog("seeParqueLaLibertad");
  }

  public void seeCetav(View view) {
    parkDialog("seeCetav");
  }

  public void seeDanceSchool(View view) {
    parkDialog("seeDanceSchool");
  }

  public void seeFairgrounds(View view) {
    parkDialog("seeFairgrounds");
  }

  public void seeAudithorium(View view) {
    parkDialog("seeAudithorium");
  }

  public void parkDialog(String text) {

    Dialog mDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    mDialog.getWindow()
        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    mDialog.setContentView(R.layout.dialog_parque);

    TextView descripcion = (TextView) mDialog.findViewById(R.id.textViewParque);
    ImageView icono = (ImageView) mDialog.findViewById(R.id.imageViewParque);

    descripcion.setTypeface(font);

    switch (text) {
      case "seeSkatePark":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.skate_park)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_skate));
        break;
      case "seeParqueLaLibertad":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.ppl)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_fundacion));
        break;
      case "seeCetav":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.cetav)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_cetav));
        break;
      case "seeAudithorium":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.auditorio)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_auditorio));
        break;
      case "seeDanceSchool":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.escuela_de_danza)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_danza));
        break;
      case "seeFairgrounds":
        descripcion.setText(Html.fromHtml(getResources().getString(R.string.campo_ferial)));
        icono.setBackground(getResources().getDrawable(R.drawable.map_campo));
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
