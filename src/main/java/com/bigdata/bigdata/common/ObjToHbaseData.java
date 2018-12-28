package com.bigdata.bigdata.common;

import com.bigdata.bigdata.entity.UcCert;
import com.bigdata.bigdata.entity.UcUser;
import com.bigdata.bigdata.entity.UcUserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.client.Put;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ObjToHbaseData {

    public static Put toPut(String entity){
        return toPut(JSONObject.fromObject(entity));
    }

    public static Put toPut(JSONObject entityJSON){
        return toPut(JSONObject.toBean(entityJSON));
    }

    public static Put toPut(Object obj){
        if(obj == null) {
            return null;
        }
        Class clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();

        System.out.println(clz.toString()+"："+JSONArray.fromObject(Arrays.asList(fields)).toString());
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldKey = field.getName();
            if(fieldKey.toLowerCase().indexOf("id") < 0) {
                return null;
            }
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldKey, clz);
                Method getMethod = propertyDescriptor.getReadMethod();
                Object methodResult = getMethod.invoke(obj);
                if (methodResult != null) {
                    System.out.println(fieldKey+"："+methodResult.toString());
                }
//                Put put = new Put();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public static void main(String[] args) {
        UcCert ucCert = new UcCert();
        ucCert.setUcCertId(1L);
        ucCert.setReallyName("yinx");
        ucCert.setCardNumber("21313");
        ucCert.setRemark("sfdsfsfdsdf");

        UcUser ucUser = new UcUser();
        ucUser.setIddCode("+86");
        ucUser.setMobile("13123123123");
        ucUser.setUcUserId(1L);
        ucUser.setBaNationalId(11L);


        UcUserInfo ucUserInfo = new UcUserInfo();
        ucUserInfo.setAddress("sfsdfsfsdf");
        ucUserInfo.setEmail("sdfsdfsdfsd");
        ucUserInfo.setFirstName("sfsdfsdf");
        ucUserInfo.setTowns("sfsdfsdf");
        ucUserInfo.setUcUserId(1L);

        ObjToHbaseData.toPut(ucCert);
        ObjToHbaseData.toPut(ucUser);
        ObjToHbaseData.toPut(ucUserInfo);

    }
}
