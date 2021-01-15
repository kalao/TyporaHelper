package com.Typora;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface gitStrategy {
    public default Map<String, Boolean>  extractModifiedMd(String input){
        Map<String,Boolean> map=new HashMap<>();
        Pattern p =Pattern.compile("modified:\\s{3}(\\S.*?\\.md)");//modified:\s*?(\S.*?\.md)
        Matcher matcher = p.matcher(input);
        while (matcher.find()){
            System.out.println(matcher.group(1));
            map.put(matcher.group(1),true);
        }
        return map;
    }
    public default Map<String, Boolean>  extractNewMd(String input){
        Map<String,Boolean> map=new HashMap<>();
        Pattern p =Pattern.compile("\"(.*?\\.md)\"");
        Matcher matcher = p.matcher(input);
        while (matcher.find()){
            if(!matcher.group(1).contains("modified")) {
                map.put(matcher.group(1), true);
            }
        }
        p =Pattern.compile("\\t(\\S.*?\\.md)");//
        matcher = p.matcher(input);
        while (matcher.find()){
            if(!matcher.group(1).contains("modified")&&!matcher.group(1).contains("deleted")) {
                map.put(matcher.group(1), true);
            }
        }
        return map;
    }
    public default Map<String, Boolean>  extractDelMd(String input){
        Map<String,Boolean> map=new HashMap<>();
        Pattern p =Pattern.compile("deleted:\\s{4}(\\S.*?\\.md)");
        Matcher matcher = p.matcher(input);
        while (matcher.find()){
            System.out.println(matcher.group(1));

            map.put(matcher.group(1),true);
        }
        return map;
    }
}
