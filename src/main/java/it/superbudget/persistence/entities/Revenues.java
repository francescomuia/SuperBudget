package it.superbudget.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
public class Revenues extends BudgetEntry
{

	private static final long serialVersionUID = 1L;

}
