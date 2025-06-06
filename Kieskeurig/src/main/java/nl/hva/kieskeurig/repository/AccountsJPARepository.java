package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * JPA implementation of the EntityRepository for Account entities.
 */
@Primary
@Repository("ACCOUNTS.JPA")
@Transactional
public class AccountsJPARepository
        implements EntityRepository<Account> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Account save(Account account) {
        return this.entityManager.merge(account);
    }

    @Override
    public List<Account> findAll() {
        TypedQuery<Account> query =
                this.entityManager.createQuery(
                        "select a from Account a", Account.class);
        return query.getResultList();
    }

    @Override
    public Account findById(long id) {
        // DONE fix below code to have the account found by the entity manager
        return this.entityManager.find(Account.class, id);
    }

    @Override
    public Account deleteById(long id) {
        // TODO fix below code to have the account deleted actually by the entity manager
        Account account = this.findById(id);
        this.entityManager.remove(account);
        return account;
    }

    public List<Account> findByQuery(String jpqlName, Object ...params) {
        TypedQuery<Account> query =
                this.entityManager.createNamedQuery(jpqlName, Account.class);

        // TODO resolve all parameter values into the query
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
        // execute the query and return the result
        return query.getResultList();
    }
}
