package com.bigdata.bigdata.common;

import net.sf.json.JSONObject;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串&JSON 转换实体对象
 * yinxiong
 * @param <E>
 */
public class ObjToEntity<E> {

    private Class<E> clz;

    public ObjToEntity(Class<E> entityClz) {
        clz = entityClz;
    }


    public E toEntity(String parms){
        return toEntity(JSONObject.fromObject(parms));
    }

    public E toENtity(Object parms){
        return toEntity(JSONObject.fromObject(parms));
    }

    public E toEntity(JSONObject parms){
        Field[] fields = clz.getDeclaredFields();
        if(fields.length <= 0){
            return null;
        }

        Object obj = null;
        try{
            obj = clz.newInstance();
        }catch (Exception e) {
        }
        if(obj == null){
            return  null;
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldKey = field.getName();
            Object fieldVal = parms.get(fieldKey);
            if(fieldVal == null || fieldVal.equals("")){
                continue;
            }
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldKey, clz);
                Method setMethod = propertyDescriptor.getWriteMethod();
                Method getMethod = propertyDescriptor.getReadMethod();
                Class returnType = getMethod.getReturnType();
                String returnTypeName = returnType.getSimpleName();
                if(returnTypeName.equals("Long")){
                    fieldVal = Long.valueOf(fieldVal.toString());
                }else if(returnTypeName.equals("Integer")){
                    fieldVal = Integer.valueOf(fieldVal.toString());
                }else if(returnTypeName.equals("Double")){
                    fieldVal = Double.valueOf(fieldVal.toString());
                }else if(returnTypeName.equals("String")){
                    fieldVal = fieldVal.toString();
                }else if(returnTypeName.equals("Date")){
                    SimpleDateFormat dateFormat = null;
                    try{
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        fieldVal = dateFormat.parse(fieldVal.toString());
                    } catch (Exception e) {
                        try{
                            fieldVal = new Date(Long.parseLong(fieldVal.toString()));
                        } catch (Exception e1) {
                            continue;
                        }
                    }
                } else {
                    fieldVal = fieldVal.toString();
                }
                setMethod.invoke(obj,fieldVal);
            }catch (Exception e) {
                System.out.println(i);
                e.printStackTrace();
                continue;
            }
        }
        return (E)obj;
    }
}
