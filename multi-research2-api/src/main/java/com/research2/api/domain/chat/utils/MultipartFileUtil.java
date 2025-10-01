package com.research2.api.domain.chat.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class MultipartFileUtil {

    /**
     * Base64 STRING TO MultipartFile
     */
    public static MultipartFile createFromBase64(String base64, String fileName, String contentType) {
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64);
        return new ByteArrayMultipartFile(decodedBytes, fileName, contentType);
    }

    /**
     * byte ARRAY TO MultipartFile
     */
    public static MultipartFile createFromBytes(byte[] bytes, String fileName, String contentType) {
        return new ByteArrayMultipartFile(bytes, fileName, contentType);
    }

    // 내부 구현체
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String filename;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] content, String filename, String contentType) {
            this.content = content;
            this.filename = filename;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return filename;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}