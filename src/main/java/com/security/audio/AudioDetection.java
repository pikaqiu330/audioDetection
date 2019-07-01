package com.security.audio;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class AudioDetection {
    //定义所需要的组件
    private JPanel jp1Buttoon,jp2,jp3;
    private JScrollPane jp4;
    private JButton startBtn;
    private JButton stopBtn;
    //定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
    TargetDataLine td = null;

    //定义录音格式
    AudioFormat af = null;

    //设置画波形线程的终止的标志
    boolean flag = true;

    //记录开始录音的时间
    long startPlay;

    //定义每次录音的时候每次提取字节来画音频波
    byte audioDataBuffer[] = null;

//    ByteArrayOutputStream baos = null;

    //定义停止录音的标志，来控制录音线程的运行
    Boolean stopflag = false;

    //定义字节数组输入输出流
    ByteArrayInputStream bais = null;

    //定义音频输入流
    AudioInputStream ais = null;

    //定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
    SourceDataLine sd = null;

    //定义音频波形每次显示的字节数
    int intBytes = 0;

    //每次保存的最后的文件名
    File tarFile = null;

    //保存间隔
    private static final int RECORDING_TIME_SLOT = 3;





    public AudioFormat getAudioFormat()
    {
        //下面注释部分是另外一种音频格式，两者都可以
//        AudioFormat.Encoding encoding = AudioFormat.Encoding.
//                PCM_SIGNED ;
//        float rate = 16000f;
//        int sampleSize = 16;
//        String signedString = "signed";
//        boolean bigEndian = true;
//        int channels = 1;
//        return new AudioFormat(encoding, rate, sampleSize, channels,
//                (sampleSize / 8) * channels, rate, bigEndian);

        //采样率是每秒播放和录制的样本数
		float sampleRate = 16000.0F;
		// 采样率8000,11025,16000,22050,44100
		//sampleSizeInBits表示每个具有此格式的声音样本中的位数
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 单声道为1，立体声为2
		boolean signed = true;
		// true,false
		boolean bigEndian = true;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
    }


    public AudioDetection()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("Audio Detection");

//        frame.setLayout(new GridLayout(4,1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //组件初始化
        jp1Buttoon = new JPanel();
        jp1Buttoon.setPreferredSize(new Dimension(800,50));

        TextArea area = new TextArea();
        area.setBackground(Color.WHITE);
        area.setEditable(false);

        placeBtn(jp1Buttoon);
        jp2 =new JPanel();
        jp4 = new JScrollPane(area);
        jp4.setPreferredSize(new Dimension(800, 300));

        // 添加面板
        frame.add(jp1Buttoon, BorderLayout.NORTH);
        frame.add(jp2, BorderLayout.CENTER);
        frame.add(jp4,BorderLayout.SOUTH);


        // 显示窗口
        frame.pack();
        frame.setSize(800,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void placeBtn(JPanel panel)
    {

        panel.setLayout(new FlowLayout(FlowLayout.LEADING,20,15));

        JLabel label1= new JLabel("Recording interval(s):");
        JTextField field = new JTextField(5);
        field.setText("5");
        panel.add(label1);
        panel.add(field);

        startBtn = new JButton("Start");

        startBtn.addMouseListener(new StartMouseListener());

        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
//        startBtn.setBounds(20,20,80,25);

        stopBtn = new JButton("Stop");

        stopBtn.addMouseListener(new StopMouseListener());

//        stopBtn.setBounds(120,20,80,25);

        panel.add(startBtn);

        panel.add(stopBtn);



    }

    class StartMouseListener implements MouseListener{

        public void mouseClicked(MouseEvent e) {

            //当开始录音按钮被按下时就开始录音
            if(e.getSource().equals(startBtn))
            {
                //调用录音的方法
                capture();

                //记录开始录音的时间
                startPlay = System.currentTimeMillis();

                startBtn.setEnabled(false);
            }

        }

        //开始录音
        public void capture()
        {
            try {
                //af为AudioFormat也就是音频格式
                af = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);
                td = (TargetDataLine)(AudioSystem.getLine(info));

                //打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
                td.open(af);
                //允许某一数据行执行数据 I/O
                td.start();

                //启动显示波形的进程
                RecordWave aw = new RecordWave();
                Thread t2 = new Thread(aw);
                t2.start();
                //把显示波形的进程标志设为true
                flag = true;

                Record record = new Record();
                Thread t1 = new Thread(record);
                t1.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }



        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {


        }
        public void mouseExited(MouseEvent e) {


        }

    }

    class StopMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e) {

            //当松开录音按钮时停止录音并保存录音的文件
            if(e.getSource().equals(stopBtn))
            {
                //调用停止录音的方法
                stop();
                //当松开按钮后对显示波形的面板进行清空
                jp2.repaint();

                startBtn.setEnabled(true);
//                System.exit(0);
            }

        }

        public void stop()
        {
            stopflag = true;
            //将画波形的进程终止
            flag = false;
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {


        }
        public void mouseExited(MouseEvent e) {

        }

    }

    //画波形的类
    //因为要使用一些主函数中的数据，所以做成内部类
    class RecordWave extends JPanel implements Runnable
    {
        //用画笔画出波形
        public void paint(Graphics g)
        {
            super.paint(g);
            g.fillRect(jp2.getX(),jp2.getY() , 800, 480);
            if( audioDataBuffer != null)
            {
                g.drawLine(jp2.getWidth() / 256, 700, jp2.getWidth() / 256, 700);

                for(int i=0; i<audioDataBuffer.length-1; ++i)
                {
                    g.setColor(Color.BLUE);
                    g.drawLine(i * jp2.getWidth() / 256, (int)audioDataBuffer[i]+200 , (i + 1)

                            * jp2.getWidth() / 256, (int)audioDataBuffer[i+1]+200);
                }
            }
        }
        public void run()
        {
            //刷新波形
            while(true)
            {
                //System.out.println("ok");
                try {
                    synchronized (this) {
                        //隔多长时间获取
                        Thread.sleep(300);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
                this.paint(jp2.getGraphics());
                //终止线程
                if(flag == false)
                {
                    break;
                }
            }
        }
    }

    //录音类，因为要用到MyRecord类中的变量，所以将其做成内部类
    class Record implements Runnable
    {
        //定义存放录音的字节数组,作为缓冲区
        byte bts[] = new byte[10000];
        //将字节数组包装到流里，最终存入到baos中
        //重写run函数
        public void run() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long recordTime = System.currentTimeMillis();
            try {
                stopflag = false;
                while(stopflag != true)
                {
                    //当停止录音没按下时，该线程一直执行
                    //从数据行的输入缓冲区读取音频数据。
                    //要读取bts.length长度的字节,cnt 是实际读取的字节数
                    int cnt = td.read(bts, 0, bts.length);
                    if(cnt > 0)
                    {
                        baos.write(bts, 0, cnt);
                    }

                    if((System.currentTimeMillis()-recordTime)/1000 > RECORDING_TIME_SLOT )
                    {
                        save(baos);
                        baos = new ByteArrayOutputStream();
                        recordTime = System.currentTimeMillis();

                    }

                    //开始从音频流中读取字节数
                    byte copyBts[] = bts;
                    bais = new ByteArrayInputStream(copyBts);
                    ais = new AudioInputStream(bais, af, copyBts.length/af.getFrameSize());
                    try{
                        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
                        sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                        sd.open(af);
                        sd.start();

                        //从音频流中读取
                        int Buffer_Size = 10000;
                        audioDataBuffer = new byte[Buffer_Size];
                        int outBytes;

                        intBytes = ais.read(audioDataBuffer, 0,audioDataBuffer.length);

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //intBytes = -1;
                    //关闭打开的字节数组流
                    if(baos != null)
                    {
                        baos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{

                    td.close();
                    //刷新显示波形的面板
                    jp2.repaint();
                }
            }
        }

        //保存录音
        public void save(ByteArrayOutputStream baos)
        {
            af = getAudioFormat();
            byte audioData[] = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());
            //定义最终保存的文件名
            File file = null;
            //写入文件
            try {
                //以当前的时间命名录音的名字
                //将录音的文件存放到F盘下语音文件夹下
                File filePath = new File("E:/AudioFile");
                String tarPath = "E:/";
                if(!filePath.exists())
                {//如果文件不存在，则创建该目录
                    filePath.mkdirs();
                }
                long time = System.currentTimeMillis();
                file = new File(filePath+"/"+time+".wav");
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
                String name =filePath+"/"+time+".wav";


            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                //关闭流
                try {

                    if(bais != null)
                    {
                        bais.close();
                    }
                    if(ais != null)
                    {
                        ais.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
