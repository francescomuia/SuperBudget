package it.superbudget.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	private String name;

	@OneToMany(mappedBy = "category")
	private List<SubCategory> subCategories;

	public Long getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(Long categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryId == null)
		{
			if (other.categoryId != null)
				return false;
		}
		else if (!categoryId.equals(other.categoryId))
			return false;
		return true;
	}

	public List<SubCategory> getSubCategories()
	{
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories)
	{
		this.subCategories = subCategories;
	}
}
