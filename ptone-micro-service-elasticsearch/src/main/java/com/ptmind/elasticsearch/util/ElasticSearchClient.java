package com.ptmind.elasticsearch.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ptmind on 2015/9/9.
 */
@Component
public class ElasticSearchClient implements SmartLifecycle {

	private Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

	private Boolean isAutoStartup = true;
	private Boolean isRunning = false;
	private Client client;

	@Value("${cluster.name}")
	private String clusterName;

	@Value("${cluster.address}")
	private String clusterAddress;


	@Override
	public boolean isAutoStartup() {
		return isAutoStartup;
	}

	@Override
	public void stop(Runnable runnable) {
		stop();
		runnable.run();
	}

	@Override
	public void start() {
		if (!isRunning) {
			logger.info("ElasticSearchClient start begin.....");
			Settings settings = Settings.builder()
							.put("cluster.name", "datadeck")
							.build();

			logger.info("cluster.name:" + getClusterName());
			logger.info("cluster.address:" + getClusterAddress());

			try {
				client = new PreBuiltTransportClient(settings)
								.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.2.33"), 9300));

			} catch (UnknownHostException e) {
				logger.error("not find validate host.");
				e.printStackTrace();
			}
			isRunning = true;

			logger.info("ElasticSearchClient start end.....");
		}
	}

	@Override
	public void stop() {
		if (client != null) {
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	public String getClusterAddress() {
		return clusterAddress;
	}

	public void setClusterAddress(String clusterAddress) {
		this.clusterAddress = clusterAddress;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public Client getClient() {
		return client;
	}

	private InetSocketTransportAddress[] getClusterAddressList() throws UnknownHostException {
		InetSocketTransportAddress[] addressList = null;
		int i = 0;
		if (clusterAddress != null && !clusterAddress.equals("")) {
			String[] addressArray = clusterAddress.split(",");
			addressList = new InetSocketTransportAddress[addressArray.length];
			for (String address : addressArray) {

				String[] hostAndPort = address.split("\\:");

				addressList[i] = new InetSocketTransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
				i++;
			}

		}
		return addressList;
	}
}
