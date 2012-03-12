package it.superbudget.persistence.dao;

import it.superbudget.persistence.PersistenceManager;
import it.superbudget.persistence.entities.Category;

import java.util.List;

public class CategoryDao
{

	@SuppressWarnings("unchecked")
	public List<Category> findAll()
	{
		return PersistenceManager.getInstance().getEntityManager().createNamedQuery("findAllCategory").getResultList();
	}

}
