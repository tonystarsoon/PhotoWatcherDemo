package it.com.ninephotodemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import it.com.ninephotoview.NinePhotoView;

public class MainActivity extends AppCompatActivity {
    private NinePhotoView mNinePhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNinePhotoView = (NinePhotoView) findViewById(R.id.ninelayout);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
    public void itemClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                mNinePhotoView.addPhotoView().setBackgroundColor(Color.GREEN);
                break;
            case R.id.button2:
                for (int i = 0; i < 29; i++) {
                    ImageView photoView = mNinePhotoView.addPhotoView();
                    photoView.setBackgroundColor(Color.GREEN);
                    photoView.setImageResource(R.mipmap.ic_launcher_round);
                    photoView.setClickable(true);
                    photoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "fuck", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.button3:
                for (int i = 0; i < 4; i++) {
                    mNinePhotoView.addPhotoView().setBackgroundColor(Color.GREEN);
                }
                break;
        }
        mNinePhotoView.showPhotoView();
    }
}

