package redis;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.index.config.MMsegElasticConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtils {

    public static ESLogger logger = Loggers.getLogger("mmseg-redis-msg-file");

    public static void append(String content) {
        try {
            File file = new File(MMsegElasticConfigurator.environment.configFile(), "mmseg/words-my.dic");
            appendFile(content, file);
        } catch (IOException e) {
            logger.error("read exception", e, new Object[0]);
            e.printStackTrace();
        }
    }

    private static void appendFile(String content, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(content);
        writer.newLine();
        writer.close();
    }

    public static void remove(String content) {
        try {
            File file = new File(MMsegElasticConfigurator.environment.configFile(), "mmseg/words-my.dic");
            removeFile(content, file, false);
            file = new File(MMsegElasticConfigurator.environment.configFile(), "mmseg/words.dic");
            removeFile(content, file, false);
        }
        catch (FileNotFoundException e) {
            logger.error("file not found", e, new Object[0]);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("read exception", e, new Object[0]);
            e.printStackTrace();
        }
    }

    private static void removeFile(String content, File file, boolean head) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> list = new ArrayList<String>();
        String text = reader.readLine();
        while (text != null) {
//            logger.info("match is {} text is{}", new Object[] { Boolean.valueOf(match(content, text, head)), text });
            if (match(content, text, head)) {
                list.add(text);
            }
            text = reader.readLine();
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String item : list) {
            writer.write(item);
            writer.newLine();
        }
        writer.close();
    }

    private static boolean match(String content, String text, boolean head) {
        if (head)
            return !text.trim().matches("^" + content + "\\D*$");
        return !text.trim().equals(content);
    }

    public static void main(String[] args) throws IOException {
        Pattern p = Pattern.compile("^满意\\D*$");
        System.out.println(p.matcher("满意  满      a       意      a").matches());
        System.out.println(p.matcher("满哈-满,意").matches());
        System.out.println("满哈-满,意".replace(",", "\t"));

        File file = new File("config/mmseg/words-my.dic");
        removeFile("白云山", file, false);
    }


}
