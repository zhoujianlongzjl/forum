package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.UserService;
import com.qingfengzhuyue.forum.utils.TencentCOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 图片上传Controller
 */
@Controller
public class ImageUploadController {

    @Value("${tencent.path}")
    private String IMAGE_PATH;

    @Autowired
    private UserService userService;

    /**
     * 上传头像
     */
    @RequestMapping(value = "/api/user/upload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        if (fileName.lastIndexOf(".") < 0)
            return  CommonResult.failed(ResultCode.NO_SUFFIX);

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //如果不是图片
        if (!prefix.equalsIgnoreCase(".jpg") && !prefix.equalsIgnoreCase(".jpeg") && !prefix.equalsIgnoreCase(".svg") && !prefix.equalsIgnoreCase(".gif") && !prefix.equalsIgnoreCase(".png")) {
            return CommonResult.failed(ResultCode.NOT_A_PICTURE);
        }

        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);
        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "avatar");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        String imageUrl=IMAGE_PATH+imageName;
        //存入图片url，用于网页显示
        //更新数据库
        userService.updateUserAvatar(imageUrl,user.getId());

        //返回成功信息
        return CommonResult.success(imageUrl);
    }

    /**
     * 上传图片
     */
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadImage(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        if (fileName.lastIndexOf(".") < 0)
            return  CommonResult.failed(ResultCode.NO_SUFFIX);

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //如果不是图片
        if (!prefix.equalsIgnoreCase(".jpg") && !prefix.equalsIgnoreCase(".jpeg") && !prefix.equalsIgnoreCase(".svg") && !prefix.equalsIgnoreCase(".gif") && !prefix.equalsIgnoreCase(".png")) {
            return CommonResult.failed(ResultCode.NOT_A_PICTURE);
        }

        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);
        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "image");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        //存入图片url，用于网页显示
        String imageUrl=IMAGE_PATH+imageName;

        //返回成功信息
        return CommonResult.success(imageUrl);
    }
    /**
     * 删除图片
     */
    @RequestMapping(value = "/api/deletefile", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deletefile(@RequestParam("url") String url, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        String key = url.replaceAll(IMAGE_PATH, "");
        TencentCOS.deletefile(key);

        //返回成功信息
        return CommonResult.success(ResultCode.DELETE_PICTURE_SUCCEEDED.getMessage());
    }

    /**
     * 删除临时文件
     *
     * @param files 临时文件，可变参数
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

}