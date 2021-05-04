//package com.demo.batchprocessing;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//@Configuration
//@EnableBatchProcessing
//public class BatchConfiguration {
//	@Autowired
//	StepBuilderFactory stepBuilderFactory;
//	@Autowired
//	JobBuilderFactory jobBuilderFactory;
//
//	@Bean
//	public DataSource dataSource() {
//		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://localhost:3306/batch_example");
//		dataSource.setUsername("root");
//		dataSource.setPassword("123456");
//		return dataSource;
//	}
//
//	@Bean
//	public FlatFileItemReader<Person> reader() {
//		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
//		reader.setResource(new ClassPathResource("data.csv"));
//		reader.setLineMapper(new DefaultLineMapper<Person>() {
//		 	{
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setNames(new String[] { "firstName", "lastName" });
//					}
//				});
//				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
//					{
//						setTargetType(Person.class);
//					}
//				});
//			}
//		});
//		return reader;
//	}
//
//	@Bean
//	public PersonItemProcessor processor() {
//		return new PersonItemProcessor();
//	}
//
//	@Bean
//	public JdbcBatchItemWriter<Person> writer(@Autowired DataSource datasource) {
//		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
//		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
//		writer.setSql("INSERT INTO person (first_name, last_name) VALUES (:firstName, :lastName)");
//		writer.setDataSource(datasource);
//		return writer;
//	}
//
//	@Bean("step6")
//	public Step step6() {
//		return stepBuilderFactory.get("step6").<Person, Person>chunk(50).reader(reader()).processor(processor())
//				.writer(writer(dataSource())).build();
//	}
//
//	@Bean
//	public Job importPersonJob(JobCompletionNotificationListener listener, @Qualifier("step6") Step step6) {
//		return jobBuilderFactory.get("importPersonJob").incrementer(new RunIdIncrementer()).listener(listener)
//				.flow(step6).end().build();
//	}
//}
