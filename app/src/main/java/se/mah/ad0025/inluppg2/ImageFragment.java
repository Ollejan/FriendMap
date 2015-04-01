package se.mah.ad0025.inluppg2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Fragment som visar komponenter som används vid uppladdning av bild.
 * @author Jonas Dahlström
 */
public class ImageFragment extends Fragment {
    private Controller controller;
    private ImageView imageView;
    private EditText etImageText;
    private Bitmap bitmap;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        etImageText = (EditText)view.findViewById(R.id.etImageText);
        Button btnSendImage = (Button)view.findViewById(R.id.btnSendImage);
        btnSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnSendImageClicked();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateImageView();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getEtImageText() {
        return etImageText.getText().toString();
    }

    public void setImageView(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private void updateImageView() {
        imageView.setImageBitmap(bitmap);
    }
}
