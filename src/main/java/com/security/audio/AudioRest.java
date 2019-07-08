package com.security.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Component
@Path("audio")
public class AudioRest {
    private static final Logger logger = LoggerFactory.getLogger(AudioRest.class);

    @POST
    public boolean voiceDetect(String fileName)
    {
        boolean b_jni = DetectionJni.audioDetection(fileName);
        logger.info("path:"+fileName+"|||||||||||||||||detection result:"+b_jni);
        return b_jni;

    }

}
