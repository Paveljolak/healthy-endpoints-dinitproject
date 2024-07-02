package com.pavelDinit.dinitProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "urlstb")

public class Urls {

    @Id
    private long urlid;
    private long userid;
    private String urlname;
    private boolean urlhealth;
    private String dateadded;
    private String lastchecked;

}
