package it.superbudget;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;

/**
 * Classe di test.
 */
@Entity
public class User {
    /**
     * Entity manager.
     */
    @Transient
    @PersistenceContext(unitName = "SuperBudget")
    private static EntityManager entityManager;

    /**
     * id.
     */
    @Id
    private long id;

    /**
     * name.
     */
    private String name;

    /**
     * Ricerca utente.
     * 
     * @param id
     *            indentificativo
     * @return User
     */
    public static User find(final long id) {
        return entityManager.find(User.class, new Long(id));
    }

    public final long getId() {
        return id;
    }

    public final void setId(final long newId) {
        this.id = newId;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String newName) {
        this.name = newName;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void setEntityManager(final EntityManager em) {
        User.entityManager = em;
    }
}
