package com.rainbowsea.yupicturebackend.api.imagesearch.sub;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.rainbowsea.yupicturebackend.exception.BusinessException;
import com.rainbowsea.yupicturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取以图搜图页面地址（step 1）
 */
@Slf4j
public class GetImagePageUrlApi {


    ///**
    // * to do 以图搜图 webp 图片搜索问题方案1
    // * 获取以图搜图页面地址
    // * @param localImagePath
    // * @return
    // */
    //public static String getImagePageUrl(String localImagePath) {
    //    // 获取当前时间戳
    //    long uptime = System.currentTimeMillis();
    //    // 请求地址
    //    String url = "https://graph.baidu.com/upload?uptime=" + uptime;
    //    // AcsToken
    //    String acsToken = "jmM4zyI8OUixvSuWh0sCy4xWbsttVMZb9qcRTmn6SuNWg0vCO7N0s6Lffec+IY5yuqHujHmCctF9BVCGYGH0H5SH/H3VPFUl4O4CP1jp8GoAzuslb8kkQQ4a21Tebge8yhviopaiK66K6hNKGPlWt78xyyJxTteFdXYLvoO6raqhz2yNv50vk4/41peIwba4lc0hzoxdHxo3OBerHP2rfHwLWdpjcI9xeu2nJlGPgKB42rYYVW50+AJ3tQEBEROlg/UNLNxY+6200B/s6Ryz+n7xUptHFHi4d8Vp8q7mJ26yms+44i8tyiFluaZAr66/+wW/KMzOhqhXCNgckoGPX1SSYwueWZtllIchRdsvCZQ8tFJymKDjCf3yI/Lw1oig9OKZCAEtiLTeKE9/CY+Crp8DHa8Tpvlk2/i825E3LuTF8EQfzjcGpVnR00Lb4/8A";
    //
    //    //String acsToken = "1747715165579_1747725369027_7/IHIkyvGdY8UY3uaX828+t7sI1CyoX6DLfTBfgiiMBzCxEOp+hwpQnubQiYiDu+o6kfkNvbPRBrJdR4g1gnAY0/nJa3o5Plyiu363hdok6se4GbMScW+FerHsKvl0URYTpLTvQpeHL2yIve4d9F280No+G1rnxZIQ8edw4rQVj36NifOCzaOXfYIgvY98lNiSR4B9QTQhEUB9VUv2Sq/yH4n3feawDOuAP0mjNK1iSWPDMycMUU13EPBwhMvVpId3yfrqrgXM2WFM2qec8P+3JgZ4p8Y28eVY1704ziVTlKj644nSa88S4OOI1fKV4oSkempR7+ZCqwr+ner7jcKLuDSdfv4z28VB9TAxNmbjqHCzL8rjHGlm0cDBIl5hjn+JuZgf+eMtNuxLMerqUucm6yNIltB9xH/4tVcyF59N8J69rAc/TsiKvMCjIsO9qUaq81BIvp1fChtAWhf1tdZcQAjjNKaYgFTV8qCNI8abM=";
    //
    //    try {
    //        // 发送POST请求并上传文件
    //        HttpResponse response = HttpRequest.post(url)
    //                .form("tn", "pc")
    //                .form("from", "pc")
    //                .form("image_source", "PC_UPLOAD_FILE") // 类型为file
    //                .header("Acs-Token", acsToken)
    //                .timeout(5000)
    //                .form("image", new java.io.File(localImagePath)) // 使用form方法上传文件
    //                .execute();
    //
    //        // 判断响应状态
    //        if (response.isOk()) {
    //            // 解析响应
    //            String responseBody = response.body();
    //            Map<String, Object> result = JSONUtil.toBean(responseBody, Map.class);
    //
    //            // 处理响应结果
    //            if (result != null && Integer.valueOf(0).equals(result.get("status"))) {
    //                Map<String, Object> data = (Map<String, Object>) result.get("data");
    //                String rawUrl = (String) data.get("url");
    //                // 对 URL 进行解码
    //                String searchResultUrl = java.net.URLDecoder.decode(rawUrl, StandardCharsets.UTF_8);
    //                return searchResultUrl;
    //            } else {
    //                throw new RuntimeException("接口调用失败或返回错误的状态码: " + result.get("status"));
    //            }
    //        } else {
    //            throw new RuntimeException("HTTP请求失败，状态码：" + response.getStatus());
    //        }
    //    } catch (Exception e) {
    //        log.error("搜索失败", e);
    //        throw new RuntimeException("搜索失败");
    //    }
    //}


    /**
     * 获取以图搜图页面地址
     *
     * @param imageUrl
     * @return
     */
    public static String getImagePageUrl(String imageUrl) {
        // image: https%3A%2F%2Fwww.codefather.cn%2Flogo.png
        //tn: pc
        //from: pc
        //image_source: PC_UPLOAD_URL
        //sdkParams:
        // 1. 准备请求参数
        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        // 请求地址
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;
        String acsToken = "jmM4zyI8OUixvSuWh0sCy4xWbsttVMZb9qcRTmn6SuNWg0vCO7N0s6Lffec+IY5yuqHujHmCctF9BVCGYGH0H5SH/H3VPFUl4O4CP1jp8GoAzuslb8kkQQ4a21Tebge8yhviopaiK66K6hNKGPlWt78xyyJxTteFdXYLvoO6raqhz2yNv50vk4/41peIwba4lc0hzoxdHxo3OBerHP2rfHwLWdpjcI9xeu2nJlGPgKB42rYYVW50+AJ3tQEBEROlg/UNLNxY+6200B/s6Ryz+n7xUptHFHi4d8Vp8q7mJ26yms+44i8tyiFluaZAr66/+wW/KMzOhqhXCNgckoGPX1SSYwueWZtllIchRdsvCZQ8tFJymKDjCf3yI/Lw1oig9OKZCAEtiLTeKE9/CY+Crp8DHa8Tpvlk2/i825E3LuTF8EQfzjcGpVnR00Lb4/8A";

        try {
            // 2. 发送请求
            HttpResponse httpResponse = HttpRequest.post(url)
                    .form(formData)
                    .header("Acs-Token", acsToken)
                    .timeout(5000)
                    .execute();
            if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            // 解析响应
            // {"status":0,"msg":"Success","data":{"url":"https://graph.baidu.com/sc","sign":"1262fe97cd54acd88139901734784257"}}
            String body = httpResponse.body();
            Map<String, Object> result = JSONUtil.toBean(body, Map.class);
            // 3. 处理响应结果
            if (result == null || !Integer.valueOf(0).equals(result.get("status"))) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            // 对 URL 进行解码
            String rawUrl = (String) data.get("url");
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            // 如果 URL 为空
            if (StrUtil.isBlank(searchResultUrl)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效的结果地址");
            }
            return searchResultUrl;
        } catch (Exception e) {
            log.error("调用百度以图搜图接口失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        //String imageUrl = "http://mms1.baidu.com/it/u=2267623452,1984140018&fm=253&app=138&f=JPEG?w=664&h=373";
        String searchResultUrl = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + searchResultUrl);
    }
}
