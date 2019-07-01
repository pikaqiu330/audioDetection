package com.security.audio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class AudioApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        SpringApplication.run(AudioApplication.class, args);
    }

    private static void createAndShowGUI()
    {
        //创造一个实例
        AudioDetection detect = new AudioDetection();
    }

}
