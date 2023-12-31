package org.slf4j;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.spi.MDCAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 重构{@link LogbackMDCAdapter}类，搭配TransmittableThreadLocal实现父子线程之间的数据传递
 * @Author 高杰
 * @create 2023/8/28 11:21
 */
public class TtlMDCAdapter implements MDCAdapter {

    private final ThreadLocal<Map<String,String>> copyOnInheritThreadLocal = new TransmittableThreadLocal<>();

    private static final int WRITE_OPERATION = 1;
    private static final int MAP_COPY_OPERATION = 2;

    private static TtlMDCAdapter mtcMDCAdapter;

    /**
     * keeps track of the last operation performed
     */
    private final ThreadLocal<Integer> lastOperation = new ThreadLocal<>();

    static {
        mtcMDCAdapter = new TtlMDCAdapter();
        MDC.mdcAdapter = mtcMDCAdapter;
    }

    public static MDCAdapter getInstance(){
        return mtcMDCAdapter;
    }

    private Integer getAndSetLastOperation(int op){
        Integer lastOp = lastOperation.get();
        lastOperation.set(op);
        return lastOp;
    }

    private static boolean wasLastOpReadOrNull(Integer lastOp){
        return lastOp == null || lastOp == MAP_COPY_OPERATION;
    }

    private Map<String,String> duplicateAndInsertNewMap(Map<String,String> oldMap){
        Map<String,String> newMap = Collections.synchronizedMap(new HashMap<>());
        if(null != oldMap){
            synchronized (oldMap){
                newMap.putAll(newMap);
            }
        }
        copyOnInheritThreadLocal.set(newMap);
        return newMap;
    }


    @Override
    public void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

        if (wasLastOpReadOrNull(lastOp) || oldMap == null) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        } else {
            oldMap.put(key, val);
        }
    }

    @Override
    public String get(String key) {
        final Map<String, String> map = copyOnInheritThreadLocal.get();
        if ((map != null) && (key != null)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get the current thread's MDC as a map. This method is intended to be used
     * internally.
     */
    public Map<String, String> getPropertyMap() {
        lastOperation.set(MAP_COPY_OPERATION);
        return copyOnInheritThreadLocal.get();
    }

    /**
     * Returns the keys in the MDC as a {@link Set}. The returned value can be
     * null.
     */
    public Set<String> getKeys() {
        Map<String, String> map = getPropertyMap();

        if (map != null) {
            return map.keySet();
        } else {
            return null;
        }
    }


    @Override
    public void remove(String key) {
        if (key == null) {
            return;
        }
        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        if (oldMap == null) {
            return;
        }

        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

        if (wasLastOpReadOrNull(lastOp)) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        } else {
            oldMap.remove(key);
        }
    }

    @Override
    public void clear() {
        lastOperation.set(WRITE_OPERATION);
        copyOnInheritThreadLocal.remove();
    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map<String, String> hashMap = copyOnInheritThreadLocal.get();
        if (hashMap == null) {
            return null;
        } else {
            return new HashMap<>(hashMap);
        }
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        lastOperation.set(WRITE_OPERATION);

        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<>());
        newMap.putAll(contextMap);

        // the newMap replaces the old one for serialisation's sake
        copyOnInheritThreadLocal.set(newMap);
    }
}
