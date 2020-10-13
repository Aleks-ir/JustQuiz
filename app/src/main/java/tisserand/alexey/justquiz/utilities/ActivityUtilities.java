package tisserand.alexey.justquiz.utilities;

import android.app.Activity;
import android.content.Intent;

import tisserand.alexey.justquiz.constants.AppConstants;


public class ActivityUtilities {

    private static ActivityUtilities activityUtilities = null;

    public static ActivityUtilities getInstance() {
        if (activityUtilities == null) {
            activityUtilities = new ActivityUtilities();
        }
        return activityUtilities;
    }

    public void invokeNewActivity(Activity activity, Class<?> tClass, boolean shoulgFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shoulgFinish) {
            activity.finish();
        }
    }


    public void invokeCommonQuizActivity(Activity activity, Class<?> tClass, String Id, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstants.BUNDLE_KEY_INDEX, Id);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

}
