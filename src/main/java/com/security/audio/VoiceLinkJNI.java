package com.security.audio;

import org.springframework.stereotype.Component;

@Component
public class VoiceLinkJNI {

    //加载动态库
    static {
        System.loadLibrary("onap");
    }


    public native boolean AnomalyDetectionJNI(String path);

}
