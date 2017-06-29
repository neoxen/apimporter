package com.whcis.data.ap.batch.totempserver;


import com.whcis.data.ap.model.LicensingTemp;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


import javax.sql.DataSource;

//@Configuration
//@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource tempDS;


    // tag::readerwriterprocessor[]
    @Bean
    public PoiItemReader reader() {
        PoiItemReader reader = new PoiItemReader();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setRowMapper(rowMapper());
        return reader;
    }

    @Bean
    public RowMapper rowMapper(){
        return new PassThroughRowMapper();
    }

    @Bean
    public LicensingItemProcessor processor() {
        return new LicensingItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<LicensingTemp> writer() {
        JdbcBatchItemWriter<LicensingTemp> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<LicensingTemp>());
        writer.setSql("INSERT INTO tab_permisson_wuhan_month (XK_WSH, XK_XMMC) VALUES (:XK_WSH, :XK_XMMC)");
        writer.setDataSource(tempDS);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importLicensingToTempServerJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importLicensingToTempServerJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<LicensingTemp, LicensingTemp> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
