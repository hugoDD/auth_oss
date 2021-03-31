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
 

package org.maxkey.autoconfigure;

import org.maxkey.authz.cas.endpoint.ticket.TicketServices;
import org.maxkey.authz.cas.endpoint.ticket.pgt.ProxyGrantingTicketServicesFactory;
import org.maxkey.authz.cas.endpoint.ticket.st.TicketServicesFactory;
import org.maxkey.authz.cas.endpoint.ticket.tgt.TicketGrantingTicketServicesFactory;
import org.maxkey.constants.ConstantsProperties;
import org.maxkey.persistence.redis.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan(basePackages = {
        "org.maxkey.authz.cas.endpoint"
})
@PropertySource(ConstantsProperties.maxKeyPropertySource)
public class CasAutoConfiguration implements InitializingBean {
    private static final  Logger _logger = LoggerFactory.getLogger(CasAutoConfiguration.class);
    
    /**
     * TicketServices. 
     * @param persistence int
     * @param validity int
     * @return casTicketServices
     */
    @Bean(name = "casTicketServices")
    public TicketServices casTicketServices(
            @Value("${config.server.persistence}") int persistence,
            @Value("${config.login.remeberme.validity}") int validity,
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory redisConnFactory) {
    	_logger.debug("init casTicketServices.");
        return new TicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
    }
   
    /**
     * TicketServices. 
     * @param persistence int
     * @param validity int
     * @return casTicketServices
     */
    @Bean(name = "casTicketGrantingTicketServices")
    public TicketServices casTicketGrantingTicketServices(
            @Value("${config.server.persistence}") int persistence,
            @Value("${config.login.remeberme.validity}") int validity,
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory redisConnFactory) {
    	_logger.debug("init casTicketGrantingTicketServices.");
        return new TicketGrantingTicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
    }
    
    @Bean(name = "casProxyGrantingTicketServices")
    public TicketServices casProxyGrantingTicketServices(
            @Value("${config.server.persistence}") int persistence,
            @Value("${config.login.remeberme.validity}") int validity,
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory redisConnFactory) {
    	_logger.debug("init casTicketGrantingTicketServices.");
        return new ProxyGrantingTicketServicesFactory().getService(persistence, jdbcTemplate, redisConnFactory);
    }
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        
    }
}
