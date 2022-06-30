package com.lzy.trading_back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @PostMapping("/goodsImg")
    public Map<String,Object> GoodsImg(@RequestParam("sku") String sku, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest req) {
        Map<String,Object> result = new HashMap<>();
        //get the name of picture uploading
        String originName = multipartFile.getOriginalFilename();
        System.out.println("originName:"+originName);
        //tell the type of the picture
        assert originName != null;
        if(!originName.endsWith(".jpg")) { //必须为jpg格式
            result.put("status","error");
            result.put("msg", "the type of the picture ERROR!");
            return result;
        }
        File folder = new File(fileUploadPath);
        //give the picture a new name, avoiding same names
        String newPicName = sku + ".jpg";
        try {
            //generate file
            multipartFile.transferTo(new File(folder, newPicName));
            //generate URL
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/pic/"+ newPicName;
            System.out.println("url:"+url);
            //put URL in result
            result.put("status", "success");
            result.put("url", url);
        }catch (IOException e) {
            result.put("status", "error");
            result.put("msg",e.getMessage());
        }
        return result;
    }
}
