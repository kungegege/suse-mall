package com.frame.utils;

import lombok.Data;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 10:16
 * Description:
 */
@Data
public class SensitiveWordUtil {

    // ROOT
    private Map<Character, TreeNode> root = new HashMap<>();


    public void init(String msg) {
        char word = msg.charAt(0);
        if (root.get(word) == null) {
            TreeNode treeNode = toTree(msg.substring(1));
            root.put(word, treeNode);
        } else {

        }
    }


    public static void main(String[] args) {
    }

    private void addNode(String msg, TreeNode node) {
        for (int i = 0; i < msg.length(); i++) {
            // node
            int index = 0;
            while (index < msg.length() && node.getWord().equals(msg.charAt(index))) {
            }

            if (index < msg.length()) {

            }
        }
    }

    /**
     * 字符串转为树化
     * @param msg
     * @return
     */
    private  TreeNode toTree(String msg){
        if (msg==null || msg.length()==0){
            return null;
        }
        TreeNode preNode = null, header = null;
        for (int i = 0; i < msg.length(); i++) {
            char currWord = msg.charAt(i);
            TreeNode currNode = new TreeNode(currWord);
            if (preNode == null) {
                preNode = currNode;
                header = currNode;
            }else {
                preNode.nextNodes.add(currNode);
                preNode = currNode;
            }
        }
        return header;
    }


    private void addSensitiveWord(String word, TreeNode root) {
        if (word == null || word.length() == 0) {
            return;
        }
        if (word.charAt(0) == root.word) {

        }
    }


    @Data
    class TreeNode {
        // 下一个节点
        private List<TreeNode> nextNodes = new ArrayList<>();
        // 当前节点的内容
        private Character word;
        public TreeNode(Character word) {
            this.word = word;
        }
    }
}
