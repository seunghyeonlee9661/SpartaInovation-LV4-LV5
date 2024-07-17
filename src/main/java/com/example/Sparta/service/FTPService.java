package com.example.Sparta.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/* 이미지 전송을 위해 FTP를 사용해 업로드와 다운로드를 진행합니다. */
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

    public String downloadImage(String filePath) throws IOException {
        logger.info("FTP download filepath: {}", filePath);
        FTPClient ftpClient = new FTPClient();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // FTP 연결 시도
            ftpClient.connect(ftpServer, ftpPort);
            ftpClient.login(ftpUser, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // FTP 서버의 디렉토리로 이동
            boolean changeDir = ftpClient.changeWorkingDirectory(ftpUploadDir);
            if (!changeDir) {
                throw new IOException("Failed to change directory on FTP server: " + ftpUploadDir);
            }

            // 파일 다운로드
            InputStream inputStream = ftpClient.retrieveFileStream(filePath);
            if (inputStream == null) {
                logger.error("File not found");
                return null;
            }

            /* 파일을 바이트 배열로 변환*/
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            /* 연결 종료 */
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            logger.error("Error downloading file from FTP server: {}", e.getMessage());
            throw e;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    logger.error("Error disconnecting from FTP server: {}", ex.getMessage());
                }
            }
        }
        byte[] byteEnc64 = Base64.encodeBase64(outputStream.toByteArray());
        return new String(byteEnc64 , "UTF-8");
    }
}

