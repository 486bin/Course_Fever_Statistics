import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CreateLogs {
    public static void main(String[] args) throws Exception {
        FileWriter fileWriter = new FileWriter(args[0]);
        while (true) {
            String line = genDateTime() + "," + genIP() + ",http://" + genSearchEngine() + "/course/" + genTeacher() + genCourse() + ".html";
            fileWriter.write(line + "\n");
            fileWriter.flush();
            Thread.sleep(new Random().nextInt(10)*100);
        }
    }

    /**
     * 生成日期时间    2018-01-23 15:32:21
     *
     * @return
     */
    public static String genDateTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


    /**
     * 生成ip
     *
     * @return ip
     */
    public static String genIP() {
        String[] ips = {"125.126.218.77", "113.122.169.78", "157.0.210.242", "123.163.117.112", "111.72.154.247", "59.62.164.89", "116.209.52.26", "183.47.2.201", "182.44.225.91", "111.177.176.10", "125.123.140.73", "112.85.166.148", "110.52.235.154", "114.104.135.187"};
        return ips[new Random().nextInt(ips.length)];
    }

    /**
     * 生成搜索引擎
     *
     * @return
     */
    public static String genSearchEngine() {
        String engines[] = {"www.baidu.com", "www.so.com", "www.google.com", "www.sogou.com", "www.bing.com", "www.search.yahoo.com"};
        return engines[new Random().nextInt(engines.length)];
    }


    /**
     * 生成老师名称
     */
    public static String genTeacher() {
        String teachers[] = {"mlq", "xiaozhao", "laowang", "dazhuang", "canglaoshi", "xiaoze", "daming", "xiaojia", "xiaodang", "feifei", "lili", "baoer"};
        return teachers[new Random().nextInt(teachers.length)];
    }


    /**
     * 生成课程
     *
     * @return
     */
    public static String genCourse() {
        String[] courses = {"/vip/100", "/vip/101", "/vip/102", "/vip/103", "/vip/104", "/vip/105", "/vip/106", "/vip/107", "/vip/108", "/vip/109", "/normal/201", "/normal/202", "/normal/203", "/normal/204", "/normal/205", "/normal/206", "/normal/207", "/normal/208", "/normal/209"};
        return courses[new Random().nextInt(courses.length)];
    }
}
