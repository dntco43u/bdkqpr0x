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

import com.fhy8vp3u.bdkqpr0x.domain.SystemEventsDTO;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xJobListener;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xStepListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.job.b04")
@Setter
public class Bdkqpr04JobConfig {
  @Autowired
  public JobBuilderFactory jobBuilderFactory;
  @Autowired
  public StepBuilderFactory stepBuilderFactory;
  @Autowired
  @Qualifier("sqlSessionFactoryCqa7wtjg")
  public SqlSessionFactory sqlSessionFactoryCqa7wtjg;
  @Autowired
  @Qualifier("sqlSessionFactoryEc4mrjp5")
  public SqlSessionFactory sqlSessionFactoryEc4mrjp5;
  @Autowired
  @Qualifier("sqlSessionTemplateCqa7wtjg")
  protected SqlSession sqlSessionCqa7wtjg;
  @Autowired
  @Qualifier("sqlSessionTemplateEc4mrjp5")
  protected SqlSession sqlSessionEc4mrjp5;  
  private String csvFile;
  private String csvFileEncoding;
  private String[] csvFileHeader;
  private String csvFileDelimeter = "█";
  private String user = "bdkqpr0x";
  
  @Bean
  public Job b04(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) throws Exception {
    return jobBuilderFactory
    .get("b04")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepB04(stepListener, null, null))    
    //.next(ec4mrjp5ToCqa7wtjgStepB04(stepListener, null)) // XXX: 파일 처리 없이 DB -> DB
    .next(ec4mrjp5ToFileStepB04(stepListener, null))
    .next(fileToCqa7wtjgStepB04(stepListener, null))
    .next(deleteEc4mrjp5StepB04(stepListener))
    .next(closeStepB04(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepB04(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("initStepB04")
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
  public Step deleteEc4mrjp5StepB04(Bdkqpr0xStepListener stepListener) throws Exception {
    return stepBuilderFactory
    .get("deleteEc4mrjp5StepB04")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionEc4mrjp5.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperEc4mrjp5.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step ec4mrjp5ToCqa7wtjgStepB04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("ec4mrjp5ToCqa7wtjgStepB04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(ec4mrjp5Select001ReaderB04(null))
    .processor(new ItemProcessorB04())
    .writer(cqa7wtjgInsert001WriterB04())
    .build();
  }

  @Bean
  @JobScope
  public Step ec4mrjp5ToFileStepB04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("ec4mrjp5ToFileStepB04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(ec4mrjp5Select001ReaderB04(null))
    .processor(new ItemProcessorB04())
    .writer(fileWriterB04())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToCqa7wtjgStepB04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return stepBuilderFactory
    .get("fileToCqa7wtjgStepB04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(fileReaderB04())
    .processor(new ItemProcessorB04())
    .writer(cqa7wtjgInsert001WriterB04())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepB04(Bdkqpr0xStepListener stepListener) throws Exception {
    return stepBuilderFactory
    .get("closeStepB04")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionCqa7wtjg.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperCqa7wtjg.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }

  @StepScope
  public class ItemProcessorB04 implements ItemProcessor<SystemEventsDTO, SystemEventsDTO> {
    @Override
    public SystemEventsDTO process(SystemEventsDTO item) throws Exception {
      log.debug(
      "customerId={}, receivedAt={}, deviceReportedTime={}, facility={}, priority={}, fromHost={}, message={}, ntSeverity={}, importance={}, eventSource={}, eventUser={}, eventCategory={}, eventId={}, eventBinaryData={}, maxAvailable={}, currUsage={}, minUsage={}, maxUsage={}, infoUnitId={}, sysLogTag={}, eventLogType={}, genericFileName={}, systemId={}",
      item.getCustomerId(), item.getReceivedAt(), item.getDeviceReportedTime(), item.getFacility(), item.getPriority(),
      item.getFromHost(), item.getMessage(), item.getNtSeverity(), item.getImportance(), item.getEventSource(),
      item.getEventUser(), item.getEventCategory(), item.getEventId(), item.getEventBinaryData(),
      item.getMaxAvailable(), item.getCurrUsage(), item.getMinUsage(), item.getMaxUsage(), item.getInfoUnitId(),
      item.getSysLogTag(), item.getEventLogType(), item.getGenericFileName(), item.getSystemId());
      item.setCreateUser(user);
      item.setUpdateUser(user);
      return item;
    }
  }

  @Bean
  @StepScope
  public MyBatisPagingItemReader<SystemEventsDTO> ec4mrjp5Select001ReaderB04(@Value("#{jobParameters[chunkSize]}") Integer chunkSize) throws Exception {
    return new MyBatisPagingItemReaderBuilder<SystemEventsDTO>()
    .sqlSessionFactory(sqlSessionFactoryEc4mrjp5)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperEc4mrjp5.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<SystemEventsDTO> cqa7wtjgInsert001WriterB04() {
    return new MyBatisBatchItemWriterBuilder<SystemEventsDTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperCqa7wtjg.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<SystemEventsDTO> fileReaderB04() {    
    return new FlatFileItemReaderBuilder<SystemEventsDTO>()    
    .name("fileReaderB04")
    .encoding(csvFileEncoding)
    .linesToSkip(0)
    .resource(new FileSystemResource(csvFile))
    .delimited()
    .delimiter(csvFileDelimeter)
    .names(csvFileHeader)
    .fieldSetMapper(new BeanWrapperFieldSetMapper<SystemEventsDTO>() {{
        setTargetType(SystemEventsDTO.class);
    }})
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemWriter<SystemEventsDTO> fileWriterB04() {
    return new FlatFileItemWriterBuilder<SystemEventsDTO>()
    .name("fileWriterB04")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<SystemEventsDTO>() {
      {
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<SystemEventsDTO>() {
          {
            setNames(csvFileHeader);
          }
        });
      }
    })  
    .build();
  }
}