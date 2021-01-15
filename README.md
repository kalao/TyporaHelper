# TyporaHelper
1. 创建一个图床github仓库和笔记仓库,并有相应本地仓库对应
2. 创建一个本地笔记仓库副本
3. git 设置全局密码
4. 按照如下在com/Typora/test.java下修改
5. 代码会根据本地笔记仓库副本的修改同步去修改本地仓库,本地仓库进而修改远程仓库,这样的好处是每次使用的是本地图库
6. 如果没有找到相应sh文件请将sh文件手动导入class路径下
```java
public static final String
        NOTEBOOK_FILE_PATH
        ="这是你的笔记本地仓库";
public static final String
        IMAGE_LOCAL_PATH=
        "这是你的图库";
public static final String
        PRO_PATH
        =Thread.currentThread().getContextClassLoader().getResource("").getPath();
public static final String
        SUFFIX="com/Typora/";
        
public static final String
        GIT_PREFIX="这是你图库的前缀";
public static final String
        BASH_PUSH="my.sh";
public static final String
        BASH_UPDATE="update.sh";
public static final String
        LOCAL_PRO_COPY="这是本地笔记仓库的副本";
public static final String
        BASH_LOCAL_COPY="localmy.sh";
```
