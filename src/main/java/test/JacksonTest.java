package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.jdbc.Null;
import org.junit.Test;
import test.Po.Product;
import test.Po.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhouxw on 17/12/6.
 */
public class JacksonTest {

    @Test
    public void ojb2Json() throws Exception{

//        String log = "save new alert historiesArrayBuffer(AlertHistory(10984,1509840127341,PROBLEM_CHANGE,None,ArrayBuffer(10833),Some(Alert(METRICS.disk.free,1509840127341,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(6)),Some(RoleIdEq(30)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/yarn/logs)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10985,1509840127392,PROBLEM_CHANGE,None,ArrayBuffer(10840),Some(Alert(METRICS.disk.free,1509840127392,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(8)),Some(RoleIdEq(34)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/ngmr/inceptorsql1)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10986,1509840127882,PROBLEM_CHANGE,None,ArrayBuffer(10846),Some(Alert(METRICS.disk.free,1509840127882,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(10)),Some(RoleIdEq(15)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/kmq)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10987,1509840128231,PROBLEM_CHANGE,None,ArrayBuffer(10857),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(5)),Some(RoleIdEq(19)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/data)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10988,1509840128231,PROBLEM_CHANGE,None,ArrayBuffer(10828),Some(Alert(METRICS.disk.free,1509840128231,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(16)),Some(RoleIdEq(69)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/elasticsearch/data)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10989,1509840128437,PROBLEM_CHANGE,None,ArrayBuffer(10848),Some(Alert(METRICS.disk.free,1509840128437,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),Some(RoleIdEq(23)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/namenode_dir)),false),true,false,AlertResult(WARNING,List(16.43))))), AlertHistory(10990,1509840128533,CLEAR,None,ArrayBuffer(10856),Some(Alert(METRICS.HDFS.remaining,1509840128533,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(14)),None,None,None,true),true,false,AlertResult(OK,List(21.03))))), AlertHistory(10991,1509840128669,PROBLEM_CHANGE,None,ArrayBuffer(10850),Some(Alert(METRICS.disk.free,1509840128669,Scope(Some(ClusterIdEq(1)),Some(ServiceIdEq(6)),Some(RoleIdEq(30)),Some(NodeIdEq(2)),Some(SimpleSubId(/mnt/disk1/hadoop/yarn/local)),false),true,false,AlertResult(WARNING,List(16.43))))))";
//        int arrayBufferIndex = log.indexOf("ArrayBuffer");
//        String stripLog = log.substring(arrayBufferIndex);  //得到在ArrayBuffer的日志(包括ArrayBuffer字符串)
//        String bufferArrayEliminatedLog = stripLog.substring("ArrayBuffer(".length(),stripLog.length()-1);  //去除ArrayBuffer()字符串
//
//
//        String[] alertHistoryArray = bufferArrayEliminatedLog.split("\\s*[,|,]\\s*(?=AlertHistory)");
//
//
//        System.out.println(alertHistoryArray[0]);
//
//        for (String s : alertHistoryArray) {
//            System.out.println(s);
//        }




        User user = new User();
        user.setName("wang");
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(produc1);
//        arrayList.add(produc2);
        Product produc1 = new Product(100.f,1,null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produc1);
        System.out.println(json);
//        Product product3 = mapper.readValue(json,Product.class);
//        System.out.println(product3.getName());


    }
    @Test
    public void test2() {
        String s = "   aba baba   ";

        char c = 'a';
        String b = "b";

//        String[] strings = s.split("b");
//        System.out.println(strings.length);
//        for (String s1 : strings) {
//            System.out.println(s1);
//        }
//        System.out.println(s.trim().length());
        for (int i = 0; i < 10; i++ ) {
            System.out.println(new Thread().getName());
        }
//        System.out.println(Thread.currentThread().getName());
    }

}
