package com.autoai.themestore.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dongrp on 2018/10/12.
 */

public class ThemeInfo implements Serializable {


    /**
     * 主题id
     * 主题名称
     * 简介
     * 作者
     * 大小
     * 是否免费
     * 价格
     * 预览图icon下载路径
     * 预览图widget下载路径
     * 预览图lockscreen下载路径
     * 主题包下载路径
     * 下载量
     * 版本号
     * 更新简介
     * 发布日期
     * 是否购买
     * 是否收藏
     * 主题包下载后本地存储绝对路径
     * 预览图icon下载后本地存储绝对路径
     * 预览图widget下载后本地存储绝对路径
     * 预览图lockscreen下载后本地存储绝对路径
     * 是否已下载
     * 是否已安装
     * <p>
     * json格式解析 {
     * "id":001,
     * "themeName":theme,
     * "intro":中国人民,
     * "author":kevin,
     * "themeSize":257,
     * "isFree":false,
     * "price":5元,
     * "preview":{
     * "iconUrl":"http://xx",
     * "widgetUrl":"http://xx",
     * "otherUrl":"http://xxx"},
     * "downloadUrl":"http://xx",
     * "downloadCount":555,
     * "version":v05,
     * "releaseDate":Unix毫秒数时间戳,
     * "isPaid":false,
     * "isCollected":false
     * },
     */
    @SerializedName("id")
    private int id;

    @SerializedName("themeName")
    private String themeName;

    @SerializedName("intro")
    private String intro;

    @SerializedName("author")
    private String author;

    @SerializedName("themeSize")
    private float themeSize;

    @SerializedName("isFree")
    private boolean isFree;

    @SerializedName("price")
    private float price;

    @SerializedName("preview")
    private PreView preView;

    @SerializedName("downloadUrl")
    private String downloadPath;

    @SerializedName("downloadCount")
    private int downloadCount;

    @SerializedName("version")
    private String version;

    @SerializedName("releaseDate")
    private String releaseDate;

    @SerializedName("isPaid")
    private boolean isPaid;

    @SerializedName("isCollected")
    private boolean isCollected;

    private String localIconPreview;

    private String localWidgetPreview;

    private String localLockScreenPreview;

    private boolean isDownload;

    private boolean isInstalled;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getThemeSize() {
        return themeSize;
    }

    public void setThemeSize(int themeSize) {
        this.themeSize = themeSize;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public PreView getPreView() {
        return preView;
    }

    public void setPreView(PreView preView) {
        this.preView = preView;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getLocalIconPreview() {
        return localIconPreview;
    }

    public void setLocalIconPreview(String localIconPreview) {
        this.localIconPreview = localIconPreview;
    }

    public String getLocalWidgetPreview() {
        return localWidgetPreview;
    }

    public void setLocalWidgetPreview(String localWidgetPreview) {
        this.localWidgetPreview = localWidgetPreview;
    }

    public String getLocalLockScreenPreview() {
        return localLockScreenPreview;
    }

    public void setLocalLockScreenPreview(String localLockScreenPreview) {
        this.localLockScreenPreview = localLockScreenPreview;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public class PreView implements Serializable {

        private String iconUrl;

        private String widgetUrl;

        private String lockScreenUrl;

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getWidgetUrl() {
            return widgetUrl;
        }

        public void setWidgetUrl(String widgetUrl) {
            this.widgetUrl = widgetUrl;
        }

        public String getLockScreenUrl() {
            return lockScreenUrl;
        }

        public void setLockScreenUrl(String lockScreenUrl) {
            this.lockScreenUrl = lockScreenUrl;
        }
    }


    @Override
    public String toString() {
        return "ThemeInfo{" +
                "id=" + id +
                ", themeName='" + themeName + '\'' +
                ", intro='" + intro + '\'' +
                ", author='" + author + '\'' +
                ", themeSize=" + themeSize +
                ", isFree=" + isFree +
                ", price='" + price + '\'' +
                ", preView=" + preView +
                ", downloadPath='" + downloadPath + '\'' +
                ", downloadCount=" + downloadCount +
                ", version='" + version + '\'' +
                ", releaseDate=" + releaseDate +
                ", isPaid=" + isPaid +
                ", isCollected=" + isCollected +
                ", localIconPreview='" + localIconPreview + '\'' +
                ", localWidgetPreview='" + localWidgetPreview + '\'' +
                ", localLockScreenPreview='" + localLockScreenPreview + '\'' +
                ", isDownload=" + isDownload +
                ", isInstalled=" + isInstalled +
                '}';
    }


}

