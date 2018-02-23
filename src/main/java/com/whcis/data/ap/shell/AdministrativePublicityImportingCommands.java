package com.whcis.data.ap.shell;

import com.whcis.data.ap.config.FilePathConfig;
import com.whcis.data.ap.newtemplate.NewTemplateUploadToTempServer;
import com.whcis.data.ap.newtemplate.UploadToCreditHubei;
import com.whcis.data.ap.temptobase.ServerInfo;
import com.whcis.data.ap.temptobase.TempToBaseServer;
import com.whcis.data.ap.temptobase.TruncateTempTables;
import com.whcis.data.ap.util.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AdministrativePublicityImportingCommands {

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

    @ShellMethod("#6 将临时库中标准化的双公示数据导入双公示基础库")
    public void base() {
        init();
        baseServer();
        reporting();
    }

    @ShellMethod("#5 在临时库中检查是否存在错误导入的时间字段")
    public void date() {
        checkDateParsing();
    }

    @ShellMethod("#4 在临时库中检查行政单位名称")
    public void organ() {
        checkTempOrgans();
    }

    @ShellMethod("#3 在临时库中初步规范化行政单位名称")
    public void normalize() {
        normalizeOrgan();
    }

    @ShellMethod("#2 向临时库导入汇集系统双公示数据")
    public void tempWhcic() {
        init();
        tempServerWhcic();
        reporting();
    }

    @ShellMethod("#2 向临时库导入双公示数据")
    public void temp() {
        init();
        tempServer();
        reporting();
    }

    @ShellMethod("#1 向信用中国库导入双公示数据")
    public void xychina() {
        init();
        xyChina();
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
//		new OldTemplateToTempServer(filePathConfig, tempJdbcTemplate).stepFour();
    }

    public void tempServerWhcic() {
        TruncateTempTables.truncateTempTables(tempJdbcTemplate);
        NewTemplateUploadToTempServer newToTemp = new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate);
        newToTemp.stepWhcic();
    }

    public void normalizeOrgan(){
        NewTemplateUploadToTempServer newToTemp = new NewTemplateUploadToTempServer(filePathConfig, tempJdbcTemplate);
            newToTemp.stepNormalization();
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
}
