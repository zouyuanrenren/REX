package eu.trowl.rex.factory;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataRangeVisitorEx;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;

import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXDatatypeImpl;

public class REXDataRangeBuilder implements OWLDataRangeVisitorEx<REXClassExpressionImpl> {

	REXDataFactory factory;
	
	public REXDataRangeBuilder(REXDataFactory rexDataFactory) {
		// TODO Auto-generated constructor stub
		factory = rexDataFactory;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDatatype datatype) {
		// TODO Auto-generated method stub
		REXDatatypeImpl newclazz = factory.datatypes.get(datatype);
		if(newclazz == null)
		{
			newclazz = new REXDatatypeImpl(datatype);
			newclazz.original = false;
			factory.datatypes.put(datatype, newclazz);
//			classExpressions.add(newclazz);
		}
//		 CONFIGURATION!
//		getREL2ObjectComplementOf(newclazz);
		return newclazz;		
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataOneOf dataRange) {
		// TODO Auto-generated method stub
		if(dataRange.getValues().size() == 1)
		{
			OWLLiteral literal = dataRange.getValues().iterator().next();
			return factory.getREXLiteral(literal);
		}
		else
		{
			List<OWLDataRange> comps = new ArrayList<OWLDataRange>();
			for(OWLLiteral literal:dataRange.getValues())
				comps.add(factory.factory.getOWLDataComplementOf(factory.factory.getOWLDataOneOf(literal)));
			REXClassExpressionImpl intersection = factory.getREXDataIntersectionOf(comps);
			REXClassExpressionImpl union = intersection.complement;
			if(union == null)
			{
				union = factory.getREXObjectComplementOf(intersection);

			}
			return union;
		}
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataComplementOf dataRange) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl complement = factory.getREXDataRange(dataRange.getDataRange());
		// do I need to do this?
		// a very simple normalisation step;
		return factory.getREXObjectComplementOf(complement);
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataIntersectionOf dataRange) {
		// TODO Auto-generated method stub
		List<OWLDataRange> ints = new ArrayList<OWLDataRange>();
		for(OWLDataRange dr:dataRange.getOperands())
			ints.add(dr);

		return factory.getREXDataIntersectionOf(ints);
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataUnionOf arg0) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.println("Unsupported DataRange: "+ arg0);
		return null;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDatatypeRestriction arg0) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.println("Unsupported DataRange: "+ arg0);
		return null;
	}

}
