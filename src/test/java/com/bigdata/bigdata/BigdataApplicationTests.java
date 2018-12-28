package com.bigdata.bigdata;

import com.bigdata.bigdata.common.HadoopFileSystemUtil;
import com.bigdata.bigdata.common.HbaseUtil;
import com.bigdata.bigdata.common.RedisUtils;
import net.sf.json.JSONArray;
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
            hbaseUtil.insertRow("t_user", "3", "info", "userId", "3");
            hbaseUtil.insertRow("t_user", "3", "info", "userName", "zk");
            hbaseUtil.getData("t_user", "3", null, null);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean insertRow(String tableName, String rowkey, String colFamily, String col, String val) throws Exception{
        TableName tabName = TableName.valueOf(tableName);
        if(!admin.tableExists(tabName)){
            return false;
        }

        Table tabl =  connection.getTable(tabName);

        Put put = new Put(rowkey.getBytes());
        put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
        tabl.put(put);
        tabl.close();
//        TableName tabName = TableName.valueOf(tableName);
//        if(!admin.tableExists(tabName)){
//            return false;
//        }
//        Table tabl =  connection.getTable(tabName);
//
//        Put put = new Put(rowkey.getBytes());
//        put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
//        tabl.put(put);
//        tabl.close();
        return true;
    }

}

