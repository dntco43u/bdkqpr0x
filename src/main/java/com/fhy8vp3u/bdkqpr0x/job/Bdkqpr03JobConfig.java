package com.fhy8vp3u.bdkqpr0x.job;

import com.fhy8vp3u.bdkqpr0x.common.BdDelimitedLineTokenizer;
import com.fhy8vp3u.bdkqpr0x.domain.Us500DTO;
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

import com.fhy8vp3u.bdkqpr0x.domain.MusicTagDTO;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xJobListener;
import com.fhy8vp3u.bdkqpr0x.listener.Bdkqpr0xStepListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.job.bd03")
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
  public Job bd03(Bdkqpr0xJobListener jobListener, Bdkqpr0xStepListener stepListener) {
    return jobBuilderFactory
    .get("bd03")
    .listener(jobListener)
    .incrementer(new RunIdIncrementer())
    .start(initStepBD03(stepListener, null, null))    
    .next(deleteCqa7wtjgStepBD03(stepListener))
    .next(cqa7wtjgToCqa7wtjgStepBD03(stepListener, null))
    .next(deleteCqa7wtjgStepBD03(stepListener))
    .next(cqa7wtjgToFileStepBD03(stepListener, null))
    .next(fileToCqa7wtjgStepBD03(stepListener, null))
    .next(closeStepBD03(stepListener))
    .build();
  }
  
  @Bean
  @JobScope
  public Step initStepBD03(Bdkqpr0xStepListener stepListener, @Value("#{jobParameters[requestDate]}") String requestDate,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("initStepBD03")
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
  public Step deleteCqa7wtjgStepBD03(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("deleteCqa7wtjgStepBD03")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      this.sqlSessionCqa7wtjg.delete("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.delete001");
      return RepeatStatus.FINISHED;
    })
    .build();
  }
  
  @Bean
  @JobScope
  public Step cqa7wtjgToCqa7wtjgStepBD03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("cqa7wtjgToCqa7wtjgStepBD03")
    .listener(stepListener)
    .<MusicTagDTO, MusicTagDTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderBD03(null))
    .processor(new ItemProcessorBD03())
    .writer(cqa7wtjgInsert001WriterBD03())
    .build();
  }

  @Bean
  @JobScope
  public Step cqa7wtjgToFileStepBD03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("cqa7wtjgToFileStepBD03")
    .listener(stepListener)
    .<MusicTagDTO, MusicTagDTO>chunk(chunkSize)
    .reader(cqa7wtjgSelect001ReaderBD03(null))
    .processor(new ItemProcessorBD03())    
    .writer(fileWriterBD03())
    .build();
  }
  
  @Bean
  @JobScope
  public Step fileToCqa7wtjgStepBD03(Bdkqpr0xStepListener stepListener,
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return stepBuilderFactory
    .get("fileToCqa7wtjgStepBD03")
    .listener(stepListener)
    .<MusicTagDTO, MusicTagDTO>chunk(chunkSize)
    .reader(fileReaderBD03())
    .processor(new ItemProcessorBD03())
    .writer(cqa7wtjgInsert001WriterBD03())
    .build();
  }
  
  @Bean
  @JobScope
  public Step closeStepBD03(Bdkqpr0xStepListener stepListener) {
    return stepBuilderFactory
    .get("closeStepBD03")
    .listener(stepListener)
    .tasklet((contribution, chunkContext) -> {
      Integer count = this.sqlSessionCqa7wtjg.selectOne("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.select003");
      log.info("count={}", count);
      return RepeatStatus.FINISHED;
    })
    .build();
  }

  @StepScope
  public class ItemProcessorBD03 implements ItemProcessor<MusicTagDTO, MusicTagDTO> {
    @Override
    public MusicTagDTO process(MusicTagDTO item) throws Exception {
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
  public MyBatisPagingItemReader<MusicTagDTO> cqa7wtjgSelect001ReaderBD03(
  @Value("#{jobParameters[chunkSize]}") Integer chunkSize) {
    return new MyBatisPagingItemReaderBuilder<MusicTagDTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .pageSize(chunkSize)
    .queryId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.select001")
    .build();
  }
  
  @Bean
  @StepScope
  public MyBatisBatchItemWriter<MusicTagDTO> cqa7wtjgInsert001WriterBD03() {
    return new MyBatisBatchItemWriterBuilder<MusicTagDTO>()
    .sqlSessionFactory(sqlSessionFactoryCqa7wtjg)
    .statementId("com.fhy8vp3u.bdkqpr0x.mapper.Bdkqpr03MapperCqa7wtjg.insert001")
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemReader<MusicTagDTO> fileReaderBD03() {
    return new FlatFileItemReaderBuilder<MusicTagDTO>()
    .name("fileReaderBD03")
    .resource(new FileSystemResource(csvFile))
    .encoding(csvFileEncoding)
    .linesToSkip(0)
    .lineMapper(new DefaultLineMapper<MusicTagDTO>() {{
      setLineTokenizer(new BdDelimitedLineTokenizer(csvFileDelimeter) {{
        setNames(csvFileHeader);
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<MusicTagDTO>() {{
        setTargetType(MusicTagDTO.class);
      }});
    }})
    .build();
  }
  
  @Bean
  @StepScope
  public FlatFileItemWriter<MusicTagDTO> fileWriterBD03() {
    return new FlatFileItemWriterBuilder<MusicTagDTO>()
    .name("fileWriterBD03")
    .encoding(csvFileEncoding)
    .resource(new FileSystemResource(csvFile))
    .append(false)
    .lineAggregator(new DelimitedLineAggregator<MusicTagDTO>() {{
        setDelimiter(csvFileDelimeter);
        setFieldExtractor(new BeanWrapperFieldExtractor<MusicTagDTO>() {{
            setNames(csvFileHeader);
          }});
      }})  
    .build();
  }
}