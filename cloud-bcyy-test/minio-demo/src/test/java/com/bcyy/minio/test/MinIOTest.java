package com.bcyy.minio.test;

import com.bcyy.file.service.FileStorageService;
import com.bcyy.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {


    public static void main(String[] args) {

        FileInputStream fileInputStream = null;
        try {

            fileInputStream =  new FileInputStream("D:\\图片\\联想锁屏壁纸\\BingWallpaper-2022-08-21.jpg");;

            //1.创建minio链接客户端
            MinioClient minioClient = MinioClient.builder().credentials("minioadmin", "minioadmin").endpoint("http://192.168.11.129:9000").build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("test3.png")//文件名
                    .contentType("image")//文件类型
                    .bucket("bcyy")//桶名词  与minio创建的名词一致
                    .stream(fileInputStream, fileInputStream.available(), -1) //文件流
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpdateImgFile() {
        try {
//            FileInputStream fileInputStream = new FileInputStream("D:\\图片\\联想锁屏壁纸\\BingWallpaper-2022-08-21.jpg");
//            String filePath = fileStorageService.uploadImgFile("", "test11.png", fileInputStream);
            fileStorageService.delete("http://192.168.11.129:9000/bcyy/2023/08/15/fc699793ca12441ea84a4cc17e1cd44f.jpg");
//            System.out.println(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}