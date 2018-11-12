package com.bigdata.bigdata.config;


import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;


/**
 * hadoop配置
 */
@Configuration
@EnableAutoConfiguration
public class HadoopConfig{
    private static final Logger logger = LoggerFactory.getLogger(HadoopConfig.class);


    @Value("${hadoop.uri}")
    private String uri;

    @Value("${hadoop.appendFile}")
    private boolean appendFile;


    @Bean
    public org.apache.hadoop.conf.Configuration Configuration(){
        org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
        config.setBoolean("dfs.support.append",appendFile);
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        config.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        return config;
    }

    @Bean
    public FileSystem FileSystem(){
        FileSystem fileSystem = null;
        try{
            URI url = new URI(uri);
            fileSystem = FileSystem.get(url,Configuration(),"hadoop");
            fileSystem.getStatus();
        }catch (Exception e) {
            logger.error("连接Hadoop文件系统失败！");
            logger.error(e.getMessage());
        }
        return fileSystem;
    }


}
