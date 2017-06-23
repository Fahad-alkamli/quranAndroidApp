package alkamli.fahad.quranapp.quranapp;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static alkamli.fahad.quranapp.quranapp.CommonFunctions.TAG;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.textSizes;
import static android.util.Log.e;

public class UserSettingsActivity extends AppCompatActivity {

    CheckBox replaceIconsCheckBox;
    CheckBox deleteFilesCheckBox;
    CheckBox resumeDownloadOption;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        replaceIconsCheckBox=(CheckBox) findViewById(R.id.replaceIconsCheckBox);
        deleteFilesCheckBox=(CheckBox) findViewById(R.id.deleteFilesCheckBox);
        resumeDownloadOption=(CheckBox) findViewById(R.id.restoreDownloadingfiles);
        radioGroup=(RadioGroup)  findViewById(R.id.radioGroup);

        try {
            replaceIconsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // Log.e(CommonFunctions.TAG,Boolean.toString(b));
                    CommonFunctions.getEditor(getApplicationContext()).putBoolean("textButtons", b).commit();
                }
            });
            //Set the default option if it exists
            if (CommonFunctions.getSharedPreferences(this).getBoolean("textButtons", false)) {
                replaceIconsCheckBox.setChecked(true);
            }
            resumeDownloadOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // Log.e(CommonFunctions.TAG,Boolean.toString(b));
                    CommonFunctions.getEditor(getApplicationContext()).putBoolean("resumeDownload", b).commit();
                }
            });

            deleteFilesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // Log.e(CommonFunctions.TAG,Boolean.toString(b));
                    CommonFunctions.getEditor(getApplicationContext()).putBoolean("deleteFilesAfterPlaying", b).commit();
                }
            });
            //Set the default option if it exists
            if (CommonFunctions.getSharedPreferences(this).getBoolean("deleteFilesAfterPlaying", false))
            {
                deleteFilesCheckBox.setChecked(true);
            }
            if (CommonFunctions.getSharedPreferences(this).getBoolean("resumeDownload", false))
            {
                resumeDownloadOption.setChecked(true);
            }

            //set the size for titles
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedID) {
                    Log.d(CommonFunctions.TAG, "Size has been changed");
                   // Log.d(CommonFunctions.TAG, "Check this: " + R.id.smallSize);

                    try {
                        String size = CommonFunctions.getSharedPreferences(getApplicationContext()).getString("titlesSize", null);
                        switch (checkedID) {
                            case R.id.smallSize:
                                {
                                CommonFunctions.getEditor(getApplicationContext()).putString("titlesSize", textSizes.smallSize.name()).commit();
                           if(size!= null &&size.equals(textSizes.smallSize.name())==false)
                           {
                               HomeActivity.updateAdapter();
                           }

                            }
                            break;
                            case R.id.defaultSize:
                                {
                                CommonFunctions.getEditor(getApplicationContext()).putString("titlesSize", textSizes.defaultSize.name()).commit();
                                    if(size!= null &&size.equals(textSizes.defaultSize.name())==false)
                                    {
                                        HomeActivity.updateAdapter();
                                    }
                                }
                            break;
                            case R.id.largeSize:
                                {
                                CommonFunctions.getEditor(getApplicationContext()).putString("titlesSize", textSizes.largeSize.name()).commit();
                                    if(size!= null &&size.equals(textSizes.largeSize.name())==false)
                                    {
                                        HomeActivity.updateAdapter();
                                    }
                                }
                            break;
                        }
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                    }


                }
            });


            //set the default size if it has been changed
            String size = CommonFunctions.getSharedPreferences(this).getString("titlesSize", null);
            if (size != null && size.equals(textSizes.defaultSize.name()) == false)
            {
                if (textSizes.smallSize.name().equals(size))
                {
                    RadioButton smallSize = (RadioButton) findViewById(R.id.smallSize);
                    smallSize.setChecked(true);
                }
                if (textSizes.largeSize.name().equals(size))
                {
                    RadioButton largeSize = (RadioButton) findViewById(R.id.largeSize);
                    largeSize.setChecked(true);
                }

            }
        }catch(Exception e) {
            class Local {
            }
            ;
            e(TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));

        }

    }
}
