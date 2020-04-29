/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.advice;

import com.sg.flooringmastery.dao.FlooringOrderAuditDao;
import com.sg.flooringmastery.dao.FlooringOrderPersistenceException;
import org.aspectj.lang.JoinPoint;

/**
 *
 * @author Jeff
 */
public class LoggingAdvice {
    FlooringOrderAuditDao auditDao;
    
    public LoggingAdvice(FlooringOrderAuditDao auditDao) {
        this.auditDao = auditDao;
    }
    
    public void createAuditLogEntry(JoinPoint jp) {
        Object[] args = jp.getArgs();
        String auditEntry = jp.getSignature().getName();
        if (auditEntry.contains("edit")) {
            auditEntry += ": " + args[1];
        } else if (auditEntry.contains("remove")) {
            auditEntry += ": " + args[0] + ", " + "order #" + args[1];
        } else {
            for (Object arg : args) {
                auditEntry += arg;
            }
        }

        try {
            auditDao.writeLogEntry(auditEntry);
        } catch (FlooringOrderPersistenceException e) {
            System.err.println("ERROR: Could not create log entry in LoggingAdvice.");
        }
    }
}
