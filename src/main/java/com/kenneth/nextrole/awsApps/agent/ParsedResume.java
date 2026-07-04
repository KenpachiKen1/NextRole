package com.kenneth.nextrole.awsApps.agent;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParsedResume {

    private String text;

    private int pageCount;

    private int characterCount;
}
