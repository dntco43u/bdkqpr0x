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
@ConfigurationProperties(prefix = "fhy8vp3u.job.bd04")
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
  public Job bd04(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) {
    return jobBuilderFactory
    .get("bd04")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepBD04(stepListener, null, null))    
    //.next(gvp6nx1aToCqa7wtjgStepBD04(stepListener, null)) // XXX: DB -> DB processing without file processing, current state is File -> DB -> File 
    .next(gvp6nx1aToFileStepBD04(stepListener, null))
    .next(fileToCqa7wtjgStepBD04(stepListener, null))
    .next(deleteGvp6nx1aStepBD04(stepListener))
    .next(closeStepBD04(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepBD04(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("initStepBD04")
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
  public Step deleteGvp6nx1aStepBD04(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("deleteGvp6nx1aStepBD04")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionGvp6nx1a.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperGvp6nx1a.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step gvp6nx1aToCqa7wtjgStepBD04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("gvp6nx1aToCqa7wtjgStepBD04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(gvp6nx1aSelect001ReaderBD04(null))
    .processor(new ItemProcessorBD04())
    .writer(cqa7wtjgInsert001WriterBD04())
    .build();
  }

  @Bean
  @JobScope
  public Step gvp6nx1aToFileStepBD04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("gvp6nx1aToFileStepBD04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(gvp6nx1aSelect001ReaderBD04(null))
    .processor(new ItemProcessorBD04())
    .writer(fileWriterBD04())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToCqa7wtjgStepBD04(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("fileToCqa7wtjgStepBD04")
    .listener(stepListener)
    .<SystemEventsDTO, SystemEventsDTO>chunk(chunkSize)
    .reader(fileReaderBD04())
    .processor(new ItemProcessorBD04())
    .writer(cqa7wtjgInsert001WriterBD04())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepBD04(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("closeStepBD04")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionCqa7wtjg.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperCqa7wtjg.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }

  @StepScope
  public class ItemProcessorBD04 implements ItemProcessor<SystemEventsDTO, SystemEventsDTO> {
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
  public MyBatisPagingItemReader<SystemEventsDTO> gvp6nx1aSelect001ReaderBD04(@Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return new MyBatisPagingItemReaderBuilder<SystemEventsDTO>()
    .sqlSessionFactory(sqlSessionFactoryGvp6nx1a)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperGvp6nx1a.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<SystemEventsDTO> cqa7wtjgInsert001WriterBD04() {
    return new MyBatisBatchItemWriterBuilder<SystemEventsDTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr04MapperCqa7wtjg.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<SystemEventsDTO> fileReaderBD04() {    
    return new FlatFileItemReaderBuilder<SystemEventsDTO>()    
    .name("fileReaderBD04")
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
  public FlatFileItemWriter<SystemEventsDTO> fileWriterBD04() {
    return new FlatFileItemWriterBuilder<SystemEventsDTO>()
    .name("fileWriterBD04")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<SystemEventsDTO>() {{
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<SystemEventsDTO>() {{
            setNames(csvFileHeader);
          }});
      }})  
    .build();
  }
}