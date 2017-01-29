package dk.brams.android.soduko;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity  {

    private static final String TAG = "TBR:MainActivity" ;

    @Override
    protected Fragment createFragment() {
        return DigitFragment.newInstance();
    }


}
