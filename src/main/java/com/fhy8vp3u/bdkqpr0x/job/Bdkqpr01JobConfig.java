package com.fhy8vp3u.bdkqpr0x.job;

import com.fhy8vp3u.bdkqpr0x.common.BdDelimitedLineTokenizer;
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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.fhy8vp3u.bdkqpr0x.domain.Us500DTO;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xJobListener;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xStepListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.job.bd01")
@Setter
public class Bdkqpr01JobConfig {
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
  public Job bd01(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) throws Exception {
    return jobBuilderFactory
    .get("bd01")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepBD01(stepListener, null, null))
    .next(deleteCqa7wtjgStepBD01(stepListener))
    .next(gvp6nx1aToCqa7wtjgStepBD01(stepListener, null))
    .next(deleteCqa7wtjgStepBD01(stepListener))
    .next(gvp6nx1aToFileStepBD01(stepListener, null))
    .next(fileToCqa7wtjgStepBD01(stepListener, null))
    .next(closeStepBD01(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepBD01(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("initStepBD01")
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
  public Step deleteCqa7wtjgStepBD01(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("deleteCqa7wtjgStepBD01")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionCqa7wtjg.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperCqa7wtjg.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step gvp6nx1aToCqa7wtjgStepBD01(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("gvp6nx1aToCqa7wtjgStepBD01")
    .listener(stepListener)
    .<Us500DTO, Us500DTO>chunk(chunkSize)
    .reader(gvp6nx1aSelect001ReaderBD01(null))
    .processor(new ItemProcessorBD01())
    .writer(cqa7wtjgInsert001WriterBD01())
    .build();
  }

  @Bean
  @JobScope
  public Step gvp6nx1aToFileStepBD01(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("gvp6nx1aToFileStepBD01")
    .listener(stepListener)
    .<Us500DTO, Us500DTO>chunk(chunkSize)
    .reader(gvp6nx1aSelect001ReaderBD01(null))
    .processor(new ItemProcessorBD01())
    .writer(fileWriterBD01())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToCqa7wtjgStepBD01(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("fileToCqa7wtjgStepBD01")
    .listener(stepListener)
    .<Us500DTO, Us500DTO>chunk(chunkSize)
    .reader(fileReaderBD01())
    .processor(new ItemProcessorBD01())
    .writer(cqa7wtjgInsert001WriterBD01())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepBD01(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("closeStepBD01")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionCqa7wtjg.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperCqa7wtjg.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }

  @StepScope
  public class ItemProcessorBD01 implements ItemProcessor<Us500DTO, Us500DTO> {
    @Override
    public Us500DTO process(Us500DTO item) {
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
                                                                                                                                              public MyBatisPagingItemReader<Us500DTO> gvp6nx1aSelect001ReaderBD01(
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return new MyBatisPagingItemReaderBuilder<Us500DTO>()
    .sqlSessionFactory(sqlSessionFactoryGvp6nx1a)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperGvp6nx1a.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<Us500DTO> cqa7wtjgInsert001WriterBD01() {
    return new MyBatisBatchItemWriterBuilder<Us500DTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr01MapperCqa7wtjg.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<Us500DTO> fileReaderBD01() {
    return new FlatFileItemReaderBuilder<Us500DTO>()
    .name("fileReaderBD01")
    .resource(new FileSystemResource(csvFile))
    .encoding(csvFileEncoding)
    .linesToSkip(0)
    .lineMapper(new DefaultLineMapper<Us500DTO>() {{
      setLineTokenizer(new BdDelimitedLineTokenizer(csvFileDelimeter) {{
        setNames(csvFileHeader);
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<Us500DTO>() {{
        setTargetType(Us500DTO.class);
      }});
    }})
    .build();
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<Us500DTO> fileWriterBD01() {
    return new FlatFileItemWriterBuilder<Us500DTO>()
    .name("fileWriterBD01")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<Us500DTO>() {{
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<Us500DTO>() {{
            setNames(csvFileHeader);
          }
        });
      }})  
    .build();
  }
}