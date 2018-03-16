package grok;



import org.apache.commons.lang3.StringUtils;

import java.io.*;
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
    public static String spliter = " ";
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

    }



    public void performance(String regex, String log) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            String myStr = new String(log);
            Pattern pattern1 = Pattern.compile(regex);
            Matcher matcher1 = pattern1.matcher(myStr);
            System.out.println(matcher1.matches());
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0);
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
        spliter = getSpliterChar(s);
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
                    c == ':' || c == '/' || c == '\\' || c == '=' || c == '^') {
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
        ArrayList<String> collect = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer("");
        int index = 0;
        for (ArrayList<String> arrayList : arrayLists) {
            if (arrayList.size() == 1) {
                collect.add(arrayList.get(0));
            }else {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).contains("(?<NUM")) {
                        index = i;
                        break;
                    }
                    if (arrayList.get(i).length() < arrayList.get(index).length()) {
                        index = i;
                    }
                }
                collect.add(arrayList.get(index));
                index = 0;
            }
        }

        for (int i = 0; i < collect.size(); i++) {
            String tmp = collect.get(i);
            String group = "";
            int repeat = 1;
            if (tmp.contains("(?<")) {
                Matcher matcher = patternName.matcher(tmp);
                while (matcher.find()) {
                    group = matcher.group();
                    break;
                }
                for (int j = i + 1; j < collect.size();j++) {
                    if (collect.get(j).contains(group)) {
                        String replace = group.substring(1,group.length() - 1);
                        replace = replace + repeat;
                        ++repeat;
                        String s = modifyPatternName(collect.get(j),replace);
                        collect.set(j,s);
                    }
                }
            }
        }
        for (int i = 0; i < collect.size() - 1; i++) {
            String str = collect.get(i);
            if (!spliter.equalsIgnoreCase(" ")) {
                str = collect.get(i) + spliter;
            }
            stringBuffer.append(str);
        }
        stringBuffer.append(collect.get(collect.size() - 1));
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

                    String token = " ".equalsIgnoreCase(spliter) ? " " : spliter.substring(1,spliter.length() - 3);
                    String tmp = "[^xxxx]+".replace("xxxx",token);
                    if (!unparsedString.contains(token)) {
                        result = tmp;
                    }else {
                        Matcher matcher = Pattern.compile("[xxxx]".replace("xxxx",token)).matcher(unparsedString);
                        for (int i = 0; i < unparsedString.split("[xxxx]".replace("xxxx",token)).length;i++) {
                            result = result + tmp + spliter;
                        }
                        result = result.substring(0,result.length() - spliter.length());

                    }
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
                if (frontAppendix.length() > 0) {
                    pattern = frontAppendix + pattern;
                }
                if (endAppendix.length() > 0) {
                    pattern = pattern + endAppendix;
                }
                pattern = "(?:xxxxxx)".replace("xxxxxx",pattern);
                String tmp = "|(?:[^xxxx\n]+)".replace("xxxx",spliter);
                pattern = pattern + tmp;
                resultStr = "(?<xx>" + pattern + ")";
                resultStr = resultStr.replace("xx",key);
                arrayList.add(resultStr);
            }
        }
        return arrayList;
    }

    public ArrayList<String> getCapturedGroupNames(String patternString) {
         Pattern namePattern = Pattern.compile("\\(\\?<[^>!=?]+>");
        ArrayList<String> arrayList = new ArrayList<String>();
        Matcher matcher = namePattern.matcher(patternString);
        while (matcher.find()) {
            String name = matcher.group().substring(3,matcher.group().length() - 1);
            arrayList.add(name);
        }
        return arrayList;
    }

    public static String convertFileToString(String path, String encoding) {
        File file = new File(path);
        if (!file.exists()) return null;
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
            } else {
                reader = new InputStreamReader(new FileInputStream(file));
            }
            //将输入流写入输出流
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //返回转换结果
        if (writer != null)
            return writer.toString();
        else return null;
    }

     public String updateRegExOnNum(String regEx,ArrayList<String> logs) {

         if (logs.size() > 50) {
             logs = new ArrayList<String>(logs.subList(0,50));
         }
         ArrayList<String> arrayList = getCapturedGroupNames(regEx);
         ArrayList<String> numGroupList = new ArrayList<String>();
         ArrayList<Map<String,String>> replaceBeans = new ArrayList<Map<String, String>>();
         String result = new String(regEx);
         String pickedGroupName = "";
         for (String s :arrayList) {
             if (s.contains("NUM")) {
                numGroupList.add(s);
             }
         }
         if (arrayList.size() < 1) return result;
         for (String log : logs) {
            Map<String,String> m = getCapturedResultByGroupNames(regEx,numGroupList,log);
            for (int i = 0; i < numGroupList.size(); i++) {
                if (m.get(numGroupList.get(i)) != null && !state(m.get(numGroupList.get(i)))) {
                    numGroupList.remove(i);
                }
                if (numGroupList.size() < 1) {
                    return result;
                }
            }
         }
         pickedGroupName = numGroupList.get(0);
         Map<String,String> replaceBean = new HashMap<String, String>();
         replaceBean.put("old",pickedGroupName);
         replaceBean.put("new","state");
         replaceBeans.add(replaceBean);
         result = updateRegEx(regEx,replaceBeans);
         return result;
    }

    public boolean state(String num) {
        int no = Integer.valueOf(num);
        if ( (no >= 100 && no < 110) || (no >= 200 && no < 210) || (no >= 300 && no < 310) || (no >= 400 && no < 420) || (no >= 500 && no < 510)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("all")
    public Map<String,String> getCapturedResultByGroupNames(String regEx, ArrayList<String> nameArrayList, String log) {
        Map<String,String> resultMap = new HashMap<String, String>();
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(log);
        while (matcher.find()) {
            int i = 0;
            while (i < nameArrayList.size()) {
                try {
                    String matchedResult = matcher.group(nameArrayList.get(i));
                    resultMap.put(nameArrayList.get(i),matchedResult);
                }catch (IllegalArgumentException iae) {

                }
                i++;
            }
        }
        return resultMap;
    }

    public Map<String,String> getCapturedResult(String regEx, String log) throws Exception{
        ArrayList<String> nameArrayList = getCapturedGroupNames(regEx);
        return getCapturedResultByGroupNames(regEx,nameArrayList,log);
    }

    /***
     *
     * @param regEx
     * @param replaceBeans (old:value(old),new:value(new))
     * @return
     */
    public String updateRegEx(String regEx,ArrayList<Map<String,String>> replaceBeans) {
        String newRegEx = new String(regEx);
        if (replaceBeans.size() > 0) {
            for (Map<String,String> replaceBean : replaceBeans) {
                String old = replaceBean.get("old") == null ? "xxxxxxxx" : replaceBean.get("old");
                String newValue = replaceBean.get("new") == null ? "xxxxxxxx" : replaceBean.get("new");
                newRegEx = newRegEx.replace("?<" + old + ">", "?<" + newValue + ">");
            }
        }
        return newRegEx;
    }





}
