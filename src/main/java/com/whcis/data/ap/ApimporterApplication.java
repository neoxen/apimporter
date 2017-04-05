package com.whcis.data.ap;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.NewTemplateUploadToTempServer;
import com.whcis.data.ap.newtemplate.UploadToCreditHubei;
import com.whcis.data.ap.oldtemplate.OldTemplateToTempServer;
import com.whcis.data.ap.temptobase.ServerInfo;
import com.whcis.data.ap.temptobase.TempToBaseServer;
import com.whcis.data.ap.temptobase.TruncateTempTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableConfigurationProperties
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
		// 1
//		xyChina();

		// 2
//		tempServer();

		// 3
		baseServer();
	}

	public void xyChina() {
		new UploadToCreditHubei(filePathConfig, xychinaJdbcTemplate).stepOne();
	}

	public void tempServer() {
		TruncateTempTables.truncateTempTables(tempJdbcTemplate);
		NewTemplateUploadToTempServer newToTemp = new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate);
		newToTemp.stepTwo();
		newToTemp.stepThree();
		new OldTemplateToTempServer(filePathConfig, tempJdbcTemplate).stepFour();
	}

	public void baseServer() {
		TruncateTempTables.truncateBaseTables(baseJdbcTemplate);
		ServerInfo.printMaxRecordID(baseJdbcTemplate);
		new TempToBaseServer(tempJdbcTemplate, baseJdbcTemplate).stepFive();
		ServerInfo.copyNewRecords(baseJdbcTemplate);
		ServerInfo.printMaxRecordID(baseJdbcTemplate);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
	}
}
