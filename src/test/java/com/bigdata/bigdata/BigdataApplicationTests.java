package com.bigdata.bigdata;

import com.bigdata.bigdata.common.HadoopFileSystemUtil;
import com.bigdata.bigdata.common.HbaseUtil;
import com.bigdata.bigdata.common.RedisUtils;
import com.bigdata.bigdata.entity.UcUserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.ByteBufferKeyOnlyKeyValue;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
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
    private HadoopFileSystemUtil hadoopSystemFileSystem;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HbaseUtil hbaseUtil;

    @Autowired
    private Admin admin;

    @Autowired
    private Connection connection;

//    @Test
//    public void contextLoads() {
//        List<Object> list = redisUtils.lRange("KLine-15min:1",0L,-1L);
//        hadoopSystemFileSystem.mkFile("/usr/sbin/hadoop/data/KLine/15min-1");
//        hadoopSystemFileSystem.writerString(JSONArray.fromObject(list).toString(),"/usr/sbin/hadoop/data/KLine/15min-1");
//    }

    @Test
    public void contextLoads() {
        try{
//            hbaseUtil.listTab();
//            UcUserInfo ucUserInfo = new UcUserInfo();
//            ucUserInfo.setUcUserId(1L);
//            ucUserInfo.setUserName("yinx");
//            ucUserInfo.setEmail("287804524@qq.com");
//            ucUserInfo.setAddress("北京");
//            hbaseUtil.insertRow(ucUserInfo);
//            UcUserInfo ucUserInfo1 = new UcUserInfo();
//            ucUserInfo1.setUcUserId(2L);
//            ucUserInfo1.setUserName("gaoy");
//            ucUserInfo1.setEmail("15510099005");
//            ucUserInfo1.setAddress("武汉");
//            hbaseUtil.insertRow(ucUserInfo1);
            long startTime = System.currentTimeMillis();
//            UcUserInfo ucUserInfo = hbaseUtil.getByRow(UcUserInfo.class, "3");
//            System.out.println(JSONObject.fromObject(ucUserInfo).toString());

            List<UcUserInfo> list = hbaseUtil.getList(UcUserInfo.class, "ucUserId", "1");
            System.out.println(JSONArray.fromObject(list).toString());
            long endTime = System.currentTimeMillis();
            System.out.println("运行时间:" + (endTime - startTime) + "ms");
//            hbaseUtil.delTab("UcUserInfo");

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}

