package com.practise.mr;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author wanmbv
 * @Date 2018/7/30 15:08
 * @Description
 * @Version 1.0
 */
public class LogAnalysis {

    public static class LogAnalysisMapper extends Mapper<LongWritable, Text, Text, LogWritable>{

        public void map(LongWritable key, Text value, Context context){
            Pattern pr = Pattern.compile("\\[([\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{2}:[\\d]{2}:[\\d]{2}) [\\w]{1,5} \\] \\(.*:[\\d]*\\) - \\[\\d*\t([\\d]*.[\\d]*.[\\d]*.[\\d]*)\t.*\thttp://www.baidu.cn/(.*)\\]");
            Matcher matcher = pr.matcher(value.toString());
            if(matcher.matches()) {
                String time = matcher.group(1);
                String ip = matcher.group(2);
                String page = matcher.group(3);
                try{
                    context.write(new Text(ip), new LogWritable(time, page));
                }catch(Exception e){
                    //
                }
            }
        }
    }

    public static class LogAnalysisReducer extends Reducer<Text, LogWritable, Text, Text>{

        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public void reduce(Text key, Iterable<LogWritable> values, Context context){
            String startTime = "", startPage = "", lastTime = "", lastPage = "";
            int pageCount = 0;
            long timeCount = 0l;
            for(LogWritable value: values){
                String time = value.getTime();
                String page = value.getPage();
                if(StringUtils.isEmpty(startTime) || time.compareTo(startTime) < 0){
                    startTime = time;
                    startPage = page;
                }
                if(time.compareTo(lastTime) > 0){
                    lastTime = time;
                    lastPage = page;
                }
                pageCount++;
                try{
                    Date startDate = format.parse(startTime);
                    Date endDate = format.parse(lastTime);
                    timeCount = endDate.getTime() - startDate.getTime();
                }catch(Exception e){
                    //
                }
            }
            String result = startTime + "\t" + startPage + "\t" + lastTime + "\t" + lastPage + "\t" + pageCount + "\t" + timeCount;
            try{
                context.write(key, new Text(result));
            }catch(Exception e){
                //
            }
        }
    }
}