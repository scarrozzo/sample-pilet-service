package io.piral.feedservice.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public abstract class BaseTransactionalCommand<T> implements Command<T>{

    @Autowired
    private TransactionTemplate transactionTemplate;

    protected T doExecute() {
        throw new UnsupportedOperationException();
    }

    public void afterRollback() {
        log.debug("No custom rollback action specified.");
    }

    @Override
    public final T execute() {
        long st = System.currentTimeMillis();

        try {
            T r = transactionTemplate.execute(status -> {
                log.info("Executing transactional command {}...", getClass().getSimpleName());
                T result = doExecute();
                log.debug("Transactional command {} executed. Elapsed: {} msec/s", getClass().getSimpleName(), (System.currentTimeMillis() - st));
                return result;
            });

            log.info("Transactional command {} closed. Elapsed: {} msec/s", getClass().getSimpleName(), (System.currentTimeMillis() - st));
            return r;
        } catch (Throwable t) {
            log.error("Transactional command error during commit.", t);
            afterRollback();
            throw t;
        }
    }
}
