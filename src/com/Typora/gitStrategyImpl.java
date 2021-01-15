package com.Typora;

import java.util.Map;

public class gitStrategyImpl implements gitStrategy {
    public static void main(String[] args) {
        Map<String, Boolean> stringBooleanMap = new gitStrategyImpl().extractNewMd("Changes not staged for commit:\n" +
                "  (use \"git add <file>...\" to update what will be committed)\n" +
                "  (use \"git checkout -- <file>...\" to discard changes in working directory)\n" +
                "\n" +
                "\tmodified:   Java学习/Java基础.md\n" +
                "\n" +
                "no changes added to commit (use \"git add\" and/or \"git commit -a\")");

        System.out.println();
    }
}
