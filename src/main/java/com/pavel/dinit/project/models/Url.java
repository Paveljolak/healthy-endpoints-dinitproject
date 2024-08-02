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
    @Column(name = "url_id")
    private long urlId;

    @Column(name = "url_name")
    private String urlName;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "url_health")
    private Boolean urlHealth;

    @Column(name = "date_added")
    private String dateAdded;

    @Column(name = "last_checked")
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
