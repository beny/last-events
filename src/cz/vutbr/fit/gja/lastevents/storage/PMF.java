package cz.vutbr.fit.gja.lastevents.storage;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Persistance manager factory for storage.
 * @author Petr Nohejl <xnohej00@stud.fit.vutbr.cz>
 */
public final class PMF 
{
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() 
    {
        return pmfInstance;
    }
}
