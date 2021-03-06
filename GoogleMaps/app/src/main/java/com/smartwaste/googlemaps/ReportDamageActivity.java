/*
 * @author Yachen Lin
 * @date 4/22/18
 */

package com.smartwaste.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

/**
 * Report damage activity (related to GUI).
 */
public class ReportDamageActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private BinStatus.Item selectedBinItem;

    private Intent intent;
    private int responseCode;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_damage);

        // Get the Intent that started this activity and extract the string
        intent = getIntent();
        selectedBinItem = (BinStatus.Item) intent.getSerializableExtra(MapsActivity.LAT_LON);

        // Capture the layout's TextView and set the string as its text
        TextView binInfoTextView = findViewById(R.id.binInfoTextView);
        binInfoTextView.setText(selectedBinItem.toString());

        descriptionEditText = findViewById(R.id.descriptionEditText);
        descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onClickSendReport(View view) {
        try {
            TextView binInfo = findViewById(R.id.binInfoTextView);
            System.out.println(">>>>click send report: " + binInfo.getText() + descriptionEditText.getText());
            ReportItem reportItem = new ReportItem(selectedBinItem.getBinId(),
                    selectedBinItem.getLatLonStr(),
                    descriptionEditText.getText().toString());
            responseCode = new DamageReport(MapsActivity.serverActionDamageReport).execute(reportItem).get();
            System.out.println(">>>$###response: " + responseCode);
            onBackPressed();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        // https://stackoverflow.com/questions/2679250/setresult-does-not-work-when-back-button-pressed
        Intent data = new Intent();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED, data);
        }

        super.finish();
    }
}
