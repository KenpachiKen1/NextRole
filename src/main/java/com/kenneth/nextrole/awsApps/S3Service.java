package com.kenneth.nextrole.awsApps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


/*
Build custom exception, have file limit sizes as well, nothing more than 5MB.
 */

/*
Very minimal as of right now, only needs delete, upload, and reading functionality.
 */


@Service
public class S3Service {

    private final Region region = Region.US_EAST_1;
    private final S3Presigner presigned = S3Presigner.builder().region(region).build();

    private final S3Client s3 = S3Client.builder().region(region).build();

    @Value("${aws.s3.bucket}")
    private String bucket;

    /*
    For viewing specific files related to the user.
     */
    public String getPresignedUrl(String objectKey){
        GetObjectRequest request = GetObjectRequest.builder().
                bucket(bucket).
                key(objectKey).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(15)).
                getObjectRequest(request).build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigned.presignGetObject(presignRequest);

        return presignedGetObjectRequest.url().toString();
    }


    /*
    Before testing, add validation so that only pdf's can be uploaded and nothing else.
     */

    //This function is for uploading files to S3. Pass file from frontend, upload it to S3.
    public void uploadResume(MultipartFile file, String objectKey) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(file.getContentType()) // Lets S3 know it's a PDF, DOCX, etc.
                .build();

        try {
            s3.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file.", e);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3.", e);
        }
    }


    /*
    Not a lot required, will upgrade this with specified error handling
     */
    public void deleteResume(String objectKey){
        DeleteObjectRequest request = DeleteObjectRequest.builder().
                key(objectKey).bucket(bucket).build();

        try {
            s3.deleteObject(request);
        } catch (S3Exception e) {
            throw new RuntimeException("failed to delete object from bucket", e);
        }
    }
    //Reading a specific resume's content to pass it to resume parser
    public InputStream getResumeStream(String objectKey){
        GetObjectRequest request = GetObjectRequest.builder().key(objectKey).bucket(bucket).build();

        return s3.getObject(request, ResponseTransformer.toInputStream());
    }


}
