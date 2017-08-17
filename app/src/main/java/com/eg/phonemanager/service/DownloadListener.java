package com.eg.phonemanager.service;

/**
 * Created by gqliu on 17-8-17.
 */

public interface DownloadListener {

    public void onProgress(int progress);

    public void onSuccess();

    public void onFailed();

    public void onPaused();

    public void onCanceled();
}
