package eu.trowl.rex.model.interfaces;

import java.util.Set;

public interface REXRoleFillerMapping {

	public Set<? extends REXObjectPropertyExpression> getRoles();
	public Set<? extends REXClassExpression> getFillers();
	public Set<? extends REXClassExpression> getUniqueFillers();
	public REXClassExpression getUniqueFiller();
	public boolean isMaximal();
	public boolean isUnique();
}
