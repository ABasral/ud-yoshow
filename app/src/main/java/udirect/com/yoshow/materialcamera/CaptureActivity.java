package udirect.com.yoshow.materialcamera;

import android.app.Fragment;
import androidx.annotation.NonNull;

import udirect.com.yoshow.materialcamera.internal.BaseCaptureActivity;
import udirect.com.yoshow.materialcamera.internal.CameraFragment;

public class CaptureActivity extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return CameraFragment.newInstance();
  }
}
