package org.roettig.NRPSpredictor2.predictors;

import java.util.List;

import org.roettig.NRPSpredictor2.ADomain;

public interface Predictor
{
	void predict(List<ADomain> domains);
}
