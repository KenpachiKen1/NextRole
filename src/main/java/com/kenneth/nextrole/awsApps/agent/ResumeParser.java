package com.kenneth.nextrole.awsApps.agent;

import com.kenneth.nextrole.awsApps.S3Service;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class ResumeParser {


    private final S3Service service;

    public ResumeParser(S3Service service){
        this.service = service;
    }

    public String extractResumeText(String objectKey) throws IOException {
        try (InputStream stream = service.getResumeStream(objectKey);
             PDDocument document = Loader.loadPDF(RandomAccessReadBuffer.createBufferFromStream(stream)); //
        ){
            // Wrap the InputStream into a RandomAccessReadBuffer required by PDFBox 3
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        } catch (IOException e){
            throw new IOException("Could not parse resume", e);
        }
    }




}
