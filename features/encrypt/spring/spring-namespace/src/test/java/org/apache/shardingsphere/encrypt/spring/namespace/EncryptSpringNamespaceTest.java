/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.spring.namespace;

import org.apache.shardingsphere.encrypt.algorithm.config.AlgorithmProvidedEncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.algorithm.encrypt.AESEncryptAlgorithm;
import org.apache.shardingsphere.encrypt.algorithm.encrypt.MD5EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.algorithm.like.CharDigestLikeEncryptAlgorithm;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath:META-INF/spring/encrypt-application-context.xml")
public final class EncryptSpringNamespaceTest extends AbstractJUnit4SpringContextTests {
    
    @Resource
    private EncryptAlgorithm<Object, String> aesEncryptor;
    
    @Resource
    private EncryptAlgorithm<Object, String> md5Encryptor;
    
    @Resource
    private EncryptAlgorithm<Object, String> likeQueryEncryptor;
    
    @Resource
    private AlgorithmProvidedEncryptRuleConfiguration encryptRule;
    
    @Test
    public void assertAESEncryptor() {
        assertThat(aesEncryptor.getType(), is("AES"));
        assertThat(aesEncryptor.getProps().getProperty("aes-key-value"), is("123456"));
    }
    
    @Test
    public void assertMD5Encryptor() {
        assertThat(md5Encryptor.getType(), is("MD5"));
    }
    
    @Test
    public void assertLikeQueryEncryptor() {
        assertThat(likeQueryEncryptor.getType(), is("CHAR_DIGEST_LIKE"));
    }
    
    @Test
    public void assertEncryptRuleConfiguration() {
        assertEncryptors(encryptRule.getEncryptors());
        assertThat(encryptRule.getTables().size(), is(1));
        assertEncryptTable(encryptRule.getTables().iterator().next());
    }
    
    private void assertEncryptors(final Map<String, EncryptAlgorithm<?, ?>> encryptors) {
        assertThat(encryptors.size(), is(3));
        assertThat(encryptors.get("aesEncryptor"), instanceOf(AESEncryptAlgorithm.class));
        assertThat(encryptors.get("aesEncryptor").getProps().getProperty("aes-key-value"), is("123456"));
        assertThat(encryptors.get("md5Encryptor"), instanceOf(MD5EncryptAlgorithm.class));
        assertThat(encryptors.get("likeQueryEncryptor"), instanceOf(CharDigestLikeEncryptAlgorithm.class));
    }
    
    private void assertEncryptTable(final EncryptTableRuleConfiguration tableRuleConfig) {
        assertThat(tableRuleConfig.getName(), is("t_order"));
        assertFalse(tableRuleConfig.getQueryWithCipherColumn());
        assertThat(tableRuleConfig.getColumns().size(), is(2));
        Iterator<EncryptColumnRuleConfiguration> columnRuleConfigs = tableRuleConfig.getColumns().iterator();
        assertEncryptColumn1(columnRuleConfigs.next());
        assertEncryptColumn2(columnRuleConfigs.next());
    }
    
    private void assertEncryptColumn1(final EncryptColumnRuleConfiguration columnRuleConfig) {
        assertThat(columnRuleConfig.getLogicColumn(), is("pwd"));
        assertThat(columnRuleConfig.getCipherColumn(), is("pwd_cipher"));
        assertThat(columnRuleConfig.getEncryptorName(), is("aesEncryptor"));
        assertTrue(columnRuleConfig.getQueryWithCipherColumn());
    }
    
    private void assertEncryptColumn2(final EncryptColumnRuleConfiguration columnRuleConfig) {
        assertThat(columnRuleConfig.getLogicColumn(), is("credit_card"));
        assertThat(columnRuleConfig.getCipherColumn(), is("credit_card_cipher"));
        assertThat(columnRuleConfig.getLikeQueryColumn(), is("credit_card_like_query"));
        assertThat(columnRuleConfig.getPlainColumn(), is("credit_card_plain"));
        assertThat(columnRuleConfig.getEncryptorName(), is("md5Encryptor"));
        assertThat(columnRuleConfig.getLikeQueryEncryptorName(), is("likeQueryEncryptor"));
        assertFalse(columnRuleConfig.getQueryWithCipherColumn());
    }
}
