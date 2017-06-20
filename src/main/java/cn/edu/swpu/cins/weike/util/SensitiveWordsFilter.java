package cn.edu.swpu.cins.weike.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by muyi on 17-6-20.
 */
@Component
public class SensitiveWordsFilter implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        try {

            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWords(lineTxt.trim());
            }
            read.close();
        } catch (Exception e) {
            throw new Exception("读取文件失败");
        }
    }


    private boolean isSymbol(char c) {
        int ic = (int) c;
        return (ic < 0X2E80 || ic > 0x9FFF);
    }

    public class TreeNode {
        private Character val;
        private HashMap<Character, TreeNode> sonNode = new HashMap<Character, TreeNode>();
        private boolean end = false;

        public void setEnd(boolean end) {
            this.end = end;
        }

        public boolean isEnd() {
            return end;
        }

        public TreeNode getSonNodes(Character val) {
            return sonNode.get(val);
        }

        public void addSonNode(Character val, TreeNode node) {
            sonNode.put(val, node);
        }
    }

    TreeNode root = new TreeNode();

    public void addWords(String words) {
        TreeNode tmpNode = root;
        for (int i = 0; i < words.length(); i++) {
            Character val = words.charAt(i);
            if (isSymbol(val)) {
                continue;
            }
            TreeNode node = tmpNode.getSonNodes(val);
            if (node == null) {
                node = new TreeNode();
                tmpNode.addSonNode(val, node);
            }
            tmpNode = node;
            if (i == words.length() - 1) {
                tmpNode.setEnd(true);
            }
        }
    }

    public String Filter(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return txt;
        }
        String replace = "***";
        StringBuffer result = new StringBuffer();
        int position = 0;
        int begin = 0;
        TreeNode tmpNode = root;
        while (position < txt.length()) {
            Character val = txt.charAt(position);
            if (isSymbol(val)) {
                if (tmpNode == root) {
                    result.append(val);
                    ++begin;
                }
                ++position;
                continue;
            }
            tmpNode = tmpNode.getSonNodes(val);
            if (tmpNode == null) {
                result.append(txt.charAt(begin));
                position = begin + 1;
                begin = position;
                tmpNode = root;
            } else if (tmpNode.isEnd()) {
                position = position + 1;
                begin = position;
                result.append(replace);
                tmpNode = root;
            } else {
                ++position;
            }
        }
        result.append(txt.substring(begin));
        return result.toString();
    }
}
