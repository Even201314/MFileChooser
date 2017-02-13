package com.even.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.even.mfilechooser.FileChooser;
import com.even.mfilechooser.interfaces.OnChooserSelectedListener;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.tv_hello_world)).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        FileChooser.Builder builder =
            new FileChooser.Builder(this).setChooserMode(FileChooser.MODE_FILE)
                .setOnChooserSelectedListener(new OnChooserSelectedListener() {
                    @Override public void onSelect(String path) {
                        Log.d(MainActivity.class.getSimpleName(), path);
                    }
                });
        builder.build().show(getFragmentManager(), MainActivity.class.getSimpleName());
    }
}
