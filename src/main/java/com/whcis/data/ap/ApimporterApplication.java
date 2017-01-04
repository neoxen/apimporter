package com.whcis.data.ap;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.LicensingUploadToTempServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ApimporterApplication implements CommandLineRunner {

	@Autowired
	private FilePathConfig filePathConfig;

	@Autowired
	@Qualifier("tempJdbcTemplate")
	private JdbcTemplate tempJdbcTemplate;

	@Override
	public void run(String... args) {
		new LicensingUploadToTempServer(filePathConfig, tempJdbcTemplate).stepOne();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
	}
}
