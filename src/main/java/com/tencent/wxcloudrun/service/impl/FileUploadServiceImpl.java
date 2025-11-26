package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.model.dto.UploadFileResponse;
import com.tencent.wxcloudrun.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Value("${wechat.cloud.env-id}")
    private String envId;

    @Value("${wechat.cloud.access-token-url}")
    private String accessTokenUrl;

    @Value("${wechat.cloud.upload-file-url}")
    private String uploadFileUrl;

    @Override
    public UploadFileResponse getUploadUrl(String path) {
        try {
            // 构造请求URL
            String url = uploadFileUrl + "?access_token=" + getAccessToken();

            // 构造请求体
            String requestBody = String.format("{\"env\":\"%s\",\"path\":\"%s\"}", envId, path);

            // 发送POST请求
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            UploadFileResponse uploadFileResponse = objectMapper.readValue(response.getBody(), UploadFileResponse.class);
            
            if (uploadFileResponse.getErrcode() != null && uploadFileResponse.getErrcode() != 0) {
                logger.error("获取上传链接失败: errcode={}, errmsg={}", uploadFileResponse.getErrcode(), uploadFileResponse.getErrmsg());
                throw new RuntimeException("获取上传链接失败: " + uploadFileResponse.getErrmsg());
            }
            
            uploadFileResponse.setKey(path);
            return uploadFileResponse;
        } catch (Exception e) {
            logger.error("获取上传链接异常", e);
            throw new RuntimeException("获取上传链接异常: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        // 1. 获取上传链接
        UploadFileResponse uploadFileResponse = getUploadUrl(path);
        
        // 2. 上传文件
        RestTemplate restTemplate = new RestTemplate();
        
        // 构造multipart/form-data请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", uploadFileResponse.getKey());
        body.add("Signature", uploadFileResponse.getAuthorization());
        body.add("x-cos-security-token", uploadFileResponse.getToken());
        body.add("x-cos-meta-fileid", uploadFileResponse.getCosFileId());
        body.add("file", new InputStreamResource(file.getInputStream()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(uploadFileResponse.getUrl(), requestEntity, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return uploadFileResponse.getFileId();
        } else {
            throw new RuntimeException("文件上传失败: " + response.getStatusCode());
        }
    }

    /**
     * 获取访问令牌
     * @return 访问令牌
     */
    private String getAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(accessTokenUrl, String.class);
            
            // 解析响应获取access_token
            ObjectMapper objectMapper = new ObjectMapper();
            // 这里需要根据实际的token获取接口响应格式进行解析
            // 假设返回格式为 {"access_token": "xxx", "expires_in": 7200}
            // 实际使用时需要根据具体的接口文档进行调整
            
            // 临时返回固定值，实际应该从配置或接口获取
            return "your_access_token";
        } catch (Exception e) {
            logger.error("获取访问令牌异常", e);
            throw new RuntimeException("获取访问令牌异常: " + e.getMessage());
        }
    }
}