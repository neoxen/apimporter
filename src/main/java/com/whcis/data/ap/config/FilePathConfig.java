package com.whcis.data.ap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by neo on 2016/12/28.
 */
@Configuration
@ConfigurationProperties(locations = "classpath:config.yml",
        ignoreUnknownFields = false, prefix = "development.proceed.file")
public class FilePathConfig {
    private String xyChina;
    private String newTemplate;
    private String oldTemplate;
    private String onlyToBase;

    public String getXyChina() {
        return xyChina;
    }

    public void setXyChina(String xyChina) {
        this.xyChina = xyChina;
    }

    public String getNewTemplate() {
        return newTemplate;
    }

    public void setNewTemplate(String newTemplate) {
        this.newTemplate = newTemplate;
    }

    public String getOldTemplate() {
        return oldTemplate;
    }

    public void setOldTemplate(String oldTemplate) {
        this.oldTemplate = oldTemplate;
    }

    public String getOnlyToBase() {
        return onlyToBase;
    }

    public void setOnlyToBase(String onlyToBase) {
        this.onlyToBase = onlyToBase;
    }
}
