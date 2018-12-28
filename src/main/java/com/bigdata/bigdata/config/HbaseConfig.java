package com.bigdata.bigdata.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * hbase配置文件
 */
@Configuration
@EnableAutoConfiguration
public class HbaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);


    @Value("${hbase.zk.clientPort}")
    private Integer clientPort;

    @Value("${hbase.zk.quorum}")
    private String quorum;

    @Value("${hbase.master}")
    private String master;

    @Bean
    public Connection getConnection(){
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        Connection connection = null;
        try{
            configuration.set("hbase.zookeeper.property.clientPort", clientPort.toString());
            configuration.set("hbase.zookeeper.quorum", quorum);
            configuration.set("hbase.master", master);
            connection = ConnectionFactory.createConnection(configuration);
            return connection;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Bean
    public Admin getAdmin(Connection connection){
        try{
            return connection.getAdmin();
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
