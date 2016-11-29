package ye.mdroid.imweather.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ye on 16-11-29.
 */

public class StreamUtils {
    private static String tag = StreamUtils.class.getSimpleName();

    /**
     * 将inputstream转化为string
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws IOException
     */
    public static String stream2String(InputStream inputStream) throws IOException {
        String res = "";
        int len;
        byte[] buffer = new byte[1024 * 500];
        while ((len = inputStream.read(buffer)) != -1) {
            res += new String(buffer, 0, len);
        }
        inputStream.close();
        return res;
    }

    /**
     * 将文字内容写入文件
     *
     * @param content  待写入的内容
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void stream2File(String content, String filePath) throws IOException {
        Log.i(tag, "下载文件路径:" + filePath);
        File file = new File(filePath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }


    /**
     * 将文件内容转化成字符串返回
     *
     * @param file 带转化的文件
     * @return content  返回的字符串
     */
    public static String file2String(File file) throws IOException {
        String content = "";
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            content += line;
        }
        fileReader.close();
        reader.close();
        return content;
    }
}
