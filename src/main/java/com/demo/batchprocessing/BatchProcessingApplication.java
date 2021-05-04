package com.demo.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class BatchProcessingApplication implements CommandLineRunner {

//	public static void main(String[] args) throws Exception {
//		System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));
//	}
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("importPersonJob1")
	private Job importPersonJob1;

	@Value("${file.input}")
	private String input;

	@Value("${file.output}")
	private String output;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BatchProcessingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		JobParametersBuilder jobParameters = new JobParametersBuilder();
		jobParameters.addString("file.input", input);
		jobParameters.addString("file.output", output);
	        jobLauncher.run(importPersonJob1, jobParameters.toJobParameters());

	}
}
