import CloudPushSDK
import UserNotifications

extension AppDelegate {
    
    override open func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey : Any]? = nil) -> Bool {
        self.viewController = MainViewController()
        CloudPushSDK.sendNotificationAck(launchOptions);
        if(launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] != nil){
            let userInfo = launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] as! [AnyHashable : Any];
            let aps = userInfo["aps"] as! [AnyHashable : Any];
            let alert = aps["alert"] as! [AnyHashable: Any];
            let title = alert["title"] as! String;
            let body = alert["body"] as! String;
            AliPushPlugin.fireNotificationEvent(object: ["eventType":"openNotification", "title": title, "content": body, "extras": userInfo]);
        }
        return super.application(application,didFinishLaunchingWithOptions:launchOptions);
    }
    
    override open func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        CloudPushSDK.registerDevice(deviceToken) {res in
            if (res!.success) {
                print("Upload deviceToken to Push Server, deviceToken: \(CloudPushSDK.getApnsDeviceToken()!), deviceId: \(CloudPushSDK.getDeviceId()!)")
            } else {
                print("Upload deviceToken to Push Server failed, error: \(String(describing: res?.error))")
            }
        }
    }
    
    override open func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("register for remote notifications error", error);
    }
    
    override open func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
        print("Receive one notification.")
        let aps = userInfo["aps"] as! [AnyHashable : Any];
        let alert = aps["alert"] as! [AnyHashable: Any];
        let title = alert["title"] as! String;
        let body = alert["body"] as! String;
        AliPushPlugin.fireNotificationEvent(object: ["eventType":"receiveNotification", "title": title, "content": body, "extras": userInfo]);
        CloudPushSDK.sendNotificationAck(userInfo)
        print("Notification, title: \(title), body: \(body).");
    }
}
