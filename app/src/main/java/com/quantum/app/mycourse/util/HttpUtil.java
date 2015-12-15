package com.quantum.app.mycourse.util;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by hua on 2015/10/10.
 */

public class HttpUtil {

    private String TAG = "HttpUtil";

    public String firstCookie = "获取firstCookie失败";
    public String logonNumber = "获取验证码失败";
    public boolean loginResult = false;
    public String loginCookie = "获取loginCookie失败";
    public String courseHtml = "获取课表失败";
    public String scoreHtml = "获取课程成绩失败";
    public String studentCode = "获取学号失败";
    public String studentPassword = "获取密码失败";

    public  String dayCourse = "获取每日课表失败";
    public  String weekCourse = "获取每周课表失败";
    public String mScore = "获取成绩失败";
    public double mGradePoint = 0;

    public List<String> dataList;

    /*获取初始cookie*/
    public void getFirstCookie() {
        URL url;
        HttpURLConnection conn;
        String session_value;
        try {
            url = new URL("http://jwc.wyu.edu.cn/student/");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                session_value = conn.getHeaderField("Set-Cookie");
                String[] sessionId = session_value.split(";");
                firstCookie = sessionId[0];
                Log.d(TAG, "firstCookie is -->" + firstCookie);

                /*获取验证码*/
                getLogonNumber();
            } else {
                throw new Exception("Error occur when try to visit the url:" + url.getPath());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*获取验证码*/
    public String getLogonNumber() {
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL("http://jwc.wyu.edu.cn/student/rndnum.asp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Cookie", firstCookie);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                String key = conn.getHeaderField("Set-Cookie");
                String[] bitmapHead = key.split(";");
                String bitmapBody = bitmapHead[0];
                String[] bitmapLast = bitmapBody.split("=");
                logonNumber = bitmapLast[1];
                Log.d(TAG, "logonNumber is -->" + logonNumber);
            } else {
                throw new Exception("Error occur when try to visit the url:" + url.getPath());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return logonNumber;
    }

    /*获取登录状态cookie*/
    public boolean login(String sCode, String pCode) {
        studentCode = sCode;
        studentPassword = pCode;
        URL url;
        HttpURLConnection conn;
        getFirstCookie();
        try {
            url = new URL("http://jwc.wyu.edu.cn/student/logon.asp");
            firstCookie = "NGID=5c3c21f7-3b82-2734-20b7-236b82faad2a;"
                    + "5c3c21f7-3b82-2734-20b7-236b82faad2a=http%3A//jwc.wyu.edu.cn/student/body.htm;"
                    + firstCookie + ";"
                    +"LogonNumber="+logonNumber;
            Log.d("InitData", "firstCookieInLogin is " + firstCookie);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cookie", firstCookie);
            conn.setRequestProperty("Referer", "http://jwc.wyu.edu.cn/student/body.htm");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            String content = "UserCode=" + URLEncoder.encode(sCode, "UTF-8")
                    + "&UserPwd=" + URLEncoder.encode(pCode, "UTF-8")
                    + "&Validate=" + URLEncoder.encode(logonNumber, "UTF-8")
                    + "&Submit=%CC%E1+%BD%BB";
            Log.d(TAG, "postContent is -->" + content);
            out.writeBytes(content);
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
                loginCookie = firstCookie.replace(logonNumber, "");
                Log.d("TAG", "logonCookie is -->" + loginCookie);
                loginResult = true;
                Log.d(TAG, "loginResult is -->" + loginResult);

                //获取课表
                getCourse();
                //获取成绩分数
                getScore();
            } else {
                throw new Exception("Error occur when try to visit the url:" + url.getPath() + " using HttpURLConnection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResult;
    }

    /*获取课表*/
    public String getCourse() {
        URL url;
        HttpURLConnection conn;
        String data;
        try {
            url = new URL("http://jwc.wyu.edu.cn/student/f3.asp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", loginCookie);
            conn.setRequestProperty("Referer", " http://jwc.wyu.edu.cn/student/menu.asp");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));
                data = "";
                while ((data = br.readLine()) != null) {
                    sb.append(data + "\n");
                }
                courseHtml = sb.toString();
                Log.d(TAG, "courseHtml is -->" + courseHtml);

                //分析每日课程html代码
                dayCourse = analyzeDayCourse();
                //分析每周课程html代码
                weekCourse = analyzeWeekCourse();
            } else {
                throw new Exception("Error occur when try to visit the url:" + url.getPath() + " using HttpURLConnection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseHtml;
    }

    /*获取课程成绩HTML*/
    public String getScore() {
        URL url;
        HttpURLConnection conn;
        String data;
        try {
            url = new URL("http://jwc.wyu.edu.cn/student/f4_myscore11.asp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", loginCookie);
            conn.setRequestProperty("Referer", " http://jwc.wyu.edu.cn/student/menu.asp");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));
                data = "";
                while ((data = br.readLine()) != null) {
                    sb.append(data + "\n");
                }
                scoreHtml = sb.toString();
                Log.d(TAG, "scoreHtml is -->" + scoreHtml);

                //分析成绩Html代码
                mScore = analyzeScore();
                //分析绩点
                mGradePoint = analyzeGradePoint();
            } else {
                throw new Exception("Error occur when try to visit the url:" + url.getPath() + " using HttpURLConnection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreHtml;
    }

    public static String getDate(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="星期天";
        }else if("2".equals(mWay)){
            mWay ="星期一";
        }else if("3".equals(mWay)){
            mWay ="星期二";
        }else if("4".equals(mWay)){
            mWay ="星期三";
        }else if("5".equals(mWay)){
            mWay ="星期四";
        }else if("6".equals(mWay)){
            mWay ="星期五";
        }else if("7".equals(mWay)){
            mWay ="星期六";
        }
        return mWay;
    }

    public String analyzeDayCourse() {
        String dayCourse = "";
        try {
            Document document = Jsoup.parse(courseHtml);
            Elements table  = document.getElementsByTag("table");
            Element table1 = table.get(0);
            Element table2 = table.get(1);
            Document table1Doc = Jsoup.parse(table1.toString());
            Elements tbody1 = table1Doc.select("tbody").select("tr");
            Log.d("Day", "tbody1 size is " + tbody1.size());
            for (int i = 0; i < tbody1.size(); i++) {
                Elements tds = tbody1.get(i).select("td");
                for (int j = 0; j < tds.size(); j++) {
                    Log.d(TAG, "tds " + j + " is -->" + tds.get(j).text());
                }
            }
            Document table2Doc = Jsoup.parse(table2.toString());
            Elements tbody2 = table2Doc.select("tbody").select("tr");
            int index = 0;
            for (int i = 0; i < tbody2.size(); i++) {
                Elements tds = tbody2.get(i).select("td");
                for (int j = 0; j < tds.size(); j++) {
                    String content1 = tds.get(j).text().replaceAll(Jsoup.parse("&nbsp").text(), "");
                    if (getDate().equals(content1)) {
                        index = j;
                        break;
                    }
                }
                if (index != 0){
                    break;
                }
            }
            for (int i = 1; i < 6; i++){
                Elements tdsDay = tbody2.get(i).select("td");
                String tdsDayValue = tdsDay.get(index).text().replaceAll(Jsoup.parse("&nbsp").text(), "");
                Log.d("DayFragment", tdsDayValue);
                if (!TextUtils.isEmpty(tdsDayValue)) {
                    dayCourse += tdsDayValue +";";
                } else {
                    dayCourse += "没课" +";";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayCourse;
    }

    public String analyzeWeekCourse() {
        String weekCourse = "";
        try {
            Document document = Jsoup.parse(courseHtml);
            Elements table  = document.getElementsByTag("table");
            Element table1 = table.get(0);
            Element table2 = table.get(1);
            Document table1Doc = Jsoup.parse(table1.toString());
            Elements tbody1 = table1Doc.select("tbody").select("tr");
            Log.d("Day", "tbody1 size is " + tbody1.size());
            for (int i = 0; i < tbody1.size(); i++) {
                Elements tds = tbody1.get(i).select("td");
                for (int j = 0; j < tds.size(); j++) {
                    Log.d(TAG, "tds " + j + " is -->" + tds.get(j).text());
                }
            }
            Document table2Doc = Jsoup.parse(table2.toString());
            Elements tbody2 = table2Doc.select("tbody").select("tr");
            for (int i = 0; i < (tbody2.size()-1); i++) {
                Elements tds = tbody2.get(i).select("td");
                for (int j = 0; j < tds.size(); j++) {
                    String wCourse = tds.get(j).text().replaceAll(Jsoup.parse("&nbsp").text(), "");
                    Log.d(TAG, "wCourse " + j + " is -->" +wCourse);
                    if (!TextUtils.isEmpty(wCourse)) {
                        weekCourse += wCourse + ";";
                    } else {
                        weekCourse += "没课" + ";";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekCourse;
    }

    public String analyzeScore() {
        String mScore = "";
        try {
            Document document = Jsoup.parse(scoreHtml);
            Log.d(TAG, "scoreHtml is -->" + scoreHtml);
            Elements table = document.getElementsByTag("table");
            Log.d(TAG, "table's size is -->" + table.size());
            for (int i = 1; i < (table.size() - 2); i++) {
                Element mTable = table.get(i);
                Document mDocument = Jsoup.parse(mTable.toString());
                Elements mElements = mDocument.select("tr");
                Log.d(TAG, "mElements is -->" + mElements.size());
                for (int j = 1; j < mElements.size(); j++) {
                    Elements tds = mElements.get(j).select("td");
                    String courseName = tds.get(1).text();
                    String courseScore = tds.get(4).text();
                    String courseResult = courseName + "~" + courseScore;
                    Log.d(TAG, "tds " + i + " is -->" + courseName);
                    Log.d(TAG, "tds " + j + " is -->" + courseScore);
                    Log.d(TAG, "courseResult is  -->" + courseResult);
                    mScore += courseResult + ";";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mScore;
    }

    public double analyzeGradePoint() {
        double mGradePoint = 0;
        double score = 0;
        double averTime = 0;
        double scoreTotal = 0;
        double averScore;
        int scoreTime = 0;
        try {
            Document document = Jsoup.parse(scoreHtml);
            Elements table  = document.getElementsByTag("table");
            for(int i = 1; i < (table.size()-2); i ++){
                Element mTable = table.get(i);
                Document mDocument = Jsoup.parse(mTable.toString());
                Elements mElements = mDocument.select("tr");
                for (int j = 1; j < mElements.size(); j++) {
                    Elements tds = mElements.get(j).select("td");
                    String courseName = tds.get(1).text();
                    String courseScore =  tds.get(4).text();
                    Pattern pattern1 = Pattern.compile("[0-9]*");
                    Pattern pattern2 = Pattern.compile("^[-\\+]?[.\\d]*$");
                    if ((tds.get(2).text()).equals("必修") || (tds.get(2).text()).equals("选修")) {
                        String sNumber = tds.get(3).text();
                        double dNumber = Double.parseDouble(sNumber);
                        if(pattern1.matcher(tds.get(4).text()).matches() || pattern2.matcher(tds.get(4).text()).matches()) {
                            score = Double.parseDouble(courseScore);
                        }
                        else {
                            if ("优秀".equals(courseScore)) {
                                score = 95;
                            } else if ("良好".equals(courseScore)) {
                                score = 85;
                            } else if ("中等".equals(courseScore)) {
                                score = 75;
                            } else if ("及格".equals(courseScore)) {
                                score = 60;
                            } else if ("不及格".equals(courseScore)) {
                                score = 0;
                            } else {
                                score = 0;
                            }
                        }
                        scoreTotal += score * dNumber;
                        scoreTime += 1;
                        averTime += dNumber;
                        Log.d(TAG, "scoreTotal is -->" + scoreTotal);
                        Log.d(TAG, "scoreTime is -->" + scoreTime);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        averScore = scoreTotal / averTime;
        if (averScore < 60) {
            mGradePoint = 0;
        } else if (averScore >= 60) {
            mGradePoint = 1 + 0.1 * (averScore - 60);
        }
        return mGradePoint;
    }

}
