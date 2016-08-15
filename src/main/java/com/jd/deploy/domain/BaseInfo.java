package com.jd.deploy.domain;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-6-8
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public class BaseInfo {
    public String appName;
    public String releaseURL;
    public String applicationStart;
    public String shortcutIcon;
    public String rootPath;
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getReleaseURL() {
        return releaseURL;
    }

    public void setReleaseURL(String releaseURL) {
        this.releaseURL = releaseURL;
    }

    public String getApplicationStart() {
        return applicationStart;
    }

    public void setApplicationStart(String applicationStart) {
        this.applicationStart = applicationStart;
    }

    public String getShortcutIcon() {
        return shortcutIcon;
    }

    public void setShortcutIcon(String shortcutIcon) {
        this.shortcutIcon = shortcutIcon;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
