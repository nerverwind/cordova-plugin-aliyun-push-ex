package __PACKAGE_NAME__.alipush;

import __PACKAGE_NAME__.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.sdk.android.push.AndroidPopupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AliPushActivity extends AndroidPopupActivity {
    static final String TAG = "PopupPushActivity";

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        try {
            JSONObject ext = new JSONObject();
            for (String key : extMap.keySet()) {
                ext.put(key, extMap.get(key));
            }
            JSONObject noti = new JSONObject();
            noti.put("title", title);
            noti.put("content", summary);
            noti.put("extras", ext);

            SharedPreferences preferences = this.getSharedPreferences("aliNotiMsg", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("msg", noti.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.e(TAG, "notification convert to json error", e);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
