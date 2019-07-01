package com.security.audio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Component
@Path("audio")
public class AudioRest {

    @Autowired
    private VoiceLinkJNI voiceLinkJNI;

    @POST
    public boolean voiceDetect(String fileName)
    {
//        System.out.println("filePath:"+file.getAbsolutePath()+",name="+fileName);
        boolean b_jni = voiceLinkJNI.AnomalyDetectionJNI(fileName);
        System.out.println(b_jni + fileName);
        return false;

    }

}
