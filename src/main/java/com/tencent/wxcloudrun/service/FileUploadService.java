package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.dto.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    /**
     * 获取文件上传链接
     * @param path 上传路径
     * @return 上传链接信息
     */
    UploadFileResponse getUploadUrl(String path);

    /**
     * 上传文件
     * @param file 文件
     * @param path 上传路径
     * @return 文件ID
     * @throws IOException IO异常
     */
    String uploadFile(MultipartFile file, String path) throws IOException;
}