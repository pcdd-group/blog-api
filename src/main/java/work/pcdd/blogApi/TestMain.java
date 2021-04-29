package work.pcdd.blogApi;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.http.HttpUtil;
import work.pcdd.blogApi.controller.BlogController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

/**
 * @author 1907263405@qq.com
 * @date 2021/3/19 15:23
 */
public class TestMain {

    public static void main(String[] args) {

        // 爬虫
        /*String content = HttpUtil.get("https://eams.sqnu.edu.cn/eams/login.action");
        System.out.println(content);*/

        Duration duration = Duration.ofDays(1);
        System.out.println(duration.getSeconds());

        String filePath = "e:/test.png";
        Image image = null;
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        ImgUtil.cut(
                FileUtil.file(filePath),
                FileUtil.file("e:/finish.png"),
                new Rectangle(0, height / 10, width, height - height / 8)
        );


    }
}
