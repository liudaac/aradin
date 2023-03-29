package cn.aradin.version.nacos.starter.manager;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.diagnostics.analyzer.NacosConnectionFailureException;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.properties.VersionNacos;

public class VersionNacosConfigManager {
	private static final Logger log = LoggerFactory.getLogger(NacosConfigManager.class);

	private static ConfigService service = null;

	private NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();

	public VersionNacosConfigManager(NacosConfigProperties nacosConfigProperties,
			VersionNacos versionNacos) {
		BeanUtils.copyProperties(nacosConfigProperties, this.nacosConfigProperties);
		if (StringUtils.isNotBlank(versionNacos.getServerAddr())) {
			this.nacosConfigProperties.setServerAddr(versionNacos.getServerAddr());
		}
		if (StringUtils.isNotBlank(versionNacos.getNamespace())) {
			this.nacosConfigProperties.setServerAddr(versionNacos.getNamespace());
		}
		if (StringUtils.isNotBlank(versionNacos.getUsername())) {
			this.nacosConfigProperties.setUsername(versionNacos.getUsername());
		}
		if (StringUtils.isNotBlank(versionNacos.getPassword())) {
			this.nacosConfigProperties.setPassword(versionNacos.getPassword());
		}
		if (StringUtils.isAnyBlank(this.nacosConfigProperties.getServerAddr(),
				this.nacosConfigProperties.getNamespace(),
				this.nacosConfigProperties.getUsername(),
				this.nacosConfigProperties.getPassword())) {
			throw new RuntimeException("Nacos配置不全");
		}
		// Compatible with older code in NacosConfigProperties,It will be deleted in the
		// future.
		createConfigService(this.nacosConfigProperties);
	}

	/**
	 * Compatible with old design,It will be perfected in the future.
	 */
	static ConfigService createConfigService(
			NacosConfigProperties nacosConfigProperties) {
		if (Objects.isNull(service)) {
			synchronized (NacosConfigManager.class) {
				try {
					if (Objects.isNull(service)) {
						service = NacosFactory.createConfigService(
								nacosConfigProperties.assembleConfigServiceProperties());
					}
				}
				catch (NacosException e) {
					log.error(e.getMessage());
					throw new NacosConnectionFailureException(
							nacosConfigProperties.getServerAddr(), e.getMessage(), e);
				}
			}
		}
		return service;
	}

	public ConfigService getConfigService() {
		if (Objects.isNull(service)) {
			createConfigService(this.nacosConfigProperties);
		}
		return service;
	}

	public NacosConfigProperties getNacosConfigProperties() {
		return nacosConfigProperties;
	}
}
