package com.bigdata.bigdata;

import com.bigdata.bigdata.common.hadoop.HadoopSystemFileSystem;
import com.bigdata.bigdata.common.redis.RedisUtils;
import net.sf.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BigdataApplicationTests {

    @Autowired
    private HadoopSystemFileSystem hadoopSystemFileSystem;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void contextLoads() {
        List<Object> list = redisUtils.lRange("KLine-15min:1",0L,-1L);
        hadoopSystemFileSystem.mkFile("/usr/sbin/hadoop/data/KLine/15min-1");
        hadoopSystemFileSystem.writerString(JSONArray.fromObject(list).toString(),"/usr/sbin/hadoop/data/KLine/15min-1");
    }

}
