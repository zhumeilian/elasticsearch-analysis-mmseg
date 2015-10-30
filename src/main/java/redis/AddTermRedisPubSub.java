package redis;

import com.chenlb.mmseg4j.Dictionary;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.index.config.MMsegElasticConfigurator;
import redis.clients.jedis.JedisPubSub;

import java.io.File;

public class AddTermRedisPubSub extends JedisPubSub {

	public static ESLogger logger = Loggers.getLogger("mmseg-redis-msg");

	@Override
	public void onMessage(String channel, String message) {
		logger.debug("channel:" + channel + " and message:" + message,
				new Object[0]);
        String[] msg = message.split(":");
        if (msg.length != 2) {
            return;
        }
        if ("c".equals(msg[0])) {
            FileUtils.append(msg[1]);
        } else if ("d".equals(msg[0])) {
            FileUtils.remove(msg[1]);
        }
        File file = new File(MMsegElasticConfigurator.environment.configFile(), "mmseg");
        Dictionary dic = Dictionary.getInstance(file);
        dic.reload();
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		logger.debug("pattern:" + pattern + " and channel:" + channel
				+ " and message:" + message);
		onMessage(channel, message);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		logger.info("psubscribe pattern:" + pattern
				+ " and subscribedChannels:" + subscribedChannels);

	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		logger.info("punsubscribe pattern:" + pattern
				+ " and subscribedChannels:" + subscribedChannels);

	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info("subscribe channel:" + channel + " and subscribedChannels:"
				+ subscribedChannels);

	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info("unsubscribe channel:" + channel
				+ " and subscribedChannels:" + subscribedChannels);
	}

}
