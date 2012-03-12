package it.superbudget.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedNativeQueries(value =
{ @NamedNativeQuery(name = "replaceDefaultBudget", query = "UPDATE Budget set DEFAULTBUDGET = 0 WHERE DEFAULTBUDGET=1") })
@NamedQueries(value =
{ @NamedQuery(name = "findAllBudget", query = "Select t From Budget t") })
@Entity
public class Budget implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long budgetId;

	private String name;

	private BigDecimal saldo;

	private boolean defaultBudget;

	@OneToMany(mappedBy = "budget")
	private List<BudgetEntry> budgetEntries;

	public Budget()
	{

	}

	public Budget(String name, BigDecimal saldo, boolean defaultBudget)
	{
		this.name = name;
		this.saldo = saldo;
		this.defaultBudget = defaultBudget;
	}

	public Long getBudgetId()
	{
		return budgetId;
	}

	public void setBudgetId(Long budgedId)
	{
		this.budgetId = budgedId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getSaldo()
	{
		return saldo;
	}

	public void setSaldo(BigDecimal saldo)
	{
		this.saldo = saldo;
	}

	public boolean isDefaultBudget()
	{
		return defaultBudget;
	}

	public void setDefaultBudget(boolean defaultBudget)
	{
		this.defaultBudget = defaultBudget;
	}

	public List<BudgetEntry> getBudgetEntries()
	{
		return budgetEntries;
	}

	public void setBudgetEntries(List<BudgetEntry> budgetEntries)
	{
		this.budgetEntries = budgetEntries;
	}

	@Override
	public String toString()
	{
		return "Budget [budgedId=" + budgetId + ", name=" + name + ", saldo=" + saldo + ", defaultBudget=" + defaultBudget + ", budgetEntries="
				+ budgetEntries + "]";
	}

}
