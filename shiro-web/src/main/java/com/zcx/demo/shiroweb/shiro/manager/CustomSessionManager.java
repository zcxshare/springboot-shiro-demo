package com.zcx.demo.shiroweb.shiro.manager;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

public class CustomSessionManager extends DefaultWebSessionManager {
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        if (sessionId == null) {
            return null;
        }
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (request != null) {
            Object session = request.getAttribute(sessionId.toString());
            if (session != null) {
                return (Session) session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (request != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
