package com.make.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.make.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author java
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        //获取源文件名称
        String originalFilename = file.getOriginalFilename();
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        //获取上传的路径
        String rootFilePath = System.getProperty("user.dir") + "/springbootdemo/src/main/resources/files/" + flag + "_" +originalFilename;
        //把文件写入上传路径
        FileUtil.writeBytes(file.getBytes(),rootFilePath);
        return Result.success(ip + ":" + port +"/files/" + flag );
    }

    @PostMapping("/editor/upload")
    public JSONObject editorUpload(MultipartFile file) throws IOException {
        //获取源文件名称
        String originalFilename = file.getOriginalFilename();
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        //获取上传的路径
        String rootFilePath = System.getProperty("user.dir") + "/springbootdemo/src/main/resources/files/" + flag + "_" +originalFilename;
        //把文件写入上传路径
        FileUtil.writeBytes(file.getBytes(),rootFilePath);
        String url = ip + ":" + port +"/files/" + flag;
        JSONObject json = new JSONObject();
        json.set("errno",0);
        JSONArray array = new JSONArray();
        JSONObject data = new JSONObject();
        array.add(data);
        data.set("url",url);
        json.set("data",array);
        return json;
    }

    @GetMapping("/{flag}")
    public void getFiles(HttpServletResponse response, @PathVariable String flag){
        // 新建一个输出流对象
        OutputStream os;
        //定于文件上传的根路径
        String basePath = System.getProperty("user.dir") + "/springbootdemo/src/main/resources/files/";
        // 获取所有的文件名称
        List<String> fileNames = FileUtil.listFileNames(basePath);
        //找到跟参数一致的文件
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");
                // 通过文件的路径读取文件字节流
                byte[] bytes = FileUtil.readBytes(basePath + avatar);
                // 通过输出流返回文件
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("文件下载失败");
        }

    }
}
