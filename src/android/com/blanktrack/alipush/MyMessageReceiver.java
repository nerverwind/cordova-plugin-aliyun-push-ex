package com.blanktrack.alipush;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.blanktrack.alipush.AliPushPlugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Blank on 2017-08-29.
 */

public class MyMessageReceiver extends MessageReceiver {
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        JSONObject extra = new JSONObject();
        JSONObject response = new JSONObject();
        try {
            for (String key : extraMap.keySet()) {
                extra.put(key, extraMap.get(key));
            }
            response.put("eventType", "receiveNotification");
            response.put("content", summary);
            response.put("title", title);
            response.put("extras", extra);

            sendEvent(response);
        } catch (JSONException e) {
            sendError(e.getMessage());
        }
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        JSONObject response = new JSONObject();
        try {
            response.put("eventType", "receiveMessage");
            response.put("messageid", cPushMessage.getMessageId());
            response.put("title", cPushMessage.getTitle());
            response.put("content", cPushMessage.getContent());
            sendEvent(response);
        } catch (JSONException e) {
            sendError(e.getMessage());
        }

    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        JSONObject response = new JSONObject();
        try {
            response.put("eventType", "openNotification");
            response.put("title", title);
            response.put("content", summary);
            response.put("extras", new JSONObject(extraMap));
            sendEvent(response);
        } catch (JSONException e) {
            sendError(e.getMessage());
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("AliPushMessageReceiver", "onNotificationRemoved, messageId: " + messageId);
    }

    private void sendEvent(JSONObject _json) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, _json);
        pluginResult.setKeepCallback(true);

        CallbackContext pushCallback = AliPushPlugin.getCurrentCallbackContext();
        if (pushCallback != null) {
            pushCallback.sendPluginResult(pluginResult);
        }
    }

    public void sendError(String message) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, message);
        pluginResult.setKeepCallback(true);
        CallbackContext pushCallback = AliPushPlugin.getCurrentCallbackContext();
        if (pushCallback != null) {
            pushCallback.sendPluginResult(pluginResult);
        }
    }
}
