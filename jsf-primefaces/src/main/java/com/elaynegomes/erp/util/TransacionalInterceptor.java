package com.elaynegomes.erp.util;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Interceptor
@Transacional
@Priority(Interceptor.Priority.APPLICATION)
public class TransacionalInterceptor implements Serializable {

	private static final long serialVersionUID = -7623798637424519733L;

	@Inject
	private EntityManager entityManager;
	
	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
		
		EntityTransaction trx = entityManager.getTransaction();
		
		boolean criador = false;
		try {
			if(!trx.isActive()) {
				//Faz Rollback
				trx.begin();
				trx.rollback();
				
				//Inicia a transacao
				trx.begin();
				
				criador = true;
			}
			return context.proceed();
		} catch (Exception e) {
			if(trx != null && criador) {
				trx.rollback();
			}
			throw e;
		} finally {
			if(trx != null && trx.isActive() && criador) {
				trx.commit();
			}
		}
	}
}