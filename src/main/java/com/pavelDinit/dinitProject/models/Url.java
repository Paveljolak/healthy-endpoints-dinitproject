package com.pavelDinit.dinitProject.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Entity
@Table(name = "urlstb")
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private long urlId;
    @Column()
    private long addedByUserId;
    @Column()
    private String urlName;
    @Column()
    private String fullUrl;
    @Column()
    private Boolean urlHealth;
    @Column()
    private String dateAdded;
    @Column()
    private String lastChecked;


    // All the getters and setters are added with the @Data function on top
    // This is automatically written with Lombok.

    @Override
    public String toString(){
        return "Url{" + "urlId=" + this.urlId + ", urlName=" + this.urlName + "}";
    }




}
