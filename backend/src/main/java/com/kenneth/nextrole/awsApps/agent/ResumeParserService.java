package com.kenneth.nextrole.awsApps.agent;

import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.awsApps.S3Service;
import com.kenneth.nextrole.awsApps.dto.ParsedResume;
import com.kenneth.nextrole.exception.ResumeNotFoundException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;


@Service
public class ResumeParserService {


    private final S3Service service;
    private final ResumeRepository repository;
    public ResumeParserService(S3Service service, ResumeRepository repository){
        this.service = service;
        this.repository = repository;
    }

    public ParsedResume buildParsedResume(String text, int pageCount, int characterCount){
        return ParsedResume.builder().text(text).pageCount(pageCount).characterCount(characterCount).build();
    }
    public ParsedResume parseResumePDF(User user, Long resumeId) throws IOException {


        if(!repository.existsByUserIdAndId(user.getId(), resumeId)){
            throw new ResumeNotFoundException("The requested resume does not belong to the user.");
        }

        Resume resume = repository.findById(resumeId).orElseThrow(() -> new ResumeNotFoundException("This resume doesn't exist"));

        try (InputStream stream = service.getResumeStream(resume.getS3ObjectKey());
             PDDocument document = Loader.loadPDF(RandomAccessReadBuffer.createBufferFromStream(stream))
        ){
            // Wrap the InputStream into a RandomAccessReadBuffer required by PDFBox 3
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).trim();
            return buildParsedResume(text, document.getNumberOfPages(), text.length());
        } catch (IOException e){
            throw new IOException("Could not parse resume", e);
        }
    }




}
