package com.fhy8vp3u.bdkqpr0x.common;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public class BdDelimitedLineTokenizer extends DelimitedLineTokenizer {
  public BdDelimitedLineTokenizer(String d) {
    super(d);
  }

  @Override
  protected boolean isQuoteCharacter(char c) {
    return false;
  }
}