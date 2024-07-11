package com.pavel.dinit.project.models;

import jakarta.persistence.*;
import lombok.*;


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
    private String urlName;

    @Column()
    private String fullUrl;

    @Column()
    private Boolean urlHealth;

    @Column()
    private String dateAdded;

    @Column()
    private String lastChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_user_id")
    private User addedByUserId;



    // All the getters and setters are added with the @Data function on top
    // This is automatically written with Lombok.

    @Override
    public String toString(){
        return "Url{" + "urlId=" + this.urlId + ", urlName=" + this.urlName + "}";
    }




}
