import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEncodingExample {

    public static void main(String[] args) throws Exception {
        // 获取文件路径
        Path filePath = Paths.get("F:\\project\\jdcode\\xingye\\aaaa.txt");
        // 获取文件的编码
        Charset charset = detectFileEncoding(filePath);
        System.out.println("File encoding: " + charset);
    }

    private static Charset detectFileEncoding(Path filePath) throws IOException {
        // 读取文件前 4 个字节
        byte[] buffer = new byte[4];
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(filePath))) {
            bis.read(buffer, 0, 4);
        }
        // 判断文件编码
        if (buffer[0] == (byte)0xEF && buffer[1] == (byte)0xBB && buffer[2] == (byte)0xBF) {
            return StandardCharsets.UTF_8;
        } else if (buffer[0] == (byte)0xFE && buffer[1] == (byte)0xFF) {
            return Charset.forName("UTF-16BE");
        } else if (buffer[0] == (byte)0xFF && buffer[1] == (byte)0xFE) {
            return Charset.forName("UTF-16LE");
        } else if (buffer[0] == (byte)0 && buffer[1] == (byte)0 && buffer[2] == (byte)0xFE && buffer[3] == (byte)0xFF) {
            return Charset.forName("UTF-32BE");
        } else if (buffer[0] == (byte)0xFF && buffer[1] == (byte)0xFE && buffer[2] == (byte)0 && buffer[3] == (byte)0) {
            return Charset.forName("UTF-32LE");
        } else {
            return StandardCharsets.ISO_8859_1;
        }
    }
}
