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

import com.fhy8vp3u.bdkqpr0x.domain.Bdkqpr03DTO;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xJobListener;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xStepListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.job.b03")
@Setter
public class Bdkqpr03JobConfig {
  @Autowired
  public JobBuilderFactory jobBuilderFactory;
  @Autowired
  public StepBuilderFactory stepBuilderFactory;
  @Autowired
  @Qualifier("sqlSessionFactoryCqa7wtjg")
  public SqlSessionFactory sqlSessionFactoryCqa7wtjg;
  @Autowired
  @Qualifier("sqlSessionTemplateCqa7wtjg")
  protected SqlSession sqlSessionCqa7wtjg;
  private String csvFile;
  private String csvFileEncoding;
  private String[] csvFileHeader;
  private String csvFileDelimeter = "█";
  private String user = "bdkqpr0x";

  @Bean
  public Job b03(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) {
    return jobBuilderFactory
    .get("b03")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepB03(stepListener, null, null))    
    .next(deleteCqa7wtjgStepB03(stepListener))
    .next(cqa7wtjgToCqa7wtjgStepB03(stepListener, null))
    .next(deleteCqa7wtjgStepB03(stepListener))
    .next(cqa7wtjgToFileStepB03(stepListener, null))
    .next(fileToCqa7wtjgStepB03(stepListener, null))
    .next(closeStepB03(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepB03(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("initStepB03")
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
  public Step deleteCqa7wtjgStepB03(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("deleteCqa7wtjgStepB03")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionCqa7wtjg.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step cqa7wtjgToCqa7wtjgStepB03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("cqa7wtjgToCqa7wtjgStepB03")
    .listener(stepListener)
    .<Bdkqpr03DTO, Bdkqpr03DTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderB03(null))
    .processor(new ItemProcessorB03())
    .writer(cqa7wtjgInsert001WriterB03())
    .build();
  }

  @Bean
  @JobScope
  public Step cqa7wtjgToFileStepB03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("cqa7wtjgToFileStepB03")
    .listener(stepListener)
    .<Bdkqpr03DTO, Bdkqpr03DTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderB03(null))
    .processor(new ItemProcessorB03())    
    .writer(fileWriterB03())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToCqa7wtjgStepB03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("fileToCqa7wtjgStepB03")
    .listener(stepListener)
    .<Bdkqpr03DTO, Bdkqpr03DTO>chunk(chunkSize)
    .reader(fileReaderB03())
    .processor(new ItemProcessorB03())
    .writer(cqa7wtjgInsert001WriterB03())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepB03(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("closeStepB03")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionCqa7wtjg.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }

  @StepScope
  public class ItemProcessorB03 implements ItemProcessor<Bdkqpr03DTO, Bdkqpr03DTO> {
    @Override
    public Bdkqpr03DTO process(Bdkqpr03DTO item) throws Exception {
      log.debug(
      "disc={}, totaldiscs={}, track={}, totaltrack={}, year={}, album={}, contentgroup={}, title={}, albumartist={}, artist={}, publisher={}, genre={}, mediatype={}",
      item.getDisc(), item.getTotaldiscs(), item.getTrack(), item.getTotaltracks(), item.getYear(), item.getAlbum(),
      item.getContentgroup(), item.getTitle(), item.getAlbumartist(), item.getArtist(), item.getPublisher(),
      item.getGenre(), item.getMediatype());
      item.setCreateUser(user);
      item.setUpdateUser(user);
      return item;
    }
  }

  @Bean
  @StepScope
  public MyBatisPagingItemReader<Bdkqpr03DTO> cqa7wtjgSelect001ReaderB03(
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return new MyBatisPagingItemReaderBuilder<Bdkqpr03DTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<Bdkqpr03DTO> cqa7wtjgInsert001WriterB03() {
    return new MyBatisBatchItemWriterBuilder<Bdkqpr03DTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<Bdkqpr03DTO> fileReaderB03() {    
    return new FlatFileItemReaderBuilder<Bdkqpr03DTO>()    
    .name("fileReaderB03")
    .encoding(csvFileEncoding)
    .linesToSkip(0)
    .resource(new FileSystemResource(csvFile))
    .delimited()
    .delimiter(csvFileDelimeter)
    .names(csvFileHeader)
    .fieldSetMapper(new BeanWrapperFieldSetMapper<Bdkqpr03DTO>() {{
        setTargetType(Bdkqpr03DTO.class);
    }})
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemWriter<Bdkqpr03DTO> fileWriterB03() {
    return new FlatFileItemWriterBuilder<Bdkqpr03DTO>()
    .name("fileWriterB03")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<Bdkqpr03DTO>() {{
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<Bdkqpr03DTO>() {{
            setNames(csvFileHeader);
          }});
      }})  
    .build();
  }
}