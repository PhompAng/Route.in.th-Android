package th.in.route.routeinth.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class UIDUtils {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String PREF_NAME = "UID";
    public class UID {
        public static final String UID = "UID";
    }

    public UIDUtils(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void putUID(String uid) {
        editor.putString(UID.UID, uid);
        editor.commit();
    }

    public String getUID() {
        return sharedPreferences.getString(UID.UID, "");
    }
}
