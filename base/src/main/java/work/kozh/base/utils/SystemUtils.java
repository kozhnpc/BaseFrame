package work.kozh.base.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

/**
 * 与系统相关的工具 如获取跳转链接等等
 *
 * @author KOZH
 * @time 2020/1/17 13:01
 */
public class SystemUtils {

    /**
     * 判断是否有Activity在运行
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isStackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
        return runningTaskInfo.numActivities > 1;
    }


    /**
     * 判断Service是否在运行
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断当前APP是否已经在运行中  （指定包名的应用）
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            if (runningTaskInfo.baseActivity.getPackageName().equals(context.getPackageName())
                    || runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /***      国际化 I18N 使用    **** /

     /**
     * 获取当前系统语言
     * @return 当前系统语言
     */
    public static String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage();
    }

    /**
     * 设置 App 语言
     *
     * @param context
     * @param language
     */
    public static void setLanguage(Context context, String language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (language) {
            case "zh":
                configuration.locale = Locale.CHINESE;
                break;
            case "en":
                configuration.locale = Locale.ENGLISH;
                break;
            default:
                configuration.locale = Locale.CHINESE;
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }

    /**
     * 重启App
     *
     * @param context
     */
    public static void resetApp(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 安装 APK
     * 建议context 都传入application的，避免内存泄漏
     * <p>
     * tips:
     * 7.0以上的手机都会有一个“未知来源应用权限”的开关
     * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
     * <p>
     * 针对安卓7.0 以上文件共享机制的修改
     * 1 定义FileProvider
     * manifest中
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * android:authorities="com.test.CrossBorderSellPlatform.fileprovider"//自定义名字 为避免重复建议设为：包名.fileprovider
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/provider_paths" />
     * </provider>
     * <p>
     * 2 res目录下，创建xml文件夹，并新建一个provider_paths.xml文件
     * <p>
     * <paths xmlns:android="http://schemas.android.com/apk/res/android">
     * <external-path
     * name="files_root"
     * path="." />
     * <root-path
     * name="root_path"
     * path="." />
     * </paths>
     *
     * @param file    下载的文件
     * @param context
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context,
                    "com.test.CrossBorderSellPlatform.fileprovider", file);  //包名.fileprovider
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }


    /**
     * 打开浏览器 系统自带的 分享链接
     *
     * @param context
     * @param content
     */
    public static void openBrowser(Context context, String content) {
        Intent intent = new Intent();
        //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(content);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 打开邮箱 系统自带的 分享内容  新建邮件
     *
     * @param context
     * @param content
     * @param emailAddress
     * @param emailTitle
     */
    public static void openEmail(Context context, String content, String emailAddress, String emailTitle) {
        Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
        mail_intent.setData(Uri.parse("mailto:" + emailAddress));
        mail_intent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
        mail_intent.putExtra(Intent.EXTRA_TEXT, content);
        mail_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(mail_intent, emailTitle));
    }


    /**
     * 调用系统自带的分享功能  这里仅限于分享文字
     *
     * @param context
     * @param content
     * @param title
     */
    public static void shareTextContent(Context context, String content, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
        LogUtils.i("SystemUtils:分享文字：" + content);
    }

    public static Intent getShareTextIntent(Context context, String content, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 获取 App 具体设置的意图
     *
     * @param packageName
     * @return
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String packageName) {
        return getLaunchAppDetailsSettingsIntent(packageName, false);
    }

    /**
     * 获取 App 具体设置的意图
     *
     * @param packageName
     * @param isNewTask   是否以新任务打开
     * @return
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String packageName,
                                                           final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return getIntent(intent, isNewTask);
    }


    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Uri file2Uri(Context context, final File file) {
        if (file == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".provider";
            return FileProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    //<editor-fold desc="获取系统分享图片的意图">

    /**
     * 获取分享图片的意图  在系统中分享  调用系统的方法
     *
     * @param content
     * @param imagePath
     * @return
     */
    public static Intent getShareImageIntent(Context context, final String content, final String imagePath) {
        return getShareImageIntent(context, content, imagePath, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content
     * @param imagePath
     * @param isNewTask 是否以新任务打开
     * @return
     */
    public static Intent getShareImageIntent(Context context, final String content,
                                             final String imagePath,
                                             final boolean isNewTask) {
        if (imagePath == null || imagePath.length() == 0) return null;
        return getShareImageIntent(context, content, new File(imagePath), isNewTask);
    }


    /**
     * 获取分享图片的意图
     *
     * @param content
     * @param image
     * @return
     */
    public static Intent getShareImageIntent(Context context, final String content, final File image) {
        return getShareImageIntent(context, content, image, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content
     * @param image
     * @param isNewTask
     * @return
     */
    public static Intent getShareImageIntent(Context context, final String content,
                                             final File image,
                                             final boolean isNewTask) {
        if (image == null || !image.isFile()) return null;
        return getShareImageIntent(content, file2Uri(context, image), isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content
     * @param uri
     * @return
     */
    public static Intent getShareImageIntent(final String content, final Uri uri) {
        return getShareImageIntent(content, uri, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content
     * @param uri
     * @param isNewTask
     * @return
     */
    public static Intent getShareImageIntent(final String content,
                                             final Uri uri,
                                             final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content    The content.
     * @param imagePaths The paths of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(Context context, final String content, final LinkedList<String> imagePaths) {
        return getShareImageIntent(context, content, imagePaths, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content    The content.
     * @param imagePaths The paths of images.
     * @param isNewTask  True to add flag of new task, false otherwise.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(Context context, final String content,
                                             final LinkedList<String> imagePaths,
                                             final boolean isNewTask) {
        if (imagePaths == null || imagePaths.isEmpty()) return null;
        List<File> files = new ArrayList<>();
        for (String imagePath : imagePaths) {
            files.add(new File(imagePath));
        }
        return getShareImageIntent(context, content, files, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content The content.
     * @param images  The files of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(Context context, final String content, final List<File> images) {
        return getShareImageIntent(context, content, images, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content   The content.
     * @param images    The files of images.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(Context context, final String content,
                                             final List<File> images,
                                             final boolean isNewTask) {
        if (images == null || images.isEmpty()) return null;
        ArrayList<Uri> uris = new ArrayList<>();
        for (File image : images) {
            if (!image.isFile()) continue;
            uris.add(file2Uri(context, image));
        }
        return getShareImageIntent(content, uris, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content The content.
     * @param uris    The uris of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content, final ArrayList<Uri> uris) {
        return getShareImageIntent(content, uris, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content   The content.
     * @param uris      The uris of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content,
                                             final ArrayList<Uri> uris,
                                             final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    //</editor-fold>


}
