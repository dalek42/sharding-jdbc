+++
title = "Mixed Rules"
weight = 9
+++

## Background

ShardingSphere provides a variety of features, such as data sharding, read/write splitting, high availability, and data decryption. These features can be used independently or in combination. 
Below, you will find the configuration samples based on JAVA API.

## Samples

```java
// Sharding configuration
private ShardingRuleConfiguration createShardingRuleConfiguration() {
    ShardingRuleConfiguration result = new ShardingRuleConfiguration();
    result.getTables().add(getOrderTableRuleConfiguration());
    result.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "inline"));
    result.setDefaultTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "standard_test_tbl"));
    Properties props = new Properties();
    props.setProperty("algorithm-expression", "demo_ds_${user_id % 2}");
    result.getShardingAlgorithms().put("inline", new AlgorithmConfiguration("INLINE", props));
    result.getShardingAlgorithms().put("standard_test_tbl", new AlgorithmConfiguration("STANDARD_TEST_TBL", new Properties()));
    result.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));
    return result;
}

private ShardingTableRuleConfiguration getOrderTableRuleConfiguration() {
    ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order", "demo_ds_${0..1}.t_order_${[0, 1]}");
    result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_id", "snowflake"));
    return result;
}

// Dynamic read/write splitting configuration
private static ReadwriteSplittingRuleConfiguration createReadwriteSplittingConfiguration() {
    ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfiguration1 = new ReadwriteSplittingDataSourceRuleConfiguration("replica_ds_0", new DynamicReadwriteSplittingStrategyConfiguration("readwrite_ds_0", true), "");
    ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfiguration2 = new ReadwriteSplittingDataSourceRuleConfiguration("replica_ds_1", new DynamicReadwriteSplittingStrategyConfiguration("readwrite_ds_1", true), "");
    Collection<ReadwriteSplittingDataSourceRuleConfiguration> dataSources = new LinkedList<>();
    dataSources.add(dataSourceRuleConfiguration1);
    dataSources.add(dataSourceRuleConfiguration2);
    return new ReadwriteSplittingRuleConfiguration(dataSources, Collections.emptyMap());
}

// Database discovery configuration
private static DatabaseDiscoveryRuleConfiguration createDatabaseDiscoveryConfiguration() {
    DatabaseDiscoveryDataSourceRuleConfiguration dataSourceRuleConfiguration1 = new DatabaseDiscoveryDataSourceRuleConfiguration("readwrite_ds_0", Arrays.asList("ds_0, ds_1, ds_2"), "mgr-heartbeat", "mgr");
    DatabaseDiscoveryDataSourceRuleConfiguration dataSourceRuleConfiguration2 = new DatabaseDiscoveryDataSourceRuleConfiguration("readwrite_ds_1", Arrays.asList("ds_3, ds_4, ds_5"), "mgr-heartbeat", "mgr");
    Collection<DatabaseDiscoveryDataSourceRuleConfiguration> dataSources = new LinkedList<>();    
    dataSources.add(dataSourceRuleConfiguration1);
    dataSources.add(dataSourceRuleConfiguration2);
    return new DatabaseDiscoveryRuleConfiguration(configs, createDiscoveryHeartbeats(), createDiscoveryTypes());
}

private static DatabaseDiscoveryRuleConfiguration createDatabaseDiscoveryConfiguration() {
    DatabaseDiscoveryDataSourceRuleConfiguration dataSourceRuleConfiguration = new DatabaseDiscoveryDataSourceRuleConfiguration("readwrite_ds_1", Arrays.asList("ds_3, ds_4, ds_5"), "mgr-heartbeat", "mgr");
    return new DatabaseDiscoveryRuleConfiguration(Collections.singleton(dataSourceRuleConfiguration), createDiscoveryHeartbeats(), createDiscoveryTypes());
}

private static Map<String, AlgorithmConfiguration> createDiscoveryTypes() {
    Map<String, AlgorithmConfiguration> result = new HashMap<>(1， 1);
    Properties props = new Properties();
    props.put("group-name", "558edd3c-02ec-11ea-9bb3-080027e39bd2");
    discoveryTypes.put("mgr", new AlgorithmConfiguration("MGR", props));
    return result;
}

private static Map<String, DatabaseDiscoveryHeartBeatConfiguration> createDiscoveryHeartbeats() {
    Map<String, DatabaseDiscoveryHeartBeatConfiguration> result = new HashMap<>(1， 1);
    Properties props = new Properties();
    props.put("keep-alive-cron", "0/5 * * * * ?");
    discoveryHeartBeatConfiguration.put("mgr-heartbeat", new DatabaseDiscoveryHeartBeatConfiguration(props));
    return result;
}

// Data decryption configuration
public EncryptRuleConfiguration createEncryptRuleConfiguration() {
    Properties props = new Properties();
    props.setProperty("aes-key-value", "123456");
    EncryptColumnRuleConfiguration columnConfigAes = new EncryptColumnRuleConfiguration("username", "username", "", "", "username_plain", "name_encryptor", null);
    EncryptColumnRuleConfiguration columnConfigTest = new EncryptColumnRuleConfiguration("pwd", "pwd", "assisted_query_pwd", "like_pwd", "", "pwd_encryptor", null);
    EncryptTableRuleConfiguration encryptTableRuleConfig = new EncryptTableRuleConfiguration("t_user", Arrays.asList(columnConfigAes, columnConfigTest), null);
    Map<String, AlgorithmConfiguration> encryptAlgorithmConfigs = new LinkedHashMap<>(2, 1);
    encryptAlgorithmConfigs.put("name_encryptor", new AlgorithmConfiguration("AES", props));
    encryptAlgorithmConfigs.put("pwd_encryptor", new AlgorithmConfiguration("assistedTest", props));
    encryptAlgorithmConfigs.put("like_encryptor", new AlgorithmConfiguration("CHAR_DIGEST_LIKE", new Properties()));
    EncryptRuleConfiguration result = new EncryptRuleConfiguration(Collections.singleton(encryptTableRuleConfig), encryptAlgorithmConfigs);
    return result;
}
```
