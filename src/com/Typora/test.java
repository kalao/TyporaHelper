package com.Typora;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.Typora.update2GithubUtils.Read;

public class test {
    public static final String
            NOTEBOOK_FILE_PATH
            ="/home/kalao/Documents/swapToUUi/文档/项目仓库/NoteBookGit";
    public static final String
            IMAGE_LOCAL_PATH=
            "/home/kalao/Documents/swapToUUi/文档/项目仓库/ImagesGit/";
    public static final String
            PRO_PATH
            =Thread.currentThread().getContextClassLoader().getResource("").getPath();
    public static final String
            SUFFIX="com/Typora/";

    public static final String
            GIT_PREFIX="https://github.com/kalao/Images/blob/master/";
    public static final String
            BASH_PUSH="my.sh";
    public static final String
            BASH_UPDATE="update.sh";
    public static final String
            LOCAL_PRO_COPY="/home/kalao/Documents/swapToUUi/文档/项目仓库/localNoteBooks";
    public static final String
            BASH_LOCAL_COPY="localmy.sh";
    /***
     * 对每一个文件的处理
     * @param singleFile
     */
    public static void readSingleMd(File singleFile){
        try {
            String text=Read(new FileInputStream(singleFile));
            mdStrategy mdS = new mdStrategyImpl();
            System.out.println(singleFile.getName());
            mdS.strategy(singleFile,text,IMAGE_LOCAL_PATH,GIT_PREFIX,NOTEBOOK_FILE_PATH);
            update2Github(PRO_PATH,SUFFIX,IMAGE_LOCAL_PATH,BASH_PUSH); //之后可以设置
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
     * 扫描文件
     */
    public static void ScanFiles(File root){
        if(root.isDirectory()){
            File[] childs=root.listFiles();
            for (File file:childs) {
                ScanFiles(file);
            }
        }else{
            //读取文件
            if(root.getName().endsWith(".md")) {
                readSingleMd(root);
            }

        }
    }
    /****
     * 1 首先读取指定文件夹下的md文件
     * 2 获取其中的图片路径
     * 3 将本地图片保存到另外的仓库 IMAGE_LOCAL_PATH
     *
     */
    public static void image2Github(){
           File root=new File(NOTEBOOK_FILE_PATH);
           ScanFiles(root);
    }
    /***
     *
     * @param PRO_PATH 项目路径
     * @param SUFFIX  项目后缀
     * @param NOTEBOOK_FILE_PATH git项目路径
     */
    public static String update2Github(String PRO_PATH,String SUFFIX,String NOTEBOOK_FILE_PATH,String cmd){
        String COMMAND_CD =
                PRO_PATH+SUFFIX+cmd+" "+NOTEBOOK_FILE_PATH+" "+PRO_PATH+SUFFIX;
        try {
            Process p=Runtime.getRuntime().exec(COMMAND_CD);
            int status = p.waitFor();
            if (status != 0)
            {
                System.out.println("Failed to call shell's command ");
            }
            return Read(p.getInputStream());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
          Java学习/1.md"
         modified:   "Java学习/1.md"
     */
    public static List<String> getLatestMsg(String PRO_PATH,String SUFFIX,String NOTEBOOK_FILE_PATH,String BASH_UPDATE){
        String in=update2Github(PRO_PATH,SUFFIX,NOTEBOOK_FILE_PATH,BASH_UPDATE);
        //封装信息种类, 枚举 TODO
        gitStrategy g=new gitStrategy(){};
        Map<String, Boolean> modified = g.extractModifiedMd(in);
        Map<String, Boolean> joined = g.extractNewMd(in);
        List<String> res=new ArrayList<>();
        Map<String,Boolean> merged=new HashMap<>(joined);
        modified.forEach((key,value)->merged.merge(key,value,(v1,v2)->true)
        );

        merged.forEach((e,v) -> {
                    res.add(NOTEBOOK_FILE_PATH+"/"+e);
                }
        );
//        modified.forEach((e,v) -> {
//                    res.add(NOTEBOOK_FILE_PATH+"/"+e);
//                }
//        );
//        joined.forEach((e,v) -> {
//                    res.add(NOTEBOOK_FILE_PATH+"/"+e);
//        });
        System.out.println();
        return res;
    }
    public static List<String> getLatestDelMsg(String PRO_PATH,String SUFFIX,String NOTEBOOK_FILE_PATH,String BASH_UPDATE){
        String in=update2Github(PRO_PATH,SUFFIX,NOTEBOOK_FILE_PATH,BASH_UPDATE);
        //封装信息种类, 枚举 TODO
        gitStrategy g=new gitStrategy(){};
        Map<String, Boolean> deleted = g.extractDelMd(in);
        List<String> res=new ArrayList<>();
        deleted.forEach((e,v) -> {
                    res.add(NOTEBOOK_FILE_PATH+"/"+e);
                }
        );
        return res;
    }
    private static Long getProcessId() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();
            String pid = name.substring(0, name.indexOf('@' ));
            return Long.parseLong(pid);
        } catch (Exception e) {
            return null;
        }
    }
    public static void localScheduler(){
        final long INTERVAL=1000*10*1;
        mdStrategy md=new mdStrategyImpl();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                System.out.println(getProcessId());
                while(true){
                    try {
                        System.out.println();
                        List<String> res = getLatestDelMsg(PRO_PATH, SUFFIX, LOCAL_PRO_COPY, BASH_UPDATE);
                        System.out.println("待删除");
                        if (res.size() > 0) {
                            res.forEach((e) -> {
                                System.out.println(e);
                                //删除
//                        e=e.replaceFirst(LOCAL_PRO_COPY+SUFFIX,NOTEBOOK_FILE_PATH+SUFFIX);
                                e = NOTEBOOK_FILE_PATH + e.substring(LOCAL_PRO_COPY.length());
                                System.out.println(e);
                                md.del(new File(e));
                            });
                        }
                        System.out.println("待增加");

                        List<String> res1 = getLatestMsg(PRO_PATH, SUFFIX, LOCAL_PRO_COPY, BASH_UPDATE);
                        if (res1.size() > 0) {
                            res1.forEach((e) -> {
                                System.out.println(e);
                                //先删除
                                String copy = e;
                                e = NOTEBOOK_FILE_PATH + e.substring(LOCAL_PRO_COPY.length());
                                md.del(new File(e));
                                md.copy(new File(copy), new File(e));
                                //后拷贝
                            });

                        }
                    }catch (Exception e){
                        System.out.println(e.getStackTrace());
                        break;
                    }
                    //执行bash
                    update2Github(PRO_PATH,SUFFIX,LOCAL_PRO_COPY,BASH_LOCAL_COPY);
//
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }
    public static void Scheduler(){
        Runnable runnable=new Runnable(){
            public final long INTERVAL=1000*60*2;
            @Override
            public void run() {
                while(true){
                    try {
                        System.out.println(getProcessId());
//                        List<String> res=getLatestMsg();
                        List<String> res=getLatestMsg(PRO_PATH,SUFFIX,NOTEBOOK_FILE_PATH,BASH_UPDATE);

                        if(res.size()>0){
                            res.forEach(
                                (e) -> {
                                    System.out.println(e);
                                    ScanFiles(new File(e));
                                    update2Github(PRO_PATH,SUFFIX,NOTEBOOK_FILE_PATH,BASH_PUSH);
                                }
                            );
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
                            System.out.println("有task了");
                        }
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        new Thread(runnable).start();
    }
    public static void main(String[] args) {
        //1 将图片上传到github上
//        Image2Github();

        //2 将readme文件的图片路径替换
//        update2Github(PRO_PATH,SUFFIX,NOTEBOOK_FILE_PATH,"my.sh");

        //3 定时线程,<定制>
        // 按时查看新增的文件,和修改的文件路径
//        getLatestMsg();
        Scheduler();
        localScheduler();
    }

    /**
      Java学习/1.md"
      modified:   "Java学习/1.md"
     */
}
