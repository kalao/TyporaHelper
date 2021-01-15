package com.Typora;

import java.io.*;

public interface mdStrategy {
    public static final int BUFFER_MAX_SIZE=1024;
    public void strategy(File pro, String text, String localImageFoler, String PREFFIX, String localProFoler);
    default void copy(File in, File out){
        InputStream fi=null;
        OutputStream fo=null;
        try {
            //有可能图片不存在
            if(!in.exists()){
                //log
                return ;
            }
            fi=new FileInputStream(in);
            fo=new FileOutputStream(out);
            int len;
            byte[] buffer=new byte[BUFFER_MAX_SIZE];
            while((len=fi.read(buffer))>0){
                fo.write(buffer,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//                try {
////                    fo.close();//TODO 为什么空指针
////                    fi.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


        }
    }
    default String replace(String text, String origin_url, String git_url){
      return  text.replaceFirst(origin_url,git_url);
    }
    default void del(File delMd){
        if(delMd.exists()){
            delMd.delete();
        }
    }
}
