package com.pavelDinit.dinitProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "urlstb")

public class Url {
    @Id
    private long urlid;
    private long addedbyuserid;
    private String urlname;
    private String fullurl;
    private boolean urlhealth;
    private String dateadded;
    private String lastchecked;


    // All the getters and setters are added with the @Data function on top
    // This is automatically written with Lombok.


    public String toString(){
        return "Url{" + "urlId=" + this.urlid + ", urlName=" + this.urlname + "}";
    }


}
