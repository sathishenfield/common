package com.core.lib.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class RedisCacheProvider {

    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisCacheProvider(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    /**
     * Serialize object to JSON
     */
    private <T> String serialize(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            log.error("Error serializing object: {}", ex.getMessage(), ex);
            throw new RuntimeException("Serialization error", ex);
        }
    }

    /**
     * Deserialize JSON to object
     */
    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            log.error("Error deserializing JSON: {}", ex.getMessage(), ex);
            throw new RuntimeException("Deserialization error", ex);
        }
    }

    /**
     * Add data into Redis hash.
     */
    public <T> void addData(String hashName, String key, T data) {
        try {
            log.info("Adding Data in hashName [{}] for key [{}] into Cache", hashName, key);
            hashOperations.put(hashName, key, serialize(data));
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed while adding data: {}", ex.getMessage(), ex);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Redis DataAccessException while adding data: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Fetch single entry from Redis hash.
     */
    public <T> Optional<T> getData(String hashName, String key, Class<T> clazz) {
        try {
            log.debug("Fetching Data using hashName [{}] for key [{}] from Cache", hashName, key);
            String json = hashOperations.get(hashName, key);
            return json != null ? Optional.of(deserialize(json, clazz)) : Optional.empty();
        } catch (Exception ex) {
            log.error("Error fetching data from Redis: {}", ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    /**
     * Fetch all entries from Redis hash.
     */
    public <T> Map<String, T> getAllData(String hashName, Class<T> clazz) {
        try {
            log.debug("Fetching All Data using hashName [{}] from Cache", hashName);
            Map<String, String> allData = hashOperations.entries(hashName);
            if (allData.isEmpty()) return Collections.emptyMap();
            return allData.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> deserialize(e.getValue(), clazz)
                    ));
        } catch (Exception ex) {
            log.error("Error fetching all data from Redis: {}", ex.getMessage(), ex);
            return Collections.emptyMap();
        }
    }

    /**
     * Update (same as put) data in Redis hash.
     */
    public <T> void updateData(String hashName, String key, T data) {
        log.info("Updating Data in hashName [{}] for key [{}] in Cache", hashName, key);
        addData(hashName, key, data);
    }

    /**
     * Delete a key from Redis hash.
     */
    public boolean deleteData(String hashName, String key) {
        try {
            log.info("Deleting Data in hashName [{}] for key [{}] in Cache", hashName, key);
            return hashOperations.delete(hashName, key) > 0;
        } catch (Exception ex) {
            log.error("Error deleting data from Redis: {}", ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Check if hash contains a given key.
     */
    public boolean ifHasKey(String hashName, String key) {
        try {
            log.debug("Checking if hashName [{}] has key [{}] in Cache", hashName, key);
            return hashOperations.hasKey(hashName, key);
        } catch (Exception ex) {
            log.error("Error checking if key exists in Redis: {}", ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Set expiry for a Redis hash.
     */
    public void setExpiry(String hashName, Duration ttl) {
        try {
            log.info("Setting expiry of [{}] seconds on hashName [{}]", ttl.getSeconds(), hashName);
            redisTemplate.expire(hashName, ttl);
        } catch (Exception ex) {
            log.error("Error setting expiry on Redis key: {}", ex.getMessage(), ex);
        }
    }

    /**
     * Get expiry for a Redis hash.
     */
    public Optional<Long> getExpiry(String hashName) {
        try {
            return Optional.of(redisTemplate.getExpire(hashName));
        } catch (Exception ex) {
            log.error("Error getting expiry for [{}]: {}", hashName, ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    /**
     * Clear entire hash.
     */
    public void clearHash(String hashName) {
        try {
            log.warn("Clearing all data for hashName [{}]", hashName);
            redisTemplate.delete(hashName);
        } catch (Exception ex) {
            log.error("Error clearing Redis hash [{}]: {}", hashName, ex.getMessage(), ex);
        }
    }
}