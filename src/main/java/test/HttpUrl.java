package test;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by zhouxw on 18/1/23.
 */
public class HttpUrl {
    public static void main(String[] args) throws Exception{
        String filepath = "#!/bin/bash\r\n" +
                "\r\n" +
                "echo \"niubility\"\r\n" +
                "\r\n" +
                "if [ -f d:/dtstack/easyagent/conf/easyagent.yml ]; then\r\n" +
                "\techo \"zhalie\"\r\n" +
                "fi" +
                "\r\n" +
                "echo \"$env:temp\"\r\n" +
                "[ -f $env:temp/zlib.dll ] && echo \"wangzha*2\"\r\n";
//        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
//        Yaml yaml = new Yaml();
//        Map map = (Map) yaml.load(fileInputStream);
//        System.out.println(map);

        StringTokenizer stringTokenizer = new StringTokenizer(filepath,"\r\n");
        System.out.println(stringTokenizer.countTokens());

        while (stringTokenizer.hasMoreTokens()) {
           System.out.println(stringTokenizer.nextElement().toString());
            System.out.println(stringTokenizer.countTokens());

        }



    }
}
