/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.debezium.springboot;

import javax.annotation.Generated;
import org.apache.camel.spring.boot.ComponentConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents a Debezium MongoDB endpoint which is used to capture changes in
 * MongoDB database so that that applications can see those changes and respond
 * to them.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.component.debezium-mongodb")
public class DebeziumMongodbComponentConfiguration
        extends
            ComponentConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the debezium-mongodb component.
     * This is enabled by default.
     */
    private Boolean enabled;
    /**
     * Allow pre-configured Configurations to be set.
     */
    private MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration configuration;
    /**
     * Whether the component should use basic property binding (Camel 2.x) or
     * the newer property binding with additional capabilities
     */
    private Boolean basicPropertyBinding = false;
    /**
     * Whether the producer should be started lazy (on the first message). By
     * starting lazy you can use this to allow CamelContext and routes to
     * startup in situations where a producer may otherwise fail during starting
     * and cause the route to fail being started. By deferring this startup to
     * be lazy then the startup failure can be handled during routing messages
     * via Camel's routing error handlers. Beware that when the first message is
     * processed then creating and starting the producer may take a little time
     * and prolong the total processing time of the processing.
     */
    private Boolean lazyStartProducer = false;
    /**
     * Allows for bridging the consumer to the Camel routing Error Handler,
     * which mean any exceptions occurred while the consumer is trying to pickup
     * incoming messages, or the likes, will now be processed as a message and
     * handled by the routing Error Handler. By default the consumer will use
     * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that
     * will be logged at WARN or ERROR level and ignored.
     */
    private Boolean bridgeErrorHandler = false;

    public MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(
            MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration configuration) {
        this.configuration = configuration;
    }

    public Boolean getBasicPropertyBinding() {
        return basicPropertyBinding;
    }

    public void setBasicPropertyBinding(Boolean basicPropertyBinding) {
        this.basicPropertyBinding = basicPropertyBinding;
    }

    public Boolean getLazyStartProducer() {
        return lazyStartProducer;
    }

    public void setLazyStartProducer(Boolean lazyStartProducer) {
        this.lazyStartProducer = lazyStartProducer;
    }

    public Boolean getBridgeErrorHandler() {
        return bridgeErrorHandler;
    }

    public void setBridgeErrorHandler(Boolean bridgeErrorHandler) {
        this.bridgeErrorHandler = bridgeErrorHandler;
    }

    public static class MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration {
        public static final Class CAMEL_NESTED_CLASS = org.apache.camel.component.debezium.configuration.MongoDbConnectorEmbeddedDebeziumConfiguration.class;
        /**
         * Maximum size of the queue for change events read from the database
         * log but not yet recorded or forwarded. Defaults to 8192, and should
         * always be larger than the maximum batch size.
         */
        private Integer maxQueueSize = 8192;
        /**
         * Password to be used when connecting to MongoDB, if necessary.
         */
        private String mongodbPassword;
        /**
         * Maximum number of threads used to perform an intial sync of the
         * collections in a replica set. Defaults to 1.
         */
        private Integer initialSyncMaxThreads = 1;
        /**
         * The number of milliseconds to delay before a snapshot will begin.
         */
        private Long snapshotDelayMs = 0L;
        /**
         * Description is not available here, please check Debezium website for
         * corresponding key 'collection.blacklist' description.
         */
        private String collectionBlacklist;
        /**
         * The collections for which changes are to be captured
         */
        private String collectionWhitelist;
        /**
         * Should connector use SSL to connect to MongoDB instances
         */
        private Boolean mongodbSslEnabled = false;
        /**
         * Whether delete operations should be represented by a delete event and
         * a subsquenttombstone event (true) or only by a delete event (false).
         * Emitting the tombstone event (the default behavior) allows Kafka to
         * completely delete all events pertaining to the given key once the
         * source record got deleted.
         */
        private Boolean tombstonesOnDelete = false;
        /**
         * Specifies whether the addresses in 'hosts' are seeds that should be
         * used to discover all members of the cluster or replica set ('true'),
         * or whether the address(es) in 'hosts' should be used as is ('false').
         * The default is 'true'.
         */
        private Boolean mongodbMembersAutoDiscover = true;
        /**
         * Description is not available here, please check Debezium website for
         * corresponding key 'field.renames' description.
         */
        private String fieldRenames;
        /**
         * Frequency in milliseconds to wait for new change events to appear
         * after receiving no events. Defaults to 500ms.
         */
        private Long pollIntervalMs = 500L;
        /**
         * The prefix that is used to name heartbeat topics.Defaults to
         * __debezium-heartbeat.
         */
        private String heartbeatTopicsPrefix = "__debezium-heartbeat";
        /**
         * The databases for which changes are to be captured
         */
        private String databaseWhitelist;
        /**
         * The hostname and port pairs (in the form 'host' or 'host:port') of
         * the MongoDB server(s) in the replica set.
         */
        private String mongodbHosts;
        /**
         * The initial delay when trying to reconnect to a primary after a
         * connection cannot be made or when no primary is available. Defaults
         * to 1 second (1000 ms).
         */
        private Long connectBackoffInitialDelayMs = 1000L;
        /**
         * The maximum number of records that should be loaded into memory while
         * performing a snapshot
         */
        private Integer snapshotFetchSize;
        /**
         * The path to the file that will be used to record the database history
         */
        private String databaseHistoryFileFilename;
        /**
         * Maximum number of failed connection attempts to a replica set primary
         * before an exception occurs and task is aborted. Defaults to 16, which
         * with the defaults for 'connect.backoff.initial.delay.ms' and
         * 'connect.backoff.max.delay.ms' results in just over 20 minutes of
         * attempts before failing.
         */
        private Integer connectMaxAttempts = 16;
        /**
         * Database user for connecting to MongoDB, if necessary.
         */
        private String mongodbUser;
        /**
         * Description is not available here, please check Debezium website for
         * corresponding key 'field.blacklist' description.
         */
        private String fieldBlacklist;
        /**
         * A version of the format of the publicly visible source part in the
         * message
         */
        private String sourceStructVersion = "v2";
        /**
         * Length of an interval in milli-seconds in in which the connector
         * periodically sends heartbeat messages to a heartbeat topic. Use 0 to
         * disable heartbeat messages. Disabled by default.
         */
        private Integer heartbeatIntervalMs = 0;
        /**
         * Unique name that identifies the MongoDB replica set or cluster and
         * all recorded offsets, andthat is used as a prefix for all schemas and
         * topics. Each distinct MongoDB installation should have a separate
         * namespace and monitored by at most one Debezium connector.
         */
        private String mongodbName;
        /**
         * The maximum delay when trying to reconnect to a primary after a
         * connection cannot be made or when no primary is available. Defaults
         * to 120 second (120,000 ms).
         */
        private Long connectBackoffMaxDelayMs = 120000L;
        /**
         * Whether invalid host names are allowed when using SSL. If true the
         * connection will not prevent man-in-the-middle attacks
         */
        private Boolean mongodbSslInvalidHostnameAllowed = false;
        /**
         * The databases for which changes are to be excluded
         */
        private String databaseBlacklist;
        /**
         * Maximum size of each batch of source records. Defaults to 2048.
         */
        private Integer maxBatchSize = 2048;
        /**
         * The criteria for running a snapshot upon startup of the connector.
         * Options include: 'initial' (the default) to specify the connector
         * should always perform an initial sync when required; 'never' to
         * specify the connector should never perform an initial sync
         */
        private String snapshotMode = "initial";
        /**
         * The name of the Java class for the connector
         */
        private Class connectorClass;
        /**
         * Unique name for the connector. Attempting to register again with the
         * same name will fail.
         */
        private String name;
        /**
         * The name of the Java class that is responsible for persistence of
         * connector offsets.
         */
        private String offsetStorage = "org.apache.kafka.connect.storage.FileOffsetBackingStore";
        /**
         * Path to file where offsets are to be stored. Required when
         * offset.storage is set to the FileOffsetBackingStore
         */
        private String offsetStorageFileName;
        /**
         * The name of the Kafka topic where offsets are to be stored. Required
         * when offset.storage is set to the KafkaOffsetBackingStore.
         */
        private String offsetStorageTopic;
        /**
         * Replication factor used when creating the offset storage topic.
         * Required when offset.storage is set to the KafkaOffsetBackingStore
         */
        private Integer offsetStorageReplicationFactor;
        /**
         * The name of the Java class of the commit policy. It defines when
         * offsets commit has to be triggered based on the number of events
         * processed and the time elapsed since the last commit. This class must
         * implement the interface 'OffsetCommitPolicy'. The default is a
         * periodic commit policy based upon time intervals.
         */
        private String offsetCommitPolicy = "io.debezium.embedded.spi.OffsetCommitPolicy.PeriodicCommitOffsetPolicy";
        /**
         * Interval at which to try committing offsets. The default is 1 minute.
         */
        private Long offsetFlushIntervalMs = 60000L;
        /**
         * Maximum number of milliseconds to wait for records to flush and
         * partition offset data to be committed to offset storage before
         * cancelling the process and restoring the offset data to be committed
         * in a future attempt. The default is 5 seconds.
         */
        private Long offsetCommitTimeoutMs = 5000L;
        /**
         * The number of partitions used when creating the offset storage topic.
         * Required when offset.storage is set to the 'KafkaOffsetBackingStore'.
         */
        private Integer offsetStoragePartitions;
        /**
         * The Converter class that should be used to serialize and deserialize
         * key data for offsets. The default is JSON converter.
         */
        private String internalKeyConverter = "org.apache.kafka.connect.json.JsonConverter";
        /**
         * The Converter class that should be used to serialize and deserialize
         * value data for offsets. The default is JSON converter.
         */
        private String internalValueConverter = "org.apache.kafka.connect.json.JsonConverter";

        public Integer getMaxQueueSize() {
            return maxQueueSize;
        }

        public void setMaxQueueSize(Integer maxQueueSize) {
            this.maxQueueSize = maxQueueSize;
        }

        public String getMongodbPassword() {
            return mongodbPassword;
        }

        public void setMongodbPassword(String mongodbPassword) {
            this.mongodbPassword = mongodbPassword;
        }

        public Integer getInitialSyncMaxThreads() {
            return initialSyncMaxThreads;
        }

        public void setInitialSyncMaxThreads(Integer initialSyncMaxThreads) {
            this.initialSyncMaxThreads = initialSyncMaxThreads;
        }

        public Long getSnapshotDelayMs() {
            return snapshotDelayMs;
        }

        public void setSnapshotDelayMs(Long snapshotDelayMs) {
            this.snapshotDelayMs = snapshotDelayMs;
        }

        public String getCollectionBlacklist() {
            return collectionBlacklist;
        }

        public void setCollectionBlacklist(String collectionBlacklist) {
            this.collectionBlacklist = collectionBlacklist;
        }

        public String getCollectionWhitelist() {
            return collectionWhitelist;
        }

        public void setCollectionWhitelist(String collectionWhitelist) {
            this.collectionWhitelist = collectionWhitelist;
        }

        public Boolean getMongodbSslEnabled() {
            return mongodbSslEnabled;
        }

        public void setMongodbSslEnabled(Boolean mongodbSslEnabled) {
            this.mongodbSslEnabled = mongodbSslEnabled;
        }

        public Boolean getTombstonesOnDelete() {
            return tombstonesOnDelete;
        }

        public void setTombstonesOnDelete(Boolean tombstonesOnDelete) {
            this.tombstonesOnDelete = tombstonesOnDelete;
        }

        public Boolean getMongodbMembersAutoDiscover() {
            return mongodbMembersAutoDiscover;
        }

        public void setMongodbMembersAutoDiscover(
                Boolean mongodbMembersAutoDiscover) {
            this.mongodbMembersAutoDiscover = mongodbMembersAutoDiscover;
        }

        public String getFieldRenames() {
            return fieldRenames;
        }

        public void setFieldRenames(String fieldRenames) {
            this.fieldRenames = fieldRenames;
        }

        public Long getPollIntervalMs() {
            return pollIntervalMs;
        }

        public void setPollIntervalMs(Long pollIntervalMs) {
            this.pollIntervalMs = pollIntervalMs;
        }

        public String getHeartbeatTopicsPrefix() {
            return heartbeatTopicsPrefix;
        }

        public void setHeartbeatTopicsPrefix(String heartbeatTopicsPrefix) {
            this.heartbeatTopicsPrefix = heartbeatTopicsPrefix;
        }

        public String getDatabaseWhitelist() {
            return databaseWhitelist;
        }

        public void setDatabaseWhitelist(String databaseWhitelist) {
            this.databaseWhitelist = databaseWhitelist;
        }

        public String getMongodbHosts() {
            return mongodbHosts;
        }

        public void setMongodbHosts(String mongodbHosts) {
            this.mongodbHosts = mongodbHosts;
        }

        public Long getConnectBackoffInitialDelayMs() {
            return connectBackoffInitialDelayMs;
        }

        public void setConnectBackoffInitialDelayMs(
                Long connectBackoffInitialDelayMs) {
            this.connectBackoffInitialDelayMs = connectBackoffInitialDelayMs;
        }

        public Integer getSnapshotFetchSize() {
            return snapshotFetchSize;
        }

        public void setSnapshotFetchSize(Integer snapshotFetchSize) {
            this.snapshotFetchSize = snapshotFetchSize;
        }

        public String getDatabaseHistoryFileFilename() {
            return databaseHistoryFileFilename;
        }

        public void setDatabaseHistoryFileFilename(
                String databaseHistoryFileFilename) {
            this.databaseHistoryFileFilename = databaseHistoryFileFilename;
        }

        public Integer getConnectMaxAttempts() {
            return connectMaxAttempts;
        }

        public void setConnectMaxAttempts(Integer connectMaxAttempts) {
            this.connectMaxAttempts = connectMaxAttempts;
        }

        public String getMongodbUser() {
            return mongodbUser;
        }

        public void setMongodbUser(String mongodbUser) {
            this.mongodbUser = mongodbUser;
        }

        public String getFieldBlacklist() {
            return fieldBlacklist;
        }

        public void setFieldBlacklist(String fieldBlacklist) {
            this.fieldBlacklist = fieldBlacklist;
        }

        public String getSourceStructVersion() {
            return sourceStructVersion;
        }

        public void setSourceStructVersion(String sourceStructVersion) {
            this.sourceStructVersion = sourceStructVersion;
        }

        public Integer getHeartbeatIntervalMs() {
            return heartbeatIntervalMs;
        }

        public void setHeartbeatIntervalMs(Integer heartbeatIntervalMs) {
            this.heartbeatIntervalMs = heartbeatIntervalMs;
        }

        public String getMongodbName() {
            return mongodbName;
        }

        public void setMongodbName(String mongodbName) {
            this.mongodbName = mongodbName;
        }

        public Long getConnectBackoffMaxDelayMs() {
            return connectBackoffMaxDelayMs;
        }

        public void setConnectBackoffMaxDelayMs(Long connectBackoffMaxDelayMs) {
            this.connectBackoffMaxDelayMs = connectBackoffMaxDelayMs;
        }

        public Boolean getMongodbSslInvalidHostnameAllowed() {
            return mongodbSslInvalidHostnameAllowed;
        }

        public void setMongodbSslInvalidHostnameAllowed(
                Boolean mongodbSslInvalidHostnameAllowed) {
            this.mongodbSslInvalidHostnameAllowed = mongodbSslInvalidHostnameAllowed;
        }

        public String getDatabaseBlacklist() {
            return databaseBlacklist;
        }

        public void setDatabaseBlacklist(String databaseBlacklist) {
            this.databaseBlacklist = databaseBlacklist;
        }

        public Integer getMaxBatchSize() {
            return maxBatchSize;
        }

        public void setMaxBatchSize(Integer maxBatchSize) {
            this.maxBatchSize = maxBatchSize;
        }

        public String getSnapshotMode() {
            return snapshotMode;
        }

        public void setSnapshotMode(String snapshotMode) {
            this.snapshotMode = snapshotMode;
        }

        public Class getConnectorClass() {
            return connectorClass;
        }

        public void setConnectorClass(Class connectorClass) {
            this.connectorClass = connectorClass;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOffsetStorage() {
            return offsetStorage;
        }

        public void setOffsetStorage(String offsetStorage) {
            this.offsetStorage = offsetStorage;
        }

        public String getOffsetStorageFileName() {
            return offsetStorageFileName;
        }

        public void setOffsetStorageFileName(String offsetStorageFileName) {
            this.offsetStorageFileName = offsetStorageFileName;
        }

        public String getOffsetStorageTopic() {
            return offsetStorageTopic;
        }

        public void setOffsetStorageTopic(String offsetStorageTopic) {
            this.offsetStorageTopic = offsetStorageTopic;
        }

        public Integer getOffsetStorageReplicationFactor() {
            return offsetStorageReplicationFactor;
        }

        public void setOffsetStorageReplicationFactor(
                Integer offsetStorageReplicationFactor) {
            this.offsetStorageReplicationFactor = offsetStorageReplicationFactor;
        }

        public String getOffsetCommitPolicy() {
            return offsetCommitPolicy;
        }

        public void setOffsetCommitPolicy(String offsetCommitPolicy) {
            this.offsetCommitPolicy = offsetCommitPolicy;
        }

        public Long getOffsetFlushIntervalMs() {
            return offsetFlushIntervalMs;
        }

        public void setOffsetFlushIntervalMs(Long offsetFlushIntervalMs) {
            this.offsetFlushIntervalMs = offsetFlushIntervalMs;
        }

        public Long getOffsetCommitTimeoutMs() {
            return offsetCommitTimeoutMs;
        }

        public void setOffsetCommitTimeoutMs(Long offsetCommitTimeoutMs) {
            this.offsetCommitTimeoutMs = offsetCommitTimeoutMs;
        }

        public Integer getOffsetStoragePartitions() {
            return offsetStoragePartitions;
        }

        public void setOffsetStoragePartitions(Integer offsetStoragePartitions) {
            this.offsetStoragePartitions = offsetStoragePartitions;
        }

        public String getInternalKeyConverter() {
            return internalKeyConverter;
        }

        public void setInternalKeyConverter(String internalKeyConverter) {
            this.internalKeyConverter = internalKeyConverter;
        }

        public String getInternalValueConverter() {
            return internalValueConverter;
        }

        public void setInternalValueConverter(String internalValueConverter) {
            this.internalValueConverter = internalValueConverter;
        }
    }
}