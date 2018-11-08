package com.bigdata.bigdata.common.hadoop;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 操作Hadoop
 */
@Component
public class HadoopSystemFileSystem {

    private static final Logger logger = LoggerFactory.getLogger(HadoopSystemFileSystem.class);

    @Autowired
    private FileSystem fileSystem;


    /**
     * 创建文件夹
     * @param dirName
     * @return
     */
    public  boolean mkdir(String dirName){
        if(checkFileExist(dirName)){
            return true;
        }

        try{
            Path path = new Path(dirName);
            return fileSystem.mkdirs(path);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 创建空文件
     * @param fileName
     * @return
     */
    public boolean mkFile(String fileName){
        if(checkFileExist(fileName)){
            return true;
        }

        try{
            Path path = new Path(fileName);
            FSDataOutputStream os = fileSystem.create(path, true);
            os.flush();
            os.close();
            return true;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }


    /**
     *字符串写入到文件
     * @param text
     * @param f 路径
     */
    public void writerString(String text, String f){
        try {
            Path path = new Path(f);
            FSDataOutputStream os = os = fileSystem.create(path);
            OutputStreamWriter outputStream = new OutputStreamWriter(os, "utf-8");
            BufferedWriter writer = new BufferedWriter(outputStream);// 以UTF-8格式写入文件，不乱码
            writer.write(text);
            writer.flush();
            writer.close();
            outputStream.close();
            os.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 读取文件内容
     * @param f
     * @return
     */
    public String readByLine(String f){
        StringBuffer content = new StringBuffer();
        try
        {
            Path path = new Path(f);
            FSDataInputStream dis = fileSystem.open(path);
            BufferedReader bf=new BufferedReader(new InputStreamReader(dis));//防止中文乱码
            String line = null;
            while ((line=bf.readLine())!=null) {
                content.append(new String(line.getBytes(),"utf-8"));
            }
            dis.close();
            bf.close();
            return content.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 检查文件/文件夹是否存在
     * @param fileName
     * @return
     */
    public boolean checkFileExist(String fileName){
        try{
            Path path = new Path(fileName);
            return fileSystem.exists(path);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return  false;
        }
    }

}
