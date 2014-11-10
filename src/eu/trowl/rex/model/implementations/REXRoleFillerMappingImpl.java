package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.model.interfaces.REXClassExpression;
import eu.trowl.rex.model.interfaces.REXRoleFillerMapping;

public class REXRoleFillerMappingImpl implements REXRoleFillerMapping {
	
	Set<REXObjectPropertyExpressionImpl> RN;
	Set<REXClassExpressionImpl> CN;
	Set<REXClassExpressionImpl> UFs;
	
	boolean isMaximal = false;
	boolean isUnique = false;

	public REXRoleFillerMappingImpl(Set<REXObjectPropertyExpressionImpl> rN2,
			Set<REXClassExpressionImpl> cN2) {
		// TODO Auto-generated constructor stub
		RN = rN2;
		CN = cN2;
	}

	@Override
	public Set<REXObjectPropertyExpressionImpl> getRoles() {
		// TODO Auto-generated method stub
		return RN;
	}

	@Override
	public Set<REXClassExpressionImpl> getFillers() {
		// TODO Auto-generated method stub
		return CN;
	}

	@Override
	public boolean isMaximal() {
		// TODO Auto-generated method stub
		return isMaximal;
	}

	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		return isUnique;
	}

	public void addAll(Set<REXObjectPropertyExpressionImpl> roles, Set<REXClassExpressionImpl> fillers) {
		// TODO Auto-generated method stub
		RN.addAll(roles);
		CN.addAll(fillers);
	}

	@Override
	public Set<REXClassExpressionImpl> getUniqueFillers() {
		// TODO Auto-generated method stub
		return UFs;
	}

	@Override
	public REXClassExpression getUniqueFiller() {
		// TODO Auto-generated method stub
		return UFs.iterator().next();
	}

	public void addUFs(Set<REXClassExpressionImpl> UFs){
		this.UFs = UFs;
	}
}
