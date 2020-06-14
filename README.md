# NotificationListenerBug

Device used - Emulator API 29 (Also tried Samsung S10, API 29)

I'm experiencing an app startup fail when using a NotificationListenerService (registered and with permissions) and reinstalling the debug application.

Steps to reproduce:
1) Build and install the app in debug mode, have a NotificationListenerService registered in Manifest, and ask the user to give the corresponding permissions in settings.
2) Make code changes, rebuild/reinstall the app (also in debug mode)
3) The app will fail to start on the first try, and will start properly only after another reinstall or manual launch.
The following stacktrace is printed:
```
 E/NotificationListeners: notification listener ComponentInfo{com.eightbitlab.notificationlistenerbug/com.eightbitlab.notificationlistenerbug.NotificationListener} could not be unbound
    java.lang.IllegalArgumentException: Service not registered: com.android.server.notification.ManagedServices$1@532f46d
        at android.app.LoadedApk.forgetServiceDispatcher(LoadedApk.java:1751)
        at android.app.ContextImpl.unbindService(ContextImpl.java:1776)
        at com.android.server.notification.ManagedServices.unbindService(ManagedServices.java:1275)
        at com.android.server.notification.ManagedServices.registerServiceLocked(ManagedServices.java:1086)
        at com.android.server.notification.ManagedServices.registerServiceLocked(ManagedServices.java:1062)
        at com.android.server.notification.ManagedServices.registerService(ManagedServices.java:1048)
        at com.android.server.notification.ManagedServices.bindToServices(ManagedServices.java:1035)
        at com.android.server.notification.ManagedServices.rebindServices(ManagedServices.java:998)
        at com.android.server.notification.ManagedServices.onPackagesChanged(ManagedServices.java:612)
        at com.android.server.notification.NotificationManagerService.handleOnPackageChanged(NotificationManagerService.java:6267)
        at com.android.server.notification.NotificationManagerService.access$8600(NotificationManagerService.java:277)
        at com.android.server.notification.NotificationManagerService$WorkerHandler.handleMessage(NotificationManagerService.java:6305)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:214)
        at com.android.server.SystemServer.run(SystemServer.java:541)
        at com.android.server.SystemServer.main(SystemServer.java:349)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:908)
```
Expected behavior - the service should be properly rebound and the app should start from the first try.

I've provided an example project, to reproduce this issue you need to build and run it, give permission to the NotificationListenerService, then make some code changes and try to run it again.

As far as I understand this issue, the system detects that there are changes made in the certain app and tries to rebind the NotificationListenerService, but for some reason fails to do so.

Additionally, this stacktrace is printed, but it's probably due to fail during apk launch:

```
 java.lang.ClassNotFoundException: Didn't find class "androidx.core.app.CoreComponentFactory" on path: DexPathList[[],nativeLibraryDirectories=[/data/app/com.eightbitlab.stacker-fQZn2o3kDSNXUniYxWSpyw==/lib/x86, /system/lib, /system/product/lib]]
        at dalvik.system.BaseDexClassLoader.findClass(BaseDexClassLoader.java:196)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:379)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:312)
        at android.app.LoadedApk.createAppFactory(LoadedApk.java:256)
        at android.app.LoadedApk.updateApplicationInfo(LoadedApk.java:370)
        at android.app.ActivityThread.handleDispatchPackageBroadcast(ActivityThread.java:5951)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1941)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:214)
        at com.android.server.SystemServer.run(SystemServer.java:541)
        at com.android.server.SystemServer.main(SystemServer.java:349)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:908)
```
