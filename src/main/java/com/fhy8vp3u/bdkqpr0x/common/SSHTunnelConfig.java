package com.fhy8vp3u.bdkqpr0x.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fhy8vp3u.ssh")
@Setter
public class SSHTunnelConfig {
  private String user;
  private String pemKey;
  private String remoteServer;
  private int remotePort;
  private String tunnelRemoteServer;
  private int tunnelRemotePort;
  private static JSch jsch = new JSch();
  private Session session;

  public Map<String, String> init() {    
    int forwardedPort = 0;
    try {
      jsch.addIdentity(pemKey);
      session = jsch.getSession(user, remoteServer, remotePort);
      // XXX: 추후 host key 지정 위치 찾아서 변경한 후 삭제할 것
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();
      forwardedPort = session.setPortForwardingL(0, tunnelRemoteServer, tunnelRemotePort);
      log.info("connected {}:{}@{}:{} -> forwardedPort:{} -> {}:{}", user, pemKey, remoteServer, remotePort,
      forwardedPort, tunnelRemoteServer, tunnelRemotePort);
    } catch (Exception e) {
      log.error("", e);
      this.destory();
      System.exit(-1);
    }
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("forwardedPort", String.valueOf(forwardedPort));
    paramMap.put("tunnelRemotePort", String.valueOf(tunnelRemotePort));
    return paramMap;
  }

  @PreDestroy
  public void destory() {
    if (session != null && session.isConnected()) {
      session.disconnect();
    }
  }
}
