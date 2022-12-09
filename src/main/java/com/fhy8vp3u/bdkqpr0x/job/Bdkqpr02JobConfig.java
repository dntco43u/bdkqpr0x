package com.fhy8vp3u.bdkqpr0x.job;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.fhy8vp3u.bdkqpr0x.domain.Bdkqpr01DTO;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xJobListener;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xStepListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.job.b02")
@Setter
public class Bdkqpr02JobConfig {
  @Autowired
  public JobBuilderFactory jobBuilderFactory;
  @Autowired
  public StepBuilderFactory stepBuilderFactory;
  @Autowired
  @Qualifier("sqlSessionFactoryCqa7wtjg")
  public SqlSessionFactory sqlSessionFactoryCqa7wtjg;
  @Autowired
  @Qualifier("sqlSessionFactoryGvp6nx1a")
  public SqlSessionFactory sqlSessionFactoryGvp6nx1a;
  @Autowired
  @Qualifier("sqlSessionTemplateCqa7wtjg")
  protected SqlSession sqlSessionCqa7wtjg;
  @Autowired
  @Qualifier("sqlSessionTemplateGvp6nx1a")
  protected SqlSession sqlSessionGvp6nx1a;
  private String csvFile;
  private String csvFileEncoding;
  private String[] csvFileHeader;
  private String csvFileDelimeter = "█";
  private String user = "bdkqpr0x";

  @Bean
  public Job b02(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) throws Exception {
    return jobBuilderFactory
    .get("b02")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepB02(stepListener, null, null))    
    .next(deleteGvp6nx1aStepB02(stepListener))
    .next(cqa7wtjgToGvp6nx1aStepB02(stepListener, null))
    .next(deleteGvp6nx1aStepB02(stepListener))
    .next(cqa7wtjgToFileStepB02(stepListener, null))
    .next(fileToGvp6nx1aStepB02(stepListener, null))
    .next(closeStepB02(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepB02(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("initStepB02")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      log.info("requestDate={}, chunkSize={}", requestDate, chunkSize);
      log.info("csvFile={}, csvFileEncoding={}, csvFileHeader={}", csvFile, csvFileEncoding, csvFileHeader);
      log.info("csvFileDelimeter={}, user={}", csvFileDelimeter, user);
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step deleteGvp6nx1aStepB02(Bdkqpr0xStepListener stepListener) throws Exception {
    return stepBuilderFactory
    .get("deleteGvp6nx1aStepB02")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionGvp6nx1a.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperGvp6nx1a.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step cqa7wtjgToGvp6nx1aStepB02(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("cqa7wtjgToGvp6nx1aStepB02")
    .listener(stepListener)
    .<Bdkqpr01DTO, Bdkqpr01DTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderB02(null))
    .processor(new ItemProcessorB02())
    .writer(gvp6nx1aInsert001WriterB02())
    .build();
  }
  
  @Bean
  @JobScope
  public Step cqa7wtjgToFileStepB02(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("cqa7wtjgToFileStepB02")
    .listener(stepListener)
    .<Bdkqpr01DTO, Bdkqpr01DTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderB02(null))
    .processor(new ItemProcessorB02())
    .writer(fleWriterB02())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToGvp6nx1aStepB02(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("fileToGvp6nx1aStepB02")
    .listener(stepListener)
    .<Bdkqpr01DTO, Bdkqpr01DTO>chunk(chunkSize)
    .reader(fileReaderB02())
    .processor(new ItemProcessorB02())
    .writer(gvp6nx1aInsert001WriterB02())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepB02(Bdkqpr0xStepListener stepListener) throws Exception {
    return stepBuilderFactory
    .get("closeStepB02")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionGvp6nx1a.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperGvp6nx1a.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @StepScope
  public class ItemProcessorB02 implements ItemProcessor<Bdkqpr01DTO, Bdkqpr01DTO> {
    @Override
    public Bdkqpr01DTO process(Bdkqpr01DTO item) throws Exception {
      log.debug(
      "employeeFirstName={}, employeeLastName={}, companyName={}, companyAddress={}, companyCity={}, companyCounty={}, companyState={}, companyZip={}, companyPhone1={}, personalPhone2={}, personalEmail={}, companyWeb={}",
      item.getEmployeeFirstName(), item.getEmployeeLastName(), item.getCompanyName(), item.getCompanyAddress(),
      item.getCompanyCity(), item.getCompanyCounty(), item.getCompanyState(), item.getCompanyZip(),
      item.getCompanyPhone1(), item.getPersonalPhone2(), item.getPersonalEmail(), item.getCompanyWeb());
      item.setCreateUser(user);
      item.setUpdateUser(user);
      return item;
    }
  }
  
  @Bean
  @StepScope
  public MyBatisPagingItemReader<Bdkqpr01DTO> cqa7wtjgSelect001ReaderB02(
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return new MyBatisPagingItemReaderBuilder<Bdkqpr01DTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperCqa7wtjg.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<Bdkqpr01DTO> gvp6nx1aInsert001WriterB02() {
    return new MyBatisBatchItemWriterBuilder<Bdkqpr01DTO>()
    .sqlSessionFactory(sqlSessionFactoryGvp6nx1a)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperGvp6nx1a.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<Bdkqpr01DTO> fileReaderB02() {    
    return new FlatFileItemReaderBuilder<Bdkqpr01DTO>()    
    .name("fileReaderB02")
    .encoding(csvFileEncoding)
    .linesToSkip(0)
    .resource(new FileSystemResource(csvFile))
    .delimited()
    .delimiter(csvFileDelimeter)
    .names(csvFileHeader)
    .fieldSetMapper(new BeanWrapperFieldSetMapper<Bdkqpr01DTO>() {{
        setTargetType(Bdkqpr01DTO.class);
    }})
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemWriter<Bdkqpr01DTO> fleWriterB02() {
    return new FlatFileItemWriterBuilder<Bdkqpr01DTO>()
    .name("fleWriterB02")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<Bdkqpr01DTO>() {
      {
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<Bdkqpr01DTO>() {
          {
            setNames(csvFileHeader);
          }
        });
      }
    })  
    .build();
  }
}