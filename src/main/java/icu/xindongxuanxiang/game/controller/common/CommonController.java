package icu.xindongxuanxiang.game.controller.common;

import icu.xindongxuanxiang.game.common.ApiResponse;
import icu.xindongxuanxiang.game.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 通用控制器
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传
     * @param file 文件
     * @param path 上传路径
     * @return 文件ID
     */
    @PostMapping("/upload")
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file,
                                  @RequestParam("path") String path) {
        try {
            logger.info("文件上传请求: filename={}, path={}", file.getOriginalFilename(), path);

            if (file.isEmpty()) {
                return ApiResponse.error("文件不能为空");
            }

            String fileId = fileUploadService.uploadFile(file, path);
            return ApiResponse.ok(fileId);
        } catch (IOException e) {
            logger.error("文件上传IO异常", e);
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
    }
}