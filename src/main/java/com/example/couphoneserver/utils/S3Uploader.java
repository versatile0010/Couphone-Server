package com.example.couphoneserver.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private AmazonS3 amazonS3Client;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }


    public String upload(MultipartFile image) {

        if(Objects.requireNonNull(image.getContentType()).contains("image")) {  //이미지가 있다면 실행하고 없다면 패스

            String fileName = createFileName(image);//중복되지않게 이름을  randomUUID()를 사용해서 생성함
            String fileFormat = image.getContentType().substring(image.getContentType().lastIndexOf("/") + 1); //파일 확장자명 추출

            MultipartFile resizedImage = resizer(fileName, fileFormat, image, 100); //오늘의 핵심 메서드

            return putS3(resizedImage, fileName);
        }
        return "failed!!!";
    }

    private String createFileName(MultipartFile image) {
        return UUID.randomUUID() + "_" + image.getOriginalFilename();
    }

    @Transactional
    public MultipartFile resizer(String fileName, String fileFormat, MultipartFile originalImage, int width) {

        try {
            BufferedImage image = ImageIO.read(originalImage.getInputStream());// MultipartFile -> BufferedImage Convert

            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 400보다 작으면 패스
            if(originWidth < width)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", width);
            scale.setAttribute("newHeight", width * originHeight / originWidth);//비율유지를 위해 높이 유지
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormat, baos);
            baos.flush();

            return new CustomMultipartFile(fileName,fileFormat,originalImage.getContentType(), baos.toByteArray());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 줄이는데 실패했습니다.");
        }
    }


    private String putS3(MultipartFile file, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try{
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch(IOException e){
           log.error("IMAGE UPLOAD FAILED");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
