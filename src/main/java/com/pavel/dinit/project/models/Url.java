package com.pavel.dinit.project.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "urlstb")
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

    @OneToMany(mappedBy = "urlId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UrlHealthHistory> healthHistories;

    @Override
    public String toString(){
        return "Url{" + "urlId=" + this.urlId + ", urlName=" + this.urlName + "}";
    }
}
