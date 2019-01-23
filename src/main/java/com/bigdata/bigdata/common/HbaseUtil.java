package com.bigdata.bigdata.common;

import com.bigdata.bigdata.entity.UcUser;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
            createTable(tableName,new String[]{"info"});
//            return false;
        }
        Table tabl =  connection.getTable(tabName);

        Put put = new Put(rowkey.getBytes());
        put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
        tabl.put(put);
        tabl.close();
        return true;
    }

    public boolean insertRow(Object entity) throws Exception{
        String tableName = entity.getClass().getSimpleName();
        TableName tabName = TableName.valueOf(tableName);
        if(!tableExists(tabName)){
            createTable(tableName,new String[]{"info"});
        }
        Table tabl =  connection.getTable(tabName);
        Put put = ObjToHbaseData.toPut(entity);
        tabl.put(put);
        tabl.close();
        return true;
    }


    public ResultScanner getScanner(Class entityTab, Filter ... filter){
        TableName tabName = TableName.valueOf(entityTab.getSimpleName());
        if(!tableExists(tabName)){
            return null;
        }

        Table tab;
        try{
            tab = connection.getTable(tabName);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }

        Scan scan = new Scan();
        if(filter != null && filter.length > 0) {
            FilterList filterList = new FilterList(filter);
            scan.setFilter(filterList);
        }
        try {
            return tab.getScanner(scan);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }

    }

    public Object getEntity(Object obj, String key, String val){
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(key, obj.getClass());
            Method setMethod = propertyDescriptor.getWriteMethod();
            Method getMethod = propertyDescriptor.getReadMethod();
            Class returnType = getMethod.getReturnType();
            String returnTypeName = returnType.getSimpleName();
            Object fieldVal;
            if(returnTypeName.equals("Long")){
                fieldVal = Long.valueOf(val);
            }else if(returnTypeName.equals("Integer")){
                fieldVal = Integer.valueOf(val);
            }else if(returnTypeName.equals("Double")){
                fieldVal = Double.valueOf(val);
            }else if(returnTypeName.equals("String")){
                fieldVal = val;
            }else if(returnTypeName.equals("Date")){
                SimpleDateFormat dateFormat = null;
                try{
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fieldVal = dateFormat.parse(val);
                } catch (Exception e) {
                    try{
                        fieldVal = new Date(Long.parseLong(val));
                    } catch (Exception e1) {
                        return null;
                    }
                }
            } else {
                fieldVal = val;
            }
            setMethod.invoke(obj,fieldVal);
        }catch (Exception e) {
            return null;
        }
        return obj;
    }


    public <T> List<T> getList(Class<T> entityTab, String column, String parms) {
        Filter columnFilter = new QualifierFilter(CompareOperator.EQUAL,new BinaryComparator(Bytes.toBytes(column)));
        Filter parmsFilter = new ValueFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes(parms)));
        Filter[] filters = new Filter[]{columnFilter,parmsFilter};
        return getList(entityTab,filters);
    }

    public <T> List<T> getList(Class<T> entityTab, Filter ... filter) {
        ResultScanner hbaseResult = getScanner(entityTab,filter);

        List<T> returnList = new LinkedList<T>();
        for(Result result : hbaseResult) {
            Cell[] cells = result.rawCells();
            Object obj = null;
            try{
                obj = entityTab.newInstance();
            }catch (Exception e) {
            }
            if(obj == null){
                continue;
            }
            for (int i = 0; i < cells.length; i++) {
                KeyValue keyValue = new KeyValue(cells[i]);
                String key = Bytes.toString(keyValue.getQualifierArray(),keyValue.getQualifierOffset(),keyValue.getQualifierLength());
                String val = Bytes.toString(keyValue.getValueArray(),keyValue.getValueOffset(),keyValue.getValueLength());
                obj = getEntity(obj,key,val);
            }
            returnList.add((T)obj);
        }
        return returnList;
    }

    public <T> T getByRow(Class<T> entityTab, String rowkey){
        Object obj = null;
        try{
            obj = entityTab.newInstance();
        }catch (Exception e) {
        }
        if(obj == null){
            return  null;
        }

        Field[] fields = entityTab.getDeclaredFields();
        if(fields.length <= 0){
            return null;
        }

        Filter rowFilter = new RowFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes(rowkey)));
        ResultScanner hbaseResult = getScanner(entityTab,rowFilter);
        try {
            Result result = hbaseResult.next();
            if(result == null) {
                return null;
            }
            Cell[] cells = result.rawCells();

            for (int i = 0; i < cells.length; i++) {
                KeyValue keyValue = new KeyValue(cells[i]);
                String key = Bytes.toString(keyValue.getQualifierArray(),keyValue.getQualifierOffset(),keyValue.getQualifierLength());
                String val = Bytes.toString(keyValue.getValueArray(),keyValue.getValueOffset(),keyValue.getValueLength());
                obj = getEntity(obj,key,val);
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e );
            return null;
        }
        return (T)obj;
    }

    /**
     * 根据表名删除表
     * @param tableName
     * @return
     */
    public boolean delTab(String tableName){
        TableName tabName = TableName.valueOf(tableName);
        if(!tableExists(tabName)){
            return false;
        }

        try{
            admin.disableTable(tabName);
            admin.deleteTable(tabName);
            return true;
        }catch (Exception e) {
            logger.error(e.getMessage(),e );
            return false;
        }
    }

    /**
     * 根据  .class 删除表
     * @param tableName
     * @return
     */
    public boolean delTab(Class tableName){
        return delTab(tableName.getSimpleName());
    }

    public boolean tableExists(TableName tableName){
        try{
            return admin.tableExists(tableName);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }



}
