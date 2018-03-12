package test;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhouxw on 17/12/6.
 */



public class StringToJson {
    private static Pattern numPattern = Pattern.compile("([+-]?\\d+|[+-]?\\d+[.]?\\d*[eE][+-]?\\d+|[+-]?\\d+.\\d+)");
    private static Pattern arrOrListpattern = Pattern.compile(".*(arraybuffer|list).*");
    private static ReentrantLock lock = new ReentrantLock();
    private class StoreClass {
        int cursor = 0;
        int go = 0;
        StoreClass() {
            cursor = 0;
            go = 0;
        }
        public StoreClass(int cursor, int go) {
            this.cursor = cursor;
            this.go = go;
        }
    }

    //    判断所传进去的类名是否需要递归
    private static boolean isKeyWord(String word, int index, String log) { //index为word最后一个字符在log中的位置
        String s = eliminateEmptyChar(word);

        if (log.charAt(index + 1) == '(' && s.indexOf('(') == -1 && s.indexOf(')') == -1 && !s.equals("Some")) {
            return true;
        } else {
            return false;
        }
    }
    private static Boolean availableKeyWord(String string) {
        boolean available = true;
        for (int i = 0; i < string.length(); i++) {
            if (!(string.charAt(i) == '_' || Character.isDigit(string.charAt(i)) || (string.charAt(i) >= 'a' && string.charAt(i) <= 'z') || (string.charAt(i) >= 'A' && string.charAt(i) <= 'Z'))) {
                available = false;
                break;
            }
        }
        return available;
    }

    private static boolean isNum(String s) {
        String tmp = s.trim();
        Matcher matcher = numPattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isArrOrList(String word) {
        Matcher matcher = arrOrListpattern.matcher(word);
        return matcher.matches();
    }

    //预加工,改造成 k : v 形式
    private static String preTreatment(String log, StoreClass storeClass) throws Exception {
        if (log == null || log.length() == 0 || storeClass.cursor < 0 || storeClass.cursor >= log.length()) {
            return "";
        }
        int count = 0; //当前递归左括号的个数,用来判断是否跳出当前递归,如果等于0,表示如果碰到右括号就可以跳出当前递归
        String jsonString = "{";
        int left = storeClass.cursor;
        int i = 1;

        String word = "";
        while (storeClass.cursor < log.length()) {
            if (storeClass.cursor > 0 && log.charAt(storeClass.cursor) == ',') {
                int numOfEmpty = numOfEmptyCharBeforeIndex(log, storeClass.cursor);
                if (storeClass.cursor >= 1 && log.charAt(storeClass.cursor - 1 - numOfEmpty) != ')') {
                    word = eliminateEmptyChar(log.substring(left, storeClass.cursor));
                    if (!isNum(word) && !word.equals("false") && !word.equals("true") && !word.toLowerCase().equals("none")) {
                        word = "\"" + word + "\"";
                    }
                    if (word.toLowerCase().equals("none")) { //如果为none,则设置为null

                            jsonString += "\"" + i + "_" + word + "\":" + "null,";
                            i++;

                    } else {
                        jsonString += "\"" + i  + "\":" + word + ",";
                        i++;
                    }
                }
                if (storeClass.cursor > 1 && log.charAt(storeClass.cursor - 1 - numOfEmpty) == ')' && log.charAt(storeClass.cursor - 2 - numOfEmpty) == ')') {
                    jsonString += ",";
                }
                left = ++storeClass.cursor;
            } else if (log.charAt(storeClass.cursor) == '(') {
                word = eliminateEmptyChar(log.substring(left, storeClass.cursor));
                String kv = "";

                if (isKeyWord(word, storeClass.cursor - 1, log)) {
                    if (!availableKeyWord(word) || word.trim().length() == 0 || isNum(word)) {
                        throw new Exception("字符串格式不对!");
                    }
                    storeClass.cursor++;

                    kv = "\"" + i + "_" + word.toLowerCase() + "\":" + preTreatment(log,storeClass);
                    i++;
                    jsonString += kv;
                } else {
                    left = ++storeClass.cursor;
                    count++; //左括号加1
                }
            } else if (log.charAt(storeClass.cursor) == ')') {
                if (count == 0) {
                    word = eliminateEmptyChar(log.substring(left, storeClass.cursor));
                    if (storeClass.cursor < log.length() - 1 && log.charAt(storeClass.cursor + 1) != ')') {
                        if (word.indexOf("(") > 0 || word.indexOf(")") > 0) {
                            return jsonString + "}";
                        } else {
                            if (log.charAt(storeClass.cursor - 1) == '(' || !isNum(word)) { //若无形参,则应该加双引号
                                word = "\"" + word + "\"";
                            }
                            String res = jsonString + "\"" + i  + "\":" + word + "}" + ",";
                            i++;
                            return res;
                        }
                    } else {
                        if (word.indexOf("(") > 0 || word.indexOf(")") > 0) {
                            if (storeClass.cursor < log.length() - 1 && log.charAt(storeClass.cursor + 1) == ',') {
                                return jsonString + "},";
                            } else {
                                return jsonString + "}";
                            }

                        } else {
                            if (!isNum(word) || log.charAt(storeClass.cursor - 1) == '(') { //如果无形参(如: A()),则应该加双引号
                                word = "\"" + word + "\"";
                            }
                            String res = jsonString + "\"" + i  + "\":" + word + "}";
//                            String res = jsonString + "\"" + i + "_" + "\":" + word + "}";
                            i++;
                            return res;
                        }
                    }
                } else {
                    count--;
                }
            }
            storeClass.cursor++;
        }

        return jsonString + "}";
    }

    private static String eliminateEmptyChar(String string) {
        int index = 0;
        for (int i = 0; i < string.length(); i++) {
            char tmp = string.charAt(i);
            if (!isEmptyChar(tmp)) {
                index = i;
                break;
            }
        }
        return string.substring(index, string.length());
    }

    private static boolean isEmptyChar(char c) {
        return (c == ' ' || c == ' ' || c == '\n' || c == '\t');
    }

    private static int numOfEmptyCharBeforeIndex(String string, int index) {
        index--;
        int num = 0;
        while (index >= 1) {
            if (isEmptyChar(string.charAt(index))) {
                num++;
                if (!isEmptyChar(string.charAt(index - 1))) break;
            } else {
                break;
            }
            index--;
        }
        if (index == 0 && isEmptyChar(string.charAt(index))) {
            num++;
        }
        return num;
    }

    //数组化处理
    private static String trim(String json,StoreClass storeClass) {
        if (json == null || json.length() == 0 || storeClass.go < 0 || storeClass.go >= json.length()) {
            return "";
        }
        int count = 0; //左大括号的个数

        int left = 0;
        String jsonString = "";
        String word = "";
        left = storeClass.go;
        while (storeClass.go < json.length()) {

            if (storeClass.go > 0 && json.charAt(storeClass.go) == ',') {
                word = json.substring(left, storeClass.go);
                if (count == 0) {
                    if (word.indexOf("{") > 0 && word.indexOf("}") > 0) {
                        jsonString += "},{";
                    } else {
                        jsonString += word + "},{";

                    }
                } else {
                    if (word.indexOf("{") > 0 && word.indexOf("}") > 0) {
                        jsonString += ",";
                    } else {
                        jsonString += word + ",";
                    }
                }
                left = ++storeClass.go;
            } else if (json.charAt(storeClass.go) == '{') {
                word = json.substring(left, storeClass.go);
                storeClass.go++;
                if (isArrOrList(word)) {

                    jsonString += word + "[{" + trim(json,storeClass);
                } else {
                    jsonString += json.substring(left, storeClass.go);
                    count++;
                    left = storeClass.go;
                }
            } else if (json.charAt(storeClass.go) == '}') {
                word = json.substring(left, storeClass.go);
                if (count == 0) {
                    if (word.indexOf("{") > 0 && word.indexOf("}") > 0) {
                        word = "";
                    }
                    jsonString += word + "}]";
                    return jsonString;
                } else {
                    if (word.indexOf("{") > 0 && word.indexOf("}") > 0) {
                        word = "";
                    }
                    if (storeClass.go == json.length() - 1) { //如果是最后一个字符则应该补一个右大括号
                        word = word + "}";
                    }
                    jsonString += word;
                    left = storeClass.go;
                    count--;
                }
            }
            storeClass.go++;
        }
        return jsonString;
    }

    public String json(String string,StringToJson stringToJson) throws Exception {
        StoreClass storeClass = new StoreClass();
        storeClass.cursor = 0;
        storeClass.go = 0;
        return trim(preTreatment(string,storeClass),storeClass);
    }


    public static void main(String[] args) throws Exception {
//        String string = "10985,1509840127392,PROBLEM_CHANGE,None,ArrayBuffer(10840),Some(RoleIdEq(34))";
//          String string3 =  "Ao(Ao(Ao(10,10)))";
//        String string = "Some(SimpleSubId(/mnt/disk1/hadoop/ngmr/inceptorsql1))";
//        String string = "ArrayBuffer(10,10)";


        final String string = "  ArrayBuffer(ArrayBuffer(),AlertHistory(APP(10),10984,A(A(A(A(A(1509840127341))))),1509840127341,PROBLEM_CHANGE,None,ArrayBuffer" +
                "(10833),Some(Alert(METRICS.disk.free,1509840127341,Scope(Some(ClusterIdEq(1)),Some" +
                "(ServiceIdEq(6)),Some(RoleIdEq(30)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hado" +
                "op/yarn/logs)),false),true,false,AlertResult(WARNING,List(16.43))`))), AlertHistory(1098" +
                "5,1509840127392,PROBLEM_CHANGE,None,ArrayBuffer(10840),Some(Alert(METRICS.disk.free,1509840" +
                "127392,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(8)),Some(RoleIdEq(34)),Some(NodeIdEq(2))," +
                "Some(SimpleSubId(/mnt/disk1/hadoop/ngmr/inceptorsql1)),false),true,false,AlertResult(WARNING," +
                "List(16.43))))), AlertHistory(10986,1509840127882,PROBLEM_CHANGE,None,ArrayBuffer(10846),Some" +
                "(Alert(METRICS.disk.free,1509840127882,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(10)),Some(Ro" +
                "leIdEq(15)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/kmq)),false),true,false,AlertR" +
                "esult(WARNING,List(16.43))))), AlertHistory(10987,1509840128231,PROBLEM_CHANGE,None,ArrayBuffer(1" +
                "0857),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(5))," +
                "Some(RoleIdEq(19)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/data)),false),true,false," +
                "AlertResult(WARNING,List(16.43))))), AlertHistory(10988,1509840128231,PROBLEM_CHANGE,None,ArrayBuff" +
                "er(10828),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(16" +
                ")),Some(RoleIdEq(69)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/elasticsearch/data)),false),true," +
                "false,AlertResult(WARNING,List(16.43))))), AlertHistory(10989,1509840128437,PROBLEM_CHANGE,None,ArrayBuf" +
                "fer(10848),Some(Alert(METRICS.disk.free,1509840128437,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),S" +
                "ome(RoleIdEq(23)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/namenode_dir)),false),true,false,Ale" +
                "rtResult(WARNING,List(16.43))))), AlertHistory(10990,1509840128533,CLEAR,None,ArrayBuffer(10856),Some(Aler" +
                "t(METRICS.HDFS.remaining,1509840128533,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),None,None,None,true)," +
                "true,false,AlertResult(OK,List(21.03))))),      AlertHistory(10991,1509840128669,PROBLEM_CHANGE,None,ArrayBuffer(1085" +
                "0),Some(Alert(METRICS.disk.free,1509840128669,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(6)),Some(RoleIdEq(30))," +
                "Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/yarn/local)),false),true,false,AlertResult(WARNING,List(16.43))))))";

        String string1 = "ClusterIdEq(1)";


        String string2 = "AlertHistory(10989,1509840128437,PROBLEM_CHANGE,None,ArrayBuffer(10848,10849),Some(Alert(METRICS.disk.free,1509840,128437,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),Some(RoleIdEq(23)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/namenode_dir)),false),true,false,AlertResult(WARNING,List(16.43)))))";
        String string3 = "ArrayBuffer(1.0,A(ArrayBuffer(.1,a,-1.1e-2),10),10)";
        String string4 = "ArrayBuffer(AlertHistory(10984,1509840127341,PROBLEM_CHANGE,None,ArrayBuffer(10833),Some(Alert(METRICS.disk.free,1509840127341,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(6)),Some(RoleIdEq(30)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/yarn/logs)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10985,1509840127392,PROBLEM_CHANGE,None,ArrayBuffer(10840),Some(Alert(METRICS.disk.free,1509840127392,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(8)),Some(RoleIdEq(34)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/ngmr/inceptorsql1)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10986,1509840127882,PROBLEM_CHANGE,None,ArrayBuffer(10846),Some(Alert(METRICS.disk.free,1509840127882,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(10)),Some(RoleIdEq(15)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/kmq)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10987,1509840128231,PROBLEM_CHANGE,None,ArrayBuffer(10857),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(5)),Some(RoleIdEq(19)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/data)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10988,1509840128231,PROBLEM_CHANGE,None,ArrayBuffer(10828),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(16)),Some(RoleIdEq(69)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/elasticsearch/data)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10989,1509840128437,PROBLEM_CHANGE,None,ArrayBuffer(10848),Some(Alert(METRICS.disk.free,1509840128437,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),Some(RoleIdEq(23)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/namenode_dir)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10990,1509840128533,CLEAR,None,ArrayBuffer(10856),Some(Alert(METRICS.HDFS.remaining,1509840128533,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),None,None,None,true),true,false,AlertResult(OK,List(21.03))))), AlertHistory(10991,1509840128669,PROBLEM_CHANGE,None,ArrayBuffer(10850),Some(Alert(METRICS.disk.free,1509840128669,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(6)),Some(RoleIdEq(30)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/yarn/local)),false),true,false,AlertResult(WARNING,List(16.43))))))";

        String s1 = "a(a(abc,df,eg,hj),b(123),c(456))";

        final StringToJson stringToJson = new StringToJson();
        String s = stringToJson.json(string,stringToJson);
        System.out.println(s);

//        Map<String,Object> map1 = jsonString2RecursiveMap(s);
//
//        Map<String,Object> map = jsonString2RecursiveMap("{\"a\" : 1.3}");
//        System.out.println(map);
//        Double d = 1.3;
//        System.out.println(d);

//        System.out.println(s);
//
//        System.out.println(stringToJson.json(string3,stringToJson));
//        String res = stringToJson.json(string,stringToJson);
//        int index = 0;
//        for (int i = 0 ; i < res.length(); i ++) {
//            if (res.charAt(i) == ':') {
//                index = i;
//                break;
//            }
//        }
//        String k = res.substring(1,index);
//        String v = res.substring(index + 1,res.length()-1);
//        System.out.println(k);
//        System.out.println(v);

//        long start = System.currentTimeMillis();
//
//        for (int j = 0 ; j < 10000; j++) {
//            stringToJson.json(string,stringToJson);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println((end - start) / 10000.0);

    }

    public  static Map<String,Object> jsonString2RecursiveMap(String jsonString) throws Exception{
        JSONObject jsonObject  = JSON.parseObject(jsonString);
        return toMap(jsonObject);
    }

    public static Map<String,Object> toMap(JSONObject object) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : object.keySet()) {
            Object value;
            try {
                value = object.get(key);
                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }
                else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }

                if (value instanceof BigDecimal) {
                    double v = ((BigDecimal) value).doubleValue();
                    map.put(key,v);
                }else {
                    map.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value;
            try {
                value = array.get(i);
                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }
                else if(value instanceof JSONObject) {
                    value =  toMap((JSONObject) value);
                }

                if (value instanceof BigDecimal) {
                    double v = ((BigDecimal) value).doubleValue();
                    list.add(v);
                }else {
                    list.add(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}