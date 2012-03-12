package it.superbudget.gui;

import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Recurrence;

import java.util.List;

public class RecurrenceDao
{

	@SuppressWarnings("unchecked")
	public List<Recurrence> findAll()
	{
		return PersistenceManager.getInstance().getEntityManager().createNamedQuery("findAllRecurrences").getResultList();
	}

	public Recurrence getNoneRecurrence()
	{
		return PersistenceManager.getInstance().getEntityManager().find(Recurrence.class, 1L);
	}

}
