package com.security.audio;

import org.springframework.stereotype.Component;

@Component
public class DetectionJni {

    //loading DLL
    static {
        System.loadLibrary("onap");
    }


    public static native boolean audioDetection(String path);

}
