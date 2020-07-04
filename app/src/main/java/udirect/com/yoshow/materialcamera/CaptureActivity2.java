package udirect.com.yoshow.materialcamera;

import android.app.Fragment;
import androidx.annotation.NonNull;

import udirect.com.yoshow.materialcamera.internal.BaseCaptureActivity;
import udirect.com.yoshow.materialcamera.internal.Camera2Fragment;


public class CaptureActivity2 extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return Camera2Fragment.newInstance();
  }
}
