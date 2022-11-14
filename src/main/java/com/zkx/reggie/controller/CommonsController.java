package com.zkx.reggie.controller;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zkx.reggie.commons.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

@RequestMapping("/common")
@RestController
@Slf4j
public class CommonsController {
    /*
    文件上传。
     */
    @Value("D:\\learn资料文件\\1 瑞吉外卖项目\\资料\\图片资源\\")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fN = UUID.randomUUID().toString()+suffix;

        //创建一个目录
        File dir = new File(basePath);

        if(!dir.exists()){
            dir.mkdir();
        }

        try{
            file.transferTo(new File(basePath+fN));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.success(fN);
    }

    @GetMapping("/download")
    public R<String> download(String name, HttpServletResponse response){

        try {
            //文件输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

            //文件输出流输出到浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            int length = 0;

            byte [] bytes = new byte[1024];


            while((length = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes);
                outputStream.flush();
            }

            outputStream.close();;
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return R.success("success");
    }

}
