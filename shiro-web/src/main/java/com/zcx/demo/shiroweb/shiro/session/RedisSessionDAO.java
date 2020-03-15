package com.zcx.demo.shiroweb.shiro.session;

import com.zcx.demo.shiroweb.utils.RedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class RedisSessionDAO extends AbstractSessionDAO {
    private String SESSION_PREFIX = "session:";

    @Autowired
    private RedisUtil redisUtil;


    private String getKey(String key) {
        return SESSION_PREFIX + key;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    private void saveSession(Session session) {
        if (session == null || session.getId() == null){
            return;
        }
        Serializable id = session.getId();
        String key = getKey(id.toString());
        redisUtil.set(key, session);
        redisUtil.expire(key, 600);
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        System.out.println("read session");
        String key = getKey(serializable.toString());
        return (Session) redisUtil.get(key);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        Serializable id = session.getId();
        String key = getKey(id.toString());
        redisUtil.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisUtil.keys(SESSION_PREFIX);
        Set<Session> sessionSet = new HashSet<>(keys.size());
        if (CollectionUtils.isEmpty(keys)){
            return null;
        }
        for (String key : keys) {
            Session value = (Session) redisUtil.get(key);
            if (value == null) {
                continue;
            }
            sessionSet.add(value);
        }
        return sessionSet;
    }
}
