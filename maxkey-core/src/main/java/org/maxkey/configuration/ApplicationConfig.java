/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.maxkey.configuration;

import org.maxkey.constants.ConstantsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 全局应用程序配置 包含 1、数据源配置 dataSoruceConfig 2、字符集转换配置 characterEncodingConfig
 * 3、webseal认证集成配置 webSealConfig 4、系统的配置 sysConfig 5、所有用户可访问地址配置 allAccessUrl
 * 
 * 其中1、2、3项在applicationContext.xml中配置，配置文件applicationConfig.properties
 * 4项根据dynamic的属性判断是否动态从sysConfigService动态读取
 * 
 * @author Crystal.Sea
 * 
 */
@Component
@PropertySource(ConstantsProperties.maxKeyPropertySource)
@PropertySource(ConstantsProperties.applicationPropertySource)
public class ApplicationConfig {
    private static final Logger _logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    EmailConfig emailConfig;
    
    @Autowired
    CharacterEncodingConfig characterEncodingConfig;
    
    @Autowired
    LoginConfig loginConfig;

    @Value("${config.server.basedomain}")
    String baseDomainName;

    @Value("${config.server.domain}")
    String domainName;

    @Value("${config.server.name}")
    String serverName;

    @Value("${config.server.uri}")
    String serverPrefix;

    @Value("${config.server.default.uri}")
    String defaultUri;

    @Value("${config.server.management.uri}")
    String managementUri;

    @Value("${server.port:8080}")
    private int port;
    
    @Value("${server.servlet.session.timeout:1800}")
    private int sessionTimeout;

    @Value("${config.identity.kafkasupport:false}")
    private boolean kafkaSupport;
    
    @Value("${config.maxkey.uri}")
    private String maxKeyUri;
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ApplicationConfig() {
        super();
    }

    /**
     * @return the characterEncodingConfig
     */
    public CharacterEncodingConfig getCharacterEncodingConfig() {
        return characterEncodingConfig;
    }

    /**
     * @param characterEncodingConfig the characterEncodingConfig to set
     */
    public void setCharacterEncodingConfig(CharacterEncodingConfig characterEncodingConfig) {
        this.characterEncodingConfig = characterEncodingConfig;
    }

    public LoginConfig getLoginConfig() {
        return loginConfig;
    }

    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPrefix() {
        return serverPrefix;
    }

    public void setServerPrefix(String serverPrefix) {
        this.serverPrefix = serverPrefix;
    }

    /**
     * @return the domainName
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * @param domainName the domainName to set
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
       
    }

    public String getBaseDomainName() {
        return baseDomainName;
    }

    public void setBaseDomainName(String baseDomainName) {
        this.baseDomainName = baseDomainName;
    }

    /**
     * @return the emailConfig
     */
    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    /**
     * @param emailConfig the emailConfig to set
     */
    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public String getManagementUri() {
        return managementUri;
    }

    public void setManagementUri(String managementUri) {
        this.managementUri = managementUri;
    }

    public String getDefaultUri() {
        return defaultUri;
    }

    public void setDefaultUri(String defaultUri) {
        this.defaultUri = defaultUri;
    }

    public boolean isKafkaSupport() {
        return kafkaSupport;
    }

    public void setKafkaSupport(boolean kafkaSupport) {
        this.kafkaSupport = kafkaSupport;
    }

    public String getMaxKeyUri() {
        return maxKeyUri;
    }

    public void setMaxKeyUri(String maxKeyUri) {
        this.maxKeyUri = maxKeyUri;
    }

    public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApplicationConfig [emailConfig=");
        builder.append(emailConfig);
        builder.append(", characterEncodingConfig=");
        builder.append(characterEncodingConfig);
        builder.append(", loginConfig=");
        builder.append(loginConfig);
        builder.append(", baseDomainName=");
        builder.append(baseDomainName);
        builder.append(", domainName=");
        builder.append(domainName);
        builder.append(", serverName=");
        builder.append(serverName);
        builder.append(", serverPrefix=");
        builder.append(serverPrefix);
        builder.append(", defaultUri=");
        builder.append(defaultUri);
        builder.append(", managementUri=");
        builder.append(managementUri);
        builder.append(", port=");
        builder.append(port);
        builder.append(", kafkaSupport=");
        builder.append(kafkaSupport);
        builder.append(", maxKeyUri=");
        builder.append(maxKeyUri);
        builder.append("]");
        return builder.toString();
    }

}
