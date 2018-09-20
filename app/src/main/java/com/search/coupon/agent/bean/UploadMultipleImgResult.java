package com.search.coupon.agent.bean;

import java.util.List;

/**
 * @author baipeng
 * @Title UploadMultipleResult
 * @Date 2017/9/10 10:11
 * @Description UploadMultipleResult.
 */
public class UploadMultipleImgResult {

    private List<FileListBean> fileList;

    public List<FileListBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileListBean> fileList) {
        this.fileList = fileList;
    }

    public static class FileListBean {
        /**
         * fileType : H1
         * fileName : 01.jpg
         * filePath : url
         */

        private String fileType;
        private String fileName;
        private String filePath;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
