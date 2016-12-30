package com.whcis.data.ap;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.LicensingUploadToTempServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApimporterApplication implements CommandLineRunner {

	@Autowired
	private FilePathConfig filePathConfig;

	@Autowired
	@Qualifier("tempJdbcTemplate")
	private JdbcTemplate tempJdbcTemplate;

	@Override
	public void run(String... args) {
		new LicensingUploadToTempServer().stepOne();

		System.out.println("****************************");
		System.out.println(filePathConfig.getNewTemplate());
		System.out.println("****************************");

		Map<String, Object> map = new HashMap<String, Object>();
		String query = " select * from tab_penaly_wuhan_month";
		try {
			map = tempJdbcTemplate.queryForMap(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("MySQL Data: " + map.toString());
	}

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
	}
}
