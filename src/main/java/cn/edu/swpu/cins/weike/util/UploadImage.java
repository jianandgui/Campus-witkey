package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadImage {



    public String uploadImage(MultipartFile image, String username) throws IOException {
        String path = "/home/tangxudong/images";
        String fileName =null;
        fileName = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        path += "/" +username.hashCode()+fileName;
        File file = new File(path);
        try {
            image.transferTo(file);
            path = getPath(path);
            return path;
        } catch (IOException e) {
            throw new ProjectException(ExceptionEnum.FILE_UPLOAD_FAILED.getMsg());
        }
    }

    public String getPath(String path) {
        return path.replaceFirst("/home/tangxudong", "");
    }
}
