package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    /*
    Some queries I may want
     */

    //Optional is for data i need but ensures i handle it if it doesn't exist, loads in entire entity
    Optional<Company> findByName(String name);

    //Used for straight forward answers, can I use it or not, typically just for sign-up validation.
    boolean existsByName(String name);


    //existsBy just checks for presence
}
