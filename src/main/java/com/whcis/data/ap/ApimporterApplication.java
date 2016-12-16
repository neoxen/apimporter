package com.whcis.data.ap;

import com.whcis.data.ap.newtemplate.LicensingUploadToTempServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApimporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApimporterApplication.class, args);
		LicensingUploadToTempServer.main(args);
	}
}
