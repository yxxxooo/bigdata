package com.bigdata.bigdata.common;

import org.apache.hadoop.hbase.client.Put;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjToHbaseData {

    public synchronized static Put toPut(Object obj){
        if(obj == null) {
            return null;
        }
        Class clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();

        if(fields.length <= 0) {
            return null;
        }
        Put put = null;
        try {
            Field rowKeyField = fields[0];
            String rowKey = rowKeyField.getName();
            PropertyDescriptor rowKeyPropertyDescriptor = new PropertyDescriptor(rowKey, clz);
            Method rowKeyGetMethod = rowKeyPropertyDescriptor.getReadMethod();
            Object rowKeyMethodResult = rowKeyGetMethod.invoke(obj);
            put = new Put(rowKeyMethodResult.toString().getBytes());

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldKey = field.getName();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldKey, clz);
                Method getMethod = propertyDescriptor.getReadMethod();
                Object methodResult = getMethod.invoke(obj);
                if(methodResult != null) {
                    put.addColumn("info".getBytes(), fieldKey.getBytes(), methodResult.toString().getBytes());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return put;
    }

}
