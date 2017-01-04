package com.whcis.data.ap;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.NewTemplateUploadToTempServer;
import com.whcis.data.ap.newtemplate.UploadToCreditHubei;
import com.whcis.data.ap.oldtemplate.OldTemplateToTempServer;
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
	@Qualifier("xychinaJdbcTemplate")
	private JdbcTemplate xychinaJdbcTemplate;

	@Autowired
	@Qualifier("tempJdbcTemplate")
	private JdbcTemplate tempJdbcTemplate;

	@Autowired
	@Qualifier("baseJdbcTemplate")
	private JdbcTemplate baseJdbcTemplate;

	@Override
	public void run(String... args) {
		//new UploadToCreditHubei(filePathConfig, xychinaJdbcTemplate).stepOne();
		new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate).stepTwo();
		new OldTemplateToTempServer(filePathConfig, tempJdbcTemplate).StepThree();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
	}
}
