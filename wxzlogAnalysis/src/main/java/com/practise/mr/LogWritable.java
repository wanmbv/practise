package com.practise.mr;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author wanmbv
 * @Date 2018/7/30 15:11
 * @Description
 * @Version 1.0
 */
public class LogWritable implements Writable {
    private Text time, page;

    public LogWritable(){
        this.time = new Text();
        this.page = new Text();
    }

    public LogWritable(String time, String page){
        this.time = new Text(time);
        this.page = new Text(page);
    }
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        time.write(dataOutput);
        page.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        time.readFields(dataInput);
        page.readFields(dataInput);
    }

    public String getTime(){
        return this.time.toString();
    }

    public String getPage(){
        return this.page.toString();
    }
}