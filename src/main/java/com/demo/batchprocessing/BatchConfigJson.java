package com.demo.batchprocessing;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.demo.service.PersonFieldSetMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfigJson {

	public static final Logger LOGGER = LoggerFactory.getLogger(BatchConfigJson.class);

	public static final String[] TOKENS = { "firstName", "lastName" };

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Bean
	public FlatFileItemReader<Person> csvItemReader(@Value("#{jobParameters['file.input']}") String input) {

		FlatFileItemReaderBuilder<Person> reader = new FlatFileItemReaderBuilder<Person>();
		FieldSetMapper<Person> personFieldSetMapper = new PersonFieldSetMapper();
		LOGGER.info("Configuring reader to input {}", input);
		return reader.name("personItemReader").resource(new FileSystemResource(input)).delimited().names(TOKENS)
				.fieldSetMapper(personFieldSetMapper).build();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public JsonFileItemWriter<Person> jsonItemWriter(@Value("#{jobParameters['file.output']}") String output) {
		JsonFileItemWriterBuilder<Person> builder = new JsonFileItemWriterBuilder<Person>();
		JacksonJsonObjectMarshaller<Person> marshaller = new JacksonJsonObjectMarshaller<Person>();
		LOGGER.info("Configuring reader to input {}", output);
		return builder.name("personItemWriter").jsonObjectMarshaller(marshaller)
				.resource(new FileSystemResource(output)).build();
	}

	@Bean
	public Step step1(ItemReader<Person> itemReader, ItemWriter<Person> itemWriter) {
		return stepBuilderFactory.get("step1").<Person, Person>chunk(3).reader(itemReader).processor(processor())
				.writer(itemWriter).build();
	}

	@Bean
	public Step step2(ItemReader<Person> itemReader, ItemWriter<Person> itemWriter) {
		return stepBuilderFactory.get("step2").<Person, Person>chunk(3).reader(itemReader).processor(processor())
				.writer(itemWriter).build();
	}

	@Bean(name = "importPersonJob1")
	public Job importPersonJob1(Step step1, Step step2) throws IOException {
		return jobBuilderFactory.get("importPersonJob1").flow(step1).next(step2).end().build();
	}
}
