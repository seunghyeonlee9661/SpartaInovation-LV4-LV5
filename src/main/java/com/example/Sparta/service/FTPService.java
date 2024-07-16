package com.example.Sparta.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FTPService {

    private static final Logger logger = LoggerFactory.getLogger(FTPService.class);

    @Value("${ftp.server}")
    private String ftpServer;

    @Value("${ftp.port}")
    private int ftpPort;

    @Value("${ftp.user}")
    private String ftpUser;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.uploadDir}")
    private String ftpUploadDir;

    public boolean uploadImageToFtp(String filename, MultipartFile file) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(ftpServer, ftpPort);
        ftpClient.login(ftpUser, ftpPassword);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        logger.info("FTP Upload Directory: {}", ftpUploadDir);
        // FTP 서버의 디렉토리로 이동
        boolean changeDir = ftpClient.changeWorkingDirectory(ftpUploadDir);
        if (!changeDir) {
            System.out.println("Failed to change directory on FTP server: " + ftpUploadDir);
            throw new IOException("Failed to change directory on FTP server: " + ftpUploadDir);
        }


        // 파일 업로드
        System.out.println("File uploaded start to FTP server: " + ftpUploadDir + filename);
        InputStream inputStream = file.getInputStream();
        boolean uploaded = ftpClient.storeFile(filename, inputStream);
        inputStream.close();
        if (uploaded) {
            System.out.println("File uploaded successfully to FTP server: " + ftpUploadDir + filename);
        } else {
            System.out.println("Failed to upload file to FTP server");
        }
        return uploaded;
    }
}

