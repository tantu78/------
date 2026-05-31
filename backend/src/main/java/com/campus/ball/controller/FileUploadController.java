package com.campus.ball.controller;

import com.campus.ball.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@Api(tags = "文件上传接口")
public class FileUploadController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @PostMapping("/image")
    @ApiOperation("上传图片")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return Result.error("文件名无效");
        }

        String ext = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            ext = originalName.substring(dotIndex).toLowerCase();
        }

        if (!ext.matches("\\.(jpg|jpeg|png|gif|webp|bmp)$")) {
            return Result.error("不支持的图片格式，仅支持 jpg/jpeg/png/gif/webp/bmp");
        }

        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.error("文件大小不能超过10MB");
        }

        String newFileName = UUID.randomUUID().toString() + ext;

        try {
            Path targetPath = Paths.get(uploadPath, newFileName);
            Files.copy(file.getInputStream(), targetPath);

            String imageUrl = "/uploads/" + newFileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", imageUrl);
            result.put("fileName", originalName);
            return Result.success(result);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}
