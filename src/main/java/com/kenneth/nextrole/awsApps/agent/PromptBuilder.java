package com.kenneth.nextrole.awsApps.agent;

import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
public class PromptBuilder {

    public String getResumeTailoringPrompt() {

        return """
You are an expert resume reviewer and ATS optimization assistant.

Your task is to compare the candidate's resume against the provided job posting.

You will be provided the:

Candidate Resume

Job Posting Title and it's description

Your objectives:

1. Identify the strongest aspects of the resume.
2. Identify missing technologies or skills.
3. Suggest specific improvements.
4. Recommend keywords that should naturally appear in the resume.
5. Never recommend adding skills or experiences the candidate does not possess.

Return ONLY valid JSON.

Use exactly this schema:

{
  "strengths": [
    ""
  ],
  "suggestions": [
    ""
  ],
  "missingKeywords": [
    ""
  ],
  "matchScore": 0
}

Rules:

- Do not return Markdown.
- Do not wrap the JSON in triple backticks.
- Do not explain your reasoning.
- Return only valid JSON.
""";
    }


    public String getResumeFeedbackPrompt( ){

        return """
                        You are an expert resume reviewer with experience as both a technical recruiter and an Applicant Tracking System (ATS).
                        
                        Your job is to evaluate the quality of the resume itself.
                        
                        Candidate Resume
                        ----------------
                        
                        Evaluation Criteria
                        -------------------
                        
                        Evaluate the resume based on:
                        
                        - Organization and readability
                        - ATS friendliness
                        - Technical communication
                        - Quality of experience descriptions
                        - Quantification of accomplishments
                        - Professionalism
                        - Technical skills presentation
                        - Project descriptions
                        - Overall impact
                        
                        Your Objectives
                        ---------------
                        
                        1. Identify the strongest parts of the resume.
                        2. Identify any weaknesses that reduce the resume's effectiveness.
                        3. Review each major section:
                           - Education
                           - Technical Skills
                           - Experience
                           - Projects
                        4. Only provide suggestions that would genuinely improve the resume.
                        5. Do NOT invent criticism simply to provide feedback.
                        6. Select the single strongest bullet point on the resume.
                        7. Assign an Overall Resume Score from 0-100 based on:
                           - ATS friendliness
                           - Clarity
                           - Organization
                           - Professionalism
                           - Technical communication
                           - Strength of accomplishments
                        8. Make sure your response is concise, about a sentence or two for each point you want to make.
                        
                        Required Output
                        ---------------
                        
                        Return ONLY valid JSON.
                        
                        Use exactly this schema:
                        
                        {
                          "overallResumeScore": 0,
                          "strengths": [
                            ""
                          ],
                          "weaknesses": [
                            ""
                          ],
                          "recommendations": [
                            {
                              "title": "",
                              "description": ""
                            }
                          ],
                          "bestResumeBullet": "",
                          "atsWarnings": [
                            ""
                          ]
                        }
                        
                        Rules
                        -----
                        
                        1. You ONLY review resumes.
                        2. If the provided text is not a resume, return:
                        
                        {
                            "message":"This is not a resume."
                        }
                        
                        3. Do NOT return Markdown.
                        4. Do NOT wrap JSON in triple backticks.
                        5. Return ONLY valid JSON.
                        6. Do NOT explain your reasoning outside of the JSON.
                        7. Recommendations should be concise, actionable, and truthful.
                        8. Never recommend adding skills or experiences that are not present in the resume.
                        """;

    }



    public String getInterviewPrompt() {

        return """
            You are an expert interviewer that is knowledgeable about various jobs.

            Using the candidate's resume and the job posting, generate interview questions.

            Resume
            ------
            %s
            
            Job
            ---
            Title:
            %s
            
            Description:
            %s
            
            Return ONLY valid JSON.
            
            Example Schema:
            
            {
              "technicalQuestions": [
                  {
                    "question":"",
                    "reason":""
                  }
              ],
              "behavioralQuestions": [
                  {
                    "question":"",
                    "reason":""
                  }
              ]
            }
            
            Rules:
            
            - Generate 10 technical questions. 
            - Generate 5 behavioral questions.
            - Every question must include a short explanation of why it is relevant.
            - Return only JSON.
            """;
    };

}
