package com.bigdata.bigdata;

import com.bigdata.bigdata.common.HadoopFileSystemUtil;
import com.bigdata.bigdata.common.HbaseUtil;
import com.bigdata.bigdata.common.RedisUtils;
import com.bigdata.bigdata.entity.UcUserInfo;
import net.sf.json.JSONArray;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HadoopTest {

    @Autowired
    private HadoopFileSystemUtil hadoopSystemFileSystem;

    @Test
    public void contextLoads() {




    }

}

