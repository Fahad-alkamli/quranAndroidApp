package alkamli.fahad.quranapp.quranapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class UserSettingsActivity extends AppCompatActivity {

    CheckBox replaceIconsCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        replaceIconsCheckBox=(CheckBox) findViewById(R.id.replaceIconsCheckBox);
        replaceIconsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // Log.e(CommonFunctions.TAG,Boolean.toString(b));
                CommonFunctions.getEditor(getApplicationContext()).putBoolean("textButtons",b).commit();
            }
        });
        //Set the default option if it exists
        if(CommonFunctions.getSharedPreferences(this).getBoolean("textButtons",false))
        {
            replaceIconsCheckBox.setChecked(true);
        }
    }
}
