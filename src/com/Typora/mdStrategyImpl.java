package com.Typora;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Typora.update2GithubUtils.Read;
import static com.Typora.update2GithubUtils.Write;

public class mdStrategyImpl implements mdStrategy {
    /***
     * src="/home/kalao/.config/Typora/typora-user-images/image-20201207204142611.png"
     * ![image-20201124205144927](/home/kalao/.config/Typora/typora-user-images/image-20201124205144927.png)
     * @param text
     * 替换成仓库地址
     */
    @Override
    public void strategy(File pro,String text,String localImageFoler,String PREFFIX,String localProFoler) {

        //https://github.com/kalao/Images/tree/master/
        //1 获得原来的所有图片路径
        String[] patterns = new String[]{"src=\"(.*?image-(\\d*?)\\.png)\""
                , "\\]\\((.*?image-(\\d*?)\\.png)\\)"};
        ArrayList<String> originImagePaths = new ArrayList<>();
        for (String pattern : patterns) {
            Pattern r = Pattern.compile(pattern);
            Matcher matcher = r.matcher(text);
            while (matcher.find()) {
                originImagePaths.add(matcher.group(1));
            }
        }

        ArrayList<String> imageNames = new ArrayList<>();
        Pattern r = Pattern.compile("image-(\\d*?\\.png)");
        for (String imagepath : originImagePaths) {
            Matcher matcher = r.matcher(imagepath);
            while (matcher.find()) {
                imageNames.add(matcher.group(1));
            }
        }
        ArrayList<String> gitUrl = new ArrayList<>();
        for (String image : imageNames) {
//            System.out.println(image);
            gitUrl.add(PREFFIX + pro.getName() + "/" + image);
        }
//            replace();
            // 2将所有的文件上传到仓库,并返回网络地址
            //新建md的文件
            if (pro == null || "".equals(pro.getName())) {
                return;
                //抛出异常
            }
            File proFolder = new File(localImageFoler + pro.getName());
            if (!proFolder.exists()) {
                proFolder.mkdir();
            }
            InputStream fi = null;
            OutputStream fo = null;
            for (int i = 0; i < originImagePaths.size(); ++i) {
                copy(new File(originImagePaths.get(i)), new File(proFolder.getAbsolutePath() + "/" + imageNames.get(i)));
            }

            for (int i = 0; i < originImagePaths.size(); ++i) {
                System.out.println(originImagePaths.get(i));
                System.out.println(gitUrl.get(i));
                text=replace(text,originImagePaths.get(i),gitUrl.get(i));
            }
//            System.out.println(text);
//            System.out.println(text);
        try {
            Write(new FileOutputStream(pro),text);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        String content= null;
        File pro=new File("/home/kalao/Documents/swapToUUi/文档/项目仓库/NoteBookGit/Java学习/JVM基础.md");
        try {
            content = Read(new FileInputStream(pro));
            mdStrategy mdStrategy = new mdStrategyImpl();
            mdStrategy.strategy(pro,content,"/home/kalao/Documents/swapToUUi/文档/项目仓库/ImagesGit/","https://github.com/kalao/Images/tree/master/","/home/kalao/Documents/swapToUUi/文档/项目仓库/NoteBookGit");
//
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String text="<img src=\"/home/kalao/.config/Typora/typora-user-images/image-20201207204142611.png\" ";
//        mdStrategy mdStrategy = new mdStrategyImpl();
//        mdStrategy.strategy(pro,text,"/home/kalao/Documents/swapToUUi/文档/项目仓库/ImagesGit/","https://github.com/kalao/Images/tree/master/","/home/kalao/Documents/swapToUUi/文档/项目仓库/NoteBookGit");
//        System.out.println(mdStrategy.replace(text,"/home/kalao/.config/Typora/typora-user-images/image-20201207204142611.png","https://github.com/kalao/Images/tree/master/ppp/20201207204142611.png"));
    }
}
