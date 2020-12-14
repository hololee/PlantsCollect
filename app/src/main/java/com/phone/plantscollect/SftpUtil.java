package com.phone.plantscollect;

import android.app.Dialog;
import android.content.Context;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SftpUtil {


    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;


    // SFTP 서버연결
    public void init() {
        String url = "***.***.***.***";
        String user = "****";
        String password = "****";

        System.out.println(url);
        //JSch 객체 생성
        JSch jsch = new JSch();
        try {
            //세션객체 생성 ( user , host, port )
            session = jsch.getSession(user, url);

            //password 설정
            session.setPassword(password);

            //세션관련 설정정보 설정
            java.util.Properties config = new java.util.Properties();

            //호스트 정보 검사하지 않는다.
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            //접속
            session.connect();

            //sftp 채널 접속
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

        } catch (JSchException e) {
            e.printStackTrace();
        }


    }

    // 단일 파일 업로드
    public boolean upload(String dir, File file) {
        FileInputStream in = null;

        try { //파일을 가져와서 inputStream에 넣고 저장경로를 찾아 put
            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());
            in.close();
            return true;
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
//        {
//            try {
//
//
//                }
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//                return false;
//            }
//        }
    }

    // 단일 파일 다운로드
    public InputStream download(String dir, String fileNm) {
        InputStream in = null;

        try { //경로탐색후 inputStream에 데이터를 넣음
            channelSftp.cd(dir);
            in = channelSftp.get(fileNm);

        } catch (SftpException se) {
            se.printStackTrace();
        }

        return in;
    }

    // 파일서버와 세션 종료
    public void disconnect() {
        channelSftp.quit();
        session.disconnect();
    }

}
