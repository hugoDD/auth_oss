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
 

package org.maxkey.authz.cas.endpoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.maxkey.authn.online.OnlineTicketServices;
import org.maxkey.authz.cas.endpoint.ticket.CasConstants;
import org.maxkey.authz.cas.endpoint.ticket.TicketServices;
import org.maxkey.authz.endpoint.AuthorizeBaseEndpoint;
import org.maxkey.constants.ContentType;
import org.maxkey.persistence.service.AppsCasDetailsService;
import org.maxkey.persistence.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CasBaseAuthorizeEndpoint  extends AuthorizeBaseEndpoint{
    final static Logger _logger = LoggerFactory.getLogger(CasBaseAuthorizeEndpoint.class);
    
    @Autowired
    @Qualifier("appsCasDetailsService")
    protected AppsCasDetailsService casDetailsService;
    
    @Autowired
    @Qualifier("userInfoService")
    protected UserInfoService userInfoService;
    
    @Autowired
    @Qualifier("casTicketServices")
    protected TicketServices ticketServices;
    
    @Autowired
    @Qualifier("casTicketGrantingTicketServices")
    protected TicketServices casTicketGrantingTicketServices;
    
    @Autowired
    @Qualifier("onlineTicketServices")
    protected OnlineTicketServices onlineTicketServices;
    
    @Autowired
    @Qualifier("casProxyGrantingTicketServices")
    protected TicketServices casProxyGrantingTicketServices;
    
    
    public void setContentType(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        String format) {
        
        if(format == null || format.equalsIgnoreCase("") || format.equalsIgnoreCase(CasConstants.FORMAT_TYPE.XML)) {
            //response.setContentType(ContentType.APPLICATION_XML_UTF8);
        }else {
            response.setContentType(ContentType.APPLICATION_JSON_UTF8);
        }
    }
    
    public void postMessage(String url,Map<String, Object> paramMap) {
        // ??????httpClient??????
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // ??????httpPost??????????????????
        HttpPost httpPost = new HttpPost(url);
        // ????????????????????????
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// ????????????????????????????????????
                .setConnectionRequestTimeout(35000)// ??????????????????????????????
                .setSocketTimeout(60000)// ????????????????????????????????????
                .build();
        // ???httpPost??????????????????
        httpPost.setConfig(requestConfig);
        // ???????????????
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // ??????post????????????
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // ??????map??????entrySet????????????entity
            Set<Entry<String, Object>> entrySet = paramMap.entrySet();
            // ??????????????????????????????
            Iterator<Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> mapEntry = iterator.next();
                _logger.debug("Name " + mapEntry.getKey() + " , Value " +mapEntry.getValue());
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // ???httpPost??????????????????????????????
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            _logger.debug("Post Message \n" + 
                    httpPost.getEntity().toString()
                 );
        }
        
        
        try {
            // httpClient????????????post??????,???????????????????????????
            httpResponse = httpClient.execute(httpPost);
            // ????????????????????????????????????
            HttpEntity entity = httpResponse.getEntity();
            _logger.debug("Http Response StatusCode " + 
                    httpResponse.getStatusLine().getStatusCode()+
                    " , Content " +EntityUtils.toString(entity)
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ????????????
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
