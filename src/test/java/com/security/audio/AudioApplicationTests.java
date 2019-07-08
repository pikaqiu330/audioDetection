package com.security.audio;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AudioApplicationTests {

    @Autowired
    private AudioRest audioRest;
    @Test
    public void contextLoads() {
        String path = "E://119fire2.wav";
        boolean detect = audioRest.voiceDetect(path);
        System.out.println(detect);
    }

}
