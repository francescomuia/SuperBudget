package it.superbudget.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Budget
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long budgedId;

	private String name;

	public Long getBudgedId()
	{
		return budgedId;
	}

	public void setBudgedId(Long budgedId)
	{
		this.budgedId = budgedId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
