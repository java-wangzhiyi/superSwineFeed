import com.alibaba.excel.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SplitUtils {
    /**
     *
     * solr检索时，转换特殊字符
     *
     */
    public static String escapeQueryChars(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
                    || c == ':' || c == '^'	|| c == '[' || c == ']' || c == '\"'
                    || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
                    || c == '|' || c == '&' || c == ';' || c == '/' || c == '.'
                    || c == '$' || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static Boolean hasMoreChar(String s){
        boolean hasMoreChar = false;
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\'){
                j++;
            }
        }
        return j > 1;
    }

    public static List<String> changeTitle(Collection c){
        ArrayList<String> strings = new ArrayList<>(c);
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i).contains(".") ? strings.get(i).split("\\.")[1] : strings.get(i);
            strings.set(i, s);
        }
        return strings;
    }
}
