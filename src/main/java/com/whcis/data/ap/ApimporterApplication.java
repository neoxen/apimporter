package com.whcis.data.ap;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.NewTemplateUploadToTempServer;
import com.whcis.data.ap.newtemplate.UploadToCreditHubei;
import com.whcis.data.ap.oldtemplate.OldTemplateToTempServer;
import com.whcis.data.ap.temptobase.ServerInfo;
import com.whcis.data.ap.temptobase.TempToBaseServer;
import com.whcis.data.ap.temptobase.TruncateTempTables;
import com.whcis.data.ap.util.Report;
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

	private Report report = new Report();

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
		// 0
		init();

		// 1
//		xyChina();

		// 2
		tempServer();
//		tempServerWhcic();

		// 3
//		checkTempOrgans();
//		checkDateParsing();

		// 4
		baseServer();

		reporting();
	}

	public void init() {
		report.countEntries(filePathConfig);
	}

	public void xyChina() {
		UploadToCreditHubei uploadToCreditHubei = new UploadToCreditHubei(filePathConfig, xychinaJdbcTemplate);
		uploadToCreditHubei.stepOne();
		report.setDuplicateEntryCH(uploadToCreditHubei.getDuplicateEntryCH());
	}

	public void tempServer() {
		TruncateTempTables.truncateTempTables(tempJdbcTemplate);
		NewTemplateUploadToTempServer newToTemp = new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate);
		newToTemp.stepTwo();
		newToTemp.stepThree();
		new OldTemplateToTempServer(filePathConfig, tempJdbcTemplate).stepFour();
	}

	public void tempServerWhcic() {
		TruncateTempTables.truncateTempTables(tempJdbcTemplate);
		NewTemplateUploadToTempServer newToTemp = new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate);
		newToTemp.stepTwo();
	}

	public void checkTempOrgans(){
		TempToBaseServer tempToBase =  new TempToBaseServer(tempJdbcTemplate, baseJdbcTemplate);
		tempToBase.checkOrgans();
	}

	public void checkDateParsing(){
		TempToBaseServer tempToBase =  new TempToBaseServer(tempJdbcTemplate, baseJdbcTemplate);
		tempToBase.checkDate();
	}

	public void baseServer() {
		TruncateTempTables.truncateBaseTables(baseJdbcTemplate);
		ServerInfo.printMaxRecordID(baseJdbcTemplate);
		TempToBaseServer tempToBase =  new TempToBaseServer(tempJdbcTemplate, baseJdbcTemplate);
		tempToBase.stepFive();
		report.setDuplicateEntryBase(tempToBase.getDuplicateEntryBase());
		ServerInfo.copyNewRecords(baseJdbcTemplate);
		ServerInfo.printMaxRecordID(baseJdbcTemplate);
	}

	public void reporting() {
		report.highlightDuplicateEntries(filePathConfig);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
	}
}
