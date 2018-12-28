package com.bigdata.bigdata.common;

import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HbaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(HbaseUtil.class);

    @Autowired
    private Admin admin;

    @Autowired
    private Connection connection;


    /**
     * 创建表
     * @param tableName
     * @param cols
     */
    public int createTable(String tableName, String[] cols) throws Exception{
        TableName tabName = TableName.valueOf(tableName);
        if(tableExists(tabName)){
            return 201;
        }

        TableDescriptorBuilder htable = TableDescriptorBuilder.newBuilder(tabName);
        Collection<ColumnFamilyDescriptor> families = new LinkedList<ColumnFamilyDescriptor>();
        for (int i = 0; i < cols.length; i++) {
            ColumnFamilyDescriptor column = ColumnFamilyDescriptorBuilder.of(cols[i]);
            families.add(column);
        }

        if(families.size() <= 0){
            return 404;
        }

        htable.setColumnFamilies(families);
        admin.createTable(htable.build());
        return 200;
    }

    /**
     * 插入数据
     * @param tableName
     * @param rowkey
     * @param colFamily
     * @param col
     * @param val
     * @return
     * @throws Exception
     */
    public boolean insertRow(String tableName, String rowkey, String colFamily, String col, String val) throws Exception{
        TableName tabName = TableName.valueOf(tableName);
        if(!tableExists(tabName)){
            return false;
        }
        Table tabl =  connection.getTable(tabName);

        Put put = new Put(rowkey.getBytes());
        put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
        tabl.put(put);
        tabl.close();
        return true;
    }

    public String getData(String tableName, String rowkey, String colFamily, String col) throws Exception{
        TableName tabName = TableName.valueOf(tableName);
        if(!tableExists(tabName)){
            return null;
        }

        Table tab = connection.getTable(tabName);

        Get get = new Get(rowkey.getBytes());

        Result result = tab.get(get);

        formatResultASMap(result);


        return null;
    }


    public void listTab(){
        List<TableDescriptor> listTab;
        try{
            listTab = admin.listTableDescriptors();
            for (int i = 0; i < listTab.size(); i++) {
                System.out.println(listTab.get(i).getTableName().getNameAsString());
            }
        }catch (Exception e) {

        }

    }


    public boolean tableExists(TableName tableName){
        try{
            return admin.tableExists(tableName);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }


    /**
     * 格式化result
     * @param result
     * @return
     */
    public Map<String ,Object> formatResultASMap(Result result){
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
            System.out.println("Timetamp:" + cell.getTimestamp() + " ");
            System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
            System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
            System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
        }
        return null;
    }

    /**
     * 格式化result
     * @param result
     * @return
     */
    public JSONObject formatResultAsJSON(Result result){
        return null;
    }

}
