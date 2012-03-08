package it.superbudget.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SubCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subCategoryId;

	private String name;

	private Category category;

	public Long getSubCategoryId()
	{
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId)
	{
		this.subCategoryId = subCategoryId;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((subCategoryId == null) ? 0 : subCategoryId.hashCode());
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
		SubCategory other = (SubCategory) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (subCategoryId == null)
		{
			if (other.subCategoryId != null)
				return false;
		}
		else if (!subCategoryId.equals(other.subCategoryId))
			return false;
		return true;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

}
