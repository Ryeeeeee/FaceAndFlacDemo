package com.ryeeeeee.faceandflacdemo.faceplusplus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ryeeeeee.faceandflacdemo.R;
import com.ryeeeeee.faceandflacdemo.utils.FileUtil;
import com.ryeeeeee.faceandflacdemo.utils.ImageUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class FaceFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = FaceFragment.class.getSimpleName();

    private static final String SERVER = "http://api.cn.faceplusplus.com/";
    private static final String API_KEY = "a52e3c6d9eb9715e9f003f8a0dda65cd";
    private static final String API_SECRET = "gT6xqffa8LY62ui30Vrm3yIFxmoaVT7T";

    private static final int REQUEST_CODE_PICTURE_PICK = 1;

    private static final int MSG_SHOW_TOAST = 1;
    private static final int MSG_MARK_INFO_DONE = 2;

    private static FaceHandler sHandler;

    private ImageView mImageView;
    private Button mSelectButton;
    private Context mContext;
    private Bitmap mPicture;
    private String mImagePath;

    private Retrofit mRetrofit;
    private DetectionApi mDetectionApi;

    // Required empty public constructor
    public FaceFragment() {
    }

    public static FaceFragment newInstance() {
        FaceFragment fragment = new FaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sHandler = new FaceHandler(this);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDetectionApi = mRetrofit.create(DetectionApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_face, container, false);

        mImageView = (ImageView) root.findViewById(R.id.image_view);
        mSelectButton = (Button) root.findViewById(R.id.select_button);

        mSelectButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_button:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICTURE_PICK);
                break;
            default:
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICTURE_PICK:
                if (data != null) {
                    mImagePath = FileUtil.getPathFromUri(mContext, data.getData());
                    if (mImagePath != null) {
                        mPicture = ImageUtil.decodeSampledBitmapFromPath(mImagePath, mImageView.getWidth(),
                                mImageView.getHeight());
                        mImageView.setImageBitmap(mPicture);
                        new DetectThread().start();
                        return;
                    }
                    Toast.makeText(mContext.getApplicationContext(), R.string.failed_to_select_picture,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private Bitmap markFaceInfo(Bitmap originBitmap, DetectionResult result) {

        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.RED);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(10);

        //create a new canvas
        Bitmap bitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), originBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(originBitmap, new Matrix(), null);

        // find out all faces
        for (DetectionResult.FaceEntity faceEntity : result.face) {
            float x, y, w, h;

            // get the center point
            x = (float) faceEntity.position.center.x;
            y = (float) faceEntity.position.center.y;

            // get face size
            w = (float) faceEntity.position.width;
            h = (float) faceEntity.position.height;

            // change percent value to the real size
            x = x / 100 * originBitmap.getWidth();
            w = w / 100 * originBitmap.getWidth() * 0.5f;
            y = y / 100 * originBitmap.getHeight();
            h = h / 100 * originBitmap.getHeight() * 0.5f;

            //draw the box to mark it out
            // TODO: 1/13/16
            canvas.drawRect(x - w, y - h, x + w, y + h, borderPaint);

            String ageText = "Age: " + (faceEntity.attribute.age.value - faceEntity.attribute.age.range)
                    + "~" + (faceEntity.attribute.age.value + faceEntity.attribute.age.range);
            canvas.drawText(ageText, x - w, y + h + 10, textPaint);

        }

        //save new image
        return bitmap;
    }

    private class DetectThread extends Thread {
        @Override
        public void run() {
            File file = new File(mImagePath);
            RequestBody apiKeyBody = RequestBody.create(MediaType.parse("text/plain"), API_KEY);
            RequestBody apiSecretBody = RequestBody.create(MediaType.parse("text/plain"), API_SECRET);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            Call<DetectionResult> call = mDetectionApi.detect(apiKeyBody, apiSecretBody, requestBody);
            String errorMessage = null;
            try {
                Response<DetectionResult> response = call.execute();
                DetectionResult result = response.body();
                if (result != null) {
                    Message message = sHandler.obtainMessage(MSG_MARK_INFO_DONE);
                    message.obj = markFaceInfo(mPicture, result);
                    message.sendToTarget();
                    return;
                } else {
                    errorMessage = response.errorBody().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = sHandler.obtainMessage(MSG_SHOW_TOAST);
            message.obj = errorMessage == null ? mContext.getString(R.string.network_error) : errorMessage;
            message.sendToTarget();
        }
    }

    private static class FaceHandler extends Handler {
        private WeakReference<FaceFragment> mWeakReference;

        public FaceHandler(FaceFragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FaceFragment fragment = mWeakReference.get();
            if (fragment == null) {
                return;
            }

            switch (msg.what) {
                case MSG_SHOW_TOAST:
                    Toast.makeText(fragment.mContext.getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;

                case MSG_MARK_INFO_DONE:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    fragment.mImageView.setImageBitmap(bitmap);
                    break;

                default:
            }
        }
    }
}
