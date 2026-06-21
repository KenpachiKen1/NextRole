package com.kenneth.nextrole.Model;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity //database table definition
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //would utilize this to build out each object in the table
@Table(name = "JobPosting")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private double salary;
    private String location;
    private String postingUrl;
    private String requisitionCode;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;




    @Override
    public String toString(){
        return String.format("Role title: %s, Salary: $%.2f, Location: %s, PostingUrl: %s", title, salary, location, postingUrl);
    }
}
