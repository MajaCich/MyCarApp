package com.example.common.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

public class UriUtils {
    static final String URI_SCHEME = "example";
    static final String URI_HOST = "mycarapp";
    @NonNull
    public static Uri createDeepLinkUri(@NonNull String deepLinkAction) {
        return Uri.fromParts(URI_SCHEME, URI_HOST, deepLinkAction);
    }
}
