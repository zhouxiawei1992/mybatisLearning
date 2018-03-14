package grok;



import io.thekraken.grok.api.Match;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 18/3/5.
 */

public class AutoGenTemplate {
    public static String userAgentRegex = "(?<USERAGENT>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?<ubantu>(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?)\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))|(?<name1>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}Gecko/[\\d\\w.\\s/]+))|(?<name2>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}(?:\\s{0,3}like\\s{0,3}Gecko)?))|(?<name3>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s{0,5}\\([^\\)]+\\)\\s{0,5}Version/\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))";
    public static String quotationRegex = "\"[^\"]*\"";
    public static String squareBracketRegex = "\\[[^]]*\\]";
    public static String parenthesisRegex = "\\([^)]*\\)";
    public static Pattern patternName = Pattern.compile("<[^>]+>");
    public static Pattern filterPattern = Pattern.compile("%\\{[^}]+\\}");
    private static final String GROK_PATTERN_PATH = "/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns";
    public static Map<String, String> originalPatternMap = new HashMap<String, String>();
    public static Map<String, String> trimedPatternMap = new HashMap<String, String>();

    public void init() throws Exception{
        originalPatternMap = createPatternMap(GROK_PATTERN_PATH);
        trimOriginalPatternMap();
    }

    public static Map<String, String> createPatternMap(String patternPath) throws Exception {
        Map<String, String> map = new TreeMap<String, String>();
        if (StringUtils.isBlank(patternPath)) {
            throw new Exception("patternPath should not be empty or null");
        }else {
            File file = new File(patternPath);
            if (!file.exists()) {
                throw new Exception("pattern file does not exist!");
            }else if (!file.canRead()) {
                throw new Exception("pattern file can not read!");
            }
            Pattern pattern = Pattern.compile("^([A-z0-9_]+)\\s+(.*)$");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                Matcher e = pattern.matcher(line);
                if (e.matches()) {
                    map.put(e.group(1),e.group(2));
                }
            }
            br.close();
        }
        return map;
    }
    public String trim(String field) throws Exception{
        String value = originalPatternMap.get(field);
        if (value == null) {
            System.out.println(field + "---" + value);
            throw new Exception("field can not find value");
        }
        if (!value.contains("%{")) {
            return  value;
        }else {
            Matcher matcher = filterPattern.matcher(value);
            while (matcher.find()) {
                String group = matcher.group();
                String subKey = group.substring(2,group.length() - 1);
                String replacement = trim(subKey);
                value = value.replace(group,replacement);
            }
            return value;
        }
    }
    public void trimOriginalPatternMap() throws Exception{
        for (String key : originalPatternMap.keySet()) {
            trimedPatternMap.put(key,trim(key));
        }
    }

    public static void main(String[] args) throws Exception{

        AutoGenTemplate autoGenTemplate = new AutoGenTemplate();
        autoGenTemplate.init();
        Pattern pattern = Pattern.compile("(?<chromeOrEdgeAgent>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?<ubantu>(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?)\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))");
        String sss = "10.1.0.105 - - 24/Jan/2016:22:21:28 +0800 \"GET / HTTP/1.1\" 304 0 \"-\" \"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36\" \"-\"";
        String log = "  10.1.0.105\t|24/Jan/2016:22:20:21 +0800|GET / HTTP/1.1|304|0|-|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36|-|-|-|0.000|-";
        String log1 = "2010-07-30 01:06:43 192.168.0.102 - W3SVC1 MGL 192.168.0.102 80 GET /css/rss.xslt - 304 0 140 358 0 HTTP/1.1 192.168.0.102 Mozilla/4.0+(compatible;+MSIE+7.0;+Windows+NT+5.1;+Trident/4.0;+InfoPath.2;+360SE) ASPSESSIONIDACRRDABA=IDDHCBBBHBMBODAGCIDKAGLM -";
        String apaLog = "192.168.1.2 - - [02/Feb/2016:17:44:13 +0800] \"GET /favicon.ico HTTP/1.1\" 404 209 \"http://localhost/x1.html\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String nodeLog = "[2016-01-31 12:02:25.84] [INFO] access - 42.120.73.203 - - \"GET /user/projects/ali_sls_log?ignoreError=true HTTP/1.1\" 304 - \"http://\n" +
                "aliyun.com/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String tomCatLog = "127.0.0.1 192.168.254.108 - -1 127.0.0.1 HTTP/1.1 - GET 80&<60; GET /rightmainima/leftbott4.swf HTTP/1.1 304 5563A67708646B6AA299C33D59BE132A [22/Sep/2007:10:08:52 +0800] - /rightmainima/leftbott4.swf localhost 0 0.000";
        String sub = "POST /api HTTP/1.1";
        String ll = "2010-07-30";
        String tmp = "https://www.baidu.com/s?wd=%E5%8F%AF%E8%83%BD%E7%9A%84%E6%97%B6%E9%97%B4%E6%A0%BC%E5%BC%8F&rsv_spt=1&rsv_iqid=0xd503fc06000500f3&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&oq=%25E6%2597%25B6%25E5%2588%2586%25E7%25A7%2592%25E6%2597%25B6%25E9%2597%25B4%25E6%25A0%25BC%25E5%25BC%258F&rsv_t=4b79tpN5ab1PwFp5kdPLo%2B58cJeaFb5Gn6KT%2BGqxi%2FOwFlDzGSDiNapKJF5lf1KMvJ8h&rsv_pq=93cb9e7f0000b57e&inputT=9621&rsv_sug3=106&rsv_sug1=81&rsv_sug7=100&rsv_sug2=0&rsv_sug4=9621";
        String access01 = "27.194.142.75 - - [13/Mar/2018:09:48:32 +0800] \"POST /api HTTP/1.1\" 200 240";
        String access02 = "125.120.161.43 - - [13/Mar/2018:19:07:54 +0800] \"POST /app_activityPopUp.do HTTP/2.0\" 200 160 \"-\" \"GJB4iPhone/2.8.6 (iPhone; iOS 10.3.2; Scale/2.00)\" \"-\" \"0.011\"";
        ArrayList<String> unparsedLogList = autoGenTemplate.getUnparsedLogList(apaLog);
        Map<String, String> patternMap = autoGenTemplate.createPatternMap("/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns");
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        ArrayList<String> arrayList = autoGenTemplate.translate(ll);
        System.out.println(arrayLists);
        String s = "(?<URL>(?:(?:[HhTtPpSsMmOo3Ff]{3,5}://)?(?:(?<name0>(?:(?:[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(?:\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+)))|(?<name1>(?:(?<name2>(?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9])):(?:\\d{2,5})?)))))\n";
        String r = autoGenTemplate.modifyPatternName(s,"xxxx");
        System.out.println(r);
        System.out.println(apaLog);
        System.out.println("ss");

    }

    public String modifyPatternName(String patternString,String name) {
        Matcher matcher = patternName.matcher(patternString);
        String result = patternString;
        while (matcher.find()) {
            result = result.replace(matcher.group(),"<" + name + ">");
            break;
        }
        return result;
    }

    public ArrayList<String> getUnparsedLogList(String s) {
        String spliter = getSpliterChar(s);
        ArrayList  arrayList = new ArrayList<String>();
        int numOfSpace = 0;
        int from  = 0;
        int numOfDQ = 0;
        boolean stop = false;
        if (!StringUtils.isBlank(s)) {
            if (!" ".equalsIgnoreCase(spliter)) {
                arrayList = new ArrayList(Arrays.asList(s.split(spliter)));
                return arrayList;
            }
           for (int i = 0; i < s.length(); i++) {
               if (s.charAt(i) == ' ' && !stop) {
                   numOfSpace++;
                   if (i + 1 < s.length() && s.charAt(i + 1) != ' ') {
                       arrayList.add(s.substring(from,i).trim());
                       arrayList.add(numOfSpace + "###");
                       numOfSpace = 0;
                       from = i + 1;
                   }
               }else if (s.charAt(i) == '[') {
                   stop = true;
               }else if (s.charAt(i) == ']'){
                   stop = false;
               }else if (s.charAt(i) == '\"') {
                    if (numOfDQ == 0) {
                        numOfDQ++;
                        stop = true;
                    }else {
                        numOfDQ = 0;
                        stop = false;
                    }
               }
           }
           arrayList.add(s.substring(from,s.length()).trim());
           arrayList.add(numOfSpace + "###");
        }
        return arrayList;
    }

    public String getSpliterChar(String log) {
        log = log.replaceAll(userAgentRegex,"");
        log = log.replaceAll(quotationRegex,"");
        log = log.replaceAll(squareBracketRegex,"");
        log = log.replaceAll(parenthesisRegex,"");
        ArrayList<Character> arrayList = new ArrayList<Character>();
        for (int i = 0; i < log.length(); i++) {
            char c = log.charAt(i);
            if (c <= '9' && c >= '0' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '.' || c == '/' || c == '+' || c == '-' ||
                    c == ':' || c == '/' || c == '\\' || c == '=') {
            }else {
                arrayList.add(c);
            }
        }

        int most = 0;
        int second = 0;
        char c1 = ' ';
        char c2 = ' ';
        HashMap<Character,Integer> hashMap = new HashMap();
        for (int j = 0; j < arrayList.size(); j++) {
            Integer i = hashMap.get(arrayList.get(j));
            hashMap.put(arrayList.get(j),i == null ? 0 : ++i);
        }
        for (Character character : hashMap.keySet()) {
            if (hashMap.get(character) >= most) {
                second = most;
                most = hashMap.get(character);
                c1 = character;
                c2 = c1;
            } else if (hashMap.get(character) > second) {
                second = hashMap.get(character);
                c2 = character;
            }
        }
        char result = ' ';
        if (c1 != ' ') {
            result  = c1;
        }else {
            if (second / 1.0 * most >= 0.6) {
                result = c2;
            }
        }
        if (result == ' ') {
            return result + "";
        }
        String patternString = "\\|{2,5}";


        if (needsTransfer(result)) {
            patternString = patternString.replace("|",result + "");
        }else {
            patternString = result + "{2,5}";
        }

        Pattern multiSpliterPattern = Pattern.compile(patternString);
        Matcher matcher = multiSpliterPattern.matcher(log);
        int multiNum = 0;
        String spliter = result + "";
        String expectedSpliter = "";
        while (matcher.find()) {
            String s = matcher.group();
            if (!s.equalsIgnoreCase("")) {
                expectedSpliter = matcher.group();
                System.out.println(matcher.group()+ " " + matcher.start() + " " + matcher.end());
                multiNum++;
            }
        }
        if (multiNum > 3) {
            spliter = patternString.replaceAll("2,5","" + expectedSpliter.length());
        }else {
            spliter = patternString.replaceAll("2,5",1 + "");
        }
       return spliter;
    }

    public boolean needsTransfer(char c) {
        if (c == '.' || c == '|' || c == '$' || c == '￥' || c == '?' || c == '+' || c == '-'
                || c == '*' || c == '\t' || c == ' ') {
            return true;
        }else {
            return false;
        }
    }

    public String concatenate(ArrayList<ArrayList<String>> arrayLists) {
        StringBuffer stringBuffer = new StringBuffer("");

        int index = 0;
        for (ArrayList<String> arrayList : arrayLists) {
            if (arrayList.size() == 1) {
                stringBuffer.append(arrayList.get(0));
            }else {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).length() < arrayList.get(index).length()) {
                        index = i;
                    }
                }
                stringBuffer.append(arrayList.get(index));
                index = 0;
            }
        }
        return stringBuffer.toString();
    }

    public ArrayList<ArrayList<String>> translate(ArrayList<String> unparsedList) throws Exception{
        ArrayList<ArrayList<String>> arrayLists = new ArrayList();
        for (String unparsedString : unparsedList) {
            ArrayList<String> arrayList = translate(unparsedString);
            if (arrayList.size() == 0) {
                String result = "";
                if (unparsedString.contains("###")) {
                    unparsedString = "" + Integer.valueOf(unparsedString.substring(0,unparsedString.length() - 3));
                    result =  "[\\S\\s]{x}".replace("x",unparsedString);
                }else {
                    //todo
                    result = unparsedString;
                }
                arrayList.add(result);
            }
            arrayLists.add(arrayList);
        }
        return arrayLists;
    }

    public ArrayList<String> translate(String log) {
        int frontSpace = 0;
        int endSpace = 0;
        String frontSpacePattern = "\\s{1,x}";
        String endSpacePattern = "\\s{1,x}";
        String quotation = "\"";
        String leftSquareBracket = "\\[";
        String rightSquareBracket = "\\]";
        String frontAppendix = "";
        String endAppendix = "";

        int i = 0,j = log.length() - 1;
        while (i <= log.length() - 1) {
            if (log.charAt(i) == ' ' || log.charAt(i) == '\t') {
                frontSpace++;
                i++;
            }else {
                break;
            }
        }
        while (j >= 0) {
            if (log.charAt(j) == ' ' || log.charAt(j) == '\t') {
                endSpace++;
                --j;
            }else {
                break;
            }
        }
        if (frontSpace > 0 || endSpace > 0) {
            log = log.substring(frontSpace,log.length() - endSpace);
            if (frontSpace != 0) {
                frontSpacePattern = frontSpacePattern.replace("x",frontSpace + "");
                frontAppendix = frontSpacePattern;
            }
            if (endSpace != 0) {
                endSpacePattern = endSpacePattern.replace("x",endSpace + "");
                endAppendix = endSpacePattern;
            }
        }

        frontSpacePattern = frontSpacePattern.contains("x") ? "" : frontSpacePattern;
        endSpacePattern = endSpacePattern.contains("x") ? "" : endSpacePattern;
        if (log.charAt(0) == '[' && log.charAt(log.length() -1) == ']') {
            frontAppendix  = frontSpacePattern + leftSquareBracket;
            endAppendix = rightSquareBracket + endSpacePattern;
            log = log.substring(1,log.length() - 1);
        }
        if (log.charAt(0) == '\"' && log.charAt(log.length() - 1) == '\"') {
            frontAppendix = frontSpacePattern + quotation;
            endAppendix = quotation + endSpacePattern;
            log = log.substring(1,log.length() - 1);
        }

        ArrayList<String> arrayList = new ArrayList<String>();
        for (String key : trimedPatternMap.keySet()) {
            String resultStr = "";
            String pattern = trimedPatternMap.get(key);
            if (log.matches(pattern)) {
                resultStr = "(?<xx>" + pattern + ")";
                resultStr = resultStr.replace("xx",key);
                if (frontAppendix.length() > 0) {
                    resultStr = frontAppendix + resultStr;
                }
                if (endAppendix.length() > 0) {
                    resultStr = resultStr + endAppendix;
                }
                arrayList.add(resultStr);
                System.out.println(resultStr);
            }
        }
        return arrayList;
    }


}
